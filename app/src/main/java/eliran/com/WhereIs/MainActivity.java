package eliran.com.WhereIs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.SugarContext;

import eliran.com.WhereIs.Frags.FavoriteFrag;
import eliran.com.WhereIs.Frags.MainFragMap;
import eliran.com.WhereIs.Frags.MapFrag;
import eliran.com.WhereIs.Instruments.FragmentChangerInterface;
import eliran.com.WhereIs.Instruments.SettingPref;
import eliran.com.WhereIs.Objects.Place;

public class MainActivity extends AppCompatActivity implements FragmentChangerInterface {
    MainFragMap mainFragMap;
    public static boolean IsFirstTime;
    public static boolean IsLargeDevice = false;

    IntentFilter ifilter;
    MapFrag mapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(getApplicationContext());
        ///////////defined if is this large or normal device
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.MapContainer);
        if (linearLayout != null) {
            IsLargeDevice = true;
            mapFrag = new MapFrag();
            getFragmentManager().beginTransaction().replace(R.id.MapContainer, mapFrag, "map").commit();
        }
        ///////////Add the main fragment to activity
        if (getFragmentManager().findFragmentByTag("main") == null) {
            mainFragMap = new MainFragMap();
            MainActivity.IsFirstTime = true;
            getFragmentManager().beginTransaction().replace(R.id.MainContainer, mainFragMap, "main").commit();
        }
    }


    @Override
    public void FromMainToMap(Place currentPlace) {
        ///////////transaction the Main Fragment
        MapFrag mapFrag = new MapFrag();
        mapFrag.selectedPlace = currentPlace;
        mapFrag.mylng = mainFragMap.lng;
        mapFrag.mylat = mainFragMap.lat;

        getFragmentManager().beginTransaction().addToBackStack("MapFrag").replace(R.id.MainContainer, mapFrag).commit();

    }

    @Override
    public void FromMainToFavorite() {
        ///////////transaction the Favorite Fragment
        ///////////defined if is this large or normal device and open the correct Xml file
        if (IsLargeDevice == true) {
            FavoriteFrag favoriteFrag = new FavoriteFrag();
            getFragmentManager().beginTransaction().addToBackStack("FavFrag").replace(R.id.LargeMainContainer, favoriteFrag, "FavFrag").commit();
        } else {
            FavoriteFrag favoriteFrag = new FavoriteFrag();
            getFragmentManager().beginTransaction().addToBackStack("FavFrag").replace(R.id.MainContainer, favoriteFrag, "FavFrag").commit();
        }
    }

    @Override
    public void FromLargeMainToMap() {
        mapFrag = new MapFrag();
        getFragmentManager().beginTransaction().replace(R.id.MapContainer, mapFrag, "map").commit();
    }

    @Override
    public void InflateMapFragment(final Place place) {
        ///////////when the user clicked one of the item in places list view
        ///////////open the GoogleMap Fragment
        MapFragment mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.MapContainer, mapFragment).commit();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ///////////set the Lat Lng from the current places clicked
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng latLng = new LatLng(place.getLat(), place.getLng());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(MainFragMap.lat, MainFragMap.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                googleMap.moveCamera(update);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.SettingsItem:
                ///////////go to app settings
                Intent intent = new Intent(this, SettingPref.class);
                startActivity(intent);
                break;
            case R.id.GoToFavoriteItem:
                if (IsLargeDevice == true) {
                    ///////////mean while there is no different screen for large device
                    //TODO add designe screen for large device
                }
                if (getFragmentManager().findFragmentByTag("FavFrag") == null) {
                    FromMainToFavorite();
                }
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        ///////////make method know that is not the first time app up
        MainActivity.IsFirstTime = false;
        super.onBackPressed();


    }
}
