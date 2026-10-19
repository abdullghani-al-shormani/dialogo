package com.abdullghani.dialogo.core;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullghani.dialogo.R;
import com.abdullghani.dialogo.adapter.DialogoListAdapter;
import com.abdullghani.dialogo.animation.DialogoAnimator;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.DialogoResult;
import com.abdullghani.dialogo.renderers.ButtonRenderer;
import com.abdullghani.dialogo.renderers.ContentRenderer;
import com.abdullghani.dialogo.window.WindowHelper;

/**
 * Default modal dialog fragment implementation.
 *
 * <p>This is the view that gets displayed when you call
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder#show(
 *     androidx.fragment.app.FragmentManager)}. It handles rendering of
 * content, buttons, animations, positioning, dimming, and lifecycle.</p>
 *
 * <p>Normal usage goes through {@link com.abdullghani.dialogo.Dialogo} or
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder}. You rarely need
 * to instantiate this class directly.</p>
 *
 * @see DialogoController
 * @see com.abdullghani.dialogo.builder.DialogoBuilder
 */
public class DialogoFragment extends DialogFragment implements DialogoController {

    private static final String ARG_PARAMS_ID = "dialogo_params_id";
    private static final String ARG_PREVENT_DISMISS = "dialogo_prevent_dismiss";

    private DialogoParams params;
    private int paramsId = -1;
    private View rootCustomView;
    private boolean preventDismiss = false;
    private boolean isDismissing = false;

    private DialogoResult.Type dismissalType = DialogoResult.Type.DISMISSED;

    private ProgressBar progressBar;
    private ProgressBar progressCircular;
    private TextView progressTextView;
    private TextView progressPercentView;

    private final Handler countdownHandler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private long countdownEndTime;

    // ==================== Creation ====================

    /**
     * Creates a new instance wrapping the given parameters.
     *
     * <p>Prefer using
     * {@link com.abdullghani.dialogo.builder.DialogoBuilder#show(
     *     androidx.fragment.app.FragmentManager)}
     * instead of calling this directly.</p>
     *
     * @param params The dialog parameters.
     * @return A new {@link DialogoFragment}.
     */
    public static DialogoFragment newInstance(DialogoParams params) {
        int id = DialogoRegistry.register(params);
        DialogoFragment fragment = new DialogoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAMS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            paramsId = getArguments().getInt(ARG_PARAMS_ID, -1);
        }
        if (paramsId != -1) {
            params = DialogoRegistry.get(paramsId);
        }

        if (savedInstanceState != null) {
            preventDismiss = savedInstanceState.getBoolean(ARG_PREVENT_DISMISS, false);
        }

