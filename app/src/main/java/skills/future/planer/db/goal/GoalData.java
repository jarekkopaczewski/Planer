package skills.future.planer.db.goal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GoalData {
    @PrimaryKey(autoGenerate = true)
    private Long goalId;
    private String title, details;

    public GoalData(String title, String details) {
        this.title = title;
        this.details = details;
    }
}
