package skills.future.planer.ui.summary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import skills.future.planer.R;
import skills.future.planer.databinding.ActivitySummaryEditorBinding;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.ui.AnimateView;

public class SummaryEditorActivity extends AppCompatActivity {

    private final SimpleDateFormat formatterDate = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private boolean isEdited = false;
    private FloatingActionButton editSummaryFab;
    private Bundle parameters;
    private SummaryData summaryData;
    private EditText summaryAchievementsText;
    private EditText summaryNotFinishedText;
    private EditText summaryToWorkOutText;
    private TextView summaryTitleText;
    private TextView summaryTitle;
    private ImageView isEditedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var binding = ActivitySummaryEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getColor(R.color.navigationBarColor));

        // binding
        editSummaryFab = binding.editSummaryFab2;
        summaryAchievementsText = binding.summaryAchievementsText2;
        summaryNotFinishedText = binding.summaryNotFinishedText2;
        summaryToWorkOutText = binding.summaryToWorkOutText2;
        summaryTitleText = binding.summaryTitleText;
        isEditedImage = binding.isEditedImage;
        summaryTitle = binding.summaryTitle;

        this.parameters = getIntent().getExtras();
        setUpValues();

        setTextListener(summaryAchievementsText);
        setTextListener(summaryNotFinishedText);
        setTextListener(summaryToWorkOutText);

        setUpFabListener();
    }

    /**
     * Sets up save listener
     */
    private void setUpFabListener() {
        editSummaryFab.setOnClickListener(e -> {
            AnimateView.singleAnimation(editSummaryFab, this, R.anim.editsummary);
            AnimateView.singleAnimation(isEditedImage, this, R.anim.editsummary);
            summaryData.setAchievements(summaryAchievementsText.getText().toString());
            summaryData.setNotFinished(summaryNotFinishedText.getText().toString());
            summaryData.setToWorkOut(summaryToWorkOutText.getText().toString());
            isEdited = false;
            isEditedImage.setVisibility(View.INVISIBLE);
            editSummaryFab.hide();
            new ViewModelProvider(this).get(SummaryViewModel.class).edit(summaryData);
        });
    }

    /**
     * Adds listener to change text
     *
     * @param view EditText
     */
    private void setTextListener(EditText view) {
        Context context = this;
        view.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isEdited) {
                    editSummaryFab.show();
                    isEditedImage.setVisibility(View.VISIBLE);
                    AnimateView.singleAnimation(editSummaryFab, context, R.anim.editsummary2);
                    AnimateView.singleAnimation(isEditedImage, context, R.anim.editsummary2);
                }
                isEdited = true;
            }
        });
    }

    /**
     * pass values of summary from database to text views
     */
    @SuppressLint("SetTextI18n")
    private void setUpValues() {
        editSummaryFab.hide();
        // check edit
        if (!parameters.getBoolean("editable")) {
            summaryAchievementsText.setEnabled(false);
            summaryNotFinishedText.setEnabled(false);
            summaryToWorkOutText.setEnabled(false);
        } else {
            AnimateView.singleAnimation(editSummaryFab, this, R.anim.downup);
        }

        summaryData = new ViewModelProvider(this).get(SummaryViewModel.class).findById(parameters.getLong("summaryId"));

        summaryAchievementsText.setText(summaryData.getAchievements());
        summaryNotFinishedText.setText(summaryData.getNotFinished());
        summaryToWorkOutText.setText(summaryData.getToWorkOut());

        if (summaryData.getSummaryType() == SummaryType.monthSummary) {
            var text = this.getResources().getStringArray(R.array.months)[summaryData.getMonth() - 1];
            String upper = text.substring(0, 1).toUpperCase() + text.substring(1);
            summaryTitleText.setText(upper + " " + summaryData.getYear());
            summaryTitle.setText(R.string.achievments_month);

        } else {
            Calendar date = Calendar.getInstance();
            date.set(summaryData.getYear(), summaryData.getMonth(), 1);
            date.set(Calendar.WEEK_OF_YEAR, summaryData.getWeekNumber());
            date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            Calendar date2 = Calendar.getInstance();
            date2.set(summaryData.getYear(), summaryData.getMonth(), 1);
            date2.set(Calendar.WEEK_OF_YEAR, summaryData.getWeekNumber());
            date2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            summaryTitleText.setText("Od " + formatterDate.format(date.getTime()) + " do " + formatterDate.format(date2.getTime()) + " - " + summaryData.getYear() + "r");
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        if (isEdited) {
            new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded)
                    .setIcon(R.drawable.warning)
                    .setTitle(R.string.exit_activity_warning_1)
                    .setMessage(R.string.exit_activity_warning_2)
                    .setPositiveButton(R.string.agree, (dialog, which) -> finish())
                    .setNegativeButton(R.string.disagree, null)
                    .show();
        } else {
            finish();
        }
    }
}