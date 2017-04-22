package eliran.com.WhereIs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FragmentChangerInterface{
    MainFragMap mainFragMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Add the main fragment to activity
       mainFragMap =new MainFragMap();
        getFragmentManager().beginTransaction().add(R.id.MainContainer,mainFragMap).commit();



        //TODO save data on screen rotation
    }


    @Override
    public void changeFragments(Place currentPlace) {
        MapFrag mapFrag=new MapFrag();
        mapFrag.selectedPlace=currentPlace;
        getFragmentManager().beginTransaction().addToBackStack("MapFrag").add(R.id.MainContainer,mapFrag).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId())
        {
            case R.id.SwitchKMtoMLItem:
                //TODO switch to mile and change text
                if (item.getTitle()==getString(R.string.SwitchKMtoMLItem)) {
                    item.setTitle(getString(R.string.SwitchMLtoKM));
                    for (int i=0;i<mainFragMap.allPlaces.size();i++){
                        //TODO change the list distance to miles   add the distance to the place object
                    }


                }else{
                    item.setTitle(getString(R.string.SwitchKMtoMLItem));
                }

                break;
            case R.id.DelFavoriteItem:
                //TODO delete all favorit DB
                break;
        }*/

        return true;
    }

    public static double distance(double lat2, double lng2, double lat1, double lng1) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        d=d/1.61;
        return d;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO get boolean from settings to change km to mile
    }
}
