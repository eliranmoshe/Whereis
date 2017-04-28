package eliran.com.WhereIs;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by eliran on 4/15/2017.
 */

public class Place implements Parcelable {
    String name;
    String vicinity;
    String icon;
    String formatted_address;
    double lat;
    double lng;
    String photo_reference;
    geometry geometry;
    ArrayList<PlacePhoto>photos;

    public Place(String name, String vicinity, String icon, String formatted_address, double lat, double lng, String photo_reference) {
        this.name = name;
        this.vicinity = vicinity;
        this.icon = icon;
        this.formatted_address = formatted_address;
        this.lat = lat;
        this.lng = lng;
        this.photo_reference = photo_reference;
    }


    protected Place(Parcel in) {
        name = in.readString();
        vicinity = in.readString();
        icon = in.readString();
        formatted_address = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        photo_reference = in.readString();
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
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(photo_reference);
    }
}
