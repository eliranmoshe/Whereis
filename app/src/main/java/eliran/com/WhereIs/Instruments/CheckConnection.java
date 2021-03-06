package eliran.com.WhereIs.Instruments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by eliran on 2/26/2017.
 */

public class CheckConnection {
    private Context context;
    ///////////check if there is a internet connaction
    ///////////return boolean to Main Fragment
    public CheckConnection(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
