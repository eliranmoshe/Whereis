package eliran.com.WhereIs.Instruments;


import eliran.com.WhereIs.Objects.Place;

public interface FragmentChangerInterface {

    ///////////interface helper to change fragment to main activity
    public void FromMainToMap(Place currentFood);
    public void FromMainToFavorite();
    public void FromLargeMainToMap();
    public void InflateMapFragment(Place place);
}
