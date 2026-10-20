### Dialogo

**An elegant, fully customizable dialog library for Android.**

Dialogs · Non-Modal Popups · Forms · Custom Views · Result API

[![](https://jitpack.io/v/abdullghani-al-shormani/dialogo.svg)](https://jitpack.io/#abdullghani-al-shormani/dialogo)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![Kotlin](https://img.shields.io/badge/Kotlin-Supported-blueviolet.svg)](https://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-Supported-orange.svg)](https://###.java.com)

---

### Overview

**Dialogo** is a lightweight, dependency-free Android library that replaces the boilerplate of `AlertDialog`, `DialogFragment`, and custom popups with a fluent, chainable API.

Whether you need a simple alert, a complex form, a custom view, a non-modal popup, or a fully themed dialog system, Dialogo handles it with a single, consistent interface.

``` kotlin
Dialogo.with(this)
    .title("Welcome")
    .message("This is a Dialogo dialog.")
    .positive("OK") { /* action */ }
    .negative("Cancel")
    .show()
```

---

### Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Preset Dialogs](#preset-dialogs)
- [Input and Lists](#input-and-lists)
- [Progress Dialogs](#progress-dialogs)
- [Custom Views](#custom-views)
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

### Features

- **Full Customization** — Colors, sizes, fonts, corner radius, elevation, stroke, blur.
- **8 Built-in Animations** — Scale, Fade, Slide (4 directions), Bounce, Shake.
- **6 Preset Dialogs** — Success, Error, Warning, Info, Loading, Delete.
- **5 Built-in Themes** — Material 3, iOS, Dark, Minimal, Rounded.
- **Custom Views** — Inflate any layout inside the dialog.
- **Form Builder** — Text, password, email, phone, switch, dropdown, checkbox.
- **Result API** — Unified callback for all dialog outcomes.
- **Non-Modal Popups** — Top, center, or bottom; supports tap-through.
- **Auto FragmentManager** — No need to pass `getSupportFragmentManager()`.
- **State Preservation** — Survives process death via internal registry.
- **RTL/LTR Support** — Automatic layout direction.
- **Accessibility** — Content descriptions and 48dp touch targets.
- **Java and Kotlin** — Full API for both languages.

---

### Installation

### Step 1 — Add JitPack to settings.gradle

``` groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2 — Add the dependency

``` groovy
dependencies {
    implementation 'com.github.abdullghani-al-shormani:dialogo:v1.0.5'
}
```

### Step 3 — Sync and use

No initialization required. Start building dialogs immediately.

---

### Quick Start

``` kotlin
Dialogo.with(this)
    .title("Welcome")
    .message("Hello, Dialogo!")
    .positive("OK") {
        // handle click
    }
    .negative("Cancel")
    .show() // FragmentManager is resolved automatically!
```

---

### Preset Dialogs

Ready-to-use dialogs with appropriate icons, colors, and animations.

### Success

``` kotlin
Dialogo.success(this, "Saved!", "Your changes were saved.")
    .show()
```

### Show Error

``` kotlin
Dialogo.error(this, "Oops!", "Something went wrong.")
    .show()
```

### Show Warning

``` kotlin
Dialogo.warning(this, "Careful", "This action cannot be undone.")
    .show()
```

### Show Info

``` kotlin
Dialogo.info(this, "Did you know?", "Long-press any item for more options.")
    .show()
```

### Show Loading

``` kotlin
val loading = Dialogo
    .loading(this, "Processing...")
    .show()

// Dismiss later:
loading.dismiss()
```

With auto-dismiss timeout:

``` kotlin
Dialogo.loading(this, "Connecting...", 5000)
    .show()
```

### Delete Confirmation

``` kotlin
Dialogo.deleteConfirm(this, "important_file.pdf") {
    // confirmed
}.show()
```

---

### Input and Lists

### Text Input

``` kotlin
Dialogo.with(this)
    .title("Enter your name")
    .input("Full name", "")
    .inputRequired(true)
    .inputRequiredError("Name is required")
    .positive("Save") { controller ->
        val name = controller.inputText
    }
    .negative("Cancel")
    .show()
```

### Single Choice

``` kotlin
Dialogo.with(this)
    .title("Select language")
    .items("Arabic", "English", "French", "Spanish")
    .singleChoice(0) { position, item, _ ->
        // item selected
    }
    .positive("OK")
    .negative("Cancel")
    .show()
```

### Multi Choice

``` kotlin
val checked = booleanArrayOf(true, false, true, false)

Dialogo.with(this)
    .title("Select interests")
    .items("Programming", "Design", "AI", "Marketing")
    .multiChoice(checked) { checkedItems, _ ->
        // use checkedItems
    }
    .positive("Confirm")
    .negative("Cancel")
    .show()
```

---

### Progress Dialogs

### Horizontal (determinate)

``` kotlin
val dialog = Dialogo.with(this)
    .title("Downloading")
    .progress()
    .progressValue(0)
    .progressText("Starting...")
    .showProgressPercent(true)
    .progressColor(0xFF10B981.toInt())
    .cancelable(false)
    .show()

// Update from anywhere:
dialog.setProgress(50)
dialog.setProgressText("Downloaded 50%")
```

### Circular (indeterminate)

``` kotlin
Dialogo.with(this)
    .title("Please wait")
    .progress(true)
    .progressText("Processing...")
    .cancelable(false)
    .autoDismissAfter(4000)
    .show()
```

---

### Custom Views

You can inflate any custom XML layout inside the dialog and configure it using the provided controller.

``` kotlin
Dialogo.with(this)
    .title("Custom Layout")
    .message("This dialog has a custom view inside.")
    .customView(R.layout.your_custom_layout) { view, controller ->
        // Find and configure views inside your layout
        val myButton = view.findViewById<Button>(R.id.my_custom_button)
        val myInput = view.findViewById<EditText>(R.id.my_custom_input)
        
        myButton.setOnClickListener {
            val text = myInput.text.toString()
            Toast.makeText(this, "You typed: $text", Toast.LENGTH_SHORT).show()
            controller.dismiss()
        }
    }
    .positive("Save") { controller ->
        // You can also get the input text if you are using the built-in input field
    }
    .negative("Cancel")
    .show()
```

---

### Non-Modal Popups

Dialogs that do not block the UI. Users can interact with the app behind them. Ideal for notifications, tips, and transient feedback.

### WhatsApp-style notification (top)

``` kotlin
Dialogo.popup(this, "New message", "You have a new message from Sara")
    .autoDismissAfter(4000)
    .show()
```

### Custom position

``` kotlin
Dialogo.with(this)
    .title("Bottom Popup")
    .message("Non-modal popup near the bottom.")
    .asPopup()
    .positionBottom()
    .marginBottomDp(80)
    .animation(DialogoAnimation.SLIDE_UP)
    .autoDismissAfter(5000)
    .show()
```

---

### Result API

Replace scattered callbacks with a single, unified listener.

``` kotlin
Dialogo.with(this)
    .title("Delete?")
    .positive("Yes")
    .negative("No")
    .showForResult { result ->
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

- `isPositive()` — positive button was pressed.
- `isNegative()` — negative button was pressed.
- `isNeutral()` — neutral button was pressed.
- `isDismissed()` — dismissed without button press.
- `isItemSelected()` — a list item was selected.
- `getInputText()` — text from the input field.
- `getSelectedIndex()` — index selected in a single-choice list.
- `getSelectedItem()` — item selected in a single-choice list.
- `getCheckedItems()` — multi-choice states.
- `getFormValues()` — collected form values.

---

### Form Builder

Build complete forms with multiple field types in a single dialog.

``` kotlin
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
    .show()
```

### Available Field Types

- `addText()` — single-line text (String).
- `addPassword()` — masked password (String).
- `addNumber()` — numeric input (String).
- `addEmail()` — email address (String).
- `addPhone()` — phone number (String).
- `addMultiline()` — multi-line text (String).
- `addSwitch()` — toggle switch (Boolean).
- `addCheckbox()` — checkbox (Boolean).
- `addDropdown()` — dropdown (Integer, selected index).

---

### Animations

Apply any of the following with `.animation(...)`:

- `NONE` — no animation.
- `SCALE` — scale and fade (default).
- `FADE` — smooth fade-in.
- `SLIDE_UP` — slides from bottom.
- `SLIDE_DOWN` — slides from top.
- `SLIDE_RIGHT` — slides from right.
- `SLIDE_LEFT` — slides from left.
- `BOUNCE` — spring bounce.
- `SHAKE` — horizontal shake (ideal for errors).

**Example:**

``` kotlin
Dialogo.with(this)
    .title("Bounce!")
    .animation(DialogoAnimation.BOUNCE)
    .positive("OK")
    .show()
```

---

### Themes

### Built-in Themes

- `BuiltInThemes.MATERIAL_3()` — modern Material You.
- `BuiltInThemes.IOS()` — iOS-style soft colors.
- `BuiltInThemes.DARK()` — dark mode.
- `BuiltInThemes.MINIMAL()` — sharp corners, no frills.
- `BuiltInThemes.ROUNDED()` — very large corner radius.

### Apply Globally

In your Application class:

``` kotlin
Dialogo.initGlobalTheme(BuiltInThemes.DARK())
```

All dialogs created afterward inherit the theme.

### Custom Theme

``` kotlin
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

### Positions and Margins

### Position

``` kotlin
.positionTop()      // or .position(DialogoPosition.TOP)
.positionCenter()   // or .position(DialogoPosition.CENTER)
.positionBottom()   // or .position(DialogoPosition.BOTTOM)
```

### Margins

``` kotlin
.marginTopDp(12)          // 12dp from top
.marginBottomDp(20)       // 20dp from bottom
.marginHorizontalDp(16)   // 16dp from both sides

.marginTop(px)            // raw pixels
.marginBottom(px)
.marginHorizontal(px)
```

---

### API Reference

### Content

- `title(CharSequence)` — set title.
- `message(CharSequence)` — set message.
- `htmlMessage(boolean)` — render message as HTML.
- `copyableMessage(boolean)` — enable long-press to copy.
- `icon(int)` — set icon resource.
- `icon(Drawable)` — set icon drawable.
- `iconTint(int)` — icon color.
- `iconSize(int)` — icon size in dp.
- `animateIcon(boolean)` — icon bounce animation.

### Styling

- `titleColor(int)` — title color.
- `messageColor(int)` — message color.
- `titleSize(int)` — title size in sp.
- `messageSize(int)` — message size in sp.
- `titleGravity(int)` — title gravity.
- `messageGravity(int)` — message gravity.
- `font(Typeface)` — font for all text.
- `backgroundColor(int)` — card background.
- `cornerRadius(float)` — corner radius in px.
- `elevation(float)` — elevation in dp.
- `stroke(int, float)` — border color and width.
- `blurBackground(boolean)` — background blur (API 31+).
- `dimAmount(float)` — background dim.
- `widthPercentage(float)` — width as fraction of screen.
- `maxHeightPercentage(float)` — max height as fraction of screen.

### Buttons

- `positive(text, listener)` — positive button.
- `negative(text, listener)` — negative button.
- `neutral(text, listener)` — neutral button.
- `positiveBackgroundColor(int)` — positive button background.
- `buttonsStacked(boolean)` — stack buttons vertically.
- `fullWidthButtons(boolean)` — full-width buttons.

### Behavior

- `cancelable(boolean)` — dismiss with Back button.
- `canceledOnTouchOutside(boolean)` — dismiss on outside tap.
- `autoDismiss(boolean)` — auto-dismiss after button press.
- `autoDismissAfter(long)` — auto-dismiss after delay in ms.
- `showCountdown(boolean)` — show countdown text.

### Showing

- `show()` — show as modal dialog (Auto FragmentManager).
- `show(fragmentManager)` — show with explicit FragmentManager.
- `show(fragmentManager, tag)` — show with fragment tag.
- `showForResult(listener)` — show with result callback.
- `build()` — build without showing.

---

### Requirements

- **Android API**: 21+ (Android 5.0)
- **Compile SDK**: 35+
- **Java**: 8+ (Java 11 recommended)
- **Kotlin**: 1.8+ (optional)

---

### License

```
MIT License

Copyright (c) 2025 Abdullghani Al-Shormani
```

See [LICENSE](LICENSE) for full text.

---

### Author

**Abdullghani Al-Shormani**

- GitHub: [@abdullghani-al-shormani](https://github.com/abdullghani-al-shormani)

---

### Support

If you find Dialogo useful, please consider giving it a star on GitHub — it helps a lot.

[![Star on GitHub](https://img.shields.io/github/stars/abdullghani-al-shormani/dialogo?style=social)](https://github.com/abdullghani-al-shormani/dialogo)
![Uploading image.png…]()
