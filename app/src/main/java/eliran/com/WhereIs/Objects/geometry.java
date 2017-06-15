package eliran.com.WhereIs.Objects;

/**
 * Created by jbt on 19/04/2017.
 */

public class geometry {
    private location location;
    /////////// helper class for Gson

    public geometry(eliran.com.WhereIs.Objects.location location) {
        this.location = location;
    }

    public eliran.com.WhereIs.Objects.location getLocation() {
        return location;
    }

    public void setLocation(eliran.com.WhereIs.Objects.location location) {
        this.location = location;
    }
}
