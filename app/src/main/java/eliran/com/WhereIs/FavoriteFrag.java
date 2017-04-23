package eliran.com.WhereIs;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        favListRV= (RecyclerView) view.findViewById(R.id.FavListRV);
        List<Place>FavPlaces=FavoritePlace.listAll(Place.class);
       FavRVadapter adapter=new FavRVadapter(getActivity(),FavPlaces);
        favListRV.setAdapter(adapter);

        return view;
    }

}
