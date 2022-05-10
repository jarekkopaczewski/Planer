package skills.future.planer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import lombok.SneakyThrows;
import skills.future.planer.databinding.ActivityMainBinding;
import skills.future.planer.db.AppDatabase;
import skills.future.planer.notification.NotificationService;
import skills.future.planer.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {


    private static BottomNavigationView bottomView;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;


    /**
     * Displays version of application in "Settings menu"
     * versionName - parameter in build.gradle (Module)
     */
    private void displayVersionOfApplication(Menu menu) {
        try {
            menu.findItem(R.id.action_version).setTitle("Wersja: " +
                    getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        themePreferences();

        createService();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // bind views
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        bottomView = binding.appBarMain.bottomBar;
        MaterialToolbar toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        var navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        // set up corner menu
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow).setOpenableLayout(drawer).build();
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // set up bottom bar
        AppBarConfiguration bottomAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_month, R.id.nav_day, R.id.nav_task_list).build();
        NavController bottomNavController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, bottomNavController, bottomAppBarConfiguration);
        NavigationUI.setupWithNavController(bottomView, bottomNavController);

        AppDatabase appDatabase = AppDatabase.getInstance(this);

        // change navigation bar color
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        // sets up menu button
        ImageView button = binding.appBarMain.testButton;
        DrawerLayout navDrawer = binding.drawerLayout;

        button.setOnClickListener(e -> {
            if (!navDrawer.isDrawerOpen(Gravity.LEFT)) navDrawer.openDrawer(Gravity.LEFT);
            else navDrawer.closeDrawer(Gravity.RIGHT);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //startService(new Intent(this, NotificationService.class));
    }

    // set/read settings
    private void themePreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPref.getString(SettingsActivity.KEY_PREF_THEME, "-1");
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(theme));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        displayVersionOfApplication(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings -> {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static BottomNavigationView getBottomView() {
        return bottomView;
    }

    /**
     * Binding service to run in background
     */
    private NotificationService notificationService;

    private void createService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            notificationService = ((NotificationService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            notificationService = null;
        }
    };
}
