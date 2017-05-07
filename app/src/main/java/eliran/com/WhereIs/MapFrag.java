package eliran.com.WhereIs;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFrag extends Fragment {
public  Place selectedPlace;
    double lat;
    double lng;
    double mylat;
    double mylng;
    LatLng latLng;
    CameraUpdate update;


    public MapFrag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_map, container, false);
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
                        mylat=savedInstanceState.getDouble("mylat");
                        mylng=savedInstanceState.getDouble("mylng");
                    } else {
                        lat = selectedPlace.lat;
                        lng = selectedPlace.lng;

                    }
                }else {
                    //Toast.makeText(getActivity(), "no place", Toast.LENGTH_SHORT).show();
                }
                if (selectedPlace!=null) {
                    latLng = new LatLng(selectedPlace.lat, selectedPlace.lng);

                }else {
                    latLng=new LatLng(MainFragMap.lat,MainFragMap.lng);
                }
                update = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
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
        outState.putDouble("mylat",mylat);
        outState.putDouble("mylng",mylng);
        super.onSaveInstanceState(outState);
    }
}



