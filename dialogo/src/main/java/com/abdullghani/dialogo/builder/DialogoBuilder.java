package com.abdullghani.dialogo.builder;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentManager;

import com.abdullghani.dialogo.Dialogo;
import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.core.DialogoBottomSheet;
import com.abdullghani.dialogo.core.DialogoFragment;
import com.abdullghani.dialogo.model.DialogoAction;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.DialogoPosition;
import com.abdullghani.dialogo.model.DialogoResult;
import com.abdullghani.dialogo.model.FormField;
import com.abdullghani.dialogo.theme.DialogoTheme;
import com.abdullghani.dialogo.window.WindowHelper;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/**
 * Fluent builder for constructing and displaying dialogs.
 *
 * <p>Obtained via {@link Dialogo#with(Context)}. Every configuration method
 * returns {@code this}, so you can chain calls in a single statement.</p>
 *
 * <h3>Basic Example</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Save changes?")
 *     .message("Do you want to save?")
 *     .positive("Save", controller -> save())
 *     .negative("Discard", null)
 *     .show(getSupportFragmentManager());
 * }</pre>
 *
 * <h3>Showing Options</h3>
 * <ul>
 *     <li>{@link #show(FragmentManager)} — regular modal dialog</li>
 *     <li>{@link #showBottomSheet(FragmentManager)} — bottom sheet dialog</li>
 *     <li>{@link #showForResult(FragmentManager, DialogoParams.ResultListener)} — dialog with unified result callback</li>
 * </ul>
 *
 * @see Dialogo
 * @see DialogoFragment
 * @see DialogoBottomSheet
 */
public class DialogoBuilder {

    private final WeakReference<Context> contextRef;
    private final DialogoParams params;

    /**
     * Creates a builder bound to the given context.
     *
     * @param context Activity or Fragment context (stored as a weak reference).
     */
    public DialogoBuilder(@NonNull Context context) {
        this.contextRef = new WeakReference<>(context);
        this.params = new DialogoParams();
        applyGlobalTheme();
    }

    /**
     * Creates a builder without a bound context.
     *
     * <p>Prefer {@link #DialogoBuilder(Context)} in most cases.</p>
     */
    public DialogoBuilder() {
        this.contextRef = null;
        this.params = new DialogoParams();
        applyGlobalTheme();
    }

    private void applyGlobalTheme() {
        DialogoTheme theme = Dialogo.getGlobalTheme();
        if (theme == null) return;

        if (theme.getBackgroundColor() != null) params.backgroundColor = theme.getBackgroundColor();
        if (theme.getCornerRadiusPx() != null) params.cornerRadiusPx = theme.getCornerRadiusPx();
        if (theme.getDefaultFont() != null) {
            params.titleFont = theme.getDefaultFont();
            params.messageFont = theme.getDefaultFont();
            params.buttonFont = theme.getDefaultFont();
        }
        if (theme.getTitleColor() != null) params.titleColor = theme.getTitleColor();
        if (theme.getMessageColor() != null) params.messageColor = theme.getMessageColor();
        if (theme.getAnimation() != null) params.animation = theme.getAnimation();
    }

    // ==================== Title & Message ====================

    /**
     * Sets the dialog title.
     *
     * @param title Title text.
     * @return This builder for chaining.
     */
    public DialogoBuilder title(CharSequence title) { params.title = title; return this; }

    /**
     * Sets the dialog message body.
     *
     * @param message Message text.
     * @return This builder for chaining.
     */
    public DialogoBuilder message(CharSequence message) { params.message = message; return this; }

    /**
     * Sets the title text color.
     *
     * @param color ARGB color (e.g., {@code 0xFF1D4ED8}).
     * @return This builder for chaining.
     */
    public DialogoBuilder titleColor(@ColorInt int color) { params.titleColor = color; return this; }

    /**
     * Sets the message text color.
     *
     * @param color ARGB color.
     * @return This builder for chaining.
     */
    public DialogoBuilder messageColor(@ColorInt int color) { params.messageColor = color; return this; }

