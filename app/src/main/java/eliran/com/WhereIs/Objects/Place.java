package eliran.com.WhereIs.Objects;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Place implements Parcelable {

    /////////// helper class for Gson
    private String name;
    private String vicinity;
    private String icon;
    private String formatted_address;
    private double lng;
    private String photo_reference;
    private geometry geometry;
    private ArrayList<PlacePhoto>photos;
    private double lat;

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

    public double getLng() {
        return lng;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public eliran.com.WhereIs.Objects.geometry getGeometry() {
        return geometry;
    }

    public ArrayList<PlacePhoto> getPhotos() {
        return photos;
    }

    public double getLat() {
        return lat;
    }

    public static Creator<Place> getCREATOR() {
        return CREATOR;
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

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public void setGeometry(eliran.com.WhereIs.Objects.geometry geometry) {
        this.geometry = geometry;
    }

    public void setPhotos(ArrayList<PlacePhoto> photos) {
        this.photos = photos;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
