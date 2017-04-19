package eliran.com.WhereIs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    public void changeFragments(Place currentFood) {
        MapFrag mapFrag=new MapFrag();
        getFragmentManager().beginTransaction().replace(R.id.MainContainer,mapFrag).commit();

    }
}
