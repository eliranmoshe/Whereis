package eliran.com.WhereIs;


import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.orm.SugarContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainFragMap extends Fragment implements LocationListener {

    LocationManager locationManager;
    public static double lng;
    public static double lat;
    RecyclerView placesRV;
    boolean IsNeerBy = true;
    CheckBox IsNeerByCB;
    PlaceRVadapter placeRVadapter;
    ArrayList<SearchPlaceSugarOrm> allPlaces;
    ProgressDialog LoadingDialog;
    ArrayList<SearchPlaceSugarOrm>landPlaces;
    SearchView serchview;

    public MainFragMap() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the fragment into layout
        View view = inflater.inflate(R.layout.main_frag_map, container, false);
        SugarContext.init(getActivity().getApplicationContext());
        // make broadcast filter and listener
        PlacesBroadCastReciever placesBroadCastReciever = new PlacesBroadCastReciever();
        IntentFilter intentFilter = new IntentFilter("intent.to.MainFragment.FINISH_PLACES");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(placesBroadCastReciever, intentFilter);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getActivity().registerReceiver(null, ifilter);


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
                    if (IsNeerBy==true) {
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        intent.putExtra("PlaceKind", Url);
                        intent.putExtra("IsNeerBy",1);
                    }else if (IsNeerBy==false){
                        intent.putExtra("query",Url);
                        intent.putExtra("IsNeerBy",-1);
                    }
               /*   LoadingDialog = new ProgressDialog(getActivity());
                  LoadingDialog.setTitle("PLEASE WAIT");
                  //show the dialog:
                  LoadingDialog.show();*/
                    getActivity().startService(intent);
                }else {
                        /*  Snackbar snackbar = Snackbar
                          .make(v, "No City Name", Snackbar.LENGTH_LONG)
                          .setDuration(5000);

                          snackbar.show();*/
                    Toast.makeText(getActivity(), "PLEASE ENTER PLACE NAME", Toast.LENGTH_SHORT).show();

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("dsds","dsdsds");
                return true;
            }
        });
        placesRV = (RecyclerView) view.findViewById(R.id.PlacesRV);
        IsNeerByCB= (CheckBox) view.findViewById(R.id.LoacalSwitchCB);
        IsNeerByCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (IsNeerBy==true){
                IsNeerBy=false;
               }else{
                    IsNeerBy=true;
                }
            }
        });

        if (savedInstanceState!=null){
            placesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            landPlaces=savedInstanceState.getParcelableArrayList("landscapeArray");
            allPlaces=landPlaces;
            placeRVadapter = new PlaceRVadapter(getActivity(), landPlaces, savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("lng"));
            placesRV.setAdapter(placeRVadapter);
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

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public class PlacesBroadCastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO handle with battary chraaged
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
            {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;

                int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                Toast.makeText(context, ""+status, Toast.LENGTH_SHORT).show();
            }else {
                //make keyboard disappear after search finish
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(serchview.getWindowToken(), 0);
                allPlaces = intent.getParcelableArrayListExtra("response");
                if (allPlaces.size() > 0) {

                    for (int i = 0; i < allPlaces.size(); i++) {
                        //  Place place= (Place) allPlaces.get(i);
                        //  SearchPlaceSugarOrm searchPlaceSugarOrm=new SearchPlaceSugarOrm(place.name,place.vicinity,place.icon,place.formatted_address,place.geometry.location.lat,place.geometry.location.lng);
                        //  searchPlaceSugarOrm.save();
                    }
                    placesRV.setLayoutManager(new LinearLayoutManager(context));
                    placeRVadapter = new PlaceRVadapter(getActivity(), allPlaces, lat, lng);
                    placesRV.setAdapter(placeRVadapter);
                    Toast.makeText(context, "service finished", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "did not find place \n please try again", Toast.LENGTH_SHORT).show();
                }
            }
            //LoadingDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (allPlaces!=null) {
            outState.putParcelableArrayList("landscapeArray", allPlaces);
            outState.putDouble("lat", 31.8903396);
            outState.putDouble("lng", 34.773063);
        }else {

        }
    }
}
