package com.abdullghani.dialogo.core;

import android.view.View;

import com.abdullghani.dialogo.model.DialogoResult;

/**
 * Controller interface exposed to click and item listeners, giving access to
 * the dialog's state and lifecycle.
 *
 * <p>An instance of this interface is passed to every listener registered on
 * a {@link com.abdullghani.dialogo.builder.DialogoBuilder} (e.g.,
 * {@code positive(...)}, {@code itemClick(...)}, {@code onShow(...)}).</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Enter your name")
 *     .input("Full name", "")
 *     .positive("Save", controller -> {
 *         String name = controller.getInputText();
 *         // use name...
 *         controller.dismiss();
 *     })
 *     .show(getSupportFragmentManager());
 * }</pre>
 *
 * @see com.abdullghani.dialogo.core.DialogoFragment
 * @see com.abdullghani.dialogo.core.DialogoBottomSheet
 */
public interface DialogoController {

    /**
     * Dismisses the dialog with a smooth exit animation.
     *
     * <p>If {@link #preventDismiss()} was previously called, this call will
     * only consume that flag and the dialog will remain visible.</p>
     */
    void dismiss();

    /**
     * Prevents the next {@link #dismiss()} call from actually dismissing the
     * dialog. Useful for validating user input before allowing dismissal.
     *
     * <p>The flag is consumed (reset) on the next {@code dismiss()} call.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * .positive("Save", controller -> {
     *     if (controller.getInputText().isEmpty()) {
     *         toast("Please enter a value");
     *         controller.preventDismiss();
     *     }
     * })
     * }</pre>
     */
    void preventDismiss();

    /**
     * Returns the custom view attached to this dialog, if any.
     *
     * @return The custom view, or {@code null} if no custom view was set.
     * @see com.abdullghani.dialogo.builder.DialogoBuilder#customView(int, com.abdullghani.dialogo.model.DialogoParams.OnViewCreatedListener)
     */
    View getCustomView();

    /**
     * Returns the current text in the input field.
     *
     * @return The trimmed input text, or an empty string if there is no input.
     */
    String getInputText();

    /**
     * Returns the currently selected index in a single-choice list.
     *
     * @return The selected index, or {@code -1} if none.
     */
    int getSelectedIndex();

    /**
     * Returns the current checked states in a multi-choice list.
     *
     * @return A boolean array, or {@code null} if not applicable.
     */
    boolean[] getCheckedItems();

    /**
     * Builds a unified {@link DialogoResult} representing the dialog's final state.
     *
     * <p>Typically consumed via
     * {@link com.abdullghani.dialogo.builder.DialogoBuilder#showForResult(
     * androidx.fragment.app.FragmentManager,
     * com.abdullghani.dialogo.model.DialogoParams.ResultListener)}.</p>
     *
     * @return A {@link DialogoResult} snapshot.
     */
    DialogoResult getResult();

    // ==================== Progress ====================

    /**
     * Updates the current progress value (for horizontal progress dialogs).
     *
     * @param progress Progress value between 0 and the max.
     */
    void setProgress(int progress);

    /**
     * Updates the text displayed alongside the progress indicator.
     *
     * @param text The new status text, or {@code null} to hide it.
     */
    void setProgressText(CharSequence text);

    /**
     * Shows or hides the percentage label.
     *
     * @param visible {@code true} to show.
     */
    void setProgressPercentVisible(boolean visible);
}