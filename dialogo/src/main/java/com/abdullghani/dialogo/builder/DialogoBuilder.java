package com.abdullghani.dialogo.builder;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.abdullghani.dialogo.Dialogo;
import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.core.DialogoFragment;
import com.abdullghani.dialogo.model.DialogoAction;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.DialogoPosition;
import com.abdullghani.dialogo.model.FormField;
import com.abdullghani.dialogo.theme.DialogoTheme;
import com.abdullghani.dialogo.window.WindowHelper;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/**
 * Fluent builder for constructing and displaying dialogs.
 */
public class DialogoBuilder {

    private final WeakReference<Context> contextRef;
    private final DialogoParams params;
    private WeakReference<DialogoFragment> lastFragmentRef;

    public DialogoBuilder(@NonNull Context context) {
        this.contextRef = new WeakReference<>(context);
        this.params = new DialogoParams();
        applyGlobalTheme();
    }

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

    public DialogoBuilder title(CharSequence title) { params.title = title; return this; }
    public DialogoBuilder message(CharSequence message) { params.message = message; return this; }
    public DialogoBuilder titleColor(@ColorInt int color) { params.titleColor = color; return this; }
    public DialogoBuilder messageColor(@ColorInt int color) { params.messageColor = color; return this; }
    public DialogoBuilder titleSize(int sp) { params.titleSizeSp = sp; return this; }
    public DialogoBuilder messageSize(int sp) { params.messageSizeSp = sp; return this; }
    public DialogoBuilder titleGravity(int gravity) { params.titleGravity = gravity; return this; }
    public DialogoBuilder messageGravity(int gravity) { params.messageGravity = gravity; return this; }
    public DialogoBuilder titleBold(boolean bold) { params.titleBold = bold; return this; }

    public DialogoBuilder font(Typeface font) {
        params.titleFont = font;
        params.messageFont = font;
        params.buttonFont = font;
        return this;
    }
    public DialogoBuilder titleFont(Typeface font) { params.titleFont = font; return this; }
    public DialogoBuilder messageFont(Typeface font) { params.messageFont = font; return this; }
    public DialogoBuilder buttonFont(Typeface font) { params.buttonFont = font; return this; }

    public DialogoBuilder htmlMessage(boolean enable) { params.htmlMessage = enable; return this; }
    public DialogoBuilder copyableMessage(boolean enable) { params.copyableMessage = enable; return this; }

    // ==================== Icon ====================

    public DialogoBuilder icon(@DrawableRes int iconRes) { params.iconRes = iconRes; return this; }
    public DialogoBuilder icon(Drawable drawable) { params.iconDrawable = drawable; return this; }
    public DialogoBuilder iconTint(@ColorInt int color) { params.iconTint = color; return this; }
    public DialogoBuilder iconSize(int dp) { params.iconSizeDp = dp; return this; }
    public DialogoBuilder animateIcon(boolean enable) { params.animateIcon = enable; return this; }

    // ==================== Custom View ====================

    public DialogoBuilder customView(@LayoutRes int layoutRes, DialogoParams.OnViewCreatedListener listener) {
        params.customLayoutRes = layoutRes;
        params.onViewCreatedListener = listener;
        return this;
    }

    public DialogoBuilder customView(View view, DialogoParams.OnViewCreatedListener listener) {
        params.customView = view;
        params.onViewCreatedListener = listener;
        return this;
    }

    // ==================== Input ====================

    public DialogoBuilder input(CharSequence hint) {
        params.showInput = true;
        params.inputHint = hint;
        return this;
    }

    public DialogoBuilder input(CharSequence hint, CharSequence preFill) {
        params.showInput = true;
        params.inputHint = hint;
        params.inputPreFill = preFill;
        return this;
    }

    public DialogoBuilder inputType(int type) { params.inputType = type; return this; }
    public DialogoBuilder inputMaxLines(int maxLines) { params.inputMaxLines = maxLines; return this; }
    public DialogoBuilder inputTextColor(@ColorInt int color) { params.inputTextColor = color; return this; }
    public DialogoBuilder inputRequired(boolean required) { params.inputRequired = required; return this; }
    public DialogoBuilder inputRequiredError(CharSequence error) { params.inputRequiredError = error; return this; }

