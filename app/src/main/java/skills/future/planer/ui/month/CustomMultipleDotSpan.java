package skills.future.planer.ui.month;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * Class implements LineBackgroundSpan and draws dots under dates
 *
 * @author Paweł Helisz
 * @version 0.2.2
 * @since 0.2.1
 */
public class CustomMultipleDotSpan implements LineBackgroundSpan {

    /**
     * dot radius
     */
    private final float radius;
    /**
     * number of dots under date
     */
    private final int dots_number;
    /**
     * colors of dots
     */
    int[] color = {
            Color.rgb(0, 0, 255),
            Color.rgb(0, 255, 0),
            Color.rgb(255, 0, 0),
            Color.rgb(255, 0, 255),
    };

    /**
     * Constructor
     * @param radius dot radius
     * @param dots_number number of dots under date
     */
    public CustomMultipleDotSpan(float radius, int dots_number) {
        this.radius = radius;
        this.dots_number = dots_number;
    }

    /**
     * Method draws background of date
     * @param canvas canvas of date
     * @param paint paint object to use colors
     * @param left left position of canvas
     * @param right right position of canvas
     * @param top top position of canvas
     * @param baseline
     * @param bottom bottom of canvas
     * @param charSequence
     * @param start
     * @param end
     * @param lineNum
     */
    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {

        int total = dots_number;
        int leftMost = (total - 1) * -10;

        //setting color and position of a dot
        for (int i = total - 1; i >= 0; i--) {
            int oldColor = paint.getColor();
            if (color[i] != 0) {
                paint.setColor(color[2]);
            }
            canvas.drawCircle((left + right) / 2 - leftMost, bottom + radius, radius, paint);
            paint.setColor(oldColor);
            leftMost = leftMost + 20;
        }
    }
}
