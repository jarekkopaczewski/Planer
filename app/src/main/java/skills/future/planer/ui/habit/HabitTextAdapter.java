package skills.future.planer.ui.habit;

import androidx.annotation.NonNull;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import lombok.Setter;

@Setter
public class HabitTextAdapter implements  CircularProgressIndicator.ProgressTextAdapter{

    private double maxProgress;
    /**
     * Sets format of displayed info in progress bar
     * @param currentProgress
     * @return
     */
    @NonNull
    @Override
    public String formatText(double currentProgress) {
        return (int) currentProgress + "/" + (int) maxProgress;
    }

}
