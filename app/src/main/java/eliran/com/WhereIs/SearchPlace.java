package eliran.com.WhereIs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

/**
 * Created by jbt on 24/04/2017.
 */

public class SearchPlace extends SugarRecord {
     String name;
        String vicinity;
     String icon;
     String formatted_address;
     double lat;
     double lng;


    public SearchPlace(){

    }

    public SearchPlace(String name, String vicinity, String icon, String formatted_address, double lat, double lng) {
        this.name = name;
        this.vicinity = vicinity;
        this.icon = icon;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
    }

}
