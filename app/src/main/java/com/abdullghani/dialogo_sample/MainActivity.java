package com.abdullghani.dialogo_sample;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.abdullghani.dialogo.Dialogo;
import com.abdullghani.dialogo.animation.DialogoAnimation;
import com.abdullghani.dialogo.builder.DialogoBuilder;
import com.abdullghani.dialogo.core.DialogoFragment;
import com.abdullghani.dialogo.model.DialogoPosition;
import com.abdullghani.dialogo.theme.BuiltInThemes;

/**
 * Dialogo Sample Application.
 *
 * This activity demonstrates the most important features of the library:
 * - Basic dialogs
 * - Presets (Success, Error, Loading, etc.)
 * - Input & Lists
 * - Progress dialogs (with programmatic dismiss)
 * - Animations & Themes
 * - Non-Modal Popups & Positions
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ==================== Basic ====================
        findViewById(R.id.btSimpleDialog).setOnClickListener(v -> showSimpleDialog());
        findViewById(R.id.btConfirmDialog).setOnClickListener(v -> showConfirmDialog());
        findViewById(R.id.btStyledDialog).setOnClickListener(v -> showStyledDialog());
        findViewById(R.id.btIconDialog).setOnClickListener(v -> showIconDialog());
        findViewById(R.id.btHtmlMessage).setOnClickListener(v -> showHtmlMessage());

        // ==================== Presets ====================
        findViewById(R.id.btSuccess).setOnClickListener(v -> showSuccessDialog());
        findViewById(R.id.btError).setOnClickListener(v -> showErrorDialog());
        findViewById(R.id.btWarning).setOnClickListener(v -> showWarningDialog());
        findViewById(R.id.btInfo).setOnClickListener(v -> showInfoDialog());
        findViewById(R.id.btLoading).setOnClickListener(v -> showLoadingDialog());
        findViewById(R.id.btDeleteConfirm).setOnClickListener(v -> showDeleteDialog());

        // ==================== Input & Lists ====================
        findViewById(R.id.btInputDialog).setOnClickListener(v -> showInputDialog());
        findViewById(R.id.btSingleChoice).setOnClickListener(v -> showSingleChoice());
        findViewById(R.id.btMultiChoice).setOnClickListener(v -> showMultiChoice());

        // ==================== Progress ====================
        findViewById(R.id.btProgressDialog).setOnClickListener(v -> showProgressDialog());
        findViewById(R.id.btIndeterminateProgress).setOnClickListener(v -> showIndeterminateProgress());

        // ==================== Custom & Advanced ====================
        findViewById(R.id.btCustomView).setOnClickListener(v -> showCustomViewDialog());
        findViewById(R.id.btAutoDismiss).setOnClickListener(v -> showAutoDismiss());
        findViewById(R.id.btNonCancelable).setOnClickListener(v -> showNonCancelable());
        findViewById(R.id.btFullExample).setOnClickListener(v -> showFullExample());

        // ==================== Animations ====================
        findViewById(R.id.btAnimScale).setOnClickListener(v -> showAnim(DialogoAnimation.SCALE));
        findViewById(R.id.btAnimFade).setOnClickListener(v -> showAnim(DialogoAnimation.FADE));
        findViewById(R.id.btAnimSlideUp).setOnClickListener(v -> showAnim(DialogoAnimation.SLIDE_UP));
        findViewById(R.id.btAnimBounce).setOnClickListener(v -> showAnim(DialogoAnimation.BOUNCE));
        findViewById(R.id.btAnimShake).setOnClickListener(v -> showAnim(DialogoAnimation.SHAKE));

        // ==================== Themes ====================
        findViewById(R.id.btThemeMaterial3).setOnClickListener(v -> applyTheme(BuiltInThemes.MATERIAL_3(), "Material 3"));
        findViewById(R.id.btThemeIOS).setOnClickListener(v -> applyTheme(BuiltInThemes.IOS(), "iOS"));
        findViewById(R.id.btThemeDark).setOnClickListener(v -> applyTheme(BuiltInThemes.DARK(), "Dark"));
        findViewById(R.id.btThemeMinimal).setOnClickListener(v -> applyTheme(BuiltInThemes.MINIMAL(), "Minimal"));
        findViewById(R.id.btThemeRounded).setOnClickListener(v -> applyTheme(BuiltInThemes.ROUNDED(), "Rounded"));

        // ==================== Popup & Positions ====================
        findViewById(R.id.btPositionTop).setOnClickListener(v -> showModalAtTop());
        findViewById(R.id.btPositionCenter).setOnClickListener(v -> showModalAtCenter());
        findViewById(R.id.btPositionBottom).setOnClickListener(v -> showModalAtBottom());
        findViewById(R.id.btPopupTop).setOnClickListener(v -> showPopupAtTop());
        findViewById(R.id.btPopupCenter).setOnClickListener(v -> showPopupAtCenter());
        findViewById(R.id.btPopupBottom).setOnClickListener(v -> showPopupAtBottom());
        findViewById(R.id.btPopupAutoDismiss).setOnClickListener(v -> showPopupAutoDismiss());
        findViewById(R.id.btPopupSlideDown).setOnClickListener(v -> showPopupSlideDown());
    }

    // ==================================================================
    // ==================== BASIC DIALOGS ===============================
    // ==================================================================

    /**
     * 1. Simple Alert (Auto FragmentManager).
     *
     * Note: You no longer need to pass `getSupportFragmentManager()`.
     * Dialogo automatically resolves it from the Activity context!
     */
    private void showSimpleDialog() {
        Dialogo.with(this)
                .title("Welcome")
                .message("This dialog automatically resolved the FragmentManager from the context.")
                .positive("OK", controller ->
                        Toast.makeText(this, "OK clicked", Toast.LENGTH_SHORT).show())
                .show(); // <-- No getSupportFragmentManager() needed!
    }

    /**
     * 2. Confirm Dialog.
     *
     * Demonstrates a typical confirmation dialog with custom colors.
     */
    private void showConfirmDialog() {
        Dialogo.with(this)
                .title("Confirm Delete")
                .message("Are you sure you want to delete this item? This action cannot be undone.")
                .positive("Yes, Delete", Color.parseColor("#DC2626"), controller ->
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show())
                .negative("Cancel", controller -> controller.dismiss())
                .cancelable(true)
                .show(getSupportFragmentManager()); // Traditional way still works!
    }

    /**
     * 3. Fully Styled.
     *
     * Shows how to customize colors, corner radius, blur, elevation, and stroke.
     */
    private void showStyledDialog() {
        Dialogo.with(this)
                .title("Fully Customized")
                .message("Large corner radius, background blur, elevation and stroke applied.")
                .titleColor(Color.parseColor("#1D4ED8"))
                .messageColor(Color.parseColor("#374151"))
                .titleSize(20)
                .messageSize(15)
                .backgroundColor(Color.WHITE)
                .cornerRadius(32f)
                .blurBackground(true)
                .elevation(16f)
                .stroke(Color.parseColor("#DBEAFE"), 2f)
                .dimAmount(0.55f)
                .positive("Agree", Color.parseColor("#1D4ED8"), null)
                .negative("Close", Color.GRAY, null)
                .show();
    }

    /**
     * 4. With Icon.
     *
     * Note: You can control the size and tint of the icon.
     */
    private void showIconDialog() {
        Dialogo.with(this)
                .icon(android.R.drawable.ic_dialog_info)
                .iconTint(Color.parseColor("#2563EB"))
                .iconSize(45) // Size in dp
                .title("Important Information")
                .message("You can easily customize the icon size and tint color.")
                .positive("Got it", null)
                .show();
    }

    /**
     * 5. HTML Message.
     *
     * Note: Enable HTML formatting using `.htmlMessage(true)`.
     */
    private void showHtmlMessage() {
        Dialogo.with(this)
                .title("Formatted Message")
                .htmlMessage(true)
                .message("You can use <b>bold</b>, <i>italic</i> and <font color='#2563EB'>colored</font> text")
                .positive("Awesome", null)
                .show();
    }

    // ==================================================================
    // ==================== PRESET DIALOGS ==============================
    // ==================================================================

    private void showSuccessDialog() {
        Dialogo.success(this, "Saved!", "Your changes have been saved successfully.")
                .show();
    }

    private void showErrorDialog() {
        Dialogo.error(this, "Oops!", "Something went wrong. Please try again.")
                .show();
    }

    private void showWarningDialog() {
        Dialogo.warning(this, "Careful", "This action cannot be undone.")
                .show();
    }

    private void showInfoDialog() {
        Dialogo.info(this, "Did you know?", "You can long-press any item to see more options.")
                .show();
    }

    /**
     * 10. Loading Dialog (Programmatic Dismiss).
     *
     * Important: Loading dialogs are non-cancelable. You MUST hold a reference
     * to dismiss them manually using `builder.dismiss()`.
     */
    private void showLoadingDialog() {
        // Hold a reference to the Builder to dismiss it later
        DialogoBuilder builder = Dialogo.loading(this, "Processing your request...");

        builder.show();

        // Simulate a network request taking 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Dismiss the dialog safely when the operation finishes
            builder.dismiss();
            Toast.makeText(this, "Operation finished!", Toast.LENGTH_SHORT).show();
        }, 3000);
    }

    private void showDeleteDialog() {
        Dialogo.deleteConfirm(this, "important_file.pdf", controller ->
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
        ).show();
    }

    // ==================================================================
    // ==================== INPUT & LISTS ===============================
    // ==================================================================

    /**
     * 12. Input Dialog (Required).
     *
     * Note: `.inputRequired(true)` prevents the dialog from dismissing if the input is empty.
     */
    private void showInputDialog() {
        Dialogo.with(this)
                .title("Enter your name")
                .message("Please type your full name in the field below")
                .input("Full name", "")
                .inputRequired(true)
                .inputRequiredError("Please enter a name")
                .positive("Save", controller -> {
                    // Retrieve the text from the input field
                    String name = controller.getInputText();
                    Toast.makeText(this, "Hello " + name, Toast.LENGTH_SHORT).show();
                })
                .negative("Cancel", null)
                .show();
    }

    private void showSingleChoice() {
        Dialogo.with(this)
                .title("Select your preferred language")
                .items("Arabic", "English", "French", "Spanish", "German")
                .singleChoice(0, (position, item, controller) ->
                        Toast.makeText(this, "Currently selected: " + item, Toast.LENGTH_SHORT).show()
                )
                .positive("OK", controller ->
                        Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show()
                )
                .negative("Cancel", null)
                .show();
    }

    private void showMultiChoice() {
        boolean[] checked = {true, false, true, false};

        Dialogo.with(this)
                .title("Select your interests")
                .items("Programming", "Design", "Artificial Intelligence", "Marketing")
                .multiChoice(checked, (checkedItems, controller) -> {
                    StringBuilder result = new StringBuilder("Selected: ");
                    String[] items = {"Programming", "Design", "Artificial Intelligence", "Marketing"};
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (checkedItems[i]) result.append(items[i]).append(" ");
                    }
                    Toast.makeText(this, result.toString().trim(), Toast.LENGTH_LONG).show();
                })
                .positive("Confirm", null)
                .negative("Cancel", null)
                .show();
    }

    // ==================================================================
    // ==================== PROGRESS ====================================
    // ==================================================================

    private void showProgressDialog() {
        DialogoFragment dialog = Dialogo.with(this)
                .title("Downloading")
                .progress()
                .progressValue(0)
                .progressText("Starting download...")
                .showProgressPercent(true)
                .progressColor(Color.parseColor("#10B981"))
                .cancelable(false)
                .show();

        Handler handler = new Handler(Looper.getMainLooper());
        final int[] progress = {0};

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progress[0] += 10;
                if (progress[0] <= 100 && dialog.isAdded()) {
                    dialog.setProgress(progress[0]);
                    dialog.setProgressText("Downloaded " + progress[0] + "%");
                    handler.postDelayed(this, 400);
                } else if (dialog.isAdded()) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Download completed", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }

    private void showIndeterminateProgress() {
        Dialogo.with(this)
                .title("Please wait")
                .progress(true)
                .progressText("Processing your request...")
                .progressColor(Color.parseColor("#2563EB"))
                .cancelable(false)
                .autoDismissAfter(4000)
                .show();
    }

    // ==================================================================
    // ==================== CUSTOM & ADVANCED ===========================
    // ==================================================================

    private void showCustomViewDialog() {
        Dialogo.with(this)
                .title("Custom Layout")
                .message("You can inflate any custom layout you want")
                .customView(R.layout.dialog_input_layout, (view, controller) -> {
                    // Configure the custom view here (e.g., setup listeners)
                })
                .positive("Save", controller -> {
                    String text = controller.getInputText();
                    Toast.makeText(this, "Entered text: " + text, Toast.LENGTH_SHORT).show();
                })
                .negative("Cancel", null)
                .show();
    }

    private void showAutoDismiss() {
        Dialogo.with(this)
                .title("Notification")
                .message("This message will disappear automatically after 3 seconds")
                .autoDismissAfter(3000)
                .positive("OK", null)
                .show();
    }

    private void showNonCancelable() {
        Dialogo.with(this)
                .title("Important Operation")
                .message("This dialog can only be dismissed using the button")
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positive("OK", null)
                .show();
    }

    private void showFullExample() {
        Dialogo.with(this)
                .icon(android.R.drawable.ic_dialog_alert)
                .iconTint(Color.parseColor("#F59E0B"))
                .title("Full Featured Dialog")
                .message("This example combines most of the available features together")
                .titleColor(Color.parseColor("#B45309"))
                .messageColor(Color.parseColor("#374151"))
                .titleSize(20)
                .messageSize(15)
                .backgroundColor(Color.WHITE)
                .cornerRadius(20f)
                .elevation(12f)
                .dimAmount(0.6f)
                .animation(DialogoAnimation.BOUNCE)
                .positive("Confirm", Color.parseColor("#D97706"), controller ->
                        Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show())
                .negative("Later", null)
                .neutral("Details", controller ->
                        Toast.makeText(this, "More details", Toast.LENGTH_SHORT).show())
                .cancelable(true)
                .show();
    }

    // ==================================================================
    // ==================== ANIMATIONS ==================================
    // ==================================================================

    private void showAnim(DialogoAnimation animation) {
        Dialogo.with(this)
                .title("Animation: " + animation.name())
                .message("This dialog uses the " + animation.name() + " animation.")
                .animation(animation)
                .positive("OK", null)
                .negative("Close", null)
                .show();
    }

    // ==================================================================
    // ==================== THEMES ======================================
    // ==================================================================

    private void applyTheme(com.abdullghani.dialogo.theme.DialogoTheme theme, String name) {
        Dialogo.initGlobalTheme(theme);
        Toast.makeText(this, name + " theme applied globally", Toast.LENGTH_SHORT).show();

        Dialogo.with(this)
                .title(name + " Theme")
                .message("This dialog uses the " + name + " theme applied globally.")
                .positive("Done", null)
                .show();
    }

    // ==================================================================
    // ==================== MODAL POSITIONS =============================
    // ==================================================================

    private void showModalAtTop() {
        Dialogo.with(this)
                .title("Top Modal")
                .message("This modal appears at the top of the screen.")
                .position(DialogoPosition.TOP)
                .marginTopDp(80)
                .positive("OK", null)
                .negative("Cancel", null)
                .show();
    }

    private void showModalAtCenter() {
        Dialogo.with(this)
                .title("Center Modal")
                .message("This modal appears at the center of the screen.")
                .position(DialogoPosition.CENTER)
                .positive("OK", null)
                .negative("Cancel", null)
                .show();
    }

    private void showModalAtBottom() {
        Dialogo.with(this)
                .title("Bottom Modal")
                .message("This modal appears near the bottom of the screen.")
                .position(DialogoPosition.BOTTOM)
                .marginBottomDp(12)
                .positive("OK", null)
                .negative("Cancel", null)
                .show();
    }

    // ==================================================================
    // ==================== POPUP (NON-MODAL) ===========================
    // ==================================================================

    private void showPopupAtTop() {
        Dialogo.popup(this, "Heads up!", "This is a non-modal popup at the top. Tap behind it to interact with the app.")
                .position(DialogoPosition.TOP)
                .marginTopDp(12)
                .widthPercentage(0.9f)
                .animation(DialogoAnimation.SLIDE_DOWN)
                .autoDismissAfter(5000)
                .show();
    }

    private void showPopupAtCenter() {
        Dialogo.with(this)
                .title("Quick Popup")
                .message("Non-modal popup in the center. You can still tap the app behind it.")
                .asPopup()
                .position(DialogoPosition.CENTER)
                .animation(DialogoAnimation.FADE)
                .autoDismissAfter(4000)
                .show();
    }

    private void showPopupAtBottom() {
        Dialogo.with(this)
                .title("Bottom Popup")
                .message("Non-modal popup near the bottom.")
                .asPopup()
                .position(DialogoPosition.BOTTOM)
                .marginBottomDp(12)
                .animation(DialogoAnimation.SLIDE_UP)
                .autoDismissAfter(5000)
                .show();
    }

    private void showPopupAutoDismiss() {
        Dialogo.popup(this, "Saved", "Auto-closing in 3 seconds...", 3000)
                .position(DialogoPosition.TOP)
                .marginTopDp(12)
                .widthPercentage(0.85f)
                .animation(DialogoAnimation.SLIDE_DOWN)
                .show();
    }

    private void showPopupSlideDown() {
        Dialogo.with(this)
                .title("New Notification")
                .message("This popup slides down from the top, and slides up on close.")
                .asPopup()
                .position(DialogoPosition.TOP)
                .marginTopDp(12)
                .widthPercentage(0.92f)
                .animation(DialogoAnimation.SLIDE_DOWN)
                .autoDismissAfter(4000)
                .show();
    }
}