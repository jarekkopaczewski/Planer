package skills.future.planer.ui.settings;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import skills.future.planer.R;
import skills.future.planer.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_THEME = "themes";
    public static final String KEY_PREF_TIME = "time_picker";
    public static String chosenTimeField = "20:00";
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

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        private final Calendar calendar = Calendar.getInstance();

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            setTheme();
            setTimeSummary();
        }

        /**
         * Finds preference responsible for summary time
         */
        private void setTimeSummary() {
            Preference time_picker = findPreference("time_picker");

            TimePickerDialog.OnTimeSetListener time = (timePicker, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                var chosenTime = formatter.format(calendar.getTime());
                Objects.requireNonNull(time_picker).setTitle("Godzina podsumowania: " + chosenTime);
                time_picker.setDefaultValue(chosenTime);
                chosenTimeField = chosenTime;
            };

            Objects.requireNonNull(time_picker).setOnPreferenceClickListener(preference -> {
                new TimePickerDialog(getContext(),
                        time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        true).show();
                return true;
            });
        }

        /**
         * Finds preference responsible for themes
         * gets value and sets theme value
         */
        private void setTheme() {
            ListPreference listPreference = findPreference("themes");
            Objects.requireNonNull(listPreference).setOnPreferenceChangeListener((preference, newValue) -> {
                AppCompatDelegate.setDefaultNightMode(Integer.parseInt((String) newValue));
                return true;
            });
        }
    }
}