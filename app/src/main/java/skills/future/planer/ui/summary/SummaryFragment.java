package skills.future.planer.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import skills.future.planer.databinding.FragmentSummaryBrowserBinding;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.ui.AnimateView;
import skills.future.planer.ui.summary.adapter.SummaryTotalAdapter;

public class SummaryFragment extends Fragment {

    private SummaryViewModel summaryViewModel;
    private FragmentSummaryBrowserBinding binding;
    private SummaryTotalAdapter summaryTotalAdapter;
    private com.shawnlin.numberpicker.NumberPicker yearPicker;

    public SummaryFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSummaryBrowserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
        yearPicker = binding.yearPicker;

        RecyclerView summaryBrowserRecycler = binding.summaryBrowserRecycler;
        summaryTotalAdapter = new SummaryTotalAdapter(this.getContext(), this);
        summaryBrowserRecycler.setAdapter(summaryTotalAdapter);
        summaryBrowserRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        loadSummaries();
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> loadSummaries());

        // set min/max value in year picker
        yearPicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR) + 1);
        if(summaryViewModel.getMinimumYear()!=0)
            yearPicker.setMinValue(summaryViewModel.getMinimumYear() - 1);
        else
            yearPicker.setMinValue(Calendar.getInstance().get(Calendar.YEAR) - 1);

        AnimateView.animateInOut(yearPicker, getContext());
        return root;
    }

    /**
     * Loads new months summaries after picker change
     */
    private void loadSummaries() {
        int currentYear = yearPicker.getValue();
        summaryTotalAdapter.clear();
        try {
            summaryTotalAdapter.setData(summaryViewModel.getMonthsFromYearSummary(currentYear));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}