package eliran.com.WhereIs;

import com.orm.SugarRecord;

/**
 * Created by jbt on 24/04/2017.
 */

public class SearchPlaceSugarOrm extends SugarRecord {
    String name;
    String vicinity;
    String icon;
    String formatted_address;
    double lat;
    double lng;
    public SearchPlaceSugarOrm(){

    }

    public SearchPlaceSugarOrm(String name, String vicinity, String icon, String formatted_address, double lat, double lng) {
        this.name = name;
        this.vicinity = vicinity;
        this.icon = icon;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
    }
}
