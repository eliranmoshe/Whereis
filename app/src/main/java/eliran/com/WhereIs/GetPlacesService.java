package eliran.com.WhereIs;

import android.app.IntentService;
import android.content.Intent;



public class GetPlacesService extends IntentService {

    public GetPlacesService() {
        super("GetPlacesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double lat=intent.getDoubleExtra("lat",0);
        double lng=intent.getDoubleExtra("lng",0);
        //TODO go to google api and get closest place bu lat lng
    }

}
