package skills.future.planer.db.summary;

import lombok.Getter;

@Getter
public enum SummaryType {
    monthSummary(2), weekSummary(3);
    private final int type;

    SummaryType(int type) {
        this.type = type;
    }
}
