package com.abdullghani.dialogo.renderers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullghani.dialogo.R;
import com.abdullghani.dialogo.adapter.DialogoListAdapter;
import com.abdullghani.dialogo.core.DialogoController;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.FormField;

import java.util.Map;

public class ContentRenderer {

    private final View rootView;
    private final DialogoParams params;
    private final DialogoController controller;
    private View renderedCustomView;
    private FormRenderer formRenderer;

    public ContentRenderer(View rootView, DialogoParams params, DialogoController controller) {
        this.rootView = rootView;
        this.params = params;
        this.controller = controller;
    }

    public void render() {
        if (rootView == null || params == null) return;

        // ✨ دعم RTL/LTR
        int layoutDir = ViewCompat.getLayoutDirection(rootView);
        rootView.setLayoutDirection(layoutDir);

        renderIcon();
        renderTitle();
        renderMessage();
        renderInput();
        renderProgress();
        renderList();
        renderForm();
        renderCustomView();
    }

    // ==================== Title ====================

    private void renderTitle() {
        TextView tvTitle = rootView.findViewById(R.id.dialogo_tv_title);
        if (tvTitle == null) return;

        if (params.title != null && params.title.length() > 0) {
            tvTitle.setText(params.title);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setGravity(params.titleGravity);

            if (params.titleColor != null) tvTitle.setTextColor(params.titleColor);
            if (params.titleFont != null) tvTitle.setTypeface(params.titleFont);
            if (params.titleSizeSp != null) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, params.titleSizeSp);
            }
            if (params.titleBold) {
                tvTitle.setTypeface(tvTitle.getTypeface(), android.graphics.Typeface.BOLD);
            }

