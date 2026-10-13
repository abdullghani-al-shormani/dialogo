package com.abdullghani.dialogo.core;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullghani.dialogo.R;
import com.abdullghani.dialogo.adapter.DialogoListAdapter;
import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.animation.DialogoAnimator;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.DialogoResult;
import com.abdullghani.dialogo.renderers.ButtonRenderer;
import com.abdullghani.dialogo.renderers.ContentRenderer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * BottomSheet variant of {@link DialogoFragment}.
 *
 * <p>Displays the dialog content as a Material BottomSheet that slides up
 * from the bottom of the screen. Includes a drag handle, top-rounded corners,
 * and full support for lists, forms, inputs, progress, and the Result API.</p>
 *
 * <p>Created via
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder#showBottomSheet(
 *     androidx.fragment.app.FragmentManager)}.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Choose action")
 *     .items("Edit", "Share", "Delete")
 *     .itemClick((pos, item, c) -> { })
 *     .showBottomSheet(getSupportFragmentManager());
 * }</pre>
 *
 * @see DialogoFragment
 * @see DialogoController
 */
public class DialogoBottomSheet extends BottomSheetDialogFragment implements DialogoController {

    private static final String ARG_PARAMS_ID = "dialogo_params_id";
    private static final String ARG_PREVENT_DISMISS = "dialogo_prevent_dismiss";

    private DialogoParams params;
    private int paramsId = -1;
    private View rootCustomView;
    private boolean preventDismiss = false;
    private boolean isDismissing = false;

    private DialogoResult.Type dismissalType = DialogoResult.Type.DISMISSED;

    private ProgressBar progressBar;
    private TextView progressTextView;
    private TextView progressPercentView;

    private final Handler countdownHandler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private long countdownEndTime;

    /**
     * Creates a new instance with the given parameters.
     *
     * <p>Prefer using
     * {@link com.abdullghani.dialogo.builder.DialogoBuilder#showBottomSheet(
     *     androidx.fragment.app.FragmentManager)} instead of calling this
     * directly.</p>
     *
     * @param params The dialog parameters.
     * @return A new {@link DialogoBottomSheet}.
     */
    public static DialogoBottomSheet newInstance(DialogoParams params) {
        int id = DialogoRegistry.register(params);
        DialogoBottomSheet sheet = new DialogoBottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAMS_ID, id);
        sheet.setArguments(args);
        return sheet;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_PREVENT_DISMISS, preventDismiss);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(d -> {
            View bottomSheet = dialog.findViewById(
                    com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
                behavior.setDraggable(true);
                behavior.setFitToContents(true);
            }
        });

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (params == null) return null;

        View view = inflater.inflate(R.layout.dialogo_bottomsheet_layout, container, false);

        View cardContainer = view.findViewById(R.id.dialogo_card_container);
        if (cardContainer != null && params.backgroundColor != null) {
            android.graphics.drawable.GradientDrawable bg =
                    new android.graphics.drawable.GradientDrawable();
            bg.setColor(params.backgroundColor);
            float radius = dpToPx(24f);
            bg.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
            cardContainer.setBackground(bg);
        }

        ContentRenderer contentRenderer = new ContentRenderer(view, params, this);
        contentRenderer.setUseBottomSheetStyle(true);
        contentRenderer.render();
        this.rootCustomView = contentRenderer.getRenderedCustomView();

        if (rootCustomView != null) {
            progressBar = rootCustomView.findViewById(R.id.dialogo_progress_bar);
            progressTextView = rootCustomView.findViewById(R.id.dialogo_tv_progress_status);
            progressPercentView = rootCustomView.findViewById(R.id.dialogo_tv_progress_percent);
        }

        ButtonRenderer buttonRenderer = new ButtonRenderer(view, params, this);
        buttonRenderer.render();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View cardContainer = view.findViewById(R.id.dialogo_card_container);
        if (params != null) {
            View target = (cardContainer != null) ? cardContainer : view;
            DialogoAnimator.applyEntry(target, DialogoAnimation.SLIDE_UP);
        }

        if (params != null && params.onShowListener != null) {
            params.onShowListener.onShow(this);
        }

        if (params != null && params.autoDismissDelayMs > 0) {
            startAutoDismiss();
        }
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

    // ==================== Auto Dismiss ====================

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
     * <p>This implementation also respects {@link #preventDismiss()}.</p>
     */
    @Override
    public void dismiss() {
        if (isDismissing) return;
        if (preventDismiss) { preventDismiss = false; return; }
        isDismissing = true;
        dismissAllowingStateLoss();
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
        View v = getView();
        if (v == null) return null;
        RecyclerView rv = v.findViewById(R.id.dialogo_rv_list);
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

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    /**
     * Dismisses the bottom sheet immediately, without playing the exit animation.
     */
    public void dismissImmediately() {
        if (isDismissing) return;
        isDismissing = true;
        dismissAllowingStateLoss();
    }
}