    // ==================== List ====================

    public DialogoBuilder items(CharSequence... items) {
        params.listItems = Arrays.asList(items);
        return this;
    }

    public DialogoBuilder items(List<CharSequence> items) {
        params.listItems = items;
        return this;
    }

    public DialogoBuilder itemIcons(@DrawableRes Integer... icons) {
        params.listIcons = Arrays.asList(icons);
        return this;
    }

    public DialogoBuilder itemIcons(List<Integer> icons) {
        params.listIcons = icons;
        return this;
    }

    public DialogoBuilder itemIconTint(@ColorInt int color) {
        params.listIconTint = color;
        return this;
    }

    public DialogoBuilder itemClick(DialogoAction.OnListItemClickListener listener) {
        params.onListItemClick = listener;
        return this;
    }

    public DialogoBuilder singleChoice(int selectedIndex, DialogoAction.OnListItemClickListener onConfirm) {
        params.listSingleChoice = true;
        params.listSelectedIndex = selectedIndex;
        params.onListItemConfirmed = onConfirm;
        return this;
    }

    public DialogoBuilder singleChoice(int selectedIndex,
                                       DialogoAction.OnListItemClickListener onSelected,
                                       DialogoAction.OnListItemClickListener onConfirmed) {
        params.listSingleChoice = true;
        params.listSelectedIndex = selectedIndex;
        params.onListItemSelected = onSelected;
        params.onListItemConfirmed = onConfirmed;
        return this;
    }

    public DialogoBuilder multiChoice(boolean[] checkedItems, DialogoAction.OnMultiChoiceListener listener) {
        params.listMultiChoice = true;
        params.listCheckedItems = checkedItems;
        params.onMultiChoiceConfirm = listener;
        return this;
    }

    // ==================== Progress ====================

    public DialogoBuilder progress() {
        params.showProgress = true;
        params.progressIndeterminate = false;
        return this;
    }

    public DialogoBuilder progress(boolean indeterminate) {
        params.showProgress = true;
        params.progressIndeterminate = indeterminate;
        return this;
    }

    public DialogoBuilder progressValue(int value) {
        params.showProgress = true;
        params.progressIndeterminate = false;
        params.progressValue = Math.max(0, Math.min(params.progressMax, value));
        return this;
    }

    public DialogoBuilder progressMax(int max) { params.progressMax = max; return this; }
    public DialogoBuilder progressText(CharSequence text) { params.progressText = text; return this; }
    public DialogoBuilder showProgressPercent(boolean show) { params.showProgressPercent = show; return this; }
    public DialogoBuilder progressColor(@ColorInt int color) { params.progressColor = color; return this; }

    // ==================== Buttons ====================

    public DialogoBuilder positive(CharSequence text, DialogoAction.OnClickListener listener) {
        return positive(text, null, listener);
    }

    public DialogoBuilder positive(CharSequence text, @ColorInt Integer textColor, DialogoAction.OnClickListener listener) {
        params.positiveAction = new DialogoAction(text, textColor, listener);
        return this;
    }

    public DialogoBuilder positive(CharSequence text) {
        return positive(text, null, null);
    }

    public DialogoBuilder negative(CharSequence text, DialogoAction.OnClickListener listener) {
        return negative(text, null, listener);
    }

    public DialogoBuilder negative(CharSequence text, @ColorInt Integer textColor, DialogoAction.OnClickListener listener) {
        params.negativeAction = new DialogoAction(text, textColor, listener);
        return this;
    }

    public DialogoBuilder negative(CharSequence text) {
        return negative(text, null, null);
    }

    public DialogoBuilder neutral(CharSequence text, DialogoAction.OnClickListener listener) {
        return neutral(text, null, listener);
    }

