## Dialogo

**An elegant, fully customizable dialog library for Android.**

Dialogs · BottomSheets · Non-Modal Popups · Forms · Result API

[![](https://jitpack.io/v/abdullghani-al-shormani/dialogo.svg)](https://jitpack.io/#abdullghani-al-shormani/dialogo)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![Kotlin](https://img.shields.io/badge/Kotlin-Supported-blueviolet.svg)](https://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-Supported-orange.svg)](https://www.java.com)

---

## Overview

**Dialogo** is a lightweight, dependency-free Android library that replaces the boilerplate of `BTAlertDialogBT`, `BTDialogFragmentBT`, and `BTBottomSheetDialogBT` with a fluent, chainable API.

Whether you need a simple alert, a complex form, a non-modal popup, or a fully themed dialog system, Dialogo handles it with a single, consistent interface.
**Java Example**
```Java
Dialogo.with(this)
    .title("Welcome")
    .message("This is a Dialogo dialog.")
    .positive("OK", null)
    .negative("Cancel", null)
    .show(getSupportFragmentManager());
```

---

## Table of Contents

- [Features](##features)
- [Installation](##installation)
- [Quick Start](##quick-start)
- [Preset Dialogs](##preset-dialogs)
- [Input and Lists](##input-and-lists)
- [Progress Dialogs](##progress-dialogs)
- [BottomSheet](##bottomsheet)
- [Non-Modal Popups](##non-modal-popups)
- [Result API](##result-api)
- [Form Builder](##form-builder)
- [Animations](##animations)
- [Themes](##themes)
- [Positions and Margins](##positions-and-margins)
- [API Reference](##api-reference)
- [Requirements](##requirements)
- [License](##license)

---

## Features

- **Full Customization** — Colors, sizes, fonts, corner radius, elevation, stroke, blur.

- **8 Built-in Animations** — Scale, Fade, Slide (4 directions), Bounce, Shake.

- **6 Preset Dialogs** — Success, Error, Warning, Info, Loading, Delete.

- **5 Built-in Themes** — Material 3, iOS, Dark, Minimal, Rounded.

- **Professional BottomSheet** — With icons, drag handle, and Material behavior.

- **Form Builder** — Text, password, email, phone, switch, dropdown, checkbox.

- **Result API** — Unified callback for all dialog outcomes.

- **Non-Modal Popups** — Top, center, or bottom; supports tap-through.

- **State Preservation** — Survives process death via internal registry.

- **RTL/LTR Support** — Automatic layout direction.

- **Accessibility** — Content descriptions and 48dp touch targets.

- **Java and Kotlin** — Full API for both languages.

---

## Installation

### Step 1 — Add JitPack to settings.gradle

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2 — Add the dependency

```groovy
dependencies {
    implementation 'com.github.abdullghani-al-shormani:dialogo:v1.0.1'
}
```

### Step 3 — Sync and use

No initialization required. Start building dialogs immediately.

---

## Quick Start

**Java**

```Java
Dialogo.with(this)
    .title("Welcome")
    .message("Hello, Dialogo!")
    .positive("OK", controller -> {
        // handle click
    })
    .negative("Cancel", null)
    .show(getSupportFragmentManager());
```

**Kotlin**

```Kotlin
Dialogo.with(this)
    .title("Welcome")
    .message("Hello, Dialogo!")
    .positive("OK") {
        // handle click
    }
    .negative("Cancel")
    .show(supportFragmentManager)
```

---

## Preset Dialogs

Ready-to-use dialogs with appropriate icons, colors, and animations.

### Success

**Java**

```Java
Dialogo.success(this, "Saved!", "Your changes were saved.")
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.success(this, "Saved!", "Your changes were saved.")
    .show(supportFragmentManager)
```

### Show Error

**Java**

```Java
Dialogo.error(this, "Oops!", "Something went wrong.")
    .show(getSupportFragmentManager());
```

**Kotlin**

```Kotlin
Dialogo.error(this, "Oops!", "Something went wrong.")
    .show(supportFragmentManager)
```

### Show Warning

**Java**

```Java
Dialogo.warning(this, "Careful", "This action cannot be undone.")
    .show(getSupportFragmentManager());
```

**Kotlin**

```Kotlin
Dialogo.warning(this, "Careful", "This action cannot be undone.")
    .show(supportFragmentManager)
```

### Show Info

**Java**

```Java
Dialogo.info(this, "Did you know?", "Long-press any item for more options.")
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.info(this, "Did you know?", "Long-press any item for more options.")
    .show(supportFragmentManager)
```

### Show Loading

**Java**
```Java
DialogoFragment loading = Dialogo
    .loading(this, "Processing...")
    .show(getSupportFragmentManager());

// Dismiss later:
loading.dismiss();
```

**Kotlin**
```Kotlin
val loading = Dialogo
    .loading(this, "Processing...")
    .show(supportFragmentManager)

// Dismiss later:
loading.dismiss()
```

### With auto-dismiss timeout:

**Java**
```Java
Dialogo.loading(this, "Connecting...", 5000)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.loading(this, "Connecting...", 5000)
    .show(supportFragmentManager)
```

### Delete Confirmation

**Java**
```Java
Dialogo.deleteConfirm(this, "important_file.pdf", controller -> {
    // confirmed
}).show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.deleteConfirm(this, "important_file.pdf") {
    // confirmed
}.show(supportFragmentManager)
```

---

## Input and Lists

### Text Input

**Java**
```Java
Dialogo.with(this)
    .title("Enter your name")
    .input("Full name", "")
    .inputRequired(true)
    .inputRequiredError("Name is required")
    .positive("Save", controller -> {
        String name = controller.getInputText();
    })
    .negative("Cancel", null)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Enter your name")
    .input("Full name", "")
    .inputRequired(true)
    .inputRequiredError("Name is required")
    .positive("Save") { controller ->
        val name = controller.inputText
    }
    .negative("Cancel")
    .show(supportFragmentManager)
```

### Single Choice

**Java**
```Java
Dialogo.with(this)
    .title("Select language")
    .items("Arabic", "English", "French", "Spanish")
    .singleChoice(0, (position, item, controller) -> {
        // item selected
    })
    .positive("OK", null)
    .negative("Cancel", null)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Select language")
    .items("Arabic", "English", "French", "Spanish")
    .singleChoice(0) { position, item, _ ->
        // item selected
    }
    .positive("OK")
    .negative("Cancel")
    .show(supportFragmentManager)
```

### Multi Choice

**Java**
```Java
boolean[] checked = {true, false, true, false};

Dialogo.with(this)
    .title("Select interests")
    .items("Programming", "Design", "AI", "Marketing")
    .multiChoice(checked, (checkedItems, controller) -> {
        // use checkedItems
    })
    .positive("Confirm", null)
    .negative("Cancel", null)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
val checked = booleanArrayOf(true, false, true, false)

Dialogo.with(this)
    .title("Select interests")
    .items("Programming", "Design", "AI", "Marketing")
    .multiChoice(checked) { checkedItems, _ ->
        // use checkedItems
    }
    .positive("Confirm")
    .negative("Cancel")
    .show(supportFragmentManager)
```

---

## Progress Dialogs

### Horizontal (determinate)

**Java**
```Java
DialogoFragment dialog = Dialogo.with(this)
    .title("Downloading")
    .progress()
    .progressValue(0)
    .progressText("Starting...")
    .showProgressPercent(true)
    .progressColor(0xFF10B981)
    .cancelable(false)
    .show(getSupportFragmentManager());

// Update from anywhere:
dialog.setProgress(50);
dialog.setProgressText("Downloaded 50%");
```

**Kotlin**
```Kotlin
val dialog = Dialogo.with(this)
    .title("Downloading")
    .progress()
    .progressValue(0)
    .progressText("Starting...")
    .showProgressPercent(true)
    .progressColor(0xFF10B981.toInt())
    .cancelable(false)
    .show(supportFragmentManager)

// Update from anywhere:
dialog.setProgress(50)
dialog.setProgressText("Downloaded 50%")
```

### Circular (indeterminate)

**Java**
```Java
Dialogo.with(this)
    .title("Please wait")
    .progress(true)
    .progressText("Processing...")
    .cancelable(false)
    .autoDismissAfter(4000)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Please wait")
    .progress(true)
    .progressText("Processing...")
    .cancelable(false)
    .autoDismissAfter(4000)
    .show(supportFragmentManager)
```

---

## BottomSheet

Professional Material bottom sheet with icon support, drag handle, and full content capabilities.

**Java**
```Java
Dialogo.with(this)
    .title("Choose an action")
    .items("Edit", "Share", "Duplicate", "Delete")
    .itemIcons(
        android.R.drawable.ic_menu_edit,
        android.R.drawable.ic_menu_share,
        android.R.drawable.ic_menu_add,
        android.R.drawable.ic_menu_delete
    )
    .itemIconTint(0xFF2563EB)
    .itemClick((position, item, controller) -> {
        // handle click
    })
    .showBottomSheet(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Choose an action")
    .items("Edit", "Share", "Duplicate", "Delete")
    .itemIcons(
        android.R.drawable.ic_menu_edit,
        android.R.drawable.ic_menu_share,
        android.R.drawable.ic_menu_add,
        android.R.drawable.ic_menu_delete
    )
    .itemIconTint(0xFF2563EB.toInt())
    .itemClick { _, item, _ ->
        // handle click
    }
    .showBottomSheet(supportFragmentManager)
```

---

## Non-Modal Popups

Dialogs that do not block the UI. Users can interact with the app behind them. Ideal for notifications, tips, and transient feedback.

### WhatsApp-style notification (top)

**Java**
```Java
Dialogo.popup(this, "New message", "You have a new message from Sara")
    .autoDismissAfter(4000)
    .show(getSupportFragmentManager());
```

**Kotlin**

```Kotlin
Dialogo.popup(this, "New message", "You have a new message from Sara")
    .autoDismissAfter(4000)
    .show(supportFragmentManager)
```

### Custom position
**Java**
```Java
Dialogo.with(this)
    .title("Bottom Popup")
    .message("Non-modal popup near the bottom.")
    .asPopup()
    .positionBottom()
    .marginBottomDp(80)
    .animation(DialogoAnimation.SLIDE_UP)
    .autoDismissAfter(5000)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Bottom Popup")
    .message("Non-modal popup near the bottom.")
    .asPopup()
    .positionBottom()
    .marginBottomDp(80)
    .animation(DialogoAnimation.SLIDE_UP)
    .autoDismissAfter(5000)
    .show(supportFragmentManager)
```

---

## Result API

Replace scattered callbacks with a single, unified listener.

**Java**
```Java
Dialogo.with(this)
    .title("Delete?")
    .positive("Yes")
    .negative("No")
    .showForResult(getSupportFragmentManager(), result -> {
        if (result.isPositive()) {
            deleteItem();
        } else if (result.isNegative()) {
            // cancelled
        } else if (result.isDismissed()) {
            // tapped outside
        }
    });
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Delete?")
    .positive("Yes")
    .negative("No")
    .showForResult(supportFragmentManager) { result ->
        if (result.isPositive) {
            deleteItem()
        } else if (result.isNegative) {
            // cancelled
        } else if (result.isDismissed) {
            // tapped outside
        }
    }
```

### DialogoResult API

- `BTisPositive()`` — positive button was pressed.
- `BTisNegative()`` — negative button was pressed.
- `BTisNeutral()`` — neutral button was pressed.
- `BTisDismissed()`` — dismissed without button press.
- `BTisItemSelected()`` — a list item was selected.
- `BTgetInputText()`` — text from the input field.
- `BTgetSelectedIndex()`` — index selected in a single-choice list.
- `BTgetSelectedItem()`` — item selected in a single-choice list.
- `BTgetCheckedItems()`` — multi-choice states.
- `BTgetFormValues()`` — collected form values.

---

## Form Builder

Build complete forms with multiple field types in a single dialog.

**Java**
```Java
Dialogo.with(this)
    .title("Sign up")
    .addText("name", "Name", "Your full name")
    .addEmail("email", "Email", "you@example.com")
    .addPassword("password", "Password", "••••••••")
    .addSwitch("remember", "Remember me", true)
    .addDropdown("country", "Country",
        Arrays.asList("Yemen", "Saudi Arabia", "Egypt"), 0)
    .positive("Create account", controller -> {
        Map<String, Object> values =
            controller.getResult().getFormValues();
        String name  = (String)  values.get("name");
        String email = (String)  values.get("email");
        boolean rem  = (Boolean) values.get("remember");
    })
    .negative("Cancel", null)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Sign up")
    .addText("name", "Name", "Your full name")
    .addEmail("email", "Email", "you@example.com")
    .addPassword("password", "Password", "••••••••")
    .addSwitch("remember", "Remember me", true)
    .addDropdown("country", "Country",
        listOf("Yemen", "Saudi Arabia", "Egypt"), 0)
    .positive("Create account") { controller ->
        val values = controller.result.formValues
        val name     = values["name"] as String
        val email    = values["email"] as String
        val remember = values["remember"] as Boolean
    }
    .negative("Cancel")
    .show(supportFragmentManager)
```

### Available Field Types

- `BTaddText()`` — single-line text (String).
- `BTaddPassword()`` — masked password (String).
- `BTaddNumber()`` — numeric input (String).
- `BTaddEmail()`` — email address (String).
- `BTaddPhone()`` — phone number (String).
- `BTaddMultiline()`` — multi-line text (String).
- `BTaddSwitch()`` — toggle switch (Boolean).
- `BTaddCheckbox()`` — checkbox (Boolean).
- `BTaddDropdown()`` — dropdown (Integer, selected index).

---

## Animations

Apply any of the following with ``.animation(...)``:

- `BTNONEBT` — no animation.
- `BTSCALEBT` — scale and fade (default).
- `BTFADEBT` — smooth fade-in.
- `BTSLIDE_UPBT` — slides from bottom.
- `BTSLIDE_DOWNBT` — slides from top.
- `BTSLIDE_RIGHTBT` — slides from right.
- `BTSLIDE_LEFTBT` — slides from left.
- `BTBOUNCEBT` — spring bounce.
- `BTSHAKEBT` — horizontal shake (ideal for errors).

Example:

**Java**
```Java
Dialogo.with(this)
    .title("Bounce!")
    .animation(DialogoAnimation.BOUNCE)
    .positive("OK", null)
    .show(getSupportFragmentManager());
```

**Kotlin**
```Kotlin
Dialogo.with(this)
    .title("Bounce!")
    .animation(DialogoAnimation.BOUNCE)
    .positive("OK")
    .show(supportFragmentManager)
```

---

## Themes

### Built-in Themes

- `BTBuiltInThemes.MATERIAL_3()`` — modern Material You.
- `BTBuiltInThemes.IOS()`` — iOS-style soft colors.
- `BTBuiltInThemes.DARK()`` — dark mode.
- `BTBuiltInThemes.MINIMAL()`` — sharp corners, no frills.
- `BTBuiltInThemes.ROUNDED()`` — very large corner radius.

### Apply Globally

In your Application class:

**Java**
```Java
Dialogo.initGlobalTheme(BuiltInThemes.DARK());
```

**Kotlin**
```Kotlin
Dialogo.initGlobalTheme(BuiltInThemes.DARK())
```

All dialogs created afterward inherit the theme.

### Custom Theme

**Java**
```Java
DialogoTheme theme = new DialogoTheme.Builder()
    .setBackgroundColor(0xFFFFFFFF)
    .setCornerRadius(24f)
    .setTitleColor(0xFF111827)
    .setMessageColor(0xFF4B5563)
    .setPositiveColor(0xFF2563EB)
    .setNegativeColor(0xFFDC2626)
    .setAnimation(DialogoAnimation.SCALE)
    .build();

Dialogo.initGlobalTheme(theme);
```

**Kotlin**
```Kotlin
val theme = DialogoTheme.Builder()
    .setBackgroundColor(0xFFFFFFFF.toInt())
    .setCornerRadius(24f)
    .setTitleColor(0xFF111827.toInt())
    .setMessageColor(0xFF4B5563.toInt())
    .setPositiveColor(0xFF2563EB.toInt())
    .setNegativeColor(0xFFDC2626.toInt())
    .setAnimation(DialogoAnimation.SCALE)
    .build()

Dialogo.initGlobalTheme(theme)
```

---

## Positions and Margins

### Position

```
.positionTop();      // or .position(DialogoPosition.TOP)
.positionCenter();   // or .position(DialogoPosition.CENTER)
.positionBottom();   // or .position(DialogoPosition.BOTTOM)
```

### Margins

```
.marginTopDp(12);          // 12dp from top
.marginBottomDp(20);       // 20dp from bottom
.marginHorizontalDp(16);   // 16dp from both sides

.marginTop(px);            // raw pixels
.marginBottom(px);
.marginHorizontal(px);
```

---

## API Reference

### Content

- `BTtitle(CharSequence)`` — set title.
- `BTmessage(CharSequence)`` — set message.
- `BTHtmlMessage(boolean)`` — render message as HTML.
- `BTcopyableMessage(boolean)`` — enable long-press to copy.
- `BTicon(int)/BTicon(Drawable)`` — set icon.
- `BTiconTint(int)`` — icon color.
- `BTiconSize(int)`` — icon size in dp.
- `BTanimateIcon(boolean)`` — icon bounce animation.

### Styling

- `BTtitleColor(int)`` — title color.
- `BTmessageColor(int)`` — message color.
- `BTtitleSize(int)`` — title size in sp.
- `BTmessageSize(int)`` — message size in sp.
- `BTtitleGravity(int)`` — title gravity.
- `BTmessageGravity(int)`` — message gravity.
- `BTfont(Typeface)`` — font for all text.
- `BTbackgroundColor(int)`` — card background.
- `BTcornerRadius(float)`` — corner radius in px.
- `BTelevation(float)`` — elevation in dp.
- `BTstroke(int, float)`` — border color and width.
- `BTblurBackground(boolean)`` — background blur (API 31+).
- `BTdimAmount(float)`` — background dim.
- `BTwidthPercentage(float)`` — width as fraction of screen.
- `BTmaxHeightPercentage(float)`` — max height as fraction of screen.

### Buttons

- `BTpositive(text, listener)`` — positive button.
- `BTnegative(text, listener)`` — negative button.
- `BTneutral(text, listener)`` — neutral button.
- `BTpositiveBackgroundColor(int)`` — positive button background.
- `BTbuttonsStacked(boolean)`` — stack buttons vertically.
- `BTfullWidthButtons(boolean)`` — full-width buttons.

### Behavior

- `BTcancelable(boolean)`` — dismiss with Back button.
- `BTcanceledOnTouchOutside(boolean)`` — dismiss on outside tap.
- `BTautoDismiss(boolean)`` — auto-dismiss after button press.
- `BTautoDismissAfter(long)`` — auto-dismiss after delay in ms.
- `BTshowCountdown(boolean)`` — show countdown text.

### Showing

- `BTshow(fm)`` — show as modal dialog.
- `BTshow(fm, tag)`` — show with fragment tag.
- `BTshowBottomSheet(fm)`` — show as bottom sheet.
- `BTshowForResult(fm, listener)`` — show with result callback.
- `BTbuild()`` — build without showing.

---

## Requirements

- **Android API**: 21+ (Android 5.0)
- **Compile SDK**: 34+
- **Java**: 8+ (Java 11 recommended)
- **Kotlin**: 1.8+ (optional)

---

## License

```
MIT License

Copyright (c) 2025 Abdullghani Al-Shormani
```

See [LICENSE](LICENSE) for full text.

---

## Author

**Abdullghani Al-Shormani**

- GitHub: [@abdullghani-al-shormani](https://github.com/abdullghani-al-shormani)

---

## Support

If you find Dialogo useful, please consider giving it a star on GitHub — it helps a lot.

[![Star on GitHub](https://img.shields.io/github/stars/abdullghani-al-shormani/dialogo?style=social)](https://github.com/abdullghani-al-shormani/dialogo)
<img width="468" height="643" alt="image" src="https://github.com/user-attachments/assets/e6f5bd8a-8e00-48d4-b7b6-0b184d1a3784" />
