package skills.future.planer.ui.summary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import skills.future.planer.R;

public class SummaryEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_editor);
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded)
                .setIcon(R.drawable.warning)
                .setTitle(R.string.exit_activity_warning_1)
                .setMessage(R.string.exit_activity_warning_2)
                .setPositiveButton(R.string.agree, (dialog, which) -> finish())
                .setNegativeButton(R.string.disagree, null)
                .show();
    }
}