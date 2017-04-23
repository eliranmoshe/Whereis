package eliran.com.WhereIs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        //Add the main fragment to activity
       mainFragMap =new MainFragMap();
        getFragmentManager().beginTransaction().add(R.id.MainContainer,mainFragMap).commit();

        SugarContext.init(getApplicationContext());


        //TODO save data on screen rotation
    }


    @Override
    public void FromMainToMap(Place currentPlace) {

        MapFrag mapFrag=new MapFrag();
        mapFrag.selectedPlace=currentPlace;
        getFragmentManager().beginTransaction().addToBackStack("MapFrag").add(R.id.MainContainer,mapFrag).commit();

    }

    @Override
    public void FromMainToFavorite() {

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

        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (IsFirstTime.equals("1")) {
            mainFragMap.placeRVadapter.notifyDataSetChanged();
            //TODO on landspace there is exeption
        }


    }

}
