package eliran.com.WhereIs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarContext;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eliran.com.WhereIs.Objects.FavoritePlace;
import eliran.com.WhereIs.Objects.Place;
import eliran.com.WhereIs.Instruments.FragmentChangerInterface;
import eliran.com.WhereIs.Instruments.Functions;
import eliran.com.WhereIs.MainActivity;
import eliran.com.WhereIs.R;


public class PlaceRVadapter extends RecyclerView.Adapter<PlaceRVadapter.Myholder> {
    Context c;
    List<Place> allPlaces;
    double currentLat;
    double currentlng;
    Place currentPlace;
    double distance;


    public PlaceRVadapter(Context c, List<Place> allPlaces, double currentLat, double currentlng) {
        this.c = c;
        this.allPlaces = allPlaces;
        this.currentLat = currentLat;
        this.currentlng = currentlng;
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.place_item, parent, false);
        Myholder myholder = new Myholder(v);

        SugarContext.init(c);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        Place place = (Place) allPlaces.get(position);
        holder.BindData(place);

    }

    @Override
    public int getItemCount() {
        ////take the allPlaces array and sort it by distance////
        Collections.sort(allPlaces, new Comparator<Place>() {

            @Override
            public int compare(Place p1, Place p2) {
                double distance1 = Functions.distance(p1.getLat(),p1.getLng(),currentLat,currentlng);
                double distance2 = Functions.distance(p2.getLat(),p2.getLng(),currentLat,currentlng);
                return Double.compare(distance1,distance2);
            }
        });
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
                    currentPlace = (Place) allPlaces.get(getAdapterPosition());
                    FragmentChangerInterface fragmentChangerInterface = (FragmentChangerInterface) c;
                    if (MainActivity.IsLargeDevice == true) {
                        fragmentChangerInterface.InflateMapFragment(currentPlace);
                    } else {

                        fragmentChangerInterface.FromMainToMap(currentPlace);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    currentPlace = (Place) allPlaces.get(getAdapterPosition());
                    android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(c, v);
                    popupMenu.inflate(R.menu.popup_menu);

                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.AddFavoritePPItem:
                                    Place p = (Place) allPlaces.get(getAdapterPosition());
                                    FavoritePlace place = new FavoritePlace(p.getName(), p.getVicinity(), p.getIcon(), p.getFormatted_address(), p.getLat(), p.getLng(), p.getPhoto_reference());
                                    place.save();
                                    break;
                                case R.id.SharedPlacePPItem:
                                    //share the current location on googleMaps
                                    currentPlace = (Place) allPlaces.get(getAdapterPosition());
                                    String location = "https://www.google.co.il/maps/@" + currentPlace.getLat() + "," + currentPlace.getLng() + ",18.79z?hl=en";
                                    //
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "place Details");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, location);
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
            currentPlace = place;
            distance = Functions.distance(currentLat, currentlng, place.getLat(), place.getLng());
            //add image icon to image view
            if (place.getPhoto_reference().equals("")) {
                itemImageIV.setImageBitmap(Functions.decodeBase64(place.getIcon()));
            } else {
                itemImageIV.setImageBitmap(Functions.decodeBase64(place.getPhoto_reference()));
            }

            itemPlaceNameTV.setText(place.getName());
            //check if is local search addres or global search
            if (place.getVicinity() != null) {
                itemAdressTV.setText(place.getVicinity());
            } else {
                itemAdressTV.setText(place.getFormatted_address());
            }
            //check if in KM or MILE
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
            boolean isCecked = preferences.getBoolean("SwitchKMtoMLItem", false);
            if (isCecked == false) {
                String km = new DecimalFormat("##.##").format(distance);
                itemDistanceTV.setText(km + " km");
            } else if (isCecked == true) {
                double distanceMl = distance / 1.61;
                String ml = new DecimalFormat("##.##").format(distanceMl);
                itemDistanceTV.setText(ml + " mile");
            }
        }
    }
//multiple the distance between current location to current place


}
