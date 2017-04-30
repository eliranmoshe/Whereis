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


    public MapFrag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_map, container, false);

        final LatLng[] latLng = new LatLng[2];
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
                    Toast.makeText(getActivity(), "no place", Toast.LENGTH_SHORT).show();
                }
                latLng[0] = new LatLng(lat, lng);
                latLng[1] = new LatLng(mylat, mylng);
                CameraUpdate update= CameraUpdateFactory.newLatLngZoom(latLng[0], 17);


                googleMap.addMarker(new MarkerOptions()
                        .position(latLng[0])
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(MainFragMap.lat,MainFragMap.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                Polyline polyline=googleMap.addPolyline(new PolylineOptions().add(latLng[0],latLng[1]).width(5).color(Color.BLUE));


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
