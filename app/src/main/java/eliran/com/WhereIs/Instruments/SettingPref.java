package eliran.com.WhereIs.Instruments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.widget.Toast;

import eliran.com.WhereIs.Objects.FavoritePlace;
import eliran.com.WhereIs.R;


/**
 * Created by eliran on 4/22/2017.
 */

public class SettingPref extends PreferenceActivity {
    public static boolean IsMile;
    public static SwitchPreference switchPreference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        ///////////set the new value off the KM to MILE CHeckBOx
        switchPreference=(SwitchPreference) findPreference("SwitchKMtoMLItem");
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                IsMile= (boolean) newValue;
                return true;
            }
        });
        ///////////AlertDialog to dell All Favorite
        Preference preference=findPreference("DelFavoriteItem");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingPref.this);

                ///////////create the dialog:
                AlertDialog.Builder dialog = builder
                        .setTitle("warning")
                        .setMessage("ARE YOU SURE YOU WANT TO DELETE ALL FAVORITE PLACES???")
                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FavoritePlace.deleteAll(FavoritePlace.class);
                                Toast.makeText(SettingPref.this, "ALL FAVORITE DELETED...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                ///////////show the dialog:
                AlertDialog TheDialog=dialog.create();
                TheDialog.show();
                TheDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                TheDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);


                return true;
            }
        });

        //TODO make edittext click listener

    }
}
