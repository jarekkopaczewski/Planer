package skills.future.planer.db.habit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import skills.future.planer.tools.DatesParser;

public class HabitViewModel extends AndroidViewModel {
    private final HabitRepository habitRepository;

    public HabitViewModel(@NonNull Application application) {
        super(application);
        habitRepository = new HabitRepository(application);
    }

    /**
     * @return list of all habits
     */
    public List<HabitData> getAllHabitsList() {
        return habitRepository.getAllHabitsList();
    }

    /**
     * @return reference to list of all habits
     */
    public LiveData<List<HabitData>> getAllHabits() {
        return habitRepository.getAllHabits();
    }

    public LiveData<List<HabitData>> getAllHabitDataFromDayLiveData(CalendarDay date) {
        return habitRepository.getAllHabitDataFromDay(DatesParser.toMilliseconds(date));
    }

    public List<HabitData> getAllHabitDataFromDay(CalendarDay date) {
        return habitRepository.getAllHabitDataFromDayList(DatesParser.toMilliseconds(date));
    }


    /**
     * Method insert habit to database
     *
     * @param habitData which will be inserted
     */
    public void insert(HabitData habitData) {
        habitRepository.insert(habitData);
    }

    /**
     * Method edit habit in database
     *
     * @param habitData which will be updated
     */
    public void edit(HabitData habitData) {
        habitRepository.edit(habitData);
    }

    /**
     * Method delete habit from database
     *
     * @param habitData which will be deleted
     */
    public void delete(HabitData habitData) {
        habitRepository.delete(habitData);
    }

    public HabitData findById(Long habitId) throws Exception {
        return habitRepository.findById(habitId);
    }
}
