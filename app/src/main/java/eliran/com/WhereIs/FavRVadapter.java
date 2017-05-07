package eliran.com.WhereIs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //delete current favorite place with alert dialog
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("ARE YOU SURE YOU WANT TO DELETE THIS PLACE???")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    List<FavoritePlace> favoritePlaces=FavoritePlace.listAll(FavoritePlace.class);
                                    FavoritePlace favoritePlace=favoritePlaces.get(getAdapterPosition());
                                    FavoritePlace currentfavoriteplace=FavoritePlace.findById(FavoritePlace.class,favoritePlace.getId());
                                    currentfavoriteplace.delete();
                                    Intent intent=new Intent("DELETE_FAVPLACE_FINISH");
                                    intent.putExtra("IsDelete",true);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("KEEP MOVIE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert.setTitle("WARNING");
                    alert.create().show();

                    return true;

                }
            });
            itemPlaceNameTV = (TextView) itemView.findViewById(R.id.ItemPlaceNameTV);
            itemAdressTV = (TextView) itemView.findViewById(R.id.ItemAdressTV);
            itemImageIV = (ImageView) itemView.findViewById(R.id.ItemImageIV);
        }

        public void BindData(FavoritePlace place){
            if (place.photo_reference.equals(""))
            {
                itemImageIV.setImageBitmap(Functions.decodeBase64(place.icon));
            }else {
                itemImageIV.setImageBitmap(Functions.decodeBase64(place.photo_reference));
            }
            itemPlaceNameTV.setText(place.name);
            if (place.vicinity!=null){
                itemAdressTV.setText(place.vicinity);
            }else{
                itemAdressTV.setText(place.formatted_address);
            }
        }
    }


}
