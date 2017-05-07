package eliran.com.WhereIs.Objects;

import com.orm.SugarRecord;

/**
 * Created by eliran on 4/23/2017.
 */

public class FavoritePlace extends SugarRecord{
    private String name;
    private String vicinity;
    private String icon;
    private String formatted_address;
    private double lat;
    private double lng;
    private String photo_reference;

    public FavoritePlace(){}

    public FavoritePlace(String name, String vicinity, String icon, String formatted_address, double lat, double lng, String photo_reference) {
        this.name = name;
        this.vicinity = vicinity;
        this.icon = icon;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
        this.photo_reference = photo_reference;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getIcon() {
        return icon;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }
}
