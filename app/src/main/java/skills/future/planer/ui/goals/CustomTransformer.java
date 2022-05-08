package skills.future.planer.ui.goals;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CustomTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageWidth = (int) (page.getWidth() * 0.8f);
        if (position <= 0.0F && position >= -0.8f) {
            page.setAlpha(1.0F + position);
            page.setTranslationX((float) pageWidth * -position);
            float v = 0.75F + 0.25F * (1.0F - Math.abs(position));
            page.setScaleX(v);
            page.setScaleY(v);
        }
    }
}
