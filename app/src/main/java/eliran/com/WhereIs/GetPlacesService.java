package eliran.com.WhereIs;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            ByLocationUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=5000&keyword="+PlaceKind+"&key=AIzaSyD55SV1_lthkEcI24oLQJ1QWV1q8NcLD5E";
            placesString=SearchByLocation(ByLocationUrl);

        }else if (IsNeerBy==-1){
            //make Url to query method
            String query=intent.getStringExtra("query");
            ByQueryUrl="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query+"&key=AIzaSyD55SV1_lthkEcI24oLQJ1QWV1q8NcLD5E";
            placesString=SearchByQuery(ByQueryUrl);
        }
        //get the Json string and add it to array list

     //   placesString=placesString.replace()
        JSONObject bigObj= null;

        try {
             bigObj = new JSONObject(placesString);
            JSONArray results= bigObj.getJSONArray("results");
            for(int i=0; i< results.length();i++)
            {
                JSONObject inner= results.getJSONObject(i);
                inner.remove("id");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str =  bigObj.toString();

        Gson gson =new Gson();

      //  jsonObj.getAsJsonObject("accounts").remove("email");

        PlacesList placesList=gson.fromJson(str,PlacesList.class);
        for (int i=0;i<placesList.results.size();i++)
        {
            placesList.results.get(i).lat=placesList.results.get(i).geometry.location.lat;
            placesList.results.get(i).lng=placesList.results.get(i).geometry.location.lng;
        }
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
