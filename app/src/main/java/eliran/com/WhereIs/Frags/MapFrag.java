package eliran.com.WhereIs.Frags;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import eliran.com.WhereIs.Objects.Place;
import eliran.com.WhereIs.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFrag extends Fragment {
    public Place selectedPlace;
    public double mylat;
    public double mylng;
    private double lat;
    private  double lng;
    private LatLng latLng;
    private CameraUpdate update;


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
                        lat = selectedPlace.getLat();
                        lng = selectedPlace.getLng();

                    }
                }else {
                    //Toast.makeText(getActivity(), "no place", Toast.LENGTH_SHORT).show();
                }
                if (selectedPlace!=null) {
                    latLng = new LatLng(selectedPlace.getLat(), selectedPlace.getLng());

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



