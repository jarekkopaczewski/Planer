package skills.future.planer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class BatteryPermissionDialog extends DialogFragment {
    private NoticeDialogListener listener;

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("Aby móc w pełni korzystać z funkcji aplikacji, takich jak " +
                        "powiadomienia należy wyłączyć w ustawieniach optymalizację baterii " +
                        "dla aplikacji Planer.")
                .setPositiveButton("Wyłącz", (dialog, id) -> listener.onDialogPositiveClick(BatteryPermissionDialog.this))
                .setNegativeButton("Pozostaw", null);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

}
