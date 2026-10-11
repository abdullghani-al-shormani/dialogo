package com.abdullghani.dialogo.model;

import com.abdullghani.dialogo.core.DialogoController;

/**
 * Represents a button action (positive, negative, or neutral) inside a Dialogo dialog.
 *
 * <p>Each action holds the button's label, its text color, and an optional click
 * listener. This class is created internally by
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder} when you call
 * {@code positive(...)}, {@code negative(...)}, or {@code neutral(...)}.</p>
 *
 * <p>This class also hosts the main listener interfaces used throughout the
 * library. These interfaces are functional interfaces (single abstract method)
 * and can be implemented with lambdas in Java 8+.</p>
 *
 * @see com.abdullghani.dialogo.builder.DialogoBuilder#positive(CharSequence, OnClickListener)
 * @see com.abdullghani.dialogo.builder.DialogoBuilder#negative(CharSequence, OnClickListener)
 * @see com.abdullghani.dialogo.builder.DialogoBuilder#neutral(CharSequence, OnClickListener)
 */
public class DialogoAction {

    private final CharSequence text;
    private final Integer textColor;
    private final OnClickListener clickListener;

    /**
     * Listener invoked when a dialog button is clicked.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * .positive("Save", controller -> {
     *     // handle save
     * })
     * }</pre>
     */
    public interface OnClickListener {
        /**
         * Called when the button is clicked.
         *
         * @param controller The dialog controller, giving access to the
         *                   input text, selected index, and lifecycle methods.
         */
        void onClick(DialogoController controller);
    }

    /**
     * Listener invoked when a list item is clicked or confirmed.
     *
     * <p>Used by:
     * <ul>
     *     <li>{@code itemClick(...)} — for plain lists</li>
     *     <li>{@code singleChoice(...)} — for single-choice lists</li>
     * </ul>
     */
    public interface OnListItemClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param position   Index of the clicked item.
         * @param item       The item's text.
         * @param controller The dialog controller.
         */
        void onItemClick(int position, CharSequence item, DialogoController controller);
    }

    /**
     * Listener invoked when a multi-choice list is confirmed.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * boolean[] checked = {true, false, true};
     * Dialogo.with(this)
     *     .items("A", "B", "C")
     *     .multiChoice(checked, (states, controller) -> {
     *         // states now holds the final checked values
     *     })
     *     .positive("OK", null)
     *     .show(getSupportFragmentManager());
     * }</pre>
     */
    public interface OnMultiChoiceListener {
        /**
         * Called when the user confirms their multi-choice selection.
         *
         * @param checkedItems The final checked states (one entry per item).
         * @param controller   The dialog controller.
         */
        void onConfirm(boolean[] checkedItems, DialogoController controller);
    }

    /**
     * Listener invoked once the dialog is fully visible.
     *
     * <p>Useful for triggering animations, focusing inputs, or logging.</p>
     */
    public interface OnShowListener {
        /**
         * Called after the dialog is shown.
         *
         * @param controller The dialog controller.
         */
        void onShow(DialogoController controller);
    }

    /**
     * Listener invoked when the dialog is dismissed, regardless of the reason.
     */
    public interface OnDismissListener {
        /**
         * Called when the dialog is dismissed.
         */
        void onDismiss();
    }

    /**
     * Internal constructor — instances are created by
     * {@link com.abdullghani.dialogo.builder.DialogoBuilder}.
     *
     * @param text          Button label.
     * @param textColor     Optional text color, or {@code null} for default.
     * @param clickListener Optional click callback, or {@code null}.
     */
    public DialogoAction(CharSequence text, Integer textColor, OnClickListener clickListener) {
        this.text = text;
        this.textColor = textColor;
        this.clickListener = clickListener;
    }

    /**
     * Returns the button label.
     *
     * @return The button text.
     */
    public CharSequence getText() { return text; }

    /**
     * Returns the button text color.
     *
     * @return The ARGB color, or {@code null} if not set.
     */
    public Integer getTextColor() { return textColor; }

    /**
     * Returns the click listener.
     *
     * @return The listener, or {@code null} if no action is attached.
     */
    public OnClickListener getClickListener() { return clickListener; }
}