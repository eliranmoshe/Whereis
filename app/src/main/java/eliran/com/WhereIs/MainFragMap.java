package eliran.com.WhereIs;


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
import android.os.BatteryManager;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.List;


public class MainFragMap extends Fragment implements LocationListener {

    View view;
    LocationManager locationManager;
    public static double lng;
    public static double lat;
    RecyclerView placesRV;
    CheckBox IsNeerByCB;
    PlaceRVadapter placeRVadapter;
    ArrayList<Place> allPlaces;
    ProgressDialog LoadingDialog;
    ArrayList<Place>landPlaces;
    SearchView serchview;
    public static boolean IsSavedInstanceState=false;
    RadioButton radioButton;
    boolean LOCATION_SERVICE_ON;
    PlacesBroadCastReciever placesBroadCastReciever;
    IntentFilter intentFilter;

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
                        LastSearch lastSearch = new LastSearch(query,"1");
                        lastSearch.save();
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        intent.putExtra("PlaceKind", Url);
                        intent.putExtra("IsNeerBy",1);
                    }else {
                        LastSearch lastSearch=new LastSearch(query,"-1");
                        lastSearch.save();
                        intent.putExtra("query",Url);
                        intent.putExtra("IsNeerBy",-1);
                    }
                  LoadingDialog = new ProgressDialog(getActivity());
                  LoadingDialog.setTitle("PLEASE WAIT");
                    LoadingDialog.setCancelable(false);
                  //show the dialog:
                  LoadingDialog.show();


                    getActivity().startService(intent);
                }else {
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
        IsNeerByCB= (CheckBox) view.findViewById(R.id.LoacalSwitchCB);
        IsNeerByCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
       radioButton= (RadioButton) view.findViewById(R.id.radioButton);
        radioButton.setChecked(true);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LOCATION_SERVICE_ON==false)
                {
                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

            }
        });

        if (savedInstanceState!=null){
            placesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            landPlaces=savedInstanceState.getParcelableArrayList("landscapeArray");
            allPlaces=landPlaces;
            placeRVadapter = new PlaceRVadapter(getActivity(), landPlaces, savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("lng"));
            placesRV.setAdapter(placeRVadapter);
            MainActivity.IsFirstTime=false;
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
        if(Build.VERSION.SDK_INT>=21)
        {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[] {

                            Color.GREEN //disabled
                            ,Color.GREEN //enabled

                    }
            );


            radioButton.setButtonTintList(colorStateList);//set the color tint list
            radioButton.setText("LOCATION SERVICE: ON");
            radioButton.invalidate(); //could not be necessary
            LOCATION_SERVICE_ON=true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if(Build.VERSION.SDK_INT>=21)
        {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[] {

                            Color.RED //disabled
                            ,Color.RED //enabled

                    }
            );


            radioButton.setButtonTintList(colorStateList);//set the color tint list
            radioButton.setTextSize(15);
            radioButton.setText("LOCATION SERVICE: OFF\n (open settings)");
            radioButton.invalidate(); //could not be necessary
            LOCATION_SERVICE_ON=false;
        }
    }


    public class PlacesBroadCastReciever extends BroadcastReceiver {
            public PlacesBroadCastReciever(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO handle with battary chraaged
            SugarContext.init(getActivity());
            LoadingDialog.dismiss();
                //make keyboard disappear after search finish
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
             //   mgr.hideSoftInputFromWindow(serchview.getWindowToken(), 0);
            List<SugarPlace> BackList = SugarPlace.listAll(SugarPlace.class);
            //TODO sort order by distance
            //List<SugarPlace> notes = SugarPlace.findWithQuery(SugarPlace.class, "SELECT * FROM SugarPlace ORDER BY lat DESC", null);

            if (!intent.getBooleanExtra("JSON_IS_NULL",false)) {
                if (!intent.getBooleanExtra("IsZeroResults", false)) {
                    allPlaces = new ArrayList<>();
                    for (int i = 0; i < BackList.size(); i++) {
                        allPlaces.add(new Place(BackList.get(i).name, BackList.get(i).vicinity, BackList.get(i).icon, BackList.get(i).formatted_address, BackList.get(i).lat, BackList.get(i).lng, BackList.get(i).photo_reference));
                    }
                    //save search list to DATABASE


                    placesRV.setLayoutManager(new LinearLayoutManager(context));
                    placeRVadapter = new PlaceRVadapter(getActivity(), allPlaces, lat, lng);
                    //TODO check allplace.photo_reference why its null
                    placesRV.setAdapter(placeRVadapter);
                    Toast.makeText(context, "service finished", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "did not find place \n please try again", Toast.LENGTH_SHORT).show();
                }
            }else {
                //TODO snack bar
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
        //TODO not alwayes work good
        if (allPlaces!=null) {
            outState.putParcelableArrayList("landscapeArray", allPlaces);
            outState.putDouble("lat", lat);
            outState.putDouble("lng", lng);
            IsSavedInstanceState=true;

        }else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(placesBroadCastReciever,intentFilter);
        if(MainActivity.IsFirstTime) {

            List<SugarPlace> BackList = SugarPlace.listAll(SugarPlace.class);
                if (BackList.size() != 0) {
                    if (!new CheckConnection(getActivity()).isNetworkAvailable()) {
                        Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
                        List<Place> allPlaces = new ArrayList<>();
                        for (int i = 0; i < BackList.size(); i++) {
                            allPlaces.add(new Place(BackList.get(i).name, BackList.get(i).vicinity, BackList.get(i).icon, BackList.get(i).formatted_address, BackList.get(i).lat, BackList.get(i).lng, BackList.get(i).photo_reference));
                        }
                        placesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                        placeRVadapter = new PlaceRVadapter(getActivity(), allPlaces, lat, lng);
                        placesRV.setAdapter(placeRVadapter);
                    } else {
                        LastSearch lastSearch = LastSearch.last(LastSearch.class);
                        LoadingDialog = new ProgressDialog(getActivity());
                        LoadingDialog.setTitle("PLEASE WAIT\n loading places for "+lastSearch.LastSearch);
                        LoadingDialog.setCancelable(false);
                        //show the dialog:
                        LoadingDialog.show();
                       // Toast.makeText(getActivity(), "internet is on going to service", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), GetPlacesService.class);
                        if (lastSearch.IsBeerBySearch.equals("1")) {
                            intent.putExtra("lat", lat);
                            intent.putExtra("lng", lng);
                            intent.putExtra("PlaceKind", lastSearch.LastSearch);
                            intent.putExtra("IsNeerBy", 1);
                        } else {
                            intent.putExtra("query", lastSearch.LastSearch);
                            intent.putExtra("IsNeerBy", -1);
                        }
                        getActivity().startService(intent);
                        Log.d("fasdfsd", "fdsfsd");

                    }
                }
        }
        else{
            if (MainActivity.IsLargeDevice==true) {
                FragmentChangerInterface fragmentChangerInterface = (FragmentChangerInterface) getActivity();
                fragmentChangerInterface.FromLargeMainToMap();
            }
            List<SugarPlace> BackList = SugarPlace.listAll(SugarPlace.class);
            List<Place> allPlaces = new ArrayList<>();
            for (int i = 0; i < BackList.size(); i++) {
                allPlaces.add(new Place(BackList.get(i).name, BackList.get(i).vicinity, BackList.get(i).icon, BackList.get(i).formatted_address, BackList.get(i).lat, BackList.get(i).lng,BackList.get(i).photo_reference));
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
    }
}
