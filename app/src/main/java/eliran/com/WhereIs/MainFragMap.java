package eliran.com.WhereIs;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class MainFragMap extends Fragment implements LocationListener {

    LocationManager locationManager;
    EditText SearchET;

    public MainFragMap() {

    }
//TODO need to outo search by location

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the fragment into layout
        View view = inflater.inflate(R.layout.main_frag_map, container, false);
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




        SearchET= (EditText) view.findViewById(R.id.SearchPlaceET);


        return view;
    }



    @Override
    public void onLocationChanged(Location location) {
        //get the current LatLng of the device
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        //TODO intent service to get all places neer by
        Intent intent=new Intent(getActivity(),GetPlacesService.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        getActivity().startService(intent);

        SearchET.setText("lat-"+lat+"lng-"+lng);
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

    //TODO get broadcast reciver from service to show the closest places list and put it in the recycler view

}
