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

**Dialogo** is a lightweight, dependency-free Android library that replaces the boilerplate of ``AlertDialogBT``, ``DialogFragment``, and ``BottomSheetDialog`` with a fluent, chainable API.

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

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Preset Dialogs](#preset-dialogs)
- [Input and Lists](#input-and-lists)
- [Progress Dialogs](#progress-dialogs)
- [BottomSheet](#bottomsheet)
- [Non-Modal Popups](#non-modal-popups)
- [Result API](#result-api)
- [Form Builder](#form-builder)
- [Animations](#animations)
- [Themes](#themes)
- [Positions and Margins](#positions-and-margins)
- [API Reference](#api-reference)
- [Requirements](#requirements)
- [License](#license)

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
    implementation 'com.github.abdullghani-al-shormani:dialogo:v1.0.2'
}
```

### Step 3 — Sync and use

No initialization required. Start building dialogs immediately.

---

## Quick Start

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

```Kotlin
Dialogo.success(this, "Saved!", "Your changes were saved.")
    .show(supportFragmentManager)
```

### Show Error

```Kotlin
Dialogo.error(this, "Oops!", "Something went wrong.")
    .show(supportFragmentManager)
```

### Show Warning

```Kotlin
Dialogo.warning(this, "Careful", "This action cannot be undone.")
    .show(supportFragmentManager)
```

### Show Info

```Kotlin
Dialogo.info(this, "Did you know?", "Long-press any item for more options.")
    .show(supportFragmentManager)
```

### Show Loading
```Kotlin
val loading = Dialogo
    .loading(this, "Processing...")
    .show(supportFragmentManager)

// Dismiss later:
loading.dismiss()
```

### With auto-dismiss timeout:

```Kotlin
Dialogo.loading(this, "Connecting...", 5000)
    .show(supportFragmentManager)
```

### Delete Confirmation

```Kotlin
Dialogo.deleteConfirm(this, "important_file.pdf") {
    // confirmed
}.show(supportFragmentManager)
```

---

## Input and Lists

### Text Input

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

```Kotlin
Dialogo.popup(this, "New message", "You have a new message from Sara")
    .autoDismissAfter(4000)
    .show(supportFragmentManager)
```

### Custom position

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

- ``isPositive()`` — positive button was pressed.
- ``isNegative()`` — negative button was pressed.
- ``isNeutral()`` — neutral button was pressed.
- ``isDismissed()`` — dismissed without button press.
- ``isItemSelected()`` — a list item was selected.
- ``getInputText()`` — text from the input field.
- ``getSelectedIndex()`` — index selected in a single-choice list.
- ``getSelectedItem()`` — item selected in a single-choice list.
- ``getCheckedItems()`` — multi-choice states.
- ``getFormValues()`` — collected form values.

---

## Form Builder

Build complete forms with multiple field types in a single dialog.


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

- ``addText()`` — single-line text (String).
- ``addPassword()`` — masked password (String).
- ``addNumber()`` — numeric input (String).
- ``addEmail()`` — email address (String).
- ``addPhone()`` — phone number (String).
- ``addMultiline()`` — multi-line text (String).
- ``addSwitch()`` — toggle switch (Boolean).
- ``addCheckbox()`` — checkbox (Boolean).
- ``addDropdown()`` — dropdown (Integer, selected index).

---

## Animations

Apply any of the following with ``.animation(...)``:

- ``NONE`` — no animation.
- ``SCALE`` — scale and fade (default).
- ``FADE`` — smooth fade-in.
- ``SLIDE_UP`` — slides from bottom.
- ``SLIDE_DOWN`` — slides from top.
- ``SLIDE_RIGHT`` — slides from right.
- ``SLIDE_LEFTBT` — slides from left.
- ``BOUNCE`` — spring bounce.
- ``SHAKE`` — horizontal shake (ideal for errors).

**Example:**

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

- ``BuiltInThemes.MATERIAL_3()`` — modern Material You.
- ``BuiltInThemes.IOS()`` — iOS-style soft colors.
- ``BuiltInThemes.DARK()`` — dark mode.
- ``BuiltInThemes.MINIMAL()`` — sharp corners, no frills.
- ``BuiltInThemes.ROUNDED()`` — very large corner radius.

### Apply Globally

In your Application class:

```Kotlin
Dialogo.initGlobalTheme(BuiltInThemes.DARK())
```

All dialogs created afterward inherit the theme.

### Custom Theme

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

- ``title(CharSequence)`` — set title.
- ``message(CharSequence)`` — set message.
- ``HtmlMessage(boolean)`` — render message as HTML.
- ``copyableMessage(boolean)`` — enable long-press to copy.
- ``icon(int)/BTicon(Drawable)`` — set icon.
- ``iconTint(int)`` — icon color.
- ``iconSize(int)`` — icon size in dp.
- ``animateIcon(boolean)`` — icon bounce animation.

### Styling

- ``titleColor(int)`` — title color.
- ``messageColor(int)`` — message color.
- ``titleSize(int)`` — title size in sp.
- ``messageSize(int)`` — message size in sp.
- ``titleGravity(int)`` — title gravity.
- ``messageGravity(int)`` — message gravity.
- ``font(Typeface)`` — font for all text.
- ``backgroundColor(int)`` — card background.
- ``cornerRadius(float)`` — corner radius in px.
- ``elevation(float)`` — elevation in dp.
- ``stroke(int, float)`` — border color and width.
- ``blurBackground(boolean)`` — background blur (API 31+).
- ``dimAmount(float)`` — background dim.
- ``widthPercentage(float)`` — width as fraction of screen.
- ``maxHeightPercentage(float)`` — max height as fraction of screen.

### Buttons

- ``positive(text, listener)`` — positive button.
- ``negative(text, listener)`` — negative button.
- ``neutral(text, listener)`` — neutral button.
- ``positiveBackgroundColor(int)`` — positive button background.
- ``buttonsStacked(boolean)`` — stack buttons vertically.
- ``fullWidthButtons(boolean)`` — full-width buttons.

### Behavior

- ``cancelable(boolean)`` — dismiss with Back button.
- ``canceledOnTouchOutside(boolean)`` — dismiss on outside tap.
- ``autoDismiss(boolean)`` — auto-dismiss after button press.
- ``autoDismissAfter(long)`` — auto-dismiss after delay in ms.
- ``showCountdown(boolean)`` — show countdown text.

### Showing

- ``show(fm)`` — show as modal dialog.
- ``show(fm, tag)`` — show with fragment tag.
- ``showBottomSheet(fm)`` — show as bottom sheet.
- `showForResult(fm, listener)`` — show with result callback.
- ``build()`` — build without showing.

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
