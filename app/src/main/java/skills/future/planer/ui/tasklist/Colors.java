package skills.future.planer.ui.tasklist;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Colors {

    /**
     * Gets color from preferences by name
     *
     * @param name    - name of preference
     * @param context - app context
     * @return
     */
    public static int getColorFromPreferences(String name, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(name, 0);
    }
}
