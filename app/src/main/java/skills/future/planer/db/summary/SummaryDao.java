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
