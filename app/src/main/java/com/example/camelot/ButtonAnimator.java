package com.example.camelot;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class ButtonAnimator {

    int defaultDuration = 90;
    float defaultScale = 0.8f;

    private final int duration;
    private final float scale;

    public ButtonAnimator() {
        this.duration = defaultDuration;
        this.scale = defaultScale;
    }

    public ButtonAnimator(int duration, float scale) {
        this.duration = duration;
        this.scale = scale;
    }

    public void animateButtonClick(View button) {
        animateView(button);
    }

    private void animateView(View view) {
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        scaleX.setDuration(duration);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setRepeatCount(1);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleY.setDuration(duration);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatCount(1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY);

        animatorSet.start();
    }

    public static void animateButtonClick(View button, int duration, float scale) {
        ButtonAnimator animator = new ButtonAnimator(duration, scale);
        animator.animateButtonClick(button);
    }
}