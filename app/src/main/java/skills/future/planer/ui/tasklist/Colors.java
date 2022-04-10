package skills.future.planer.ui.tasklist;

import android.graphics.Color;

public enum Colors {
    RED(81, 205, 197),
    BLUE(0, 126, 143),
    YELLOW(198, 162, 202),
    PINK(113, 83, 151);

    private final int r;
    private final int g;
    private final int b;

    Colors(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getColor(){
        return Color.rgb(r,g,b);
    }
}
