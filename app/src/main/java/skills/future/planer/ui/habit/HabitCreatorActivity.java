package skills.future.planer.ui.habit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Arrays;

import skills.future.planer.R;
import skills.future.planer.databinding.ActivityHabitCreatorBinding;
import skills.future.planer.databinding.ActivityMainBinding;
import skills.future.planer.ui.settings.SettingsActivity;


public class HabitCreatorActivity extends AppCompatActivity {

    private ActivityHabitCreatorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitCreatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PowerSpinnerView powerSpinnerView = binding.spinner;
        powerSpinnerView.selectItemByIndex(0);

        setTitle("Kreator nawyków");

    }
}