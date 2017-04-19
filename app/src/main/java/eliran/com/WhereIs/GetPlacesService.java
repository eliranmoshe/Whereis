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
    public GetPlacesService() {
        super("GetPlacesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double lat=intent.getDoubleExtra("lat",0);
        double lng=intent.getDoubleExtra("lng",0);
        String PlaceKind=intent.getStringExtra("PlaceKind");
        //need to check another location another lat another lng
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+32.064342+","+34.7862919+"&radius=500&keyword="+PlaceKind+"&key=AIzaSyAV3E-XQ-Zdze08MwnxBG3gNejgXlNO4MY";

        OkHttpClient client = new OkHttpClient();
        // GET request
        Request request = new Request.Builder()
                .url(url)
                .build();

        placesString="";

        try {
            client.newCall(request).execute();
            Response response = client.newCall(request).execute();
            placesString= response.body().string();

        }catch (IOException interntEX)
        {
        }
        Gson gson=new Gson();
        PlacesList placesList=gson.fromJson(placesString,PlacesList.class);
        Intent sendBroadcastIntent=new Intent("intent.to.MainFragment.FINISH_PLACES");
        sendBroadcastIntent.putParcelableArrayListExtra("response",placesList.results);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBroadcastIntent);



    }

}
