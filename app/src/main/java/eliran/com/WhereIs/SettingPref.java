package eliran.com.WhereIs;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

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
        switchPreference=(SwitchPreference) findPreference("SwitchKMtoMLItem");
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                IsMile= (boolean) newValue;
                return true;
            }
        });
        //TODO make edittext click listener

    }
}
