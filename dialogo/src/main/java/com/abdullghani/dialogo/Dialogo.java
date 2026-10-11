package com.abdullghani.dialogo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.builder.DialogoBuilder;
import com.abdullghani.dialogo.model.DialogoAction;
import com.abdullghani.dialogo.theme.DialogoTheme;

/**
 * Main entry point of the Dialogo library.
 *
 * <p>Dialogo is an elegant, customizable Android dialog library that provides
 * a fluent builder API for creating alerts, confirmations, inputs, progress
 * dialogs, lists, forms, bottom sheets, and non-modal popups.</p>
 *
 * <h3>Quick Start</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Welcome")
 *     .message("This is a simple dialog.")
 *     .positive("OK", controller -> { /* action *\/ })
 *     .negative("Cancel", null)
 *     .show(getSupportFragmentManager());
 * }</pre>
 *
 * <h3>Preset Dialogs</h3>
 * <pre>{@code
 * Dialogo.success(this, "Saved!", "Your data was saved.")
 *     .show(getSupportFragmentManager());
 * }</pre>
 *
 * <h3>Global Theme</h3>
 * <pre>{@code
 * // In your Application class:
 * Dialogo.initGlobalTheme(BuiltInThemes.DARK());
 * }</pre>
 *
 * @see DialogoBuilder
 * @see DialogoTheme
 * @see com.abdullghani.dialogo.theme.BuiltInThemes
 */
public class Dialogo {

    private static DialogoTheme globalTheme;

    // ==================== Global Theme ====================

    /**
     * Sets a global theme applied to every dialog created afterwards.
     *
     * <p>Call this once in your {@code Application.onCreate()} to keep a
     * consistent look across all dialogs.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.initGlobalTheme(BuiltInThemes.MATERIAL_3());
     * }</pre>
     *
     * @param theme The theme to apply globally, or {@code null} to clear.
     * @see DialogoTheme
     * @see com.abdullghani.dialogo.theme.BuiltInThemes
     */
    public static void initGlobalTheme(DialogoTheme theme) {
        globalTheme = theme;
    }

    /**
     * Returns the currently applied global theme.
     *
     * @return The global theme, or {@code null} if none is set.
     */
    public static DialogoTheme getGlobalTheme() {
        return globalTheme;
    }

    // ==================== Fluent Entry ====================

    /**
     * Creates a new {@link DialogoBuilder} attached to the given context.
     *
     * <p>This is the primary entry point for building dialogs.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Hello")
     *     .message("World")
     *     .positive("OK", null)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param context Any Android {@link Context} (Activity, Fragment, etc.).
     * @return A new {@link DialogoBuilder} instance.
     */
    public static DialogoBuilder with(@NonNull Context context) {
        return new DialogoBuilder(context);
    }

    /**
     * Creates a {@link DialogoBuilder} without an attached context.
     *
     * <p>Use this only for advanced cases where you manage context yourself.
     * For most use cases, prefer {@link #with(Context)}.</p>
     *
     * @return A new context-less {@link DialogoBuilder}.
     */
    public static DialogoBuilder create() {
        return new DialogoBuilder();
    }

    // ==================== Preset: SUCCESS ====================

