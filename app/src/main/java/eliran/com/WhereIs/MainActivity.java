package eliran.com.WhereIs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.orm.SugarContext;

public class MainActivity extends AppCompatActivity implements FragmentChangerInterface{
    MainFragMap mainFragMap;
    public static String IsFirstTime="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(getApplicationContext());
        //Add the main fragment to activity

        if(getFragmentManager().findFragmentByTag("main")== null) {
            mainFragMap = new MainFragMap();
            getFragmentManager().beginTransaction().replace(R.id.MainContainer, mainFragMap, "main").commit();
        }





    }


    @Override
    public void FromMainToMap(Place currentPlace) {

        MapFrag mapFrag=new MapFrag();
        mapFrag.selectedPlace=currentPlace;

        getFragmentManager().beginTransaction().addToBackStack("MapFrag").replace(R.id.MainContainer,mapFrag).commit();

    }

    @Override
    public void FromMainToFavorite() {
        FavoriteFrag favoriteFrag=new FavoriteFrag();
        getFragmentManager().beginTransaction().addToBackStack("FavFrag").replace(R.id.MainContainer,favoriteFrag,"FavFrag").commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.SettingsItem:
                Intent intent=new Intent(this,SettingPref.class);
                startActivity(intent);
                break;
            case R.id.GoToFavoriteItem:
                if (getFragmentManager().findFragmentByTag("FavFrag")==null) {
                    FromMainToFavorite();
                }
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getFragmentManager().findFragmentByTag("map")!= null) {
                mainFragMap.placeRVadapter.notifyDataSetChanged();
                Log.d("sdfdsdsd", "dsdsds");


        }


    }

}
