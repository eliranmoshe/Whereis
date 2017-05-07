package eliran.com.WhereIs;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetPlacesService extends IntentService {
    String placesString;
    String ByLocationUrl;
    String ByQueryUrl;
    Intent sendBroadcastIntent;
    Bitmap Imagebitmap;
    Bitmap IconBitMap;

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
            ByLocationUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=5000&keyword="+PlaceKind+"&key=AIzaSyB-5ouC8zRtBmVBzmnfYTHD8MwfsT8cC0o";
            placesString=SearchByLocation(ByLocationUrl);

        }else if (IsNeerBy==-1){
            //make Url to query method
            String query=intent.getStringExtra("query");
            ByQueryUrl="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query+"&key=AIzaSyB-5ouC8zRtBmVBzmnfYTHD8MwfsT8cC0o";
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
        sendBroadcastIntent = new Intent("intent.to.MainFragment.FINISH_PLACES");
        if (bigObj.toString()!=null) {
            sendBroadcastIntent.putExtra("JSON_IS_NULL",false);
            String str = bigObj.toString();


            Gson gson = new Gson();

            PlacesList placesList = gson.fromJson(str, PlacesList.class);
       /* for (int i=0;i<placesList.results.size();i++)
        {
            placesList.results.get(i).lat=placesList.results.get(i).geometry.location.lat;
            placesList.results.get(i).lng=placesList.results.get(i).geometry.location.lng;
        }


    }*/
            SugarContext.init(this);
            if (placesList.results.size() != 0) {
                sendBroadcastIntent.putExtra("IsZeroResults", false);
                SugarPlace.deleteAll(SugarPlace.class);
            } else {
                sendBroadcastIntent.putExtra("IsZeroResults", true);
            }
            for (int i = 0; i < placesList.results.size(); i++) {
                if (placesList.results.get(i).photos == null) {
                    placesList.results.get(i).photos = new ArrayList<>();
                    placesList.results.get(i).photos.add(new PlacePhoto());
                    placesList.results.get(i).photos.get(0).photo_reference = "";
                }
                //save image to BitMap with Picasso
                try {
                    Imagebitmap=Picasso.with(this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference=" +  placesList.results.get(i).photos.get(0).photo_reference + "&key=AIzaSyD55SV1_lthkEcI24oLQJ1QWV1q8NcLD5E").get();
                    IconBitMap=Picasso.with(this).load(placesList.results.get(i).icon).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Save last search history to DB
                SugarPlace sugarPlace = new SugarPlace(placesList.results.get(i).name, placesList.results.get(i).vicinity, Functions.encodeToBase64(IconBitMap,Bitmap.CompressFormat.JPEG, 100), placesList.results.get(i).formatted_address, placesList.results.get(i).geometry.location.lat, placesList.results.get(i).geometry.location.lng, Functions.encodeToBase64(Imagebitmap, Bitmap.CompressFormat.JPEG, 100));
                sugarPlace.save();
            }
        }else{
            sendBroadcastIntent.putExtra("JSON_IS_NULL",true);
        }
        //send broadcast to MainFragMap
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
