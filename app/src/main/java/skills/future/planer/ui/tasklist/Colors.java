package skills.future.planer.ui.tasklist;

import android.graphics.Color;

public enum Colors {
    RED(255, 102, 109),
    BLUE(95, 191, 217),
    YELLOW(245, 189, 76),
    PINK(255, 153, 193);

    private final int r;
    private final int g;
    private final int b;
    private final String rgb;

    private Colors(final int r,final int g,final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.rgb = r + ", " + g + ", " + b;
    }

    public String getRGB() {
        return rgb;
    }

    //You can add methods like this too
    public int getRed(){
        return r;
    }

    public int getGreen(){
        return g;
    }

    public int getBlue(){
        return r;
    }

    //Or even these
    public int getColor(){
        return Color.rgb(r,g,b);
    }
}
