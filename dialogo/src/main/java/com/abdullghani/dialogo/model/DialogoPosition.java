package com.abdullghani.dialogo.model;

/**
 * Vertical position of a Dialogo dialog on the screen.
 *
 * <p>Set using
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder#position(DialogoPosition)}
 * or one of the shortcuts {@code positionTop()}, {@code positionCenter()},
 * {@code positionBottom()}.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * Dialogo.popup(this, "Heads up!", "Non-modal popup at the top")
 *     .position(DialogoPosition.TOP)
 *     .marginTopDp(12)
 *     .show(getSupportFragmentManager());
 * }</pre>
 */
public enum DialogoPosition {

    /** Anchored to the top of the screen. */
    TOP,

    /** Centered vertically (default). */
    CENTER,

    /** Anchored to the bottom of the screen. */
    BOTTOM
}