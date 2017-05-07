package eliran.com.WhereIs.Instruments;


import eliran.com.WhereIs.Objects.Place;

public interface FragmentChangerInterface {


    public void FromMainToMap(Place currentFood);
    public void FromMainToFavorite();
    public void FromLargeMainToMap();
    public void InflateMapFragment(Place place);
}
