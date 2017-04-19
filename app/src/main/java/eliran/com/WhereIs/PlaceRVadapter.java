package eliran.com.WhereIs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PlaceRVadapter extends RecyclerView.Adapter<PlaceRVadapter.Myholder> {
    Context c;
    ArrayList<Place> allPlaces;
    double currentLat;
    double currentlng;



    public  PlaceRVadapter(Context c,ArrayList<Place> allPlaces,double currentLat,double currentlng){
        this.c=c;
        this.allPlaces=allPlaces;
        this.currentLat=currentLat;
        this.currentlng=currentlng;
    }
    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.place_item, null);
        Myholder myholder = new Myholder(v);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        //TODO bring current object frrom list to MyHolder.bindData
        Place place=allPlaces.get(position);
        holder.BindData(place);

    }

    @Override
    public int getItemCount() {
        //TODO return size of list
        return allPlaces.size();
    }

    class Myholder extends RecyclerView.ViewHolder {

        public Myholder(View itemView) {
            super(itemView);
        }
        TextView itemPlaceNameTV= (TextView) itemView.findViewById(R.id.ItemPlaceNameTV);
        TextView itemDistanceTV= (TextView) itemView.findViewById(R.id.ItemDistanceTV);
        TextView itemAdressTV= (TextView) itemView.findViewById(R.id.ItemAdressTV);
        ImageView itemImageIV= (ImageView) itemView.findViewById(R.id.ItemImageIV);

        //TODO bind data need to recive object
        public void BindData(Place place) {


            itemPlaceNameTV.setText(place.name);
            itemAdressTV.setText(place.vicinity);
            itemDistanceTV.setText(""+distance(currentLat,currentlng,place.geometry.location.lat,place.geometry.location.lng));



        }
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d;
    }

}
