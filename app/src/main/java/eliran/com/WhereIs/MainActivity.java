package eliran.com.WhereIs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Add the main fragment to activity
        MainFragMap mainFragMap=new MainFragMap();
        getFragmentManager().beginTransaction().replace(R.id.MainContainer,mainFragMap).commit();
    }
}
