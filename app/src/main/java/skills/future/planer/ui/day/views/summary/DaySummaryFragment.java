package skills.future.planer.ui.day.views.summary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import skills.future.planer.databinding.FragmentSummaryBinding;
import skills.future.planer.db.summary.SummaryViewModel;
import skills.future.planer.ui.summary.SummaryEditorActivity;

public class DaySummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;
    private FloatingActionButton editButton;
    private boolean state; // true - edit, false - view
    private SummaryViewModel summaryViewModel;
    private DaySummaryAdapter daySummaryAdapter;
    private DaySummaryViewModel daySummaryViewModel;
    private Context context;
    private RecyclerView summaryList;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        daySummaryViewModel = new ViewModelProvider(this).get(DaySummaryViewModel.class);
        summaryViewModel = ViewModelProviders.of(this).get(SummaryViewModel.class);
        daySummaryViewModel.setSummaryViewModel(summaryViewModel);
        context = requireContext();
        binding = FragmentSummaryBinding.inflate(inflater, container, false);

        daySummaryAdapter = new DaySummaryAdapter(this.getContext(), summaryViewModel, daySummaryViewModel);
        daySummaryViewModel.setDaySummaryAdapter(daySummaryAdapter);
        daySummaryViewModel.setLifecycleOwner(this.getViewLifecycleOwner());
        summaryList = binding.daySummaryList;
        summaryList.setAdapter(daySummaryAdapter);
        summaryList.setLayoutManager(new LinearLayoutManager(this.context));

        View root = binding.getRoot();

        editButton = binding.editSummaryFab;
        editButton.setOnClickListener(e->{
            if(daySummaryAdapter.getSummaryDataList().size() != 0) {
                var intent = new Intent(this.getContext(), SummaryEditorActivity.class);
                var bundle = new Bundle();
                bundle.putLong("summaryId", daySummaryAdapter.getSummaryDataList().get(0).getSummaryId());
                bundle.putBoolean("editable", true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return root;
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