    public DialogoBuilder neutral(CharSequence text, @ColorInt Integer textColor, DialogoAction.OnClickListener listener) {
        params.neutralAction = new DialogoAction(text, textColor, listener);
        return this;
    }

    public DialogoBuilder neutral(CharSequence text) {
        return neutral(text, null, null);
    }

    public DialogoBuilder positiveBackgroundColor(@ColorInt int color) { params.positiveBackgroundColor = color; return this; }
    public DialogoBuilder negativeBackgroundColor(@ColorInt int color) { params.negativeBackgroundColor = color; return this; }
    public DialogoBuilder neutralBackgroundColor(@ColorInt int color) { params.neutralBackgroundColor = color; return this; }
    public DialogoBuilder buttonsStacked(boolean stacked) { params.buttonsStacked = stacked; return this; }
    public DialogoBuilder fullWidthButtons(boolean fullWidth) { params.fullWidthButtons = fullWidth; return this; }

    // ==================== Window & Style ====================

    public DialogoBuilder backgroundColor(@ColorInt int color) { params.backgroundColor = color; return this; }
    public DialogoBuilder cornerRadius(float radiusPx) { params.cornerRadiusPx = radiusPx; return this; }
    public DialogoBuilder dimAmount(float amount) {
        params.dimAmount = Math.max(0f, Math.min(1f, amount));
        return this;
    }
    public DialogoBuilder blurBackground(boolean enable) { params.isBlurEnabled = enable; return this; }
    public DialogoBuilder animation(@StyleRes int animationRes) { params.animationRes = animationRes; return this; }
    public DialogoBuilder animation(DialogoAnimation animation) {
        if (animation != null) params.animation = animation;
        return this;
    }
    public DialogoBuilder gravity(int gravity) { params.gravity = gravity; return this; }
    public DialogoBuilder widthPercentage(float percent) {
        params.widthPercentage = Math.max(0.5f, Math.min(1f, percent));
        return this;
    }
    public DialogoBuilder maxHeightPercentage(float percent) {
        params.maxHeightPercentage = Math.max(0.3f, Math.min(0.95f, percent));
        return this;
    }
    public DialogoBuilder elevation(float dp) {
        params.elevationEnabled = true;
        params.elevationDp = dp;
        return this;
    }
    public DialogoBuilder stroke(@ColorInt int color, float widthPx) {
        params.strokeColor = color;
        params.strokeWidthPx = widthPx;
        return this;
    }
    public DialogoBuilder animateDim(boolean animate) { params.animateDim = animate; return this; }

    // ==================== Position ====================

    public DialogoBuilder position(DialogoPosition position) {
        if (position != null) params.position = position;
        return this;
    }
    public DialogoBuilder positionTop() { params.position = DialogoPosition.TOP; return this; }
    public DialogoBuilder positionCenter() { params.position = DialogoPosition.CENTER; return this; }
    public DialogoBuilder positionBottom() { params.position = DialogoPosition.BOTTOM; return this; }

    // ==================== Margins ====================

    public DialogoBuilder marginHorizontal(int px) { params.marginHorizontalPx = Math.max(0, px); return this; }
    public DialogoBuilder marginTop(int px) { params.marginTopPx = Math.max(0, px); return this; }
    public DialogoBuilder marginBottom(int px) { params.marginBottomPx = Math.max(0, px); return this; }

    public DialogoBuilder marginHorizontalDp(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.marginHorizontalPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }
    public DialogoBuilder marginTopDp(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.marginTopPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }
    public DialogoBuilder marginBottomDp(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.marginBottomPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }
    public DialogoBuilder offsetY(int px) { params.offsetYPx = px; return this; }

    // ==================== Non-Modal Popup ====================

    public DialogoBuilder nonModal(boolean nonModal) {
        params.nonModal = nonModal;
        if (nonModal) {
            params.dimAmount = 0f;
            params.canceledOnTouchOutside = true;
        }
        return this;
    }
    public DialogoBuilder asPopup() { return nonModal(true); }
    public DialogoBuilder dismissOnOutsideClick(boolean dismiss) {
        params.canceledOnTouchOutside = dismiss;
        return this;
    }

