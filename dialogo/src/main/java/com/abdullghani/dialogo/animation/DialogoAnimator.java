package com.abdullghani.dialogo.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * مُشغّل الحركات المضمنة. يعمل عبر ViewPropertyAnimator/ObjectAnimator.
 *
 * ملاحظة: نستخدم قيم بكسل ثابتة بدل view.getWidth()/getHeight()
 * لأن الـ view قد لا يكون مقيساً عند بدء الـ animation.
 */
public final class DialogoAnimator {

    private static final long DURATION_ENTER = 260L;
    private static final long DURATION_EXIT = 180L;

    // قيم إزاحة ثابتة (بالبكسل)
    private static final float SLIDE_DISTANCE = 120f;

    private DialogoAnimator() { }

    // ==================== ENTER ====================

    public static void applyEntry(View view, DialogoAnimation animation) {
        if (view == null || animation == null || animation == DialogoAnimation.NONE) return;

        switch (animation) {
            case FADE:
                applyFadeIn(view);
                break;
            case SLIDE_UP:
                // ينزلق من الأسفل للأعلى
                applySlideIn(view, 0f, SLIDE_DISTANCE, true);
                break;
            case SLIDE_DOWN:
                // ينزلق من الأعلى للأسفل
                applySlideIn(view, 0f, -SLIDE_DISTANCE, true);
                break;
            case SLIDE_RIGHT:
                // ينزلق من اليمين لليسار
                applySlideIn(view, SLIDE_DISTANCE, 0f, true);
                break;
            case SLIDE_LEFT:
                // ينزلق من اليسار لليمين
                applySlideIn(view, -SLIDE_DISTANCE, 0f, true);
                break;
            case BOUNCE:
                applyBounce(view);
                break;
            case SHAKE:
                applyShake(view);
                break;
            case SCALE:
            default:
                applyScaleIn(view);
                break;
        }
    }

    private static void applyScaleIn(View view) {
        view.setScaleX(0.85f);
        view.setScaleY(0.85f);
        view.setAlpha(0f);
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(DURATION_ENTER)
                .setInterpolator(new OvershootInterpolator(1.4f))
                .start();
    }

    private static void applyFadeIn(View view) {
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setDuration(DURATION_ENTER)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    /**
     * انزلاق من نقطة انطلاق إلى الموضع الأصلي.
     *
     * @param startTranslationX إزاحة البداية الأفقية (px) — موجبة = من اليمين
     * @param startTranslationY إزاحة البداية الرأسية (px) — موجبة = من الأسفل
     */
    private static void applySlideIn(View view, float startTranslationX,
                                     float startTranslationY, boolean withFade) {
        view.setTranslationX(startTranslationX);
        view.setTranslationY(startTranslationY);
        if (withFade) view.setAlpha(0f);

        view.animate()
                .translationX(0f)
                .translationY(0f)
                .alpha(1f)
                .setDuration(DURATION_ENTER)
                .setInterpolator(new DecelerateInterpolator(1.4f))
                .start();
    }

    private static void applyBounce(View view) {
        view.setScaleX(0.5f);
        view.setScaleY(0.5f);
        view.setAlpha(0f);

        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(420L)
                .setInterpolator(new OvershootInterpolator(2.2f))
                .start();
    }

    private static void applyShake(View view) {
        view.setAlpha(0f);
        view.setTranslationX(-30f);

        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);
        PropertyValuesHolder tx = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_X, -30f, 30f, -20f, 20f, -10f, 10f, 0f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, tx);
        animator.setDuration(420L);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    // ==================== EXIT ====================

    public static void applyExit(View view, DialogoAnimation animation, Runnable onEnd) {
        if (view == null || animation == null || animation == DialogoAnimation.NONE) {
            if (onEnd != null) onEnd.run();
            return;
        }

        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
            private boolean canceled = false;

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!canceled && onEnd != null) onEnd.run();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                canceled = true;
                if (onEnd != null) onEnd.run();
            }
        };

        switch (animation) {
            case FADE:
                view.animate().alpha(0f).setDuration(DURATION_EXIT)
                        .setListener(listener).start();
                break;
            case SLIDE_UP:
                // يخرج نحو الأسفل
                view.animate().translationY(SLIDE_DISTANCE).alpha(0f)
                        .setDuration(DURATION_EXIT)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(listener).start();
                break;
            case SLIDE_DOWN:
                // يخرج نحو الأعلى
                view.animate().translationY(-SLIDE_DISTANCE).alpha(0f)
                        .setDuration(DURATION_EXIT)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(listener).start();
                break;
            case SLIDE_RIGHT:
                // يخرج نحو اليمين
                view.animate().translationX(SLIDE_DISTANCE).alpha(0f)
                        .setDuration(DURATION_EXIT)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(listener).start();
                break;
            case SLIDE_LEFT:
                // يخرج نحو اليسار
                view.animate().translationX(-SLIDE_DISTANCE).alpha(0f)
                        .setDuration(DURATION_EXIT)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(listener).start();
                break;
            case BOUNCE:
                view.animate().scaleX(0.85f).scaleY(0.85f).alpha(0f)
                        .setDuration(DURATION_EXIT)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(listener).start();
                break;
            case SCALE:
            default:
                view.animate().scaleX(0.9f).scaleY(0.9f).alpha(0f)
                        .setDuration(DURATION_EXIT)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(listener).start();
                break;
        }
    }
}