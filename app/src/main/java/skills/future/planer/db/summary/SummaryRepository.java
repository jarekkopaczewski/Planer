package skills.future.planer.db.summary;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

import skills.future.planer.db.AppDatabase;

public class SummaryRepository {
    private final SummaryDao summaryDao;

    public SummaryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.summaryDao = db.summaryDao();
    }

    /**
     * @return reference to list of all summaries
     */
    LiveData<List<SummaryData>> getAllSummaries() {
        return summaryDao.getSummary();
    }

    public List<SummaryData> getSummary(LocalDate date, SummaryType summaryType) {
        return switch (summaryType) {
            case yearSummary -> summaryDao.getYearSummary(date.getYear());
            case monthSummary -> summaryDao.getMonthSummary(date.getYear(), date.getMonthValue());
            case weekSummary -> summaryDao.getWeekSummary(date.getYear(), date.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        };
    }

    /**
     * Method insert summary to database
     *
     * @param summaryData which will be inserted
     */
    void insert(SummaryData summaryData) {
        summaryData.setSummaryId(summaryDao.insert(summaryData));
    }

    /**
     * Method edit summary in database
     *
     * @param summaryData which will be updated
     */
    void edit(SummaryData summaryData) {
        summaryDao.editOne(summaryData);
    }

    /**
     * Method delete summary from database
     *
     * @param summaryData which will be deleted
     */
    void delete(SummaryData summaryData) {
        summaryDao.deleteOne(summaryData);
    }

    SummaryData findById(Long id) {
        return summaryDao.findById(id);
    }
}