    /**
     * Builds a "Success" preset dialog with a green check icon.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.success(this, "Saved!", "Your data was saved successfully.")
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder success(@NonNull Context context, CharSequence title, CharSequence message) {
        return success(context, title, message, null);
    }

    /**
     * Builds a "Success" preset dialog with a custom OK callback.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @param onOk    Callback invoked when the user taps OK; may be {@code null}.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder success(@NonNull Context context, CharSequence title, CharSequence message,
                                         DialogoAction.OnClickListener onOk) {
        return with(context)
                .icon(R.drawable.check_circle_1)
                .iconTint(0xFF16A34A)
                .iconSize(45)
                .title(title)
                .titleColor(0xFF15803D)
                .message(message)
                .positive("OK", 0xFF16A34A, onOk);
    }

    // ==================== Preset: ERROR ====================

    /**
     * Builds an "Error" preset dialog with a red alert icon and shake animation.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder error(@NonNull Context context, CharSequence title, CharSequence message) {
        return error(context, title, message, null);
    }

    /**
     * Builds an "Error" preset dialog with a custom OK callback.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @param onOk    Callback invoked when the user taps OK; may be {@code null}.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder error(@NonNull Context context, CharSequence title, CharSequence message,
                                       DialogoAction.OnClickListener onOk) {
        return with(context)
                .icon(android.R.drawable.ic_dialog_alert)
                .iconTint(0xFFDC2626)
                .iconSize(45)
                .title(title)
                .titleColor(0xFFDC2626)
                .message(message)
                .animation(DialogoAnimation.SHAKE)
                .positive("OK", 0xFFDC2626, onOk);
    }

    // ==================== Preset: WARNING ====================

    /**
     * Builds a "Warning" preset dialog with an amber alert icon.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder warning(@NonNull Context context, CharSequence title, CharSequence message) {
        return warning(context, title, message, null);
    }

    /**
     * Builds a "Warning" preset dialog with a custom OK callback.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @param onOk    Callback invoked when the user taps OK; may be {@code null}.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder warning(@NonNull Context context, CharSequence title, CharSequence message,
                                         DialogoAction.OnClickListener onOk) {
        return with(context)
                .icon(android.R.drawable.ic_dialog_alert)
                .iconTint(0xFFF59E0B)
                .iconSize(45)
                .title(title)
                .titleColor(0xFFB45309)
                .message(message)
                .positive("OK", 0xFFF59E0B, onOk);
    }

    // ==================== Preset: INFO ====================

    /**
     * Builds an "Info" preset dialog with a blue info icon.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder info(@NonNull Context context, CharSequence title, CharSequence message) {
        return info(context, title, message, null);
    }

    /**
     * Builds an "Info" preset dialog with a custom OK callback.
     *
     * @param context Activity or Fragment context.
     * @param title   Dialog title.
     * @param message Dialog message body.
     * @param onOk    Callback invoked when the user taps OK; may be {@code null}.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder info(@NonNull Context context, CharSequence title, CharSequence message,
                                      DialogoAction.OnClickListener onOk) {
        return with(context)
                .icon(android.R.drawable.ic_dialog_info)
                .iconTint(0xFF2563EB)
                .iconSize(45)
                .title(title)
                .titleColor(0xFF1D4ED8)
                .message(message)
                .positive("OK", 0xFF2563EB, onOk);
    }

    // ==================== Preset: LOADING ====================

    /**
     * Builds a non-cancelable loading dialog with a circular indeterminate progress.
     *
     * <p>The dialog stays open until you explicitly dismiss it. Keep a reference
     * to the returned {@link com.abdullghani.dialogo.core.DialogoFragment} to
     * dismiss it later.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * DialogoFragment loading = Dialogo.loading(this, "Processing...")
     *     .show(getSupportFragmentManager());
     *
     * // ... later:
     * loading.dismiss();
     * }</pre>
     *
     * @param context Activity or Fragment context.
     * @param message The loading message displayed to the user.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder loading(@NonNull Context context, CharSequence message) {
        return with(context)
                .message(message)
                .progress(true)
                .progressColor(0xFF2563EB)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);
    }

    /**
     * Builds a loading dialog that auto-dismisses after the given delay.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.loading(this, "Connecting...", 5000)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param context       Activity or Fragment context.
     * @param message       The loading message.
     * @param autoDismissMs Milliseconds before auto-dismiss.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder loading(@NonNull Context context, CharSequence message, long autoDismissMs) {
        return loading(context, message).autoDismissAfter(autoDismissMs);
    }

    // ==================== Preset: DELETE ====================

    /**
     * Builds a "Delete confirmation" dialog with a red delete icon
     * and shake animation.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.deleteConfirm(this, "important_file.pdf", controller -> {
     *     deleteFile();
     * }).show(getSupportFragmentManager());
     * }</pre>
     *
     * @param context   Activity or Fragment context.
     * @param itemName  Name of the item to delete (shown in the message).
     * @param onConfirm Callback invoked when the user confirms deletion.
     * @return A pre-configured {@link DialogoBuilder}.
     */
    public static DialogoBuilder deleteConfirm(@NonNull Context context,
                                               CharSequence itemName,
                                               DialogoAction.OnClickListener onConfirm) {
        return with(context)
                .icon(android.R.drawable.ic_menu_delete)
                .iconTint(0xFFDC2626)
                .iconSize(56)
                .title("Delete?")
                .message("Are you sure you want to delete \"" + itemName + "\"? This cannot be undone.")
                .animation(DialogoAnimation.SHAKE)
                .positive("Delete", 0xFFDC2626, onConfirm)
                .negative("Cancel", null);
    }

    // ==================== Preset: POPUP (Non-Modal) ====================

    /**
     * Builds a non-modal popup dialog positioned at the top of the screen,
     * 12dp below the status bar.
     *
     * <p>Non-modal popups do not block touches behind them — the user can
     * interact with the app while the popup is visible. This makes them
     * ideal for notifications, tips, or transient feedback.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.popup(this, "New message", "You have a new message from Sara")
     *     .autoDismissAfter(3000)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param context Activity or Fragment context.
     * @param title   Popup title.
     * @param message Popup message body.
     * @return A pre-configured non-modal {@link DialogoBuilder}.
     */
    public static DialogoBuilder popup(@NonNull Context context, CharSequence title, CharSequence message) {
        return with(context)
                .title(title)
                .message(message)
                .asPopup()
                .positionTop()
                .marginTopDp(12)
                .widthPercentage(0.92f)
                .animation(DialogoAnimation.SLIDE_DOWN)
                .autoDismiss(false);
    }

    /**
     * Builds a non-modal popup that auto-dismisses after the given delay.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.popup(this, "Saved", "Changes saved.", 3000)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param context       Activity or Fragment context.
     * @param title         Popup title.
     * @param message       Popup message body.
     * @param autoDismissMs Milliseconds before auto-dismiss.
     * @return A pre-configured non-modal {@link DialogoBuilder}.
     */
    public static DialogoBuilder popup(@NonNull Context context, CharSequence title,
                                       CharSequence message, long autoDismissMs) {
        return popup(context, title, message).autoDismissAfter(autoDismissMs);
    }
}