        if (params != null) {
            setCancelable(params.isCancelable);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (params != null) {
            dialog.setCanceledOnTouchOutside(params.canceledOnTouchOutside);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (params == null) return null;

        View view = inflater.inflate(R.layout.dialogo_default_layout, container, false);

        View cardContainer = view.findViewById(R.id.dialogo_card_container);

        // 1) Apply width, position, and margins to the card
        applyWidthToCard(cardContainer);
        applyCardPosition(cardContainer);
        applyMarginsToCard(cardContainer);

        // 2) Apply background, stroke, and elevation
        // 2) Apply background, stroke, and elevation
        if (cardContainer != null) {
            int bg = params.backgroundColor != null ? params.backgroundColor : 0xFFFFFFFF;
            float radius = params.cornerRadiusPx > 0 ? params.cornerRadiusPx : dpToPx(24f);

            cardContainer.setBackground(WindowHelper.createCardBackground(
                    bg, radius, params.strokeColor, params.strokeWidthPx));

            if (params.elevationEnabled) {
                cardContainer.setElevation(dpToPx(params.elevationDp));
            }

            // ✨ Apply colored shadow (API 28+)
            if (params.shadowColor != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cardContainer.setOutlineSpotShadowColor(params.shadowColor);
            }
        }

        // 3) Render content
        ContentRenderer contentRenderer = new ContentRenderer(view, params, this);
        contentRenderer.render();
        this.rootCustomView = contentRenderer.getRenderedCustomView();

        if (rootCustomView != null) {
            progressBar = rootCustomView.findViewById(R.id.dialogo_progress_bar);
            progressCircular = rootCustomView.findViewById(R.id.dialogo_progress_circular);
            progressTextView = rootCustomView.findViewById(R.id.dialogo_tv_progress_status);
            progressPercentView = rootCustomView.findViewById(R.id.dialogo_tv_progress_percent);
        }

        // 4) Render buttons
        ButtonRenderer buttonRenderer = new ButtonRenderer(view, params, this);
        buttonRenderer.render();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Window window = getDialog() != null ? getDialog().getWindow() : null;
        if (window != null && params != null) {
            WindowHelper.applyWindowStyle(window, params);
        }

        View cardContainer = view.findViewById(R.id.dialogo_card_container);

        // Constrain height if needed
        applyMaxHeight(cardContainer);

        // Entry animation (applied on the card only)
        if (params != null && cardContainer != null) {
            DialogoAnimator.applyEntry(cardContainer, params.animation);
        }

        // Dim fade-in
        if (params != null && params.animateDim && window != null) {
            animateDim(window, params.dimAmount);
        }

        if (params != null && params.onShowListener != null) {
            params.onShowListener.onShow(this);
        }

        if (params != null && params.autoDismissDelayMs > 0) {
            startAutoDismiss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_PREVENT_DISMISS, preventDismiss);
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        stopCountdown();

        if (params != null) {
            if (params.onDismissListener != null) {
                params.onDismissListener.onDismiss();
            }
            if (params.resultListener != null) {
                params.resultListener.onResult(buildResult());
            }
        }

        if (paramsId != -1) {
            DialogoRegistry.unregister(paramsId);
            paramsId = -1;
        }
    }

    // ==================== Layout Helpers ====================

    /**
     * Sets the card width based on {@code widthPercentage}.
     */
    private void applyWidthToCard(View cardContainer) {
        if (cardContainer == null || params == null) return;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int targetWidth = (int) (screenWidth * params.widthPercentage);

        ViewGroup.LayoutParams lp = cardContainer.getLayoutParams();
        if (lp != null) {
            lp.width = targetWidth;
            cardContainer.setLayoutParams(lp);
        }
    }

    /**
     * Constrains the card height to {@code maxHeightPercentage} of the screen.
     */
    private void applyMaxHeight(View cardContainer) {
        if (cardContainer == null || params == null) return;

        cardContainer.post(() -> {
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int maxHeight = (int) (screenHeight * params.maxHeightPercentage);
            if (cardContainer.getHeight() > maxHeight) {
                ViewGroup.LayoutParams lp = cardContainer.getLayoutParams();
                lp.height = maxHeight;
                cardContainer.setLayoutParams(lp);
            }
        });
    }

    /**
     * Applies the {@code layout_gravity} of the card based on the requested
     * {@link com.abdullghani.dialogo.model.DialogoPosition}.
     */
    private void applyCardPosition(View cardContainer) {
        if (cardContainer == null || params == null) return;

        if (cardContainer.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) cardContainer.getLayoutParams();

            switch (params.position) {
                case TOP:
                    lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    break;
                case BOTTOM:
                    lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                    break;
                case CENTER:
                default:
                    lp.gravity = Gravity.CENTER;
                    break;
            }

            cardContainer.setLayoutParams(lp);
        }
    }