    // ==================== Behavior ====================

    public DialogoBuilder cancelable(boolean cancelable) { params.isCancelable = cancelable; return this; }
    public DialogoBuilder canceledOnTouchOutside(boolean cancel) { params.canceledOnTouchOutside = cancel; return this; }
    public DialogoBuilder autoDismiss(boolean autoDismiss) { params.autoDismiss = autoDismiss; return this; }
    public DialogoBuilder autoDismissAfter(long delayMs) { params.autoDismissDelayMs = delayMs; return this; }
    public DialogoBuilder showCountdown(boolean show) { params.showCountdown = show; return this; }

    // ==================== Listeners ====================

    public DialogoBuilder onShow(DialogoAction.OnShowListener listener) { params.onShowListener = listener; return this; }
    public DialogoBuilder onDismiss(DialogoAction.OnDismissListener listener) { params.onDismissListener = listener; return this; }

    // ==================== Build & Show ====================

    public DialogoFragment build() {
        return DialogoFragment.newInstance(params);
    }

    /**
     * Shows the dialog automatically discovering the FragmentManager
     * from the context provided in {@link Dialogo#with(Context)}.
     */
    public DialogoFragment show() {
        return show(findFragmentManager(), "DialogoFragment");
    }

    /**
     * Shows the dialog with an explicitly provided FragmentManager.
     */
    public DialogoFragment show(@NonNull FragmentManager fragmentManager) {
        return show(fragmentManager, "DialogoFragment");
    }

    /**
     * Shows the dialog with an explicitly provided FragmentManager and tag.
     */
    public DialogoFragment show(@NonNull FragmentManager fragmentManager, String tag) {
        DialogoFragment fragment = build();
        fragment.show(fragmentManager, tag != null ? tag : "DialogoFragment");
        lastFragmentRef = new WeakReference<>(fragment);
        return fragment;
    }

    // ==================== Result API ====================

    public DialogoFragment showForResult(@NonNull DialogoParams.ResultListener listener) {
        return showForResult(findFragmentManager(), "DialogoFragment", listener);
    }

    public DialogoFragment showForResult(@NonNull FragmentManager fragmentManager,
                                         @NonNull DialogoParams.ResultListener listener) {
        return showForResult(fragmentManager, "DialogoFragment", listener);
    }

    public DialogoFragment showForResult(@NonNull FragmentManager fragmentManager,
                                         String tag,
                                         @NonNull DialogoParams.ResultListener listener) {
        params.resultListener = listener;
        DialogoFragment fragment = build();
        fragment.show(fragmentManager, tag != null ? tag : "DialogoFragment");
        lastFragmentRef = new WeakReference<>(fragment);
        return fragment;
    }

