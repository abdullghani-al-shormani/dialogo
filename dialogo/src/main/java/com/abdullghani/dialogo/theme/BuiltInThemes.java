package com.abdullghani.dialogo.theme;

import android.graphics.Color;

import com.abdullghani.dialogo.animation.DialogoAnimation;

/**
 * Factory class providing a set of ready-to-use {@link DialogoTheme} presets.
 *
 * <p>Each theme is a static method returning a fresh {@link DialogoTheme}.
 * Apply a theme globally via
 * {@link com.abdullghani.dialogo.Dialogo#initGlobalTheme(DialogoTheme)}.</p>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * // Set once, e.g. in your Application class
 * Dialogo.initGlobalTheme(BuiltInThemes.DARK());
 *
 * // All dialogs created afterwards use the Dark theme
 * Dialogo.with(this).title("Hello").positive("OK", null).show(fm);
 * }</pre>
 *
 * <h3>Available themes</h3>
 * <ul>
 *     <li>{@link #MATERIAL_3()} — modern Material You palette</li>
 *     <li>{@link #IOS()} — iOS-style soft, rounded look</li>
 *     <li>{@link #DARK()} — dark background with light text</li>
 *     <li>{@link #MINIMAL()} — sharp corners, no decoration</li>
 *     <li>{@link #ROUNDED()} — very large corner radius</li>
 * </ul>
 *
 * @see DialogoTheme
 */
public final class BuiltInThemes {

    private BuiltInThemes() { }

    /**
     * Material 3 theme — modern palette with rounded corners and a soft
     * purple accent, following Google's Material You guidelines.
     *
     * @return A fresh Material 3 {@link DialogoTheme}.
     */
    public static DialogoTheme MATERIAL_3() {
        return new DialogoTheme.Builder()
                .setBackgroundColor(Color.parseColor("#FFFBFE"))
                .setCornerRadius(28f)
                .setTitleColor(Color.parseColor("#1C1B1F"))
                .setMessageColor(Color.parseColor("#49454F"))
                .setPositiveColor(Color.parseColor("#6750A4"))
                .setNegativeColor(Color.parseColor("#B3261E"))
                .setAnimation(DialogoAnimation.SCALE)
                .build();
    }

    /**
     * iOS-style theme — soft gray background with large rounded corners
     * and native iOS accent colors (blue / red).
     *
     * @return A fresh iOS {@link DialogoTheme}.
     */
    public static DialogoTheme IOS() {
        return new DialogoTheme.Builder()
                .setBackgroundColor(Color.parseColor("#F2F2F7"))
                .setCornerRadius(36f)
                .setTitleColor(Color.parseColor("#000000"))
                .setMessageColor(Color.parseColor("#3C3C43"))
                .setPositiveColor(Color.parseColor("#007AFF"))
                .setNegativeColor(Color.parseColor("#FF3B30"))
                .setAnimation(DialogoAnimation.BOUNCE)
                .build();
    }

    /**
     * Dark theme — deep gray background with light text, ideal for
     * night mode or dark-mode applications.
     *
     * @return A fresh Dark {@link DialogoTheme}.
     */
    public static DialogoTheme DARK() {
        return new DialogoTheme.Builder()
                .setBackgroundColor(Color.parseColor("#1E1E1E"))
                .setCornerRadius(20f)
                .setTitleColor(Color.parseColor("#FFFFFF"))
                .setMessageColor(Color.parseColor("#B0B0B0"))
                .setPositiveColor(Color.parseColor("#4FC3F7"))
                .setNegativeColor(Color.parseColor("#EF5350"))
                .setAnimation(DialogoAnimation.FADE)
                .build();
    }

    /**
     * Minimal theme — sharp corners (8px), white background, monochrome
     * text. Best for applications that prefer a flat, no-frills look.
     *
     * @return A fresh Minimal {@link DialogoTheme}.
     */
    public static DialogoTheme MINIMAL() {
        return new DialogoTheme.Builder()
                .setBackgroundColor(Color.WHITE)
                .setCornerRadius(8f)
                .setTitleColor(Color.parseColor("#111111"))
                .setMessageColor(Color.parseColor("#666666"))
                .setPositiveColor(Color.parseColor("#111111"))
                .setNegativeColor(Color.parseColor("#999999"))
                .setAnimation(DialogoAnimation.FADE)
                .build();
    }

    /**
     * Rounded theme — white background with very large corner radius (48px),
     * blue positive and red negative accents. Gives a soft, friendly feel.
     *
     * @return A fresh Rounded {@link DialogoTheme}.
     */
    public static DialogoTheme ROUNDED() {
        return new DialogoTheme.Builder()
                .setBackgroundColor(Color.WHITE)
                .setCornerRadius(48f)
                .setTitleColor(Color.parseColor("#0F172A"))
                .setMessageColor(Color.parseColor("#475569"))
                .setPositiveColor(Color.parseColor("#2563EB"))
                .setNegativeColor(Color.parseColor("#DC2626"))
                .setAnimation(DialogoAnimation.SCALE)
                .build();
    }
}