    /**
     * Sets the title text size.
     *
     * @param sp Size in scale-independent pixels.
     * @return This builder for chaining.
     */
    public DialogoBuilder titleSize(int sp) { params.titleSizeSp = sp; return this; }

    /**
     * Sets the message text size.
     *
     * @param sp Size in scale-independent pixels.
     * @return This builder for chaining.
     */
    public DialogoBuilder messageSize(int sp) { params.messageSizeSp = sp; return this; }

    /**
     * Sets the title gravity.
     *
     * @param gravity One of {@link android.view.Gravity} constants.
     * @return This builder for chaining.
     */
    public DialogoBuilder titleGravity(int gravity) { params.titleGravity = gravity; return this; }

    /**
     * Sets the message gravity.
     *
     * @param gravity One of {@link android.view.Gravity} constants.
     * @return This builder for chaining.
     */
    public DialogoBuilder messageGravity(int gravity) { params.messageGravity = gravity; return this; }

    /**
     * Enables or disables bold styling for the title.
     *
     * @param bold {@code true} to make the title bold.
     * @return This builder for chaining.
     */
    public DialogoBuilder titleBold(boolean bold) { params.titleBold = bold; return this; }

    /**
     * Sets a common font for title, message, and buttons.
     *
     * @param font A {@link Typeface}.
     * @return This builder for chaining.
     */
    public DialogoBuilder font(Typeface font) {
        params.titleFont = font;
        params.messageFont = font;
        params.buttonFont = font;
        return this;
    }

    /** Sets the title font only. */
    public DialogoBuilder titleFont(Typeface font) { params.titleFont = font; return this; }
    /** Sets the message font only. */
    public DialogoBuilder messageFont(Typeface font) { params.messageFont = font; return this; }
    /** Sets the buttons font only. */
    public DialogoBuilder buttonFont(Typeface font) { params.buttonFont = font; return this; }

    /**
     * Enables HTML rendering in the message.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Formatted")
     *     .htmlMessage(true)
     *     .message("This is <b>bold</b> and <i>italic</i>.")
     *     .positive("OK", null)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param enable {@code true} to render the message as HTML.
     * @return This builder for chaining.
     */
    public DialogoBuilder htmlMessage(boolean enable) { params.htmlMessage = enable; return this; }

    /**
     * Makes the message selectable and copyable on long-press.
     *
     * @param enable {@code true} to allow copying.
     * @return This builder for chaining.
     */
    public DialogoBuilder copyableMessage(boolean enable) { params.copyableMessage = enable; return this; }

    // ==================== Icon ====================

    /** Sets the icon from a drawable resource. */
    public DialogoBuilder icon(@DrawableRes int iconRes) { params.iconRes = iconRes; return this; }

    /** Sets the icon from a {@link Drawable}. */
    public DialogoBuilder icon(Drawable drawable) { params.iconDrawable = drawable; return this; }

    /**
     * Sets the icon tint color.
     *
     * @param color ARGB color.
     * @return This builder for chaining.
     */
    public DialogoBuilder iconTint(@ColorInt int color) { params.iconTint = color; return this; }

    /**
     * Sets the icon size.
     *
     * @param dp Size in density-independent pixels.
     * @return This builder for chaining.
     */
    public DialogoBuilder iconSize(int dp) { params.iconSizeDp = dp; return this; }

    /**
     * Enables a subtle bounce animation when the icon appears.
     *
     * @param enable {@code true} to animate the icon.
     * @return This builder for chaining.
     */
    public DialogoBuilder animateIcon(boolean enable) { params.animateIcon = enable; return this; }

    // ==================== Custom View ====================

    /**
     * Inflates a custom layout inside the dialog.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Custom")
     *     .customView(R.layout.my_layout, (view, controller) -> {
     *         Button b = view.findViewById(R.id.btn);
     *         b.setOnClickListener(v -> controller.dismiss());
     *     })
     *     .positive("OK", null)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param layoutRes Layout resource ID.
     * @param listener  Callback invoked after inflation; may be {@code null}.
     * @return This builder for chaining.
     */
    public DialogoBuilder customView(@LayoutRes int layoutRes, DialogoParams.OnViewCreatedListener listener) {
        params.customLayoutRes = layoutRes;
        params.onViewCreatedListener = listener;
        return this;
    }