    // ==================== Dismiss / State APIs ====================

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean animated) {
        DialogoFragment fragment = lastFragmentRef != null ? lastFragmentRef.get() : null;
        if (fragment != null && fragment.isAdded()) {
            if (animated) fragment.dismiss();
            else fragment.dismissImmediately();
            lastFragmentRef = null;
        }
    }

    public boolean isAdded() {
        DialogoFragment fragment = lastFragmentRef != null ? lastFragmentRef.get() : null;
        return fragment != null && fragment.isAdded();
    }

    public boolean isShowing() {
        DialogoFragment fragment = lastFragmentRef != null ? lastFragmentRef.get() : null;
        return fragment != null && fragment.isVisible();
    }

    public DialogoFragment getFragment() {
        return lastFragmentRef != null ? lastFragmentRef.get() : null;
    }

    // ==================== Form ====================

    public DialogoBuilder addText(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.TEXT);
        f.label = label; f.hint = hint; f.required = true;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addPassword(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.PASSWORD);
        f.label = label; f.hint = hint; f.required = true;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addNumber(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.NUMBER);
        f.label = label; f.hint = hint;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addEmail(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.EMAIL);
        f.label = label; f.hint = hint;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addPhone(String id, CharSequence label, CharSequence hint) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.PHONE);
        f.label = label; f.hint = hint;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addMultiline(String id, CharSequence label, CharSequence hint, int maxLines) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.MULTILINE);
        f.label = label; f.hint = hint; f.maxLines = maxLines;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addSwitch(String id, CharSequence label, boolean defaultValue) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.SWITCH);
        f.label = label; f.switchDefault = defaultValue;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addCheckbox(String id, CharSequence label, boolean defaultValue) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.CHECKBOX);
        f.label = label; f.checkboxDefault = defaultValue;
        params.formFields.add(f);
        return this;
    }

    public DialogoBuilder addDropdown(String id, CharSequence label,
                                      java.util.List<CharSequence> options, int defaultIndex) {
        ensureFormFields();
        FormField f = new FormField(id, FormField.Type.DROPDOWN);
        f.label = label; f.options = options; f.dropdownSelectedIndex = defaultIndex;
        params.formFields.add(f);
        return this;
    }

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

    // ==================== Internal Helpers ====================

    /**
     * Extracts a FragmentManager from the bound Context.
     */
    private FragmentManager findFragmentManager() {
        if (contextRef == null || contextRef.get() == null) {
            throw new IllegalStateException("Context is null. Call Dialogo.with(context) first.");
        }

        Context context = contextRef.get();

        // Traverse ContextWrappers to find the underlying Activity
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return ((FragmentActivity) context).getSupportFragmentManager();
            }
            if (context instanceof Activity) {
                throw new IllegalStateException("Dialogo requires a FragmentActivity (AppCompatActivity). Found a plain Activity.");
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("Could not find a FragmentManager. Ensure the Context is an AppCompatActivity.");
    }

    /**
     * Sets the color of the dialog's shadow.
     * Note: Requires API 28+ to take effect.
     *
     * @param color ARGB color.
     * @return This builder for chaining.
     */
    public DialogoBuilder shadowColor(@ColorInt int color) {
        params.shadowColor = color;
        return this;
    }

    /**
     * Enables a "Frosted Glass" effect on the dialog card.
     * The background becomes semi-transparent white with a subtle border,
     * and enables window blur (API 31+) to make it truly glass-like.
     *
     * @param enable {@code true} to enable glass effect.
     * @return This builder for chaining.
     */
    public DialogoBuilder glassBackground(boolean enable) {
        params.isGlassEnabled = enable;
        if (enable) {
            params.isBlurEnabled = true; // Blur behind the window
            if (params.backgroundColor == null) {
                params.backgroundColor = 0xCCFFFFFF; // Semi-transparent white (80%)
            }
            if (params.strokeColor == null) {
                params.strokeColor = 0x55FFFFFF; // Subtle white border
                params.strokeWidthPx = 2f;
            }
        }
        return this;
    }


    /**
     * Sets the padding for the entire dialog card.
     *
     * @param dp Padding in density-independent pixels.
     * @return This builder for chaining.
     */
    public DialogoBuilder padding(float dp) {
        if (contextRef != null && contextRef.get() != null) {
            params.paddingPx = WindowHelper.dpToPx(contextRef.get(), dp);
        }
        return this;
    }

    /**
     * Sets individual padding for the dialog card (in dp).
     */
    public DialogoBuilder padding(float left, float top, float right, float bottom) {
        if (contextRef != null && contextRef.get() != null) {
            Context ctx = contextRef.get();
            params.paddingLeftPx = WindowHelper.dpToPx(ctx, left);
            params.paddingTopPx = WindowHelper.dpToPx(ctx, top);
            params.paddingRightPx = WindowHelper.dpToPx(ctx, right);
            params.paddingBottomPx = WindowHelper.dpToPx(ctx, bottom);
        }
        return this;
    }

    /**
     * Sets the padding for the entire dialog card in raw pixels.
     * (Use this only if you have pre-calculated pixels).
     */
    public DialogoBuilder paddingPx(int px) {
        params.paddingPx = Math.max(0, px);
        return this;
    }
}