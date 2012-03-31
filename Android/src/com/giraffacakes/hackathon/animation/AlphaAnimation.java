package com.giraffacakes.hackathon.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class AlphaAnimation {
    private AnimatorSet animSet;
    
    public AlphaAnimation(View widget, float target) {
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(widget, "alpha", target);
    	animSet = new AnimatorSet();
    	animSet.play(animatorAlpha);
    }
    
    public void start() {
    	animSet.start();
    }
}