    /** Sets a custom {@link View} instance instead of a layout resource. */
    public DialogoBuilder customView(View view, DialogoParams.OnViewCreatedListener listener) {
        params.customView = view;
        params.onViewCreatedListener = listener;
        return this;
    }

    // ==================== Input ====================

    /**
     * Adds a single-line text input field.
     *
     * @param hint Placeholder text.
     * @return This builder for chaining.
     */
    public DialogoBuilder input(CharSequence hint) {
        params.showInput = true;
        params.inputHint = hint;
        return this;
    }

    /**
     * Adds a text input field with a pre-filled value.
     *
     * @param hint    Placeholder text.
     * @param preFill Initial text value.
     * @return This builder for chaining.
     */
    public DialogoBuilder input(CharSequence hint, CharSequence preFill) {
        params.showInput = true;
        params.inputHint = hint;
        params.inputPreFill = preFill;
        return this;
    }

    /** Sets the input keyboard type (see {@link android.text.InputType}). */
    public DialogoBuilder inputType(int type) { params.inputType = type; return this; }

    /** Sets the maximum number of lines for the input field. */
    public DialogoBuilder inputMaxLines(int maxLines) { params.inputMaxLines = maxLines; return this; }

    /** Sets the input text color. */
    public DialogoBuilder inputTextColor(@ColorInt int color) { params.inputTextColor = color; return this; }

    /**
     * Marks the input as required — empty input will prevent the positive
     * button action and show {@link #inputRequiredError(CharSequence)}.
     *
     * @param required {@code true} to require non-empty input.
     * @return This builder for chaining.
     */
    public DialogoBuilder inputRequired(boolean required) { params.inputRequired = required; return this; }

    /** Sets the error message shown when required input is empty. */
    public DialogoBuilder inputRequiredError(CharSequence error) { params.inputRequiredError = error; return this; }

    // ==================== List ====================

    /**
     * Sets the list items as varargs.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Pick one")
     *     .items("Option A", "Option B", "Option C")
     *     .itemClick((pos, item, c) -> { })
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param items The list items.
     * @return This builder for chaining.
     */
    public DialogoBuilder items(CharSequence... items) {
        params.listItems = Arrays.asList(items);
        return this;
    }

    /** Sets the list items as a {@link List}. */
    public DialogoBuilder items(List<CharSequence> items) {
        params.listItems = items;
        return this;
    }

    /**
     * Sets an icon for each list item. The number of icons should match
     * the number of items.
     *
     * @param icons Drawable resource IDs.
     * @return This builder for chaining.
     */
    public DialogoBuilder itemIcons(@DrawableRes Integer... icons) {
        params.listIcons = Arrays.asList(icons);
        return this;
    }

    /** Sets list item icons from a {@link List}. */
    public DialogoBuilder itemIcons(List<Integer> icons) {
        params.listIcons = icons;
        return this;
    }

    /** Sets a tint applied to all list item icons. */
    public DialogoBuilder itemIconTint(@ColorInt int color) {
        params.listIconTint = color;
        return this;
    }

    /**
     * Registers a click listener for plain (non-choice) list items.
     *
     * @param listener Called with (position, item, controller).
     * @return This builder for chaining.
     */
    public DialogoBuilder itemClick(DialogoAction.OnListItemClickListener listener) {
        params.onListItemClick = listener;
        return this;
    }

    /**
     * Enables single-choice mode on the list.
     *
     * <p>The callback is invoked once, when the user confirms by tapping the
     * positive button or dismissing the dialog.</p>
     *
     * @param selectedIndex Initial selected index (use -1 for none).
     * @param onConfirm     Callback invoked on confirmation.
     * @return This builder for chaining.
     */
    public DialogoBuilder singleChoice(int selectedIndex, DialogoAction.OnListItemClickListener onConfirm) {
        params.listSingleChoice = true;
        params.listSelectedIndex = selectedIndex;
        params.onListItemConfirmed = onConfirm;
        return this;
    }

