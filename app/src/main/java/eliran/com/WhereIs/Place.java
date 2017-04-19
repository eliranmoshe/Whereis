package eliran.com.WhereIs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eliran on 4/15/2017.
 */

public class Place implements Parcelable {
    String name;
    String vicinity;
    String icon;
    geometry geometry;

    protected Place(Parcel in) {
        name = in.readString();
        vicinity = in.readString();
        icon = in.readString();
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
    }
    //TODO maybe need another int to Miles
    //TODO maybe i need to declarate ImagePath to not be a null String
}
