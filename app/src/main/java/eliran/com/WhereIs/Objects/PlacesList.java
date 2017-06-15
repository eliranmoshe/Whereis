package eliran.com.WhereIs.Objects;

import java.util.ArrayList;

/**
 * Created by jbt on 19/04/2017.
 */

public class PlacesList  {
    private ArrayList<Place> results;
    /////////// helper class for Gson
    public ArrayList<Place> getResults() {
        return results;
    }

    public void setResults(ArrayList<Place> results) {
        this.results = results;
    }
}