    /**
     * Enables single-choice mode with both live-preview and confirm callbacks.
     *
     * @param selectedIndex Initial selected index.
     * @param onSelected    Invoked every time the user selects an item (optional).
     * @param onConfirmed   Invoked on final confirmation.
     * @return This builder for chaining.
     */
    public DialogoBuilder singleChoice(int selectedIndex,
                                       DialogoAction.OnListItemClickListener onSelected,
                                       DialogoAction.OnListItemClickListener onConfirmed) {
        params.listSingleChoice = true;
        params.listSelectedIndex = selectedIndex;
        params.onListItemSelected = onSelected;
        params.onListItemConfirmed = onConfirmed;
        return this;
    }

    /**
     * Enables multi-choice mode with a confirmation callback.
     *
     * @param checkedItems Initial checked states.
     * @param listener     Invoked with the final checked array on confirmation.
     * @return This builder for chaining.
     */
    public DialogoBuilder multiChoice(boolean[] checkedItems, DialogoAction.OnMultiChoiceListener listener) {
        params.listMultiChoice = true;
        params.listCheckedItems = checkedItems;
        params.onMultiChoiceConfirm = listener;
        return this;
    }

    // ==================== Progress ====================

    /**
     * Shows a horizontal determinate progress bar.
     *
     * @return This builder for chaining.
     */
    public DialogoBuilder progress() {
        params.showProgress = true;
        params.progressIndeterminate = false;
        return this;
    }

    /**
     * Shows a progress bar.
     *
     * @param indeterminate {@code true} for circular indeterminate,
     *                      {@code false} for horizontal determinate.
     * @return This builder for chaining.
     */
    public DialogoBuilder progress(boolean indeterminate) {
        params.showProgress = true;
        params.progressIndeterminate = indeterminate;
        return this;
    }

    /**
     * Sets the current progress value (0..{@link #progressMax(int)}).
     *
     * @param value Progress value.
     * @return This builder for chaining.
     */
    public DialogoBuilder progressValue(int value) {
        params.showProgress = true;
        params.progressIndeterminate = false;
        params.progressValue = Math.max(0, Math.min(params.progressMax, value));
        return this;
    }

    /** Sets the maximum progress value (default 100). */
    public DialogoBuilder progressMax(int max) { params.progressMax = max; return this; }

    /** Sets the text displayed alongside the progress. */
    public DialogoBuilder progressText(CharSequence text) { params.progressText = text; return this; }

    /** Toggles the visibility of the percentage text. */
    public DialogoBuilder showProgressPercent(boolean show) { params.showProgressPercent = show; return this; }

    /** Sets the progress bar color. */
    public DialogoBuilder progressColor(@ColorInt int color) { params.progressColor = color; return this; }

    // ==================== Buttons ====================

    /**
     * Sets the positive button with a click listener.
     *
     * <p>The positive button is the primary action ("OK", "Save", "Yes").</p>
     *
     * @param text     Button label.
     * @param listener Click callback, or {@code null} for auto-dismiss only.
     * @return This builder for chaining.
     */
    public DialogoBuilder positive(CharSequence text, DialogoAction.OnClickListener listener) {
        return positive(text, null, listener);
    }

    /**
     * Sets the positive button with a text color and click listener.
     *
     * @param text      Button label.
     * @param textColor Text color, or {@code null} for default.
     * @param listener  Click callback.
     * @return This builder for chaining.
     */
    public DialogoBuilder positive(CharSequence text, @ColorInt Integer textColor, DialogoAction.OnClickListener listener) {
        params.positiveAction = new DialogoAction(text, textColor, listener);
        return this;
    }

    /** Sets the positive button with no listener. */
    public DialogoBuilder positive(CharSequence text) {
        return positive(text, null, null);
    }

