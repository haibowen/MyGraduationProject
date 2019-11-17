package com.example.administrator.filemanagementassistant.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class ScrollAwareFABBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {
    private static final android.view.animation.Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;
    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        //处理垂直方向上的滚动事件
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0) { // 向下滑动
            //如果是展开的话就先收回去
            if (child.isExpanded()) {
                child.collapse();
            }
            animateOut(child);
        } else if (dyConsumed < 0) { // 向上滑动
            animateIn(child);
        }
    }

    private void animateOut(final FloatingActionsMenu button) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) button.getLayoutParams();
        int bottomMargin = layoutParams.bottomMargin;
        button.animate().translationY(button.getHeight() + bottomMargin).setInterpolator(new LinearInterpolator()).start();
    }

    private void animateIn(FloatingActionsMenu button) {
        button.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
    }
}
