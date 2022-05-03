package skills.future.planer.db.habit;

public enum HabitDuration {
    Short(90),
    Long(120);

    private final Integer daysNumber;

    public Integer getDaysNumber() {
        return daysNumber;
    }

    HabitDuration(Integer daysNumber) {
        this.daysNumber = daysNumber;
    }
}