    /**
     * Sets the negative button with a click listener.
     *
     * <p>The negative button is the secondary action ("Cancel", "No").</p>
     *
     * @param text     Button label.
     * @param listener Click callback, or {@code null}.
     * @return This builder for chaining.
     */
    public DialogoBuilder negative(CharSequence text, DialogoAction.OnClickListener listener) {
        return negative(text, null, listener);
    }

    /** Sets the negative button with a text color. */
    public DialogoBuilder negative(CharSequence text, @ColorInt Integer textColor, DialogoAction.OnClickListener listener) {
        params.negativeAction = new DialogoAction(text, textColor, listener);
        return this;
    }

    /** Sets the negative button with no listener. */
    public DialogoBuilder negative(CharSequence text) {
        return negative(text, null, null);
    }

    /**
     * Sets the neutral button with a click listener.
     *
     * <p>The neutral button is the tertiary action ("Later", "Details").</p>
     *
     * @param text     Button label.
     * @param listener Click callback, or {@code null}.
     * @return This builder for chaining.
     */
    public DialogoBuilder neutral(CharSequence text, DialogoAction.OnClickListener listener) {
        return neutral(text, null, listener);
    }

    /** Sets the neutral button with a text color. */
    public DialogoBuilder neutral(CharSequence text, @ColorInt Integer textColor, DialogoAction.OnClickListener listener) {
        params.neutralAction = new DialogoAction(text, textColor, listener);
        return this;
    }

    /** Sets the neutral button with no listener. */
    public DialogoBuilder neutral(CharSequence text) {
        return neutral(text, null, null);
    }

    /** Sets a custom background color for the positive button. */
    public DialogoBuilder positiveBackgroundColor(@ColorInt int color) { params.positiveBackgroundColor = color; return this; }

    /** Sets a custom background color for the negative button. */
    public DialogoBuilder negativeBackgroundColor(@ColorInt int color) { params.negativeBackgroundColor = color; return this; }

    /** Sets a custom background color for the neutral button. */
    public DialogoBuilder neutralBackgroundColor(@ColorInt int color) { params.neutralBackgroundColor = color; return this; }

    /**
     * Stacks buttons vertically instead of the default horizontal layout.
     *
     * @param stacked {@code true} to stack vertically.
     * @return This builder for chaining.
     */
    public DialogoBuilder buttonsStacked(boolean stacked) { params.buttonsStacked = stacked; return this; }

    /**
     * Makes buttons expand to full width.
     *
     * @param fullWidth {@code true} for full-width buttons.
     * @return This builder for chaining.
     */
    public DialogoBuilder fullWidthButtons(boolean fullWidth) { params.fullWidthButtons = fullWidth; return this; }

    // ==================== Window & Style ====================

    /** Sets the dialog card background color. */
    public DialogoBuilder backgroundColor(@ColorInt int color) { params.backgroundColor = color; return this; }

    /** Sets the dialog corner radius in pixels. */
    public DialogoBuilder cornerRadius(float radiusPx) { params.cornerRadiusPx = radiusPx; return this; }

    /**
     * Sets the dim amount behind the dialog (0 = transparent, 1 = fully dimmed).
     *
     * @param amount Value between 0 and 1.
     * @return This builder for chaining.
     */
    public DialogoBuilder dimAmount(float amount) {
        params.dimAmount = Math.max(0f, Math.min(1f, amount));
        return this;
    }

    /**
     * Enables blur behind the dialog (requires Android 12+).
     *
     * @param enable {@code true} to enable blur.
     * @return This builder for chaining.
     */
    public DialogoBuilder blurBackground(boolean enable) { params.isBlurEnabled = enable; return this; }

    /** Sets a raw window animation style resource. */
    public DialogoBuilder animation(@StyleRes int animationRes) { params.animationRes = animationRes; return this; }

    /**
     * Sets the entry/exit animation.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Hello")
     *     .animation(DialogoAnimation.BOUNCE)
     *     .positive("OK", null)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param animation One of {@link DialogoAnimation}.
     * @return This builder for chaining.
     */
    public DialogoBuilder animation(DialogoAnimation animation) {
        if (animation != null) params.animation = animation;
        return this;
    }

