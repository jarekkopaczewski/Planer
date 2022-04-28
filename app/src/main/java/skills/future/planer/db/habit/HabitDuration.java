package skills.future.planer.db.habit;

public enum HabitDuration {
    Short(90),
    Long(120);

    private final Integer daysNumber;

    HabitDuration(Integer daysNumber) {
        this.daysNumber = daysNumber;
    }
}
