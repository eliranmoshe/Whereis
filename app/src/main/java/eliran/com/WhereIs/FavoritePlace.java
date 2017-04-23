package eliran.com.WhereIs;

import com.orm.SugarRecord;

/**
 * Created by eliran on 4/23/2017.
 */

public class FavoritePlace extends SugarRecord{
    String name;
    String vicinity;
    String icon;
    String formatted_address;
    double lat;
    double lng;
    public FavoritePlace(){

    }

    public FavoritePlace(String name, String vicinity, String icon, String formatted_address, double lat, double lng) {
        this.name = name;
        this.vicinity = vicinity;
        this.icon = icon;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
    }
}
