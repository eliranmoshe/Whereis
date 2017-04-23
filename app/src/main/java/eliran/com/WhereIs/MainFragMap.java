package eliran.com.WhereIs;


import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainFragMap extends Fragment implements LocationListener {

    LocationManager locationManager;
    EditText SearchET;
    Button SearchBtn;
    public static double lng;
    public static double lat;
    RecyclerView placesRV;
    boolean IsNeerBy = true;
    CheckBox IsNeerByCB;
    PlaceRVadapter placeRVadapter;
    ArrayList<Place> allPlaces;
    ProgressDialog LoadingDialog;


    public MainFragMap() {

    }
//TODO need to outo search by location

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the fragment into layout
        View view = inflater.inflate(R.layout.main_frag_map, container, false);
        // make broadcast filter and listener
        PlacesBroadCastReciever placesBroadCastReciever = new PlacesBroadCastReciever();
        IntentFilter intentFilter = new IntentFilter("intent.to.MainFragment.FINISH_PLACES");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(placesBroadCastReciever, intentFilter);
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

        placesRV = (RecyclerView) view.findViewById(R.id.PlacesRV);
        SearchET = (EditText) view.findViewById(R.id.SearchPlaceET);
        IsNeerByCB= (CheckBox) view.findViewById(R.id.LoacalSwitchCB);
        IsNeerByCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (IsNeerBy==true){
                IsNeerBy=false;}else{
                    IsNeerBy=true;
                }
            }
        });
        SearchBtn = (Button) view.findViewById(R.id.SearchBtn);

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchet = SearchET.getText().toString();
                String Url = "";
                searchet = searchet.trim();
                if (searchet.length() > 0) {
                    try {
                        Url = URLEncoder.encode(searchet, "UTF-8");
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
                    LoadingDialog = new ProgressDialog(getActivity());
                    LoadingDialog.setTitle("PLEASE WAIT");
                    //show the dialog:
                    LoadingDialog.show();

                    getActivity().startService(intent);
                }else {

                  /*  Snackbar snackbar = Snackbar
                            .make(v, "No City Name", Snackbar.LENGTH_LONG)
                            .setDuration(5000);

                    snackbar.show();*/
                    Toast.makeText(getActivity(), "PLEASE ENTER PLACE NAME", Toast.LENGTH_SHORT).show();

                }


            }
        });

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
            allPlaces  = intent.getParcelableArrayListExtra("response");
            if (allPlaces.size()>0){


            placesRV.setLayoutManager(new LinearLayoutManager(context));
            placeRVadapter = new PlaceRVadapter(getActivity(), allPlaces, lat, lng);
            placesRV.setAdapter(placeRVadapter);
            Toast.makeText(context, "service finished", Toast.LENGTH_SHORT).show();}
            else{
                Toast.makeText(context, "did not find place \n please try again", Toast.LENGTH_SHORT).show();
            }
            LoadingDialog.dismiss();
        }
    }
}
