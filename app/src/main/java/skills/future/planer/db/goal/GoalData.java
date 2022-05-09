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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoalData goalData = (GoalData) o;

        if (!getGoalId().equals(goalData.getGoalId())) return false;
        if (getTitle() != null ? !getTitle().equals(goalData.getTitle()) : goalData.getTitle() != null)
            return false;
        return getDetails() != null ? getDetails().equals(goalData.getDetails()) : goalData.getDetails() == null;
    }

    @Override
    public int hashCode() {
        int result = getGoalId().hashCode();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getDetails() != null ? getDetails().hashCode() : 0);
        return result;
    }
}
