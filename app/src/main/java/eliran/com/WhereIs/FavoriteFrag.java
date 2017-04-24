package eliran.com.WhereIs;


import android.os.Bundle;
import android.app.Fragment;
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


    public FavoriteFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_favorite, container, false);
        SugarContext.init(getActivity().getApplicationContext());
        List<FavoritePlace> favoritePlaces=new ArrayList<>();
        favListRV= (RecyclerView) view.findViewById(R.id.FavListRV);
        favListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<FavoritePlace>FavPlaces=FavoritePlace.listAll(FavoritePlace.class);
        for (int i=0;i<FavPlaces.size();i++)
        {
            if (FavPlaces.get(i).isFavorite==1)
            {
               favoritePlaces.add(FavPlaces.get(i));
            }
        }
       FavRVadapter adapter=new FavRVadapter(getActivity(),favoritePlaces);
        favListRV.setAdapter(adapter);

        return view;
    }

}
