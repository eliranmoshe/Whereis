package eliran.com.WhereIs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PlaceRVadapter extends RecyclerView.Adapter<PlaceRVadapter.Myholder> {
    Context c;

    //TODO make a constructor to adatper
    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.place_item, null);
        Myholder myholder = new Myholder(v);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        //TODO bring current object frrom list to MyHolder.bindData
    }

    @Override
    public int getItemCount() {
        //TODO return size of list
        return 0;
    }

    class Myholder extends RecyclerView.ViewHolder {

        public Myholder(View itemView) {
            super(itemView);
        }

        //TODO bind data need to recive object
        public void BindData() {

        }
    }
}
