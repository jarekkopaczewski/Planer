package skills.future.planer.db.summary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SummaryData {
    @PrimaryKey(autoGenerate = true)
    private Long summaryId;
    private String achievements, notFinished, toWorkOut;
    private int year, month, weekNumber;
    private SummaryType summaryType;

    public SummaryData() {
    }

    public SummaryData(String achievements, String notFinished, String toWorkOut,
                       LocalDate localDate, SummaryType summaryType) {
        this.achievements = achievements;
        this.notFinished = notFinished;
        this.toWorkOut = toWorkOut;
        this.summaryType = summaryType;
        year = localDate.getYear();
        switch (summaryType) {
            case monthSummary -> month = localDate.getMonthValue();
            case weekSummary -> weekNumber = localDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        }
    }
}
