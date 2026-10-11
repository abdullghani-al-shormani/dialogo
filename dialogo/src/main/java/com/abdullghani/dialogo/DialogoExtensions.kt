@file:JvmName("DialogoKt")

package com.abdullghani.dialogo

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.abdullghani.dialogo.builder.DialogoBuilder
import com.abdullghani.dialogo.core.DialogoFragment
import com.abdullghani.dialogo.model.DialogoResult

/**
 * Kotlin DSL entry point for building a Dialogo dialog.
 *
 * <p>Example:</p>
 * ```
 * dialogo(this) {
 *     title("Save changes?")
 *     message("Do you want to save your changes?")
 *     positive("Save") { saveChanges() }
 *     negative("Cancel")
 * }.show(supportFragmentManager)
 * ```
 *
 * @param context Activity or Fragment context.
 * @param block   Configuration lambda applied to the builder.
 * @return A configured [DialogoBuilder].
 */
fun dialogo(context: Context, block: DialogoBuilder.() -> Unit): DialogoBuilder {
    val builder = Dialogo.with(context)
    builder.block()
    return builder
}

/**
 * Shows the dialog and delivers a unified [DialogoResult] via a Kotlin lambda.
 *
 * <p>Example:</p>
 * ```
 * dialogo(this) {
 *     title("Delete?")
 *     positive("Yes")
 *     negative("No")
 * }.showForResult(supportFragmentManager) { result ->
 *     if (result.isPositive) deleteItem()
 * }
 * ```
 *
 * @param fragmentManager The host's FragmentManager.
 * @param onResult        Called once when the dialog is dismissed.
 * @return The created [DialogoFragment].
 */
fun DialogoBuilder.showForResult(
    fragmentManager: FragmentManager,
    onResult: (DialogoResult) -> Unit
): DialogoFragment {
    return this.showForResult(fragmentManager) { result -> onResult(result) }
}

// ==================== Button Shorthand Extensions ====================

/**
 * Kotlin-friendly overload of [DialogoBuilder.positive] accepting a
 * no-argument lambda instead of a [com.abdullghani.dialogo.core.DialogoController]
 * callback.
 *
 * <p>Example:</p>
 * ```
 * dialogo(this) {
 *     title("Save?")
 *     positive("Save") { saveChanges() }
 * }
 * ```
 *
 * @param text    Button label.
 * @param onClick Optional callback with no arguments; may be null.
 * @return This builder for chaining.
 */
fun DialogoBuilder.positive(text: String, onClick: (() -> Unit)? = null): DialogoBuilder {
    return if (onClick != null) {
        this.positive(text) { onClick() }
    } else {
        this.positive(text)
    }
}

/**
 * Kotlin-friendly overload of [DialogoBuilder.negative] accepting a
 * no-argument lambda.
 *
 * @param text    Button label.
 * @param onClick Optional callback; may be null.
 * @return This builder for chaining.
 */
fun DialogoBuilder.negative(text: String, onClick: (() -> Unit)? = null): DialogoBuilder {
    return if (onClick != null) {
        this.negative(text) { onClick() }
    } else {
        this.negative(text)
    }
}

/**
 * Kotlin-friendly overload of [DialogoBuilder.neutral] accepting a
 * no-argument lambda.
 *
 * @param text    Button label.
 * @param onClick Optional callback; may be null.
 * @return This builder for chaining.
 */
fun DialogoBuilder.neutral(text: String, onClick: (() -> Unit)? = null): DialogoBuilder {
    return if (onClick != null) {
        this.neutral(text) { onClick() }
    } else {
        this.neutral(text)
    }
}

// ==================== Context Preset Extensions ====================

/**
 * Shows a "Success" preset dialog directly from a [Context].
 *
 * <p>Example:</p>
 * ```
 * showSuccessDialog(supportFragmentManager, "Saved!", "Your data was saved.")
 * ```
 *
 * @param fragmentManager The host's FragmentManager.
 * @param title           Dialog title.
 * @param message         Dialog message body.
 */
fun Context.showSuccessDialog(
    fragmentManager: FragmentManager,
    title: String,
    message: String
) {
    Dialogo.success(this, title, message).show(fragmentManager)
}

/**
 * Shows an "Error" preset dialog directly from a [Context].
 *
 * @param fragmentManager The host's FragmentManager.
 * @param title           Dialog title.
 * @param message         Dialog message body.
 */
fun Context.showErrorDialog(
    fragmentManager: FragmentManager,
    title: String,
    message: String
) {
    Dialogo.error(this, title, message).show(fragmentManager)
}

// ==================== BottomSheet Extension ====================

/**
 * Shows the dialog as a BottomSheet using a default tag.
 *
 * <p>Example:</p>
 * ```
 * dialogo(this) {
 *     title("Options")
 *     items("Edit", "Share", "Delete")
 * }.showBottomSheetKt(supportFragmentManager)
 * ```
 *
 * @param fragmentManager The host's FragmentManager.
 * @param tag             Fragment tag; defaults to "DialogoBottomSheet".
 * @return The created [com.abdullghani.dialogo.core.DialogoBottomSheet].
 */
fun DialogoBuilder.showBottomSheetKt(
    fragmentManager: FragmentManager,
    tag: String = "DialogoBottomSheet"
) = this.showBottomSheet(fragmentManager, tag)