    /**
     * Applies margins around the card.
     */
    private void applyMarginsToCard(View cardContainer) {
        if (cardContainer == null || params == null) return;

        if (cardContainer.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) cardContainer.getLayoutParams();

            lp.leftMargin = params.marginHorizontalPx;
            lp.rightMargin = params.marginHorizontalPx;
            lp.topMargin = params.marginTopPx;
            lp.bottomMargin = params.marginBottomPx;

            cardContainer.setLayoutParams(lp);
        }
    }

    // ==================== Dim Animation ====================

    private void animateDim(Window window, float targetDim) {
        window.setDimAmount(0f);
        ValueAnimator anim = ValueAnimator.ofFloat(0f, targetDim);
        anim.setDuration(220);
        anim.addUpdateListener(a -> {
            Window w = getDialog() != null ? getDialog().getWindow() : null;
            if (w != null) w.setDimAmount((float) a.getAnimatedValue());
        });
        anim.start();
    }

    // ==================== Countdown ====================

    private void startAutoDismiss() {
        countdownEndTime = System.currentTimeMillis() + params.autoDismissDelayMs;

        if (params.showCountdown && progressTextView != null) {
            countdownRunnable = new Runnable() {
                @Override
                public void run() {
                    long remaining = countdownEndTime - System.currentTimeMillis();
                    if (remaining <= 0) {
                        if (isAdded()) dismiss();
                        return;
                    }
                    long seconds = (remaining + 999) / 1000;
                    progressTextView.setText("Auto-close in " + seconds + "s");
                    progressTextView.setVisibility(View.VISIBLE);
                    countdownHandler.postDelayed(this, 200);
                }
            };
            countdownHandler.post(countdownRunnable);
        }

        countdownHandler.postDelayed(() -> {
            if (isAdded()) dismiss();
        }, params.autoDismissDelayMs);
    }

    private void stopCountdown() {
        if (countdownRunnable != null) {
            countdownHandler.removeCallbacks(countdownRunnable);
            countdownRunnable = null;
        }
    }

    // ==================== DialogoController ====================

    /**
     * {@inheritDoc}
     *
     * <p>Plays the exit animation on the card and then dismisses.</p>
     */
    @Override
    public void dismiss() {
        if (isDismissing) return;
        if (preventDismiss) { preventDismiss = false; return; }
        isDismissing = true;

        View view = getView();
        if (view != null && params != null) {
            View cardContainer = view.findViewById(R.id.dialogo_card_container);
            if (cardContainer != null) {
                DialogoAnimator.applyExit(cardContainer, params.animation, this::dismissInternal);
            } else {
                dismissInternal();
            }
        } else {
            dismissInternal();
        }
    }

    private void dismissInternal() {
        if (isAdded()) {
            dismissAllowingStateLoss();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void preventDismiss() { this.preventDismiss = true; }

    /** {@inheritDoc} */
    @Override
    public View getCustomView() { return rootCustomView; }

    /** {@inheritDoc} */
    @Override
    public String getInputText() {
        if (rootCustomView != null) {
            android.widget.EditText et = rootCustomView.findViewById(R.id.dialogo_input_field);
            if (et != null && et.getText() != null) return et.getText().toString().trim();
        }
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public int getSelectedIndex() {
        DialogoListAdapter adapter = findListAdapter();
        return adapter != null ? adapter.getSelectedPosition() : -1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean[] getCheckedItems() {
        DialogoListAdapter adapter = findListAdapter();
        return adapter != null ? adapter.getCheckedItems() : null;
    }

    /** {@inheritDoc} */
    @Override
    public DialogoResult getResult() {
        return buildResult();
    }

    private DialogoListAdapter findListAdapter() {
        View view = getView();
        if (view == null) return null;
        RecyclerView rv = view.findViewById(R.id.dialogo_rv_list);
        if (rv == null || !(rv.getAdapter() instanceof DialogoListAdapter)) return null;
        return (DialogoListAdapter) rv.getAdapter();
    }

    private DialogoResult buildResult() {
        DialogoResult.Builder b = new DialogoResult.Builder();
        b.type(dismissalType);

        if (params != null) {
            b.inputText(getInputText());

            int selected = getSelectedIndex();
            if (selected >= 0 && params.listItems != null && selected < params.listItems.size()) {
                b.selectedIndex(selected);
                b.selectedItem(params.listItems.get(selected));
            }

            b.checkedItems(getCheckedItems());

            if (params.formRendererHolder != null) {
                b.formValues(params.formRendererHolder.collectValues());
            }
        }
        return b.build();
    }

    /**
     * Sets the dismissal type used to build the {@link DialogoResult}.
     *
     * <p>Called internally by the button renderer.</p>
     *
     * @param type How the dialog was dismissed.
     */
    public void setDismissalType(DialogoResult.Type type) {
        this.dismissalType = type;
    }

    // ==================== Progress ====================

    /** {@inheritDoc} */
    @Override
    public void setProgress(int progress) {
        if (progressBar != null) {
            progressBar.setIndeterminate(false);
            progressBar.setProgress(progress);
            if (progressPercentView != null && params != null && params.showProgressPercent) {
                progressPercentView.setText(progress + "%");
                progressPercentView.setVisibility(View.VISIBLE);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setProgressText(CharSequence text) {
        if (progressTextView != null) {
            progressTextView.setText(text);
            progressTextView.setVisibility(text != null && text.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setProgressPercentVisible(boolean visible) {
        if (progressPercentView != null) {
            progressPercentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Dismisses the dialog immediately, without playing the exit animation.
     *
     * <p>Useful when you need to close the dialog as fast as possible,
     * for example when the host Activity is being destroyed.</p>
     */
    public void dismissImmediately() {
        if (isDismissing) return;
        isDismissing = true;
        dismissAllowingStateLoss();
    }
    // ==================== Helpers ====================

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}