package skills.future.planer.ui.settings;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.TimePickerPreference;

import java.util.Objects;

import skills.future.planer.R;
import skills.future.planer.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_THEME = "themes";
    public static final String KEY_PREF_TIME = "time_picker";
    private SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // change navigation bar color
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends me.philio.preferencecompatextended.PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            setTheme();
        }

        /**
         * Finds preference responsible for themes
         * gets value and sets theme value
         */
        private void setTheme() {
            ListPreference listPreference = findPreference(KEY_PREF_THEME);
            Objects.requireNonNull(listPreference).setOnPreferenceChangeListener((preference, newValue) -> {
                AppCompatDelegate.setDefaultNightMode(Integer.parseInt((String) newValue));
                return true;
            });
        }

        /**
         * Finds preference responsible for time
         */
        private void setTime() {
            TimePickerPreference timePreference = findPreference(KEY_PREF_TIME);
            Objects.requireNonNull(timePreference).setOnPreferenceChangeListener((preference, newValue) -> {

                return true;
            });
        }
    }
}