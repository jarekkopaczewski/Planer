package skills.future.planer.ui.tasklist;

enum Priorities {
    Important(0),
    NotImportant(1),
    Urgent(2),
    NotUrgent(3);

    private final int priorityLvl;

    Priorities(int priorityLvl) {
        this.priorityLvl = priorityLvl;
    }

}
