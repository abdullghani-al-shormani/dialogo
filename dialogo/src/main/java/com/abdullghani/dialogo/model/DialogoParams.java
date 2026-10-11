package com.abdullghani.dialogo.model;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;

import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.core.DialogoController;

import java.util.List;
import java.util.Map;

public class DialogoParams {

    // ==================== Title & Message ====================
    public CharSequence title;
    public CharSequence message;
    public Integer titleColor;
    public Integer messageColor;
    public Typeface titleFont;
    public Typeface messageFont;
    public Integer titleSizeSp;
    public Integer messageSizeSp;
    public int titleGravity = Gravity.CENTER;
    public int messageGravity = Gravity.CENTER;
    public boolean titleBold = true;
    public boolean htmlMessage = false;
    public boolean copyableMessage = false;

    // ==================== Icon ====================
    @DrawableRes public int iconRes = 0;
    public Drawable iconDrawable;
    public Integer iconTint;
    public int iconSizeDp = 56;
    public boolean animateIcon = true;

    // ==================== Custom View ====================
    @LayoutRes public int customLayoutRes = 0;
    public View customView;
    public OnViewCreatedListener onViewCreatedListener;

    // ==================== Input ====================
    public boolean showInput = false;
    public CharSequence inputHint;
    public CharSequence inputPreFill;
    public int inputMaxLines = 1;
    public int inputType = InputType.TYPE_CLASS_TEXT;
    public Integer inputTextColor;
    public boolean inputRequired = false;
    public CharSequence inputRequiredError = "This field is required";

    // ==================== Form ====================
    public List<FormField> formFields;
    public FormRendererHolder formRendererHolder;

    // ==================== List ====================
    public List<CharSequence> listItems;
    public List<Integer> listIcons;
    public Integer listIconTint;
    public boolean listSingleChoice = false;
    public boolean listMultiChoice = false;
    public int listSelectedIndex = -1;
    public boolean[] listCheckedItems;

    public DialogoAction.OnListItemClickListener onListItemClick;
    public DialogoAction.OnListItemClickListener onListItemSelected;
    public DialogoAction.OnListItemClickListener onListItemConfirmed;
    public DialogoAction.OnMultiChoiceListener onMultiChoiceConfirm;

    // ==================== Progress ====================
    public boolean showProgress = false;
    public boolean progressIndeterminate = true;
    public int progressMax = 100;
    public int progressValue = 0;
    public CharSequence progressText;
    public boolean showProgressPercent = true;
    public Integer progressColor;

    // ==================== Window & Card ====================
    public Integer backgroundColor;
    public float cornerRadiusPx = -1f;
    public float dimAmount = 0.6f;
    public float widthPercentage = 0.85f;
    public float maxHeightPercentage = 0.85f;
    public int gravity = Gravity.CENTER;
    @StyleRes public int animationRes = 0;
    public DialogoAnimation animation = DialogoAnimation.SCALE;
    public boolean isBlurEnabled = false;
    public boolean elevationEnabled = true;
    public float elevationDp = 12f;
    public Integer strokeColor;
    public float strokeWidthPx = 0f;
    public boolean animateDim = true;

    // ==================== Position & Margins ====================
    /**
     * موقع الديالوج على الشاشة.
     * TOP, CENTER, BOTTOM
     */
    public DialogoPosition position = DialogoPosition.CENTER;

    /** هامش من حافة الشاشة (يُطبّق على جميع الحواف ما لم يُحدَّد غير ذلك). */
    public int marginHorizontalPx = 0;
    public int marginTopPx = 0;
    public int marginBottomPx = 0;

    /** إزاحة إضافية من الأعلى (للـ popup في الأعلى). */
    public int offsetYPx = 0;

    // ==================== Non-Modal Popup ====================
    /**
     * إذا true: الديالوج يعمل كـ popup غير معطّل للخلفية.
     * - لا dim
     * - لا يحجب اللمس خلفه
     * - يمكن إغلاقه بالنقر خارجه
     * - focusable = false (لا يأخذ التركيز)
     */
    public boolean nonModal = false;

    /** إذا true: يمكن إغلاق الـ popup بالنقر خارج البطاقة. */
    public boolean dismissOnOutsideClick = true;

    // ==================== Behavior ====================
    public boolean isCancelable = true;
    public boolean canceledOnTouchOutside = true;
    public boolean autoDismiss = true;
    public long autoDismissDelayMs = 0;
    public boolean showCountdown = false;

    // ==================== Actions ====================
    public DialogoAction positiveAction;
    public DialogoAction negativeAction;
    public DialogoAction neutralAction;

    public Integer positiveBackgroundColor;
    public Integer negativeBackgroundColor;
    public Integer neutralBackgroundColor;
    public Integer buttonCornerRadiusPx;
    public Typeface buttonFont;
    public boolean buttonsStacked = false;
    public boolean fullWidthButtons = false;

    // ==================== Listeners ====================
    public DialogoAction.OnShowListener onShowListener;
    public DialogoAction.OnDismissListener onDismissListener;
    public ResultListener resultListener;

    // ==================== Interfaces ====================

    public interface OnViewCreatedListener {
        void onViewCreated(View customView, DialogoController controller);
    }

    public interface ResultListener {
        void onResult(DialogoResult result);
    }

    public interface FormRendererHolder {
        Map<String, Object> collectValues();
        FormField validate();
    }

    /** علامة داخلية: marginTopPx هي قيمة dp تحتاج statusBarHeight في الوضع Modal. */
    public boolean marginTopIsDp = false;

    /** علامة داخلية: marginBottomPx هي قيمة dp تحتاج navigationBarHeight في الوضع Modal. */
    public boolean marginBottomIsDp = false;
}