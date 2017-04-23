package eliran.com.WhereIs;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by eliran on 4/15/2017.
 */

public class Place extends SugarRecord implements Parcelable {
    String name;
    String vicinity;
    String icon;
    String formatted_address;
    geometry geometry;
    double lat;
    double lng;

    public Place() {
    }

    public Place(String name, String vicinity, String icon, String formatted_address, double lat, double lng) {
        this.name = name;
        this.vicinity = vicinity;
        this.icon = icon;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
    }

    protected Place(Parcel in) {
        name = in.readString();
        vicinity = in.readString();
        icon = in.readString();
        formatted_address = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeString(icon);
        dest.writeString(formatted_address);
    }


    //TODO maybe need another int to Miles
    //TODO maybe i need to declarate ImagePath to not be a null String
}