    /** Sets the window gravity (advanced; prefer {@link #position}). */
    public DialogoBuilder gravity(int gravity) { params.gravity = gravity; return this; }

    /**
     * Sets the dialog width as a fraction of the screen.
     *
     * @param percent Value between 0.5f and 1.0f.
     * @return This builder for chaining.
     */
    public DialogoBuilder widthPercentage(float percent) {
        params.widthPercentage = Math.max(0.5f, Math.min(1f, percent));
        return this;
    }

    /**
     * Sets the maximum dialog height as a fraction of the screen.
     *
     * @param percent Value between 0.3f and 0.95f.
     * @return This builder for chaining.
     */
    public DialogoBuilder maxHeightPercentage(float percent) {
        params.maxHeightPercentage = Math.max(0.3f, Math.min(0.95f, percent));
        return this;
    }

    /** Sets the elevation (shadow depth) of the dialog card. */
    public DialogoBuilder elevation(float dp) {
        params.elevationEnabled = true;
        params.elevationDp = dp;
        return this;
    }

    /**
     * Adds a stroke (border) around the dialog card.
     *
     * @param color   Border color.
     * @param widthPx Border width in pixels.
     * @return This builder for chaining.
     */
    public DialogoBuilder stroke(@ColorInt int color, float widthPx) {
        params.strokeColor = color;
        params.strokeWidthPx = widthPx;
        return this;
    }

    /** Enables animated dim fade-in. */
    public DialogoBuilder animateDim(boolean animate) { params.animateDim = animate; return this; }

    // ==================== Position ====================

    /**
     * Sets the dialog position on screen.
     *
     * @param position One of {@link DialogoPosition}: TOP, CENTER, BOTTOM.
     * @return This builder for chaining.
     */
    public DialogoBuilder position(DialogoPosition position) {
        if (position != null) params.position = position;
        return this;
    }

    /** Convenience shortcut for {@code position(DialogoPosition.TOP)}. */
    public DialogoBuilder positionTop() {
        params.position = DialogoPosition.TOP;
        return this;
    }

    /** Convenience shortcut for {@code position(DialogoPosition.CENTER)}. */
    public DialogoBuilder positionCenter() {
        params.position = DialogoPosition.CENTER;
        return this;
    }

    /** Convenience shortcut for {@code position(DialogoPosition.BOTTOM)}. */
    public DialogoBuilder positionBottom() {
        params.position = DialogoPosition.BOTTOM;
        return this;
    }

    // ==================== Margins (px) ====================

    /** Sets horizontal margin in pixels. */
    public DialogoBuilder marginHorizontal(int px) {
        params.marginHorizontalPx = Math.max(0, px);
        return this;
    }

    /** Sets top margin in pixels. */
    public DialogoBuilder marginTop(int px) {
        params.marginTopPx = Math.max(0, px);
        return this;
    }

    /** Sets bottom margin in pixels. */
    public DialogoBuilder marginBottom(int px) {
        params.marginBottomPx = Math.max(0, px);
        return this;
    }

    // ==================== Margins (dp) ====================

    /** Sets horizontal margin in dp. */
    public DialogoBuilder marginHorizontalDp(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.marginHorizontalPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }

    /**
     * Sets top margin in dp.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.popup(this, "Hi", "Message")
     *     .marginTopDp(12)
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param dp Margin in density-independent pixels.
     * @return This builder for chaining.
     */
    public DialogoBuilder marginTopDp(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.marginTopPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }

    /** Sets bottom margin in dp. */
    public DialogoBuilder marginBottomDp(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.marginBottomPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }

    /** Sets an additional vertical offset from the top (advanced). */
    public DialogoBuilder offsetY(int px) {
        params.offsetYPx = px;
        return this;
    }

    // ==================== Non-Modal Popup ====================

