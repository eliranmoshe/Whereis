package eliran.com.WhereIs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class PlaceRVadapter extends RecyclerView.Adapter<PlaceRVadapter.Myholder> {
    Context c;
    ArrayList<SearchPlaceSugarOrm> allPlaces;
    double currentLat;
    double currentlng;
    Place currentPlace;
    double distance;




    public  PlaceRVadapter(Context c,ArrayList<SearchPlaceSugarOrm> allPlaces,double currentLat,double currentlng){
        this.c=c;
        this.allPlaces=allPlaces;
        this.currentLat=currentLat;
        this.currentlng=currentlng;
    }
    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.place_item, parent,false);
        Myholder myholder = new Myholder(v);
        MainActivity.IsFirstTime="1";
        SugarContext.init(c);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        Place place= (Place) allPlaces.get(position);
        holder.BindData(place);

    }

    @Override
    public int getItemCount() {
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
                    currentPlace= (Place) allPlaces.get(getAdapterPosition());
                    FragmentChangerInterface fragmentChangerInterface = (FragmentChangerInterface) c;
                    fragmentChangerInterface.FromMainToMap(currentPlace);
                }
            });
           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(c, v);
                   popupMenu.inflate(R.menu.popup_menu);
                   popupMenu.show();
                   popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem item) {
                           switch (item.getItemId()){
                               case R.id.AddFavoritePPItem:
                                   Place p= (Place) allPlaces.get(getAdapterPosition());
                                  FavoritePlace place=new FavoritePlace(1,p.name,p.vicinity,p.icon,p.formatted_address,p.geometry.location.lat,p.geometry.location.lng);
                                   place.save();
                                   break;
                               case R.id.SharedPlacePPItem:
                                   //share the current location on googleMaps
                                   currentPlace= (Place) allPlaces.get(getAdapterPosition());
                                   String location="https://www.google.co.il/maps/@"+currentPlace.geometry.location.lat+","+currentPlace.geometry.location.lng+",18.79z?hl=en";
                                   Intent sharingIntent=new Intent(android.content.Intent.ACTION_SEND);
                                   sharingIntent.setType("text/plain");
                                   sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "place Details");
                                   sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,location );
                                   c.startActivity(sharingIntent);
                                   break;
                           }
                           return true;
                       }
                   });
                   return true;
               }
           });


        }
        public void BindData(Place place) {
            currentPlace=place;
            distance = distance(currentLat, currentlng, place.geometry.location.lat, place.geometry.location.lng);
            //add image icon to image view
            Picasso.with(c).load(place.icon).into(itemImageIV);

            itemPlaceNameTV.setText(place.name);
            //check if is local search addres or global search
            if (place.vicinity!=null){
            itemAdressTV.setText(place.vicinity);
            }else{
                itemAdressTV.setText(place.formatted_address);
            }
            //check if in KM or MILE
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(c);
            boolean isCecked=preferences.getBoolean("SwitchKMtoMLItem",false);
            if (isCecked==false) {
                String km = new DecimalFormat("##.##").format(distance);
                itemDistanceTV.setText(km+" km");
            }else if (isCecked==true){
                double distanceMl=distance/1.61;
                String ml=new DecimalFormat("##.##").format(distanceMl);
                itemDistanceTV.setText(ml+" mile");
            }
        }
    }
//multiple the distance between current location to current place
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
