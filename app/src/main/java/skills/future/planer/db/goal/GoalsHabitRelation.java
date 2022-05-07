package skills.future.planer.db.goal;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import skills.future.planer.db.habit.HabitData;

public class GoalsHabitRelation {
    @Embedded
    public GoalData goalData;
    @Relation(
            parentColumn = "goalId",
            entityColumn = "foreignKeyToGoal"
    )
    public List<HabitData> habits;
}
