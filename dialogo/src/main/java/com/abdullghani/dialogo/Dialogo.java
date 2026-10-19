package com.abdullghani.dialogo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.builder.DialogoBuilder;
import com.abdullghani.dialogo.model.DialogoAction;
import com.abdullghani.dialogo.theme.DialogoTheme;

/**
 * Main entry point of the Dialogo library.
 */
public class Dialogo {

    private static DialogoTheme globalTheme;

    public static void initGlobalTheme(DialogoTheme theme) {
        globalTheme = theme;
    }

    public static DialogoTheme getGlobalTheme() {
        return globalTheme;
    }

    public static DialogoBuilder with(@NonNull Context context) {
        return new DialogoBuilder(context);
    }

    public static DialogoBuilder create() {
        return new DialogoBuilder();
    }

    // ==================== Preset: SUCCESS ====================

    public static DialogoBuilder success(@NonNull Context context, CharSequence title, CharSequence message) {
        return success(context, title, message, null);
    }

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

    public static DialogoBuilder error(@NonNull Context context, CharSequence title, CharSequence message) {
        return error(context, title, message, null);
    }

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

    public static DialogoBuilder warning(@NonNull Context context, CharSequence title, CharSequence message) {
        return warning(context, title, message, null);
    }

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

    public static DialogoBuilder info(@NonNull Context context, CharSequence title, CharSequence message) {
        return info(context, title, message, null);
    }

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

    public static DialogoBuilder loading(@NonNull Context context, CharSequence message) {
        return with(context)
                .message(message)
                .progress(true)
                .progressColor(0xFF2563EB)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);
    }

    public static DialogoBuilder loading(@NonNull Context context, CharSequence message, long autoDismissMs) {
        return loading(context, message).autoDismissAfter(autoDismissMs);
    }

    // ==================== Preset: DELETE ====================

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

    public static DialogoBuilder popup(@NonNull Context context, CharSequence title,
                                       CharSequence message, long autoDismissMs) {
        return popup(context, title, message).autoDismissAfter(autoDismissMs);
    }
}