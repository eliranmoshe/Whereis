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

import eliran.com.WhereIs.Instruments.Functions;
import eliran.com.WhereIs.Objects.PlacePhoto;
import eliran.com.WhereIs.Objects.PlacesList;
import eliran.com.WhereIs.Objects.SugarPlace;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GetPlacesService extends IntentService {
    private String placesString;
    private String ByLocationUrl;
    private String ByQueryUrl;
    private Intent sendBroadcastIntent;
    private Bitmap Imagebitmap;
    private Bitmap IconBitMap;

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
            ByLocationUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=5000&keyword="+PlaceKind+"&key=AIzaSyCdoGZBMRm9a1L24ql92c4ddS8cTIJUGcU";
            placesString=SearchByLocation(ByLocationUrl);

        }else if (IsNeerBy==-1){
            //make Url to query method
            String query=intent.getStringExtra("query");
            ByQueryUrl="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+query+"&key=AIzaSyCdoGZBMRm9a1L24ql92c4ddS8cTIJUGcU";
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
            //check if there is a results
            PlacesList placesList = gson.fromJson(str, PlacesList.class);
            SugarContext.init(this);
            if (placesList.getResults().size() != 0) {
                sendBroadcastIntent.putExtra("IsZeroResults", false);
                SugarPlace.deleteAll(SugarPlace.class);
            } else {
                sendBroadcastIntent.putExtra("IsZeroResults", true);
            }
            //add the results array to DATABASE
            for (int i = 0; i < placesList.getResults().size(); i++) {
                if (placesList.getResults().get(i).getPhotos() == null) {
                    placesList.getResults().get(i).setPhotos(new ArrayList<PlacePhoto>());
                    placesList.getResults().get(i).getPhotos().add(new PlacePhoto());
                    placesList.getResults().get(i).getPhotos().get(0).setPhoto_reference("");
                }
                //save image to BitMap with Picasso
                try {
                    Imagebitmap=Picasso.with(this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&photoreference=" +  placesList.getResults().get(i).getPhotos().get(0).getPhoto_reference() + "&key=AIzaSyD55SV1_lthkEcI24oLQJ1QWV1q8NcLD5E").get();
                    IconBitMap=Picasso.with(this).load(placesList.getResults().get(i).getIcon()).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Save last search history to DB
                SugarPlace sugarPlace = new SugarPlace(placesList.getResults().get(i).getName(), placesList.getResults().get(i).getVicinity(), Functions.encodeToBase64(IconBitMap,Bitmap.CompressFormat.JPEG, 100), placesList.getResults().get(i).getFormatted_address(), placesList.getResults().get(i).getGeometry().getLocation().getLat(), placesList.getResults().get(i).getGeometry().getLocation().getLng(), Functions.encodeToBase64(Imagebitmap, Bitmap.CompressFormat.JPEG, 100));
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
