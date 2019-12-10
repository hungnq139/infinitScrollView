package qe.infinitScrollView;

import android.view.animation.Interpolator;

public class LinerInterpolator implements Interpolator {
    public LinerInterpolator() {
    }

    public float getInterpolation(float t) {
        return t;
    }
}