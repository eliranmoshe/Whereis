package eliran.com.WhereIs.Frags;


import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.orm.SugarContext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eliran.com.WhereIs.Instruments.CheckConnection;
import eliran.com.WhereIs.Instruments.FragmentChangerInterface;
import eliran.com.WhereIs.GetPlacesService;
import eliran.com.WhereIs.Instruments.Functions;
import eliran.com.WhereIs.Objects.LastSearch;
import eliran.com.WhereIs.MainActivity;
import eliran.com.WhereIs.Objects.Place;
import eliran.com.WhereIs.Adapters.PlaceRVadapter;
import eliran.com.WhereIs.R;
import eliran.com.WhereIs.Objects.SugarPlace;


public class MainFragMap extends Fragment implements LocationListener {
    public static double lat;
    public static double lng;
    public static boolean IsSavedInstanceState = false;
    private View view;
    private LocationManager locationManager;
    private RecyclerView placesRV;
    private CheckBox IsNeerByCB;
    private PlaceRVadapter placeRVadapter;
    private ArrayList<Place> allPlaces;
    private ProgressDialog LoadingDialog;
    private ArrayList<Place> landPlaces;
    private SearchView serchview;
    private RadioButton radioButton;
    private boolean LOCATION_SERVICE_ON;
    private PlacesBroadCastReciever placesBroadCastReciever;
    private IntentFilter intentFilter;

    public MainFragMap() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the fragment into layout
        view = inflater.inflate(R.layout.main_frag_map, container, false);
//
        SugarContext.init(getActivity());
        // make broadcast filter and listener
        placesBroadCastReciever = new PlacesBroadCastReciever();
        intentFilter = new IntentFilter("intent.to.MainFragment.FINISH_PLACES");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(placesBroadCastReciever, intentFilter);
//TODO last known location
/*if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null){
            currentLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else{
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }*/
        //request location permission


        locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //requestLocationUpdates(the provider used to get location - gps/network , refresh time milliseconds ,minimum refresh distance,
            //location listener)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
        } else {
            //request permission 12 is the request number
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }

        serchview = (SearchView) view.findViewById(R.id.searchview);
        serchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serchview.setIconified(false);
                //TODO make to not be twice X button
            }
        });
        serchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LastSearch lastSearch;
                String Url = "";
                query = query.trim();
                if (query.length() > 0) {
                    try {
                        Url = URLEncoder.encode(query, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getActivity(), GetPlacesService.class);
                    if (!IsNeerByCB.isChecked()) {
                        lastSearch = new LastSearch(query, "1");
                        lastSearch.save();
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        intent.putExtra("PlaceKind", Url);
                        intent.putExtra("IsNeerBy", 1);
                    } else {
                        lastSearch = new LastSearch(query, "-1");
                        lastSearch.save();
                        intent.putExtra("query", Url);
                        intent.putExtra("IsNeerBy", -1);
                    }
                    LoadingBar(lastSearch);


                    getActivity().startService(intent);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(view, "No City Name", Snackbar.LENGTH_LONG)
                            .setDuration(5000);

                    snackbar.show();
                    Toast.makeText(getActivity(), "PLEASE ENTER PLACE NAME", Toast.LENGTH_SHORT).show();

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }


        });


        placesRV = (RecyclerView) view.findViewById(R.id.PlacesRV);
        IsNeerByCB = (CheckBox) view.findViewById(R.id.LoacalSwitchCB);
        IsNeerByCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
        radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LOCATION_SERVICE_ON == false) {
                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

            }
        });

        if (savedInstanceState != null) {
            placesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            landPlaces = savedInstanceState.getParcelableArrayList("landscapeArray");
            allPlaces = landPlaces;
            placeRVadapter = new PlaceRVadapter(getActivity(), landPlaces, savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("lng"));
            placesRV.setAdapter(placeRVadapter);
            MainActivity.IsFirstTime = false;
        }
        return view;
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        //get the current LatLng of the device

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (Build.VERSION.SDK_INT >= 21) {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{

                            Color.GREEN //disabled
                            , Color.GREEN //enabled

                    }
            );


            radioButton.setButtonTintList(colorStateList);//set the color tint list
            radioButton.setText("LOCATION SERVICE: ON");
            radioButton.invalidate(); //could not be necessary
            LOCATION_SERVICE_ON = true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (Build.VERSION.SDK_INT >= 21) {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{

                            Color.RED //disabled
                            , Color.RED //enabled

                    }
            );


            radioButton.setButtonTintList(colorStateList);//set the color tint list
            radioButton.setTextSize(15);
            radioButton.setText("LOCATION SERVICE: OFF\n (open settings)");
            radioButton.invalidate(); //could not be necessary
            LOCATION_SERVICE_ON = false;
        }
    }


    public class PlacesBroadCastReciever extends BroadcastReceiver {
        public PlacesBroadCastReciever() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            SugarContext.init(getActivity());
            LoadingDialog.dismiss();
            //make keyboard disappear after search finish
            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(serchview.getWindowToken(), 0);
            List<SugarPlace> BackList = SugarPlace.listAll(SugarPlace.class);


            if (!intent.getBooleanExtra("JSON_IS_NULL", false)) {
                if (!intent.getBooleanExtra("IsZeroResults", false)) {
                    allPlaces = new ArrayList<>();
                    for (int i = 0; i < BackList.size(); i++) {
                        allPlaces.add(new Place(BackList.get(i).getName(), BackList.get(i).getVicinity(), BackList.get(i).getIcon(), BackList.get(i).getFormatted_address(), BackList.get(i).getLat(), BackList.get(i).getLng(), BackList.get(i).getPhoto_reference()));
                    }
                    //save search list to DATABASE

                   /* ArrayList<Place>byDistance = new ArrayList<>();
                    byDistance.addAll(allPlaces);
                    Collections.sort(allPlaces, new Comparator<Place>() {

                        @Override
                        public int compare(Place p1, Place p2) {
                            double distance1 = Functions.distance(p1.getLat(),p1.getLng(),lat,lng);
                            double distance2 = Functions.distance(p2.getLat(),p2.getLng(),lat,lng);
                            return Double.compare(distance1,distance2);
                        }
                    });*/


                    placesRV.setLayoutManager(new LinearLayoutManager(context));
                    placeRVadapter = new PlaceRVadapter(getActivity(), allPlaces, lat, lng);
                    placesRV.setAdapter(placeRVadapter);
                    //Toast.makeText(context, "service finished", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "did not find place \n please try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar snackbar = Snackbar
                        .make(view, "No City Name", Snackbar.LENGTH_LONG)
                        .setDuration(5000);

                snackbar.show();
            }
        }
        //LoadingDialog.dismiss();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (allPlaces != null) {
            outState.putParcelableArrayList("landscapeArray", allPlaces);
            outState.putDouble("lat", lat);
            outState.putDouble("lng", lng);
            IsSavedInstanceState = true;

        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(placesBroadCastReciever, intentFilter);
        if (MainActivity.IsFirstTime) {



        } else {
            if (MainActivity.IsLargeDevice == true) {
                FragmentChangerInterface fragmentChangerInterface = (FragmentChangerInterface) getActivity();
                fragmentChangerInterface.FromLargeMainToMap();
            }
            List<SugarPlace> BackList = SugarPlace.listAll(SugarPlace.class);
            List<Place> allPlaces = new ArrayList<>();
            for (int i = 0; i < BackList.size(); i++) {
                allPlaces.add(new Place(BackList.get(i).getName(), BackList.get(i).getVicinity(), BackList.get(i).getIcon(), BackList.get(i).getFormatted_address(), BackList.get(i).getLat(), BackList.get(i).getLng(), BackList.get(i).getPhoto_reference()));
            }
            placesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            placeRVadapter = new PlaceRVadapter(getActivity(), allPlaces, lat, lng);
            placesRV.setAdapter(placeRVadapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(placesBroadCastReciever);

        allPlaces = null;
        placeRVadapter = null;
    }

    ///////////////Loading dialod/////////////////
    public void LoadingBar(LastSearch lastSearch) {
        LoadingDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_HORIZONTAL);
        LoadingDialog.setTitle("PLEASE WAIT");
        LoadingDialog.setMessage("serching results for  " + lastSearch.getLastSearch());
        LoadingDialog.setCancelable(false);
        //show the dialog:
        LoadingDialog.show();

    }
}
