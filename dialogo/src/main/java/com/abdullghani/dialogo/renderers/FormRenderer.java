package com.abdullghani.dialogo.renderers;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.abdullghani.dialogo.R;
import com.abdullghani.dialogo.core.DialogoController;
import com.abdullghani.dialogo.model.DialogoParams;
import com.abdullghani.dialogo.model.FormField;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * يبني واجهة النموذج (Form) ديناميكياً داخل custom_view_container.
 */
public class FormRenderer {

    private final View rootView;
    private final DialogoParams params;
    private final DialogoController controller;
    private final List<FormField> fields;
    private final Map<String, View> fieldViews = new HashMap<>();

    public FormRenderer(View rootView, DialogoParams params, DialogoController controller) {
        this.rootView = rootView;
        this.params = params;
        this.controller = controller;
        this.fields = params.formFields;
    }

    public void render() {
        if (fields == null || fields.isEmpty()) return;

        LinearLayout container = rootView.findViewById(R.id.dialogo_custom_view_container);
        if (container == null) return;

        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());

        for (FormField field : fields) {
            View fieldView = createFieldView(inflater, container, field);
            if (fieldView != null) {
                container.addView(fieldView);
                fieldViews.put(field.id, fieldView);
            }
        }

        container.setVisibility(View.VISIBLE);
    }

    private View createFieldView(LayoutInflater inflater, ViewGroup parent, FormField field) {
        switch (field.type) {
            case TEXT:
            case PASSWORD:
            case NUMBER:
            case EMAIL:
            case PHONE:
            case MULTILINE:
                return createTextInput(inflater, parent, field);

            case SWITCH:
            case CHECKBOX:
                return createCheckable(inflater, parent, field);

            case DROPDOWN:
                return createDropdown(inflater, parent, field);

            default:
                return null;
        }
    }

    // ==================== Text Input ====================

    private View createTextInput(LayoutInflater inflater, ViewGroup parent, FormField field) {
        View view = inflater.inflate(R.layout.dialogo_form_text, parent, false);

        TextInputLayout layout = view.findViewById(R.id.dialogo_form_layout);
        TextInputEditText edit = view.findViewById(R.id.dialogo_form_edit);

        if (layout != null && field.hint != null) {
            layout.setHint(field.hint);
        }

        if (edit != null) {
            int type = field.inputType;
            switch (field.type) {
                case PASSWORD:
                    type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    break;
                case NUMBER:
                    type = InputType.TYPE_CLASS_NUMBER;
                    break;
                case EMAIL:
                    type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                    break;
                case PHONE:
                    type = InputType.TYPE_CLASS_PHONE;
                    break;
                case MULTILINE:
                    type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                    break;
            }
            edit.setInputType(type);
            edit.setMaxLines(field.maxLines > 0 ? field.maxLines : 1);
            if (field.prefill != null) edit.setText(field.prefill);
        }

        return view;
    }

    // ==================== Switch / Checkbox ====================

    private View createCheckable(LayoutInflater inflater, ViewGroup parent, FormField field) {
        View view = inflater.inflate(R.layout.dialogo_form_checkable, parent, false);
        CheckBox cb = view.findViewById(R.id.dialogo_form_checkbox);

        if (cb != null) {
            cb.setText(field.label != null ? field.label : field.id);
            cb.setChecked(field.type == FormField.Type.SWITCH
                    ? field.switchDefault
                    : field.checkboxDefault);
        }
        return view;
    }

    // ==================== Dropdown ====================

    private View createDropdown(LayoutInflater inflater, ViewGroup parent, FormField field) {
        View view = inflater.inflate(R.layout.dialogo_form_dropdown, parent, false);

        android.widget.TextView label = view.findViewById(R.id.dialogo_form_label);
        Spinner spinner = view.findViewById(R.id.dialogo_form_spinner);

        if (label != null) {
            if (field.label != null) {
                label.setText(field.label);
                label.setVisibility(View.VISIBLE);
            } else {
                label.setVisibility(View.GONE);
            }
        }

        if (spinner != null && field.options != null) {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                    view.getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    field.options);
            spinner.setAdapter(adapter);
            spinner.setSelection(field.dropdownSelectedIndex);
        }

        return view;
    }

    // ==================== Collect values ====================

    /** يجمع قيم النموذج كـ Map. */
    public Map<String, Object> collectValues() {
        Map<String, Object> result = new HashMap<>();
        if (fields == null) return result;

        for (FormField field : fields) {
            View view = fieldViews.get(field.id);
            if (view == null) continue;

            switch (field.type) {
                case TEXT:
                case PASSWORD:
                case NUMBER:
                case EMAIL:
                case PHONE:
                case MULTILINE: {
                    TextInputEditText edit = view.findViewById(R.id.dialogo_form_edit);
                    result.put(field.id, edit != null && edit.getText() != null
                            ? edit.getText().toString() : "");
                    break;
                }
                case SWITCH:
                case CHECKBOX: {
                    CheckBox cb = view.findViewById(R.id.dialogo_form_checkbox);
                    result.put(field.id, cb != null && cb.isChecked());
                    break;
                }
                case DROPDOWN: {
                    Spinner sp = view.findViewById(R.id.dialogo_form_spinner);
                    result.put(field.id, sp != null ? sp.getSelectedItemPosition() : -1);
                    break;
                }
            }
        }
        return result;
    }

    /** يتحقق من الحقول المطلوبة. يُرجع أول حقل ناقص، أو null. */
    public FormField validate() {
        if (fields == null) return null;

        for (FormField field : fields) {
            if (!field.required) continue;
            View view = fieldViews.get(field.id);
            if (view == null) continue;

            switch (field.type) {
                case TEXT:
                case PASSWORD:
                case NUMBER:
                case EMAIL:
                case PHONE:
                case MULTILINE: {
                    TextInputEditText edit = view.findViewById(R.id.dialogo_form_edit);
                    String text = edit != null && edit.getText() != null
                            ? edit.getText().toString().trim() : "";
                    if (text.isEmpty()) return field;
                    break;
                }
            }
        }
        return null;
    }
}