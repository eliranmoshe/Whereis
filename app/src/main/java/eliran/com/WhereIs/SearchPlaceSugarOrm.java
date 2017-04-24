package eliran.com.WhereIs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

/**
 * Created by jbt on 24/04/2017.
 */

public class SearchPlaceSugarOrm extends SugarRecord implements Parcelable{
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

    protected SearchPlaceSugarOrm(Parcel in) {
        name = in.readString();
        vicinity = in.readString();
        icon = in.readString();
        formatted_address = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<SearchPlaceSugarOrm> CREATOR = new Creator<SearchPlaceSugarOrm>() {
        @Override
        public SearchPlaceSugarOrm createFromParcel(Parcel in) {
            return new SearchPlaceSugarOrm(in);
        }

        @Override
        public SearchPlaceSugarOrm[] newArray(int size) {
            return new SearchPlaceSugarOrm[size];
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
    }
}
