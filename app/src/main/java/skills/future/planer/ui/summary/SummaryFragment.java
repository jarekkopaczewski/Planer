package skills.future.planer.ui.summary;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentSummaryBrowserBinding;

public class SummaryFragment extends Fragment {

    private SummaryViewModel summaryViewModel;
    private FragmentSummaryBrowserBinding binding;
    private TextView yearSelection;
    private final SimpleDateFormat formatter =
            new SimpleDateFormat("LLLL yyyy", Locale.getDefault());
    private final Calendar calendar = Calendar.getInstance();

    public SummaryFragment(){
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        summaryViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        binding = FragmentSummaryBrowserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // przewija cie do najblizszego miejsca
        // jeżeli przewaga dni to przypisuje do miesiąca

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}