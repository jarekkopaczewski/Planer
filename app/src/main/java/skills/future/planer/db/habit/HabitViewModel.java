package skills.future.planer.db.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HabitViewModel extends AndroidViewModel {
    private final HabitRepository habitRepository;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        habitRepository = new HabitRepository(application);
    }

    /**
     * @return reference to list of all habits
     */
    LiveData<List<HabitData>> getAllHabits() {
        return habitRepository.getAllHabits();
    }

    /**
     * Method insert habit to database
     * @param habitData which will be inserted
     */
    void insert(HabitData habitData){
        habitRepository.insert(habitData);
    }

    /**
     * Method edit habit in database
     * @param habitData which will be updated
     */
    void edit(HabitData habitData){
        habitRepository.edit(habitData);
    }

    /**
     * Method delete habit from database
     * @param habitData which will be deleted
     */
    void delete(HabitData habitData){
        habitRepository.delete(habitData);
    }
}
