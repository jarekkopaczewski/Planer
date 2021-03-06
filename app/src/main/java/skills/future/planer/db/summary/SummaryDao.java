package skills.future.planer.db.summary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface SummaryDao {

    /**
     * Method get summaryData with given id
     *
     * @param id of searched summaryData
     * @return summaryData
     */
    @Query("SELECT * FROM SummaryData WHERE summaryId = :id")
    SummaryData findById(Long id);

    @Query("SELECT * FROM SummaryData WHERE year = :year and summaryType = \"yearSummary\"")
    List<SummaryData> getYearSummary(int year);

    @Query("SELECT * FROM SummaryData WHERE year = :year and month = :month and summaryType = \"monthSummary\"")
    List<SummaryData> getMonthSummary(int year, int month);

    @Query("SELECT * FROM SummaryData WHERE year = :year and summaryType = \"monthSummary\"")
    List<SummaryData> getMonthsFromYearSummary(int year);

    @Query("SELECT * FROM SummaryData WHERE year = :year and month = :month and summaryType = \"weekSummary\"")
    List<SummaryData> getWeeksFromMonthSummary(int year, int month);

    @Query("SELECT * FROM SummaryData WHERE year = :year and weekNumber =:weekNumber and  summaryType = \"weekSummary\"")
    List<SummaryData> getWeekSummary(int year, int weekNumber);

    @Query("SELECT * FROM SummaryData WHERE year = :year and weekNumber =:weekNumber and  summaryType = \"weekSummary\"")
    LiveData<List<SummaryData>> getWeekSummary2(int year, int weekNumber);


    @Query("SELECT MIN(year) FROM SummaryData;")
    int getMinimumYear();

    /**
     * @return all summaries from database in LiveData
     */
    @Query("SELECT * FROM SummaryData")
    LiveData<List<SummaryData>> getSummary();

    /**
     * Method insert given habit to database
     *
     * @param summaryData which will be inserted to database
     * @return new summary id
     */
    @Insert
    long insert(SummaryData summaryData);

    /**
     * Method delete summary from database
     *
     * @param summaryData which will be deleted
     */
    @Delete
    void deleteOne(SummaryData summaryData);

    /**
     * Method delete all summary from database
     */
    @Query("DELETE FROM SummaryData")
    void deleteAll();

    /**
     * Method update given habit
     *
     * @param summaryData given habit which will be updated
     */
    @Update
    void editOne(SummaryData summaryData);
}