    /**
     * Converts the dialog into a non-modal popup.
     *
     * <p>Non-modal popups:</p>
     * <ul>
     *     <li>Do not dim the background</li>
     *     <li>Allow touches to pass through to views behind</li>
     *     <li>Can be dismissed by tapping outside</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Quick note")
     *     .message("This doesn't block the app.")
     *     .asPopup()
     *     .positionTop()
     *     .show(getSupportFragmentManager());
     * }</pre>
     *
     * @param nonModal {@code true} to enable non-modal mode.
     * @return This builder for chaining.
     */
    public DialogoBuilder nonModal(boolean nonModal) {
        params.nonModal = nonModal;
        if (nonModal) {
            params.dimAmount = 0f;
            params.canceledOnTouchOutside = true;
        }
        return this;
    }

    /** Convenience shortcut for {@code nonModal(true)}. */
    public DialogoBuilder asPopup() {
        return nonModal(true);
    }

    /** If {@code true}, the popup dismisses when the user taps outside. */
    public DialogoBuilder dismissOnOutsideClick(boolean dismiss) {
        params.canceledOnTouchOutside = dismiss;
        return this;
    }

    // ==================== Behavior ====================

    /** Sets whether the dialog can be dismissed with the Back button. */
    public DialogoBuilder cancelable(boolean cancelable) { params.isCancelable = cancelable; return this; }

    /** Sets whether the dialog dismisses on outside touch. */
    public DialogoBuilder canceledOnTouchOutside(boolean cancel) { params.canceledOnTouchOutside = cancel; return this; }

    /** Enables or disables auto-dismiss on button press. */
    public DialogoBuilder autoDismiss(boolean autoDismiss) { params.autoDismiss = autoDismiss; return this; }

    /** Auto-dismisses the dialog after the given delay (milliseconds). */
    public DialogoBuilder autoDismissAfter(long delayMs) { params.autoDismissDelayMs = delayMs; return this; }

    /** Shows a countdown text when using {@link #autoDismissAfter(long)}. */
    public DialogoBuilder showCountdown(boolean show) { params.showCountdown = show; return this; }

    // ==================== Listeners ====================

    /** Registers a listener invoked when the dialog is shown. */
    public DialogoBuilder onShow(DialogoAction.OnShowListener listener) { params.onShowListener = listener; return this; }

    /** Registers a listener invoked when the dialog is dismissed. */
    public DialogoBuilder onDismiss(DialogoAction.OnDismissListener listener) { params.onDismissListener = listener; return this; }

    // ==================== Build & Show ====================

    /**
     * Builds the {@link DialogoFragment} without showing it.
     *
     * @return A new {@link DialogoFragment} instance.
     */
    public DialogoFragment build() {
        return DialogoFragment.newInstance(params);
    }

    /**
     * Shows the dialog as a standard modal dialog.
     *
     * @param fragmentManager The host's {@link FragmentManager}.
     * @return The created {@link DialogoFragment}.
     */
    public DialogoFragment show(@NonNull FragmentManager fragmentManager) {
        return show(fragmentManager, "DialogoFragment");
    }

    /**
     * Shows the dialog with a specific tag.
     *
     * @param fragmentManager The host's {@link FragmentManager}.
     * @param tag             Fragment tag, or {@code null} for default.
     * @return The created {@link DialogoFragment}.
     */
    public DialogoFragment show(@NonNull FragmentManager fragmentManager, String tag) {
        DialogoFragment fragment = build();
        fragment.show(fragmentManager, tag != null ? tag : "DialogoFragment");
        return fragment;
    }

    // ==================== BottomSheet ====================

    /** Shows the dialog as a BottomSheet. */
    public DialogoBottomSheet showBottomSheet(@NonNull FragmentManager fragmentManager) {
        return showBottomSheet(fragmentManager, "DialogoBottomSheet");
    }

    /** Shows the dialog as a BottomSheet with a specific tag. */
    public DialogoBottomSheet showBottomSheet(@NonNull FragmentManager fragmentManager, String tag) {
        DialogoBottomSheet sheet = DialogoBottomSheet.newInstance(params);
        sheet.show(fragmentManager, tag != null ? tag : "DialogoBottomSheet");
        return sheet;
    }

