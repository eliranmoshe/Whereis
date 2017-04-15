package eliran.com.WhereIs;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
        //TODO need to check another location another lat another lng
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=500&keyword=sushi&key=AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ";

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
        Intent sendBroadcastIntent=new Intent("intent.to.MainFragment.FINISH_PLACES");
        Log.d("aaaaaaaaaaaa",placesString);
        sendBroadcastIntent.putExtra("response",placesString);
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBroadcastIntent);


    }

}
