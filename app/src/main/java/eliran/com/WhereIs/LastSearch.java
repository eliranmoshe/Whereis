package eliran.com.WhereIs;

import com.orm.SugarRecord;

/**
 * Created by jbt on 26/04/2017.
 */

public class LastSearch extends SugarRecord {
    String LastSearch;
    String IsBeerBySearch;

    public LastSearch() {
    }

    public LastSearch(String lastSearch, String isBeerBySearch) {
        LastSearch = lastSearch;
        IsBeerBySearch = isBeerBySearch;
    }


}
