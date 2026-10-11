package com.abdullghani.dialogo.renderers;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abdullghani.dialogo.R;
import com.abdullghani.dialogo.core.DialogoBottomSheet;
import com.abdullghani.dialogo.core.DialogoController;
import com.abdullghani.dialogo.core.DialogoFragment;
import com.abdullghani.dialogo.model.DialogoAction;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.DialogoResult;
import com.abdullghani.dialogo.model.FormField;

public class ButtonRenderer {

    private final View rootView;
    private final DialogoParams params;
    private final DialogoController controller;

    public ButtonRenderer(View rootView, DialogoParams params, DialogoController controller) {
        this.rootView = rootView;
        this.params = params;
        this.controller = controller;
    }

    public void render() {
        if (rootView == null || params == null) return;

        LinearLayout buttonsContainer = rootView.findViewById(R.id.dialogo_buttons_container);
        Button btnPositive = rootView.findViewById(R.id.dialogo_btn_positive);
        Button btnNegative = rootView.findViewById(R.id.dialogo_btn_negative);
        Button btnNeutral = rootView.findViewById(R.id.dialogo_btn_neutral);

        boolean hasPositive = setupButton(btnPositive, params.positiveAction,
                params.positiveBackgroundColor, ButtonKind.POSITIVE);
        boolean hasNegative = setupButton(btnNegative, params.negativeAction,
                params.negativeBackgroundColor, ButtonKind.NEGATIVE);
        boolean hasNeutral = setupButton(btnNeutral, params.neutralAction,
                params.neutralBackgroundColor, ButtonKind.NEUTRAL);

        if (buttonsContainer != null) {
            buttonsContainer.setVisibility(
                    (hasPositive || hasNegative || hasNeutral) ? View.VISIBLE : View.GONE);

            if (params.buttonsStacked) {
                buttonsContainer.setOrientation(LinearLayout.VERTICAL);
            }
        }
    }

    private enum ButtonKind { POSITIVE, NEGATIVE, NEUTRAL }

    private boolean setupButton(Button button, DialogoAction action, Integer bgColor, ButtonKind kind) {
        if (button == null) return false;

        if (action == null || action.getText() == null || action.getText().length() == 0) {
            button.setVisibility(View.GONE);
            return false;
        }

        button.setText(action.getText());
        button.setVisibility(View.VISIBLE);

        // ✨ Accessibility: الحد الأدنى لحجم اللمس = 48dp
        button.setMinHeight((int) (48 * rootView.getResources().getDisplayMetrics().density));
        button.setMinWidth((int) (72 * rootView.getResources().getDisplayMetrics().density));

        if (action.getTextColor() != null) button.setTextColor(action.getTextColor());
        if (params.buttonFont != null) button.setTypeface(params.buttonFont);
        if (bgColor != null) button.setBackgroundColor(bgColor);

        button.setOnClickListener(v -> handleButtonClick(action, kind, v));
        return true;
    }

    private void handleButtonClick(DialogoAction action, ButtonKind kind, View sourceView) {

        // ==================== 1) Input validation ====================
        if (kind == ButtonKind.POSITIVE && params.inputRequired && params.showInput) {
            String text = controller.getInputText();
            if (text.isEmpty()) {
                Toast.makeText(sourceView.getContext(),
                        params.inputRequiredError != null ? params.inputRequiredError : "Required",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // ==================== 2) Form validation ====================
        if (kind == ButtonKind.POSITIVE && params.formFields != null
                && !params.formFields.isEmpty() && params.formRendererHolder != null) {

            FormField missing = params.formRendererHolder.validate();
            if (missing != null) {
                Toast.makeText(sourceView.getContext(),
                        missing.requiredError != null
                                ? missing.requiredError : "Required field missing",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // ==================== 3) List confirm ====================
        if (kind == ButtonKind.POSITIVE) {
            if (params.listMultiChoice && params.onMultiChoiceConfirm != null) {
                boolean[] checked = controller.getCheckedItems();
                if (checked != null) {
                    params.onMultiChoiceConfirm.onConfirm(checked, controller);
                }
            }
            if (params.listSingleChoice && params.onListItemConfirmed != null) {
                int index = controller.getSelectedIndex();
                if (index >= 0 && params.listItems != null && index < params.listItems.size()) {
                    params.onListItemConfirmed.onItemClick(
                            index, params.listItems.get(index), controller);
                }
            }
        }

        // ==================== 4) Custom callback ====================
        if (action.getClickListener() != null) {
            action.getClickListener().onClick(controller);
        }

        // ==================== 5) Set dismissal type ====================
        DialogoResult.Type type;
        switch (kind) {
            case POSITIVE: type = DialogoResult.Type.POSITIVE; break;
            case NEGATIVE: type = DialogoResult.Type.NEGATIVE; break;
            default:       type = DialogoResult.Type.NEUTRAL; break;
        }
        if (controller instanceof DialogoFragment) {
            ((DialogoFragment) controller).setDismissalType(type);
        } else if (controller instanceof DialogoBottomSheet) {
            ((DialogoBottomSheet) controller).setDismissalType(type);
        }

        // ==================== 6) Auto dismiss ====================
        if (params.autoDismiss) {
            controller.dismiss();
        }
    }
}