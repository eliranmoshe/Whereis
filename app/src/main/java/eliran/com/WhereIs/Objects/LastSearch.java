package eliran.com.WhereIs.Objects;

import com.orm.SugarRecord;

/**
 * Created by jbt on 26/04/2017.
 */

public class LastSearch extends SugarRecord {
    private String LastSearch;
    private String IsBeerBySearch;

    public LastSearch() {
    }

    public LastSearch(String lastSearch, String isBeerBySearch) {
        LastSearch = lastSearch;
        IsBeerBySearch = isBeerBySearch;
    }

    public String getLastSearch() {
        return LastSearch;
    }

    public String getIsBeerBySearch() {
        return IsBeerBySearch;
    }

    public void setLastSearch(String lastSearch) {
        LastSearch = lastSearch;
    }

    public void setIsBeerBySearch(String isBeerBySearch) {
        IsBeerBySearch = isBeerBySearch;
    }
}
