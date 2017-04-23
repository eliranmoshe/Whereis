package eliran.com.WhereIs;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetPlacesService extends IntentService {
    String placesString;
    String ByLocationUrl;
    String ByQueryUrl;

    public GetPlacesService() {
        super("GetPlacesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int IsNeerBy=intent.getIntExtra("IsNeerBy",0);
        if (IsNeerBy==1) {
            //make Url to local method
            double lat = intent.getDoubleExtra("lat", 0);
            double lng = intent.getDoubleExtra("lng", 0);
            String PlaceKind = intent.getStringExtra("PlaceKind");
            ByLocationUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+31.8903396+","+34.773063+"&radius=5000&keyword="+PlaceKind+"&key=AIzaSyAV3E-XQ-Zdze08MwnxBG3gNejgXlNO4MY";
            placesString=SearchByLocation(ByLocationUrl);

        }else if (IsNeerBy==-1){
            //make Url to query method
            String query=intent.getStringExtra("query");
            ByQueryUrl="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query+"&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ";
            placesString=SearchByQuery(ByQueryUrl);
        }
        //get the Json string and add it to array list
        Gson gson=new Gson();
        PlacesList placesList=gson.fromJson(placesString,PlacesList.class);
        Intent sendBroadcastIntent=new Intent("intent.to.MainFragment.FINISH_PLACES");
        sendBroadcastIntent.putParcelableArrayListExtra("response",placesList.results);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBroadcastIntent);

    }
    public String SearchByLocation(String bylocationurl){
        //go to web to get json by searching local
        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url(bylocationurl)
                .build();

        placesString="";

        try {
            client.newCall(request).execute();
            Response response = client.newCall(request).execute();
            placesString= response.body().string();

        }catch (IOException interntEX)
        {
        }
        return placesString;
    }
    public String SearchByQuery(String byqueryurl){
        //go to web to get json by searching global
        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url(byqueryurl)
                .build();

        placesString="";

        try {
            client.newCall(request).execute();
            Response response = client.newCall(request).execute();
            placesString= response.body().string();

        }catch (IOException interntEX)
        {
        }
        return placesString;
    }


}
