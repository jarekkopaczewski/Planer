package skills.future.planer.db.task;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

@Getter
public class DayCategorizedTaskData {
    private final Map<CalendarDay, CategorizedTaskData> taskDataMap;
    private final ArrayList<CalendarDay> listOfAddedDays;
    public DayCategorizedTaskData(List<TaskData> data) {
        listOfAddedDays = new ArrayList<>();
        taskDataMap = new HashMap<>();
        run(data);
    }

    private void run(List<TaskData> data) {
        for (TaskData datum : data) {
            if (datum.getStartingCalendarDate() != null) {
                for (CalendarDay calendarDay : getAllDaysBetween(datum.getStartingCalendarDate(), datum.getEndingCalendarDate())) {
                    if (!taskDataMap.containsKey(calendarDay)) {
                        taskDataMap.put(calendarDay, new CategorizedTaskData());
                        listOfAddedDays.add(calendarDay);
                    }
                    switch (datum.getPriorities()) {
                        case Important:
                            switch (datum.getTimePriority()) {
                                case Urgent -> Objects.requireNonNull(taskDataMap.get(calendarDay)).importantUrgentTask.add(datum);
                                case NotUrgent -> Objects.requireNonNull(taskDataMap.get(calendarDay)).importantNotUrgent.add(datum);
                            }
                            break;
                        case NotImportant:
                            switch (datum.getTimePriority()) {
                                case Urgent -> Objects.requireNonNull(taskDataMap.get(calendarDay)).notImportantUrgentTask.add(datum);
                                case NotUrgent -> Objects.requireNonNull(taskDataMap.get(calendarDay)).notImportantNotUrgent.add(datum);
                            }
                            break;
                    }
                }
            }
        }
    }

    public List<TaskData> getAllCategorizedDayFromQuarter(int i){
        List<TaskData> result = new ArrayList<>();
        for (CalendarDay listOfAddedDay : listOfAddedDays) {
            switch (i) {
                case 0 -> result.addAll(Objects.requireNonNull(taskDataMap.get(listOfAddedDay)).getImportantUrgentTask());
                case 1 -> result.addAll(Objects.requireNonNull(taskDataMap.get(listOfAddedDay)).getImportantNotUrgent());
                case 2 -> result.addAll(Objects.requireNonNull(taskDataMap.get(listOfAddedDay)).getNotImportantUrgentTask());
                case 3 -> result.addAll(Objects.requireNonNull(taskDataMap.get(listOfAddedDay)).getNotImportantNotUrgent());
            }
        }
        return result;
    }

    public CategorizedTaskData getCategorizedTaskDataFromDay(CalendarDay day) {

        if (taskDataMap.containsKey(day))
            return taskDataMap.get(day);
        return null;
    }

    private List<CalendarDay> getAllDaysBetween(CalendarDay start, CalendarDay end) {
        List<CalendarDay> days = new ArrayList<>();
        CalendarDay day = CalendarDay.from(start.getYear(), start.getMonth(), start.getDay());
        days.add(day);
        int i = 1;
        while (!day.equals(end)) {
            try {
                day = CalendarDay.from(start.getYear(), start.getMonth(), start.getDay() + i++);
            } catch (Exception e) {
                try {
                    day = CalendarDay.from(start.getYear(), start.getMonth() + 1, 1);
                } catch (Exception e2) {
                    day = CalendarDay.from(start.getYear() + 1, 1, 1);
                }
            }
            days.add(day);
        }
        return days;
    }
}