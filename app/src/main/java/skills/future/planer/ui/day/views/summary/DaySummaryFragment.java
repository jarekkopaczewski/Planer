package skills.future.planer.ui.day.views.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.codesgood.views.JustifiedTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.util.List;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentSummaryBinding;
import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.tools.DatesParser;
import skills.future.planer.ui.month.MonthFragment;

public class DaySummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;
    private JustifiedTextView editText1;
    private JustifiedTextView editText2;
    private JustifiedTextView editText3;
    private NestedScrollView nestedScrollView3;
    private FloatingActionButton editButton;
    private int height;
    private boolean state; // true - edit, false - view
    private SummaryViewModel summaryViewModel;
    private List<SummaryData> summaryData;
    private RecyclerView recyclerView;
    private DaySummaryAdapter daySummaryAdapter;
    private DaySummaryViewModel daySummaryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);
        daySummaryAdapter = new DaySummaryAdapter();
        daySummaryViewModel = new DaySummaryViewModel();
        View root = binding.getRoot();
        daySummaryViewModel = ViewModelProviders.of(this).get(DaySummaryViewModel.class);
        DaySummaryViewModel.setSummaryViewModel(summaryViewModel);
        DaySummaryViewModel.setDaySummaryAdapter(daySummaryAdapter);
        DaySummaryViewModel.setLifecycleOwner(getViewLifecycleOwner());
        summaryData = daySummaryAdapter.getSummaryDataList();
        System.out.println(summaryData);
        System.out.println("summary");


        editText1 = binding.summaryEditText;
        editText2 = binding.summaryEditText2;
        editText3 = binding.summaryEditText3;

        //System.out.println("elo");
        CalendarDay calendarDay = MonthFragment.getGlobalSelectedDate();
        LocalDate localDate = DatesParser.toLocalDate(calendarDay);
        List<SummaryData> summary = summaryViewModel.getSummary(localDate, SummaryType.weekSummary);

        editText1.setText(summary.get(0).getAchievements());
        editText2.setText(summary.get(0).getNotFinished());
        editText3.setText(summary.get(0).getToWorkOut());
        System.out.println(summary);


        editButton = binding.editSummaryFab;
        editButton.setOnClickListener(e->{
            if(state) saveClick();
            else editClick();
        });
        return root;
    }

    private void saveClick()
    {
        state = false;
        setEditable(false);
        editButton.setForeground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.pencil2, null));
        Toast.makeText(getContext(), "Dane zapisane.", Toast.LENGTH_SHORT).show();
        // save to database
    }

    private void editClick()
    {
        state = true;
        setEditable(true);
        editButton.setForeground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.save, null));
        Toast.makeText(getContext(), "Edytujesz podsumowanie.", Toast.LENGTH_SHORT).show();
    }

    private void setEditable(boolean value)
    {
        editText1.setEnabled(value);
        editText2.setEnabled(value);
        editText3.setEnabled(value);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}