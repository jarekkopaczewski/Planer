package skills.future.planer.db.summary;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import lombok.NonNull;

public class SummaryViewModel extends AndroidViewModel {
    SummaryRepository summaryRepository;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        summaryRepository = new SummaryRepository(application.getApplicationContext());
    }

    /**
     * get month summary from year
     * @param year
     * @param month
     * @return
     */
    public List<SummaryData> getMonthSummary(int year, int month){
        return summaryRepository.getMonthSummary(year, month);
    }

    /**
     * Get min year in database
     * @return
     */
    public int getMinimumYear(){
        return summaryRepository.getMinimumYear();
    }

    /**
     * @return reference to list of all summaries
     */
    public LiveData<List<SummaryData>> getAllSummary() {
        return summaryRepository.getAllSummaries();
    }

    /**
     * @return return all months summary from given year
     */
    public List<SummaryData> getMonthsFromYearSummary(int year){
        return summaryRepository.getMonthsFromYearSummary(year);
    }

    /**
     * @return return all months summary from given year
     */
    public List<SummaryData> getWeeksFromMonthSummary(int year, int month){
        return summaryRepository.getWeeksFromMonthSummary(year, month);
    }

    /**
     * example you want month summary from january, requires parameters are: localDate(2022.1.1) and MonthSummary
     *
     * @param date        specify from which period is summary required
     * @param summaryType what type of summary is required
     * @return list of summaries
     */
    public List<SummaryData> getSummary(LocalDate date, SummaryType summaryType) {
        return summaryRepository.getSummary(date, summaryType);
    }

    public LiveData<List<SummaryData>> getSummary2(LocalDate date, int weekNumber) {
        return summaryRepository.getSummary2(date, weekNumber);
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
