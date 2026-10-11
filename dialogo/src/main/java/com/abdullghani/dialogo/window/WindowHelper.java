package com.abdullghani.dialogo.window;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.DialogoPosition;

public class WindowHelper {

    public static void applyWindowStyle(Window window, DialogoParams params) {
        if (window == null || params == null) return;

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (params.nonModal) {
            window.setDimAmount(0f);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setFormat(PixelFormat.TRANSLUCENT);

            // إزالة insets الإضافية
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false);
            } else {
                window.getDecorView().setSystemUiVisibility(
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                );
            }
        } else {
            window.setDimAmount(params.dimAmount);
        }

        applyPosition(window, params);

        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        if (params.animationRes != 0) {
            window.setWindowAnimations(params.animationRes);
        }

        if (!params.nonModal && params.isBlurEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            window.getAttributes().setBlurBehindRadius(20);
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private static void applyPosition(Window window, DialogoParams params) {
        WindowManager.LayoutParams lp = window.getAttributes();

        int gravity;
        if (params.position != null) {
            switch (params.position) {
                case TOP:
                    gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    break;
                case BOTTOM:
                    gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    break;
                case CENTER:
                default:
                    gravity = Gravity.CENTER;
                    break;
            }
        } else {
            gravity = params.gravity;
        }

        window.setGravity(gravity);

        if (params.offsetYPx != 0) {
            lp.y = params.offsetYPx;
        }

        window.setAttributes(lp);
    }

    // ==================== Card Background ====================

    public static GradientDrawable createCardBackground(int backgroundColor, float cornerRadiusPx,
                                                        Integer strokeColor, float strokeWidthPx) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(backgroundColor);
        float radius = cornerRadiusPx > 0 ? cornerRadiusPx : 28f;
        drawable.setCornerRadius(radius);

        if (strokeColor != null && strokeWidthPx > 0) {
            drawable.setStroke((int) strokeWidthPx, strokeColor);
        }
        return drawable;
    }

    // ==================== System Bars ====================

    /**
     * يرجع ارتفاع الـ status bar بالبكسل.
     * يستخدم داخلياً لضبط marginTop للـ popups في الأعلى.
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * يرجع ارتفاع الـ navigation bar بالبكسل.
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * يحول dp إلى بكسل.
     */
    public static int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}