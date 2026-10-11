package com.abdullghani.dialogo.model;

import java.util.Map;

/**
 * Unified result object representing the final state of a dialog when it is
 * dismissed.
 *
 * <p>Typically consumed via
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder#showForResult(
 *     androidx.fragment.app.FragmentManager,
 *     DialogoParams.ResultListener)}.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Save changes?")
 *     .positive("Yes")
 *     .negative("No")
 *     .showForResult(getSupportFragmentManager(), result -> {
 *         if (result.isPositive()) {
 *             saveChanges();
 *         } else if (result.isNegative()) {
 *             discard();
 *         }
 *     });
 * }</pre>
 *
 * @see com.abdullghani.dialogo.model.DialogoParams.ResultListener
 */
public class DialogoResult {

    /**
     * Describes how the dialog was closed.
     */
    public enum Type {
        /** Closed via the positive button. */
        POSITIVE,
        /** Closed via the negative button. */
        NEGATIVE,
        /** Closed via the neutral button. */
        NEUTRAL,
        /** Closed by tapping outside or pressing Back. */
        DISMISSED,
        /** An item was selected (single or plain list). */
        ITEM_SELECTED,
        /** A list was confirmed via the positive button. */
        CONFIRMED
    }

    private final Type type;

    private final String inputText;
    private final int selectedIndex;
    private final CharSequence selectedItem;
    private final boolean[] checkedItems;
    private final Map<String, Object> formValues;

    private DialogoResult(Builder b) {
        this.type = b.type;
        this.inputText = b.inputText;
        this.selectedIndex = b.selectedIndex;
        this.selectedItem = b.selectedItem;
        this.checkedItems = b.checkedItems;
        this.formValues = b.formValues;
    }

    // ==================== Getters ====================

    /**
     * Returns how the dialog was closed.
     *
     * @return The result type.
     */
    public Type getType() { return type; }

    /**
     * @return {@code true} if the user pressed the positive button
     *         or confirmed a list.
     */
    public boolean isPositive() { return type == Type.POSITIVE || type == Type.CONFIRMED; }

    /** @return {@code true} if the user pressed the negative button. */
    public boolean isNegative() { return type == Type.NEGATIVE; }

    /** @return {@code true} if the user pressed the neutral button. */
    public boolean isNeutral() { return type == Type.NEUTRAL; }

    /** @return {@code true} if the dialog was dismissed without a button press. */
    public boolean isDismissed() { return type == Type.DISMISSED; }

    /** @return {@code true} if a list item was selected. */
    public boolean isItemSelected() { return type == Type.ITEM_SELECTED; }

    /**
     * Returns the input text captured when the dialog was closed.
     *
     * @return The trimmed input string, or an empty string if none.
     */
    public String getInputText() { return inputText != null ? inputText : ""; }

    /**
     * Returns the selected index for single-choice lists.
     *
     * @return The selected index, or {@code -1} if none.
     */
    public int getSelectedIndex() { return selectedIndex; }

    /**
     * Returns the selected item's text for single-choice or plain lists.
     *
     * @return The item text, or {@code null} if none.
     */
    public CharSequence getSelectedItem() { return selectedItem; }

    /**
     * Returns the checked states for multi-choice lists.
     *
     * @return A boolean array, or {@code null} if not applicable.
     */
    public boolean[] getCheckedItems() { return checkedItems; }

    /**
     * Returns the collected form values (when using the Form builder).
     *
     * @return A map of {@code fieldId -> value}, or {@code null} if no form was used.
     */
    public Map<String, Object> getFormValues() { return formValues; }

    // ==================== Builder ====================

    /**
     * Internal builder used to construct {@link DialogoResult} instances.
     *
     * <p>Not intended for direct use by application code.</p>
     */
    public static class Builder {
        private Type type = Type.DISMISSED;
        private String inputText;
        private int selectedIndex = -1;
        private CharSequence selectedItem;
        private boolean[] checkedItems;
        private Map<String, Object> formValues;

        /** @param t How the dialog was closed. */
        public Builder type(Type t) { this.type = t; return this; }

        /** @param text Captured input text. */
        public Builder inputText(String text) { this.inputText = text; return this; }

        /** @param index Selected list index. */
        public Builder selectedIndex(int index) { this.selectedIndex = index; return this; }

        /** @param item Selected list item. */
        public Builder selectedItem(CharSequence item) { this.selectedItem = item; return this; }

        /** @param items Multi-choice states. */
        public Builder checkedItems(boolean[] items) { this.checkedItems = items; return this; }

        /** @param values Collected form values. */
        public Builder formValues(Map<String, Object> values) { this.formValues = values; return this; }

        /**
         * Builds the result.
         *
         * @return A new {@link DialogoResult}.
         */
        public DialogoResult build() { return new DialogoResult(this); }
    }
}