            // ✨ Accessibility
            tvTitle.setContentDescription("Dialog title: " + params.title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    // ==================== Message ====================

    private void renderMessage() {
        TextView tvMessage = rootView.findViewById(R.id.dialogo_tv_message);
        if (tvMessage == null) return;

        if (params.message != null && params.message.length() > 0) {
            if (params.htmlMessage) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvMessage.setText(Html.fromHtml(params.message.toString(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tvMessage.setText(Html.fromHtml(params.message.toString()));
                }
            } else {
                tvMessage.setText(params.message);
            }
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setGravity(params.messageGravity);

            if (params.messageColor != null) tvMessage.setTextColor(params.messageColor);
            if (params.messageFont != null) tvMessage.setTypeface(params.messageFont);
            if (params.messageSizeSp != null) {
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, params.messageSizeSp);
            }

            // ✨ Copy to clipboard (long-press)
            if (params.copyableMessage) {
                tvMessage.setTextIsSelectable(true);
                tvMessage.setOnLongClickListener(v -> {
                    copyToClipboard(tvMessage.getContext(), params.message.toString());
                    return true;
                });
            }

            // ✨ Accessibility
            tvMessage.setContentDescription("Dialog message: " + params.message);
        } else {
            tvMessage.setVisibility(View.GONE);
        }
    }

    private void copyToClipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Dialogo", text));
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    // ==================== Icon ====================

    private void renderIcon() {
        ImageView ivIcon = rootView.findViewById(R.id.dialogo_iv_icon);
        if (ivIcon == null) return;

        if (params.iconDrawable != null) {
            ivIcon.setImageDrawable(params.iconDrawable);
            ivIcon.setVisibility(View.VISIBLE);
        } else if (params.iconRes != 0) {
            ivIcon.setImageResource(params.iconRes);
            ivIcon.setVisibility(View.VISIBLE);
        } else {
            ivIcon.setVisibility(View.GONE);
            return;
        }

        if (params.iconTint != null) ivIcon.setColorFilter(params.iconTint);

        // ✨ ضبط الحجم بشكل صحيح
        int sizePx = (int) (params.iconSizeDp * rootView.getResources().getDisplayMetrics().density);

        ViewGroup.LayoutParams lp = ivIcon.getLayoutParams();
        if (lp != null) {
            lp.width = sizePx;
            lp.height = sizePx;
            ivIcon.setLayoutParams(lp);
        }
        ivIcon.setMinimumWidth(sizePx);
        ivIcon.setMinimumHeight(sizePx);
        ivIcon.setMaxWidth(sizePx);
        ivIcon.setMaxHeight(sizePx);

        ivIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivIcon.requestLayout();

        if (params.animateIcon) {
            ivIcon.setScaleX(0.5f);
            ivIcon.setScaleY(0.5f);
            ivIcon.setAlpha(0f);
            ivIcon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setStartDelay(120)
                    .setDuration(400)
                    .setInterpolator(new android.view.animation.OvershootInterpolator(1.6f))
                    .start();
        }

        ivIcon.setContentDescription("Dialog icon");
    }

    // ==================== Input ====================

    private void renderInput() {
        if (!params.showInput) return;

        LinearLayout customContainer = rootView.findViewById(R.id.dialogo_custom_view_container);
        if (customContainer == null) return;

        View inputView = LayoutInflater.from(rootView.getContext())
                .inflate(R.layout.dialogo_input_layout, customContainer, false);

        com.google.android.material.textfield.TextInputLayout inputLayout =
                inputView.findViewById(R.id.dialogo_input_layout);
        com.google.android.material.textfield.TextInputEditText editText =
                inputView.findViewById(R.id.dialogo_input_field);

        if (inputLayout != null && params.inputHint != null) {
            inputLayout.setHint(params.inputHint);
        }
        if (editText != null) {
            if (params.inputPreFill != null) editText.setText(params.inputPreFill);
            editText.setInputType(params.inputType);
            editText.setMaxLines(params.inputMaxLines);
            if (params.inputTextColor != null) editText.setTextColor(params.inputTextColor);
            editText.setContentDescription(params.inputHint != null ? params.inputHint : "Input field");
        }

        customContainer.addView(inputView);
        customContainer.setVisibility(View.VISIBLE);
        renderedCustomView = inputView;
    }

    // ==================== Progress ====================

    private void renderProgress() {
        if (!params.showProgress) return;

        LinearLayout customContainer = rootView.findViewById(R.id.dialogo_custom_view_container);
        if (customContainer == null) return;

        View progressView = LayoutInflater.from(rootView.getContext())
                .inflate(R.layout.dialogo_progress_layout, customContainer, false);

        View circularContainer = progressView.findViewById(R.id.dialogo_circular_container);
        View horizontalContainer = progressView.findViewById(R.id.dialogo_horizontal_container);

        ProgressBar circularBar = progressView.findViewById(R.id.dialogo_progress_circular);
        TextView statusCircular = progressView.findViewById(R.id.dialogo_tv_progress_status_circular);

        ProgressBar horizontalBar = progressView.findViewById(R.id.dialogo_progress_bar);
        TextView statusHorizontal = progressView.findViewById(R.id.dialogo_tv_progress_status);
        TextView percent = progressView.findViewById(R.id.dialogo_tv_progress_percent);

        if (params.progressIndeterminate) {
            if (circularContainer != null) circularContainer.setVisibility(View.VISIBLE);
            if (horizontalContainer != null) horizontalContainer.setVisibility(View.GONE);

            if (circularBar != null && params.progressColor != null) {
                circularBar.setIndeterminateTintList(
                        android.content.res.ColorStateList.valueOf(params.progressColor));
            }
            if (statusCircular != null) {
                if (params.progressText != null) {
                    statusCircular.setText(params.progressText);
                } else if (params.message != null) {
                    statusCircular.setText(params.message);
                } else {
                    statusCircular.setText("Please wait...");
                }
            }
        } else {
            if (circularContainer != null) circularContainer.setVisibility(View.GONE);
            if (horizontalContainer != null) horizontalContainer.setVisibility(View.VISIBLE);

            if (horizontalBar != null) {
                horizontalBar.setMax(params.progressMax);
                horizontalBar.setProgress(params.progressValue);
                if (params.progressColor != null) {
                    horizontalBar.setProgressTintList(
                            android.content.res.ColorStateList.valueOf(params.progressColor));
                }
            }
            if (statusHorizontal != null) {
                if (params.progressText != null) {
                    statusHorizontal.setText(params.progressText);
                    statusHorizontal.setVisibility(View.VISIBLE);
                } else {
                    statusHorizontal.setVisibility(View.GONE);
                }
            }
            if (percent != null) {
                if (params.showProgressPercent) {
                    percent.setText(params.progressValue + "%");
                    percent.setVisibility(View.VISIBLE);
                } else {
                    percent.setVisibility(View.GONE);
                }
            }
        }

        customContainer.addView(progressView);
        customContainer.setVisibility(View.VISIBLE);
        renderedCustomView = progressView;
    }

    // ==================== List ====================

    private void renderList() {
        if (params.listItems == null || params.listItems.isEmpty()) return;

        RecyclerView rv = rootView.findViewById(R.id.dialogo_rv_list);
        if (rv == null) return;

        rv.setVisibility(View.VISIBLE);
        rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        DialogoListAdapter adapter = new DialogoListAdapter(params, controller);
        rv.setAdapter(adapter);
    }

    // ==================== Form ====================

    private void renderForm() {
        if (params.formFields == null || params.formFields.isEmpty()) return;

        formRenderer = new FormRenderer(rootView, params, controller);
        formRenderer.render();

        params.formRendererHolder = new DialogoParams.FormRendererHolder() {
            @Override
            public Map<String, Object> collectValues() {
                return formRenderer.collectValues();
            }

            @Override
            public FormField validate() {
                return formRenderer.validate();
            }
        };
    }

    public FormRenderer getFormRenderer() {
        return formRenderer;
    }

    // ==================== Custom View ====================

    private void renderCustomView() {
        if (params.showInput || params.showProgress) return;
        if (params.formFields != null && !params.formFields.isEmpty()) return;

        LinearLayout customContainer = rootView.findViewById(R.id.dialogo_custom_view_container);
        if (customContainer == null) return;

        customContainer.removeAllViews();

        if (params.customView != null) {
            renderedCustomView = params.customView;
            customContainer.addView(renderedCustomView);
            customContainer.setVisibility(View.VISIBLE);
        } else if (params.customLayoutRes != 0) {
            renderedCustomView = LayoutInflater.from(rootView.getContext())
                    .inflate(params.customLayoutRes, customContainer, false);
            customContainer.addView(renderedCustomView);
            customContainer.setVisibility(View.VISIBLE);
        } else {
            customContainer.setVisibility(View.GONE);
            renderedCustomView = null;
        }

        if (renderedCustomView != null && params.onViewCreatedListener != null) {
            params.onViewCreatedListener.onViewCreated(renderedCustomView, controller);
        }
    }

    public View getRenderedCustomView() {
        return renderedCustomView;
    }
}