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

public class FavRVadapter extends RecyclerView.Adapter<FavRVadapter.MyViewHolder> {
Context context;
    List<FavoritePlace> favPlaces;

    public FavRVadapter(Context context, List<FavoritePlace> favPlaces) {
        this.context = context;
       this.favPlaces = favPlaces;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.place_item, parent,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FavoritePlace favoritePlace=favPlaces.get(position);
        holder.BindData(favoritePlace);

    }

    @Override
    public int getItemCount() {
        return favPlaces.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemPlaceNameTV;
        TextView itemAdressTV;
        ImageView itemImageIV;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemPlaceNameTV = (TextView) itemView.findViewById(R.id.ItemPlaceNameTV);
            itemAdressTV = (TextView) itemView.findViewById(R.id.ItemAdressTV);
            itemImageIV = (ImageView) itemView.findViewById(R.id.ItemImageIV);
        }

        public void BindData(FavoritePlace place){
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
