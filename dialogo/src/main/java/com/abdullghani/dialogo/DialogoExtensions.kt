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
 * Example:
 * ```
 * dialogo(this) {
 *     title("Save changes?")
 *     message("Do you want to save your changes?")
 *     positive("Save") { saveChanges() }
 *     negative("Cancel")
 * }.show()
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

// ==================== Result API Extension ====================

/**
 * Shows the dialog and delivers a unified [DialogoResult] via a Kotlin lambda.
 * Automatically discovers the FragmentManager from the bound context.
 *
 * Example:
 * ```
 * dialogo(this) {
 *     title("Delete?")
 *     positive("Yes")
 *     negative("No")
 * }.showForResult { result ->
 *     if (result.isPositive) deleteItem()
 * }
 * ```
 *
 * @param onResult Called once when the dialog is dismissed.
 * @return The created [DialogoFragment].
 */
fun DialogoBuilder.showForResult(
    onResult: (DialogoResult) -> Unit
): DialogoFragment {
    return this.showForResult { result -> onResult(result) }
}

/**
 * Overload for explicitly passing a [FragmentManager].
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
 * no-argument lambda instead of a DialogoController callback.
 */
fun DialogoBuilder.positive(text: String, onClick: (() -> Unit)? = null): DialogoBuilder {
    return if (onClick != null) {
        this.positive(text) { onClick() }
    } else {
        this.positive(text)
    }
}

/**
 * Kotlin-friendly overload of [DialogoBuilder.negative].
 */
fun DialogoBuilder.negative(text: String, onClick: (() -> Unit)? = null): DialogoBuilder {
    return if (onClick != null) {
        this.negative(text) { onClick() }
    } else {
        this.negative(text)
    }
}

/**
 * Kotlin-friendly overload of [DialogoBuilder.neutral].
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
 * Automatically discovers the FragmentManager.
 */
fun Context.showSuccessDialog(title: String, message: String) {
    Dialogo.success(this, title, message).show()
}

/**
 * Shows a "Success" preset dialog using an explicit [FragmentManager].
 */
fun Context.showSuccessDialog(fragmentManager: FragmentManager, title: String, message: String) {
    Dialogo.success(this, title, message).show(fragmentManager)
}

/**
 * Shows an "Error" preset dialog directly from a [Context].
 * Automatically discovers the FragmentManager.
 */
fun Context.showErrorDialog(title: String, message: String) {
    Dialogo.error(this, title, message).show()
}

/**
 * Shows an "Error" preset dialog using an explicit [FragmentManager].
 */
fun Context.showErrorDialog(fragmentManager: FragmentManager, title: String, message: String) {
    Dialogo.error(this, title, message).show(fragmentManager)
}