package skills.future.planer.db.summary;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import lombok.NonNull;

public class SummaryViewModel extends AndroidViewModel {
    SummaryRepository summaryRepository;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        summaryRepository = new SummaryRepository(application);
    }

    /**
     * @return reference to list of all summaries
     */
    public LiveData<List<SummaryData>> getAllGoals() {
        return summaryRepository.getAllSummaries();
    }

    /**
     * Method insert summary to database
     *
     * @param summaryData which will be inserted
     */
    public void insert(SummaryData summaryData) {
        summaryRepository.insert(summaryData);
    }

    /**
     * Method edit summary in database
     *
     * @param summaryData which will be updated
     */
    public void edit(SummaryData summaryData) {
        summaryRepository.edit(summaryData);
    }

    /**
     * Method delete summary from database
     *
     * @param summaryData which will be deleted
     */
    public void delete(SummaryData summaryData) {

        summaryRepository.delete(summaryData);
    }

    public SummaryData findById(Long goalId) {
        return summaryRepository.findById(goalId);
    }
}
