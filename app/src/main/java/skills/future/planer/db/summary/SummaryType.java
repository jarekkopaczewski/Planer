package skills.future.planer.db.summary;

import lombok.Getter;

@Getter
public enum SummaryType {
    yearSummary(1), monthSummary(2), weekSummary(2);
    private final int type;

    SummaryType(int type) {
        this.type = type;
    }
}
