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

import com.shawnlin.numberpicker.NumberPicker;

import skills.future.planer.databinding.FragmentSummaryBrowserBinding;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.ui.summary.adapter.SummaryTotalAdapter;

public class SummaryFragment extends Fragment {

    private SummaryViewModel summaryViewModel;
    private FragmentSummaryBrowserBinding binding;
    private RecyclerView summaryBrowserRecycler;
    private SummaryTotalAdapter summaryTotalAdapter;
    private NumberPicker yearPicker;
    private int currentYear;

    public SummaryFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSummaryBrowserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        yearPicker = binding.yearPicker;

        summaryBrowserRecycler = binding.summaryBrowserRecycler;
        summaryTotalAdapter = new SummaryTotalAdapter(this.getContext(), this);
        summaryBrowserRecycler.setAdapter(summaryTotalAdapter);
        summaryBrowserRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        loadSummaries();
        yearPicker.setOnValueChangedListener((X,D,DD)-> loadSummaries());

        return root;
    }

    /**
     * Loads new months summaries after picker change
     */
    private void loadSummaries()
    {
        currentYear = yearPicker.getValue();
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