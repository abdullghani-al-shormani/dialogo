package com.abdullghani.dialogo.animation;

/**
 * Built-in animations that can be applied to a Dialogo dialog when it appears
 * and disappears.
 *
 * <p>Set the animation using
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder#animation(DialogoAnimation)}.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Saved!")
 *     .message("Your changes have been saved.")
 *     .animation(DialogoAnimation.BOUNCE)
 *     .positive("OK", null)
 *     .show(getSupportFragmentManager());
 * }</pre>
 *
 * @see com.abdullghani.dialogo.animation.DialogoAnimator
 */
public enum DialogoAnimation {

    /** No animation — the dialog appears instantly. */
    NONE,

    /** Scale + fade-in (default). */
    SCALE,

    /** Smooth fade-in. */
    FADE,

    /** Slides in from the bottom of the screen. */
    SLIDE_UP,

    /** Slides in from the top of the screen. */
    SLIDE_DOWN,

    /** Slides in from the right edge. */
    SLIDE_RIGHT,

    /** Slides in from the left edge. */
    SLIDE_LEFT,

    /** Springy scale-in with an overshoot effect. */
    BOUNCE,

    /** Horizontal shake — ideal for error dialogs. */
    SHAKE
}