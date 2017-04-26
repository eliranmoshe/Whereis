package eliran.com.WhereIs;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFrag extends Fragment {
public  Place selectedPlace;
    double lat;
    double lng;


    public MapFrag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_map, container, false);

        final LatLng[] latLng = new LatLng[1];
         MapFragment mapFragment= new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.MapContainer, mapFragment).commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (selectedPlace!=null) {
                    if (savedInstanceState != null) {
                        lat = savedInstanceState.getDouble("lat");
                        lng = savedInstanceState.getDouble("lng");
                        latLng[0] = new LatLng(lat, lng);
                    } else {
                        lat = selectedPlace.lat;
                        lng = selectedPlace.lng;
                        latLng[0] = new LatLng(lat, lng);
                    }
                }else {
                    latLng[0] = new LatLng(31.8948203, 34.8092537);
                }
                CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng[0], 17);


                googleMap.addMarker(new MarkerOptions()
                        .position(latLng[0])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(MainFragMap.lat,MainFragMap.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                googleMap.moveCamera(update);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putDouble("lat",lat);
        outState.putDouble("lng",lng);
        super.onSaveInstanceState(outState);
    }
}
