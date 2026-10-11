package com.abdullghani.dialogo.model;

import android.text.InputType;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Represents a single field inside a Dialogo form.
 *
 * <p>Instances are created internally by
 * {@link com.abdullghani.dialogo.builder.DialogoBuilder} when you call
 * {@code addText(...)}, {@code addPassword(...)}, {@code addSwitch(...)}, etc.
 * You normally do not need to construct this class directly.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * Dialogo.with(this)
 *     .title("Sign up")
 *     .addText("name", "Name", "Your name")
 *     .addEmail("email", "Email", "you@example.com")
 *     .addPassword("password", "Password", "••••••••")
 *     .addSwitch("remember", "Remember me", true)
 *     .positive("Create", controller -> {
 *         Map<String, Object> values = controller.getResult().getFormValues();
 *     })
 *     .show(getSupportFragmentManager());
 * }</pre>
 */
public class FormField {

    /**
     * Field type — determines the rendered view and the value type in the result.
     */
    public enum Type {
        /** Single-line text. */
        TEXT,
        /** Password (masked). */
        PASSWORD,
        /** Numeric input. */
        NUMBER,
        /** Email address input. */
        EMAIL,
        /** Phone number input. */
        PHONE,
        /** Multi-line text. */
        MULTILINE,
        /** Toggle switch (boolean). */
        SWITCH,
        /** Dropdown menu (index). */
        DROPDOWN,
        /** Checkbox (boolean). */
        CHECKBOX
    }

    /** Unique identifier used as the key in the result map. */
    public final String id;

    /** The field type. */
    public final Type type;

    /** Optional visible label. */
    public CharSequence label;

    /** Optional hint / placeholder. */
    public CharSequence hint;

    /** Optional initial value. */
    public CharSequence prefill;

    /** Whether the field must be filled before confirmation. */
    public boolean required = false;

    /** Error message shown when {@code required} validation fails. */
    public CharSequence requiredError = "This field is required";

    /** Android input type (for text-based fields). */
    public int inputType = InputType.TYPE_CLASS_TEXT;

    /** Maximum number of lines (for MULTILINE fields). */
    public int maxLines = 1;

    /** Default value for SWITCH fields. */
    public boolean switchDefault = false;

    /** Default value for CHECKBOX fields. */
    public boolean checkboxDefault = false;

    /** Options for DROPDOWN fields. */
    public List<CharSequence> options;

    /** Initially selected index for DROPDOWN fields. */
    public int dropdownSelectedIndex = 0;

    /**
     * Creates a new form field.
     *
     * <p>Prefer {@link com.abdullghani.dialogo.builder.DialogoBuilder}'s
     * {@code addXxx(...)} methods instead of calling this constructor directly.</p>
     *
     * @param id   Unique field ID.
     * @param type Field type.
     */
    public FormField(@NonNull String id, @NonNull Type type) {
        this.id = id;
        this.type = type;
    }
}