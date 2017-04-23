package eliran.com.WhereIs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by eliran on 4/23/2017.
 */

public class FavRVadapter extends RecyclerView.Adapter<FavRVadapter.viewHolder> {
Context context;
    List<Place> FavPlaces;

    public FavRVadapter(Context context, List<Place> favPlaces) {
        this.context = context;
        FavPlaces = favPlaces;
    }

    @Override
    public FavRVadapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.place_item,parent,false );

        viewHolder holder= new viewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavRVadapter.viewHolder holder, int position) {
        Place currentPlace=FavPlaces.get(position);
        holder.BindData(currentPlace);
    }

    @Override
    public int getItemCount() {
        return FavPlaces.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView itemPlaceNameTV;
        TextView itemAdressTV;
        ImageView itemImageIV;
        public viewHolder(View itemView) {
            super(itemView);
            itemPlaceNameTV = (TextView) itemView.findViewById(R.id.ItemPlaceNameTV);
            itemAdressTV = (TextView) itemView.findViewById(R.id.ItemAdressTV);
            itemImageIV = (ImageView) itemView.findViewById(R.id.ItemImageIV);
        }

        public void BindData(Place place){
            Picasso.with(context).load(place.icon).into(itemImageIV);
            itemPlaceNameTV.setText(place.name);
            if (place.vicinity!=null){
                itemAdressTV.setText(place.vicinity);
            }else{
                itemAdressTV.setText(place.formatted_address);
            }
        }
    }
}
