package eliran.com.WhereIs;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFrag extends Fragment {

RecyclerView favListRV;
    FavRVadapter adapter;


    public FavoriteFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_favorite, container, false);
        SugarContext.init(getActivity().getApplicationContext());
        IntentFilter intentFilter=new IntentFilter("DELETE_FAVPLACE_FINISH");
        DeleteReciver deleteReciver=new DeleteReciver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteReciver,intentFilter);

        favListRV= (RecyclerView) view.findViewById(R.id.FavListRV);
        favListRV.setLayoutManager(new LinearLayoutManager(getActivity()));

       adapter=new FavRVadapter(getActivity(),GetArrayToAdapter());
        favListRV.setAdapter(adapter);

        return view;
    }
    class DeleteReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("IsDelete",false))
            {
                adapter=new FavRVadapter(getActivity(),GetArrayToAdapter());
                favListRV.setAdapter(adapter);
            }
        }
    }
    public List<FavoritePlace> GetArrayToAdapter()
    {
        List<FavoritePlace> favoritePlaces=new ArrayList<>();
        List<FavoritePlace>FavPlaces=FavoritePlace.listAll(FavoritePlace.class);
        for (int i=0;i<FavPlaces.size();i++)
        {

            favoritePlaces.add(FavPlaces.get(i));

        }
        return favoritePlaces;
    }

}
