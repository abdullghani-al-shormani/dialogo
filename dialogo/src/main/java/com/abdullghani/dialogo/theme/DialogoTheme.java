package com.abdullghani.dialogo.theme;

import android.graphics.Typeface;

import androidx.annotation.ColorInt;

import com.abdullghani.dialogo.animation.DialogoAnimation;

/**
 * Immutable theme definition for Dialogo dialogs.
 *
 * <p>A theme describes the visual identity of a dialog — background color,
 * corner radius, font, text colors, and default animation. Once built, a
 * {@link DialogoTheme} can be applied globally (via
 * {@link com.abdullghani.dialogo.Dialogo#initGlobalTheme(DialogoTheme)}) or
 * per-dialog (if you use the {@code theme(...)} method on the builder).</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * DialogoTheme theme = new DialogoTheme.Builder()
 *         .setBackgroundColor(Color.WHITE)
 *         .setCornerRadius(24f)
 *         .setTitleColor(0xFF111827)
 *         .setMessageColor(0xFF4B5563)
 *         .setPositiveColor(0xFF2563EB)
 *         .setNegativeColor(0xFFDC2626)
 *         .setAnimation(DialogoAnimation.SCALE)
 *         .build();
 *
 * Dialogo.initGlobalTheme(theme);
 * }</pre>
 *
 * @see com.abdullghani.dialogo.theme.BuiltInThemes
 * @see com.abdullghani.dialogo.Dialogo#initGlobalTheme(DialogoTheme)
 */
public class DialogoTheme {

    private final Integer backgroundColor;
    private final Float cornerRadiusPx;
    private final Typeface defaultFont;
    private final Integer titleColor;
    private final Integer messageColor;
    private final Integer positiveColor;
    private final Integer negativeColor;
    private final DialogoAnimation animation;

    private DialogoTheme(Builder builder) {
        this.backgroundColor = builder.backgroundColor;
        this.cornerRadiusPx = builder.cornerRadiusPx;
        this.defaultFont = builder.defaultFont;
        this.titleColor = builder.titleColor;
        this.messageColor = builder.messageColor;
        this.positiveColor = builder.positiveColor;
        this.negativeColor = builder.negativeColor;
        this.animation = builder.animation;
    }

    /** @return The card background color, or {@code null} if unset. */
    public Integer getBackgroundColor() { return backgroundColor; }

    /** @return The corner radius in pixels, or {@code null} if unset. */
    public Float getCornerRadiusPx() { return cornerRadiusPx; }

    /** @return The default font applied to title, message, and buttons. */
    public Typeface getDefaultFont() { return defaultFont; }

    /** @return The title text color, or {@code null} if unset. */
    public Integer getTitleColor() { return titleColor; }

    /** @return The message text color, or {@code null} if unset. */
    public Integer getMessageColor() { return messageColor; }

    /** @return The positive button color, or {@code null} if unset. */
    public Integer getPositiveColor() { return positiveColor; }

    /** @return The negative button color, or {@code null} if unset. */
    public Integer getNegativeColor() { return negativeColor; }

    /** @return The default animation, or {@code null} if unset. */
    public DialogoAnimation getAnimation() { return animation; }

    /**
     * Builder for constructing {@link DialogoTheme} instances.
     *
     * <p>All setters are optional. Unset properties fall back to the dialog's
     * built-in defaults.</p>
     */
    public static class Builder {
        private Integer backgroundColor;
        private Float cornerRadiusPx;
        private Typeface defaultFont;
        private Integer titleColor;
        private Integer messageColor;
        private Integer positiveColor;
        private Integer negativeColor;
        private DialogoAnimation animation;

        /**
         * Sets the card background color.
         *
         * @param color ARGB color.
         * @return This builder for chaining.
         */
        public Builder setBackgroundColor(@ColorInt int color) { this.backgroundColor = color; return this; }

        /**
         * Sets the corner radius in pixels.
         *
         * @param radiusPx Corner radius in pixels.
         * @return This builder for chaining.
         */
        public Builder setCornerRadius(float radiusPx) { this.cornerRadiusPx = radiusPx; return this; }

        /**
         * Sets the font applied to title, message, and buttons.
         *
         * @param font A {@link Typeface}.
         * @return This builder for chaining.
         */
        public Builder setDefaultFont(Typeface font) { this.defaultFont = font; return this; }

        /**
         * Sets the title text color.
         *
         * @param color ARGB color.
         * @return This builder for chaining.
         */
        public Builder setTitleColor(@ColorInt int color) { this.titleColor = color; return this; }

        /**
         * Sets the message text color.
         *
         * @param color ARGB color.
         * @return This builder for chaining.
         */
        public Builder setMessageColor(@ColorInt int color) { this.messageColor = color; return this; }

        /**
         * Sets the positive button text color.
         *
         * @param color ARGB color.
         * @return This builder for chaining.
         */
        public Builder setPositiveColor(@ColorInt int color) { this.positiveColor = color; return this; }

        /**
         * Sets the negative button text color.
         *
         * @param color ARGB color.
         * @return This builder for chaining.
         */
        public Builder setNegativeColor(@ColorInt int color) { this.negativeColor = color; return this; }

        /**
         * Sets the default animation.
         *
         * @param animation One of {@link DialogoAnimation}.
         * @return This builder for chaining.
         */
        public Builder setAnimation(DialogoAnimation animation) { this.animation = animation; return this; }

        /**
         * Builds the immutable theme.
         *
         * @return A new {@link DialogoTheme}.
         */
        public DialogoTheme build() { return new DialogoTheme(this); }
    }
}