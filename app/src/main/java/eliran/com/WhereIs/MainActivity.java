package eliran.com.WhereIs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements FragmentChangerInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Add the main fragment to activity
        MainFragMap mainFragMap=new MainFragMap();
        getFragmentManager().beginTransaction().replace(R.id.MainContainer,mainFragMap).commit();



        //TODO save data on screen rotation
    }


    @Override
    public void changeFragments(Place currentPlace) {
        MapFrag mapFrag=new MapFrag();
        mapFrag.selectedPlace=currentPlace;
        getFragmentManager().beginTransaction().replace(R.id.MainContainer,mapFrag).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.SwitchKMtoMLItem:
                //TODO switch to mile and change text
                break;
            case R.id.DelFavoriteItem:
                //TODO delete all favorit DB
                break;
        }

        return true;
    }
}
