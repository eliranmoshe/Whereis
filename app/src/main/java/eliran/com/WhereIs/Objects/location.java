package eliran.com.WhereIs.Objects;

/**
 * Created by jbt on 19/04/2017.
 */

public class location {
    private double lat;
    private double lng;

    public location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
