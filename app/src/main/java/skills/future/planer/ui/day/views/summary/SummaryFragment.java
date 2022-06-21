package skills.future.planer.ui.day.views.summary;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.codesgood.views.JustifiedTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import skills.future.planer.R;
import skills.future.planer.databinding.FragmentSummaryBinding;

public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;
    private JustifiedTextView editText1;
    private JustifiedTextView editText2;
    private JustifiedTextView editText3;
    private NestedScrollView nestedScrollView3;
    private FloatingActionButton editButton;
    private int height;
    private boolean state; // true - edit, false - view

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editText1 = binding.summaryAchievementsText;
        editText2 = binding.summaryNotFinishedText;
        editText3 = binding.summaryToWorkOutText;

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