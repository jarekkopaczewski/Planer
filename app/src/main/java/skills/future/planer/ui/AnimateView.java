package skills.future.planer.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import skills.future.planer.R;

public class AnimateView {
    public static void animateInOut(View view, Context context) {
        Animation zoomIn  = AnimationUtils.loadAnimation(context, R.anim.zoomin);
        Animation zoomOut  = AnimationUtils.loadAnimation(context, R.anim.zoomout);
        view.startAnimation(zoomIn);
        view.startAnimation(zoomOut);
    }

    public static void singleAnimation(View view, Context context, int id) {
        Animation animation  = AnimationUtils.loadAnimation(context, id);
        view.startAnimation(animation);
    }

    public static void animationSlideIn(View view, int duration)
    {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", 200f);
        animation.setDuration(duration);
        animation.start();
    }
}
