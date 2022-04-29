package skills.future.planer.ui.day.views.habits;

import androidx.annotation.NonNull;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class TextAdapter implements  CircularProgressIndicator.ProgressTextAdapter{

    /**
     * Sets format of displayed info in progress bar
     * @param currentProgress
     * @return
     */
    @NonNull
    @Override
    public String formatText(double currentProgress) {
        return (int) currentProgress + "%";
    }
}
