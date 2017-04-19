package eliran.com.WhereIs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class PlaceRVadapter extends RecyclerView.Adapter<PlaceRVadapter.Myholder> {
    Context c;
    ArrayList<Place> allPlaces;
    double currentLat;
    double currentlng;
    Place currentPlace;



    public  PlaceRVadapter(Context c,ArrayList<Place> allPlaces,double currentLat,double currentlng){
        this.c=c;
        this.allPlaces=allPlaces;
        this.currentLat=currentLat;
        this.currentlng=currentlng;
    }
    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.place_item, parent,false);
        Myholder myholder = new Myholder(v);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        //TODO bring current object frrom list to MyHolder.bindData
        Place place=allPlaces.get(position);
        currentPlace=place;
        holder.BindData(place);

    }

    @Override
    public int getItemCount() {
        //TODO return size of list
        return allPlaces.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView itemPlaceNameTV;
        TextView itemDistanceTV;
        TextView itemAdressTV;
        ImageView itemImageIV;
        public Myholder(final View itemView) {
            super(itemView);

            itemPlaceNameTV = (TextView) itemView.findViewById(R.id.ItemPlaceNameTV);
            itemDistanceTV = (TextView) itemView.findViewById(R.id.ItemDistanceTV);
             itemAdressTV = (TextView) itemView.findViewById(R.id.ItemAdressTV);
            itemImageIV = (ImageView) itemView.findViewById(R.id.ItemImageIV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // FragmentChangerInterface fragmentChangerInterface = (FragmentChangerInterface) c;
                   // fragmentChangerInterface.changeFragments(currentPlace);
                    MapFrag mapFrag=new MapFrag();
                    FragmentTransaction transaction = ((Activity) c).getFragmentManager().beginTransaction();
                    transaction.replace(R.id.MapContainer, mapFrag);
                    //TODO check why it doesnt replace the fragments
                }
            });

        }
        //TODO bind data need to recive object
        public void BindData(Place place) {


            itemPlaceNameTV.setText(place.name);
            itemAdressTV.setText(place.vicinity);
            double distance=distance(currentLat,currentlng,place.geometry.location.lat,place.geometry.location.lng);
            String km=new DecimalFormat("##.##").format(distance);
            itemDistanceTV.setText(km+"km");
            Picasso.with(c).load(place.icon).into(itemImageIV);



        }
    }

    public static double distance(double lat2, double lng2, double lat1, double lng1) {
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
