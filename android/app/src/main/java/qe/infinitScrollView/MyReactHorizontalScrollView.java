package qe.infinitScrollView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.facebook.react.views.scroll.FpsListener;
import com.facebook.react.views.scroll.ReactHorizontalScrollView;
import com.facebook.react.views.scroll.ReactScrollViewHelper;

public class MyReactHorizontalScrollView extends ReactHorizontalScrollView {
    private ObjectAnimator animator;

    public MyReactHorizontalScrollView(Context context) {
        super(context);
    }

    public MyReactHorizontalScrollView(Context context, @Nullable FpsListener fpsListener) {
        super(context, fpsListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);

        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            this.disableAutoScroll();
        }
        return result;
    }

    public void autoScroll(MyReactScrollViewCommandHelper.AutoScrollCommandData data){
        int right = this.getChildAt(0).getWidth() + this.getPaddingRight();

        float mDuration = right / 100 * data.mDuration;
        animator= ObjectAnimator.ofInt(this, "scrollX",right );
        animator.setInterpolator(new LinerInterpolator());
        animator.setDuration((long)mDuration);
        animator.setStartDelay((long)data.mDelay);
        animator.start();
    }

    public void disableAutoScroll(){
        if(animator != null){
            animator.cancel();
            animator=null;
        }
    }
}