    // ==================== Result API ====================

    /**
     * Shows the dialog and delivers a unified {@link DialogoResult} via a listener.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * Dialogo.with(this)
     *     .title("Delete?")
     *     .positive("Yes")
     *     .negative("No")
     *     .showForResult(getSupportFragmentManager(), result -> {
     *         if (result.isPositive()) { deleteItem(); }
     *     });
     * }</pre>
     *
     * @param fragmentManager The host's {@link FragmentManager}.
     * @param listener        Called once when the dialog is dismissed.
     * @return The created {@link DialogoFragment}.
     */
    public DialogoFragment showForResult(@NonNull FragmentManager fragmentManager,
                                         @NonNull DialogoParams.ResultListener listener) {
        return showForResult(fragmentManager, "DialogoFragment", listener);
    }

    /** Same as {@link #showForResult(FragmentManager, DialogoParams.ResultListener)} with a custom tag. */
    public DialogoFragment showForResult(@NonNull FragmentManager fragmentManager,
                                         String tag,
                                         @NonNull DialogoParams.ResultListener listener) {
        params.resultListener = listener;
        DialogoFragment fragment = build();
        fragment.show(fragmentManager, tag != null ? tag : "DialogoFragment");
        return fragment;
    }

    // ==================== Form ====================

    /** Adds a required single-line text field. */
    public DialogoBuilder addText(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.TEXT);
        f.label = label;
        f.hint = hint;
        f.required = true;
        params.formFields.add(f);
        return this;
    }

    /** Adds a required password field. */
    public DialogoBuilder addPassword(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.PASSWORD);
        f.label = label;
        f.hint = hint;
        f.required = true;
        params.formFields.add(f);
        return this;
    }

    /** Adds a number field. */
    public DialogoBuilder addNumber(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.NUMBER);
        f.label = label;
        f.hint = hint;
        params.formFields.add(f);
        return this;
    }

    /** Adds an email field. */
    public DialogoBuilder addEmail(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.EMAIL);
        f.label = label;
        f.hint = hint;
        params.formFields.add(f);
        return this;
    }

    /** Adds a phone-number field. */
    public DialogoBuilder addPhone(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.PHONE);
        f.label = label;
        f.hint = hint;
        params.formFields.add(f);
        return this;
    }

    /** Adds a multi-line text field. */
    public DialogoBuilder addMultiline(String id, CharSequence label, CharSequence hint, int maxLines) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.MULTILINE);
        f.label = label;
        f.hint = hint;
        f.maxLines = maxLines;
        params.formFields.add(f);
        return this;
    }

    /** Adds a toggle switch. */
    public DialogoBuilder addSwitch(String id, CharSequence label, boolean defaultValue) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.SWITCH);
        f.label = label;
        f.switchDefault = defaultValue;
        params.formFields.add(f);
        return this;
    }

    /** Adds a checkbox. */
    public DialogoBuilder addCheckbox(String id, CharSequence label, boolean defaultValue) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.CHECKBOX);
        f.label = label;
        f.checkboxDefault = defaultValue;
        params.formFields.add(f);
        return this;
    }

    /**
     * Adds a dropdown selector.
     *
     * @param id           Unique field ID.
     * @param label        Field label.
     * @param options      Dropdown options.
     * @param defaultIndex Initially selected index.
     * @return This builder for chaining.
     */
    public DialogoBuilder addDropdown(String id, CharSequence label,
                                      java.util.List<CharSequence> options, int defaultIndex) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.DROPDOWN);
        f.label = label;
        f.options = options;
        f.dropdownSelectedIndex = defaultIndex;
        params.formFields.add(f);
        return this;
    }

    /** Marks the previously added field as required. */
    public DialogoBuilder required(boolean req) {
        if (params.formFields != null && !params.formFields.isEmpty()) {
            params.formFields.get(params.formFields.size() - 1).required = req;
        }
        return this;
    }

    private void ensureFormFields() {
        if (params.formFields == null) {
            params.formFields = new java.util.ArrayList<>();
        }
    }
}