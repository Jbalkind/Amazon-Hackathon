package com.giraffacakes.hackathon.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class CollapseAnimation {
    private AnimatorSet animSet;
    
    public CollapseAnimation(View widget, float scaleY, float pivotY) {
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(widget, "scaleY", scaleY);
    	ObjectAnimator animatorPivotY = ObjectAnimator.ofFloat(widget, "pivotY", pivotY);
    	animSet = new AnimatorSet();
    	animSet.playTogether(animatorScaleY, animatorPivotY);
    }
    
    public void start() {
    	animSet.start();
    }
}