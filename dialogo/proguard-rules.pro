# ==================== Dialogo Library ====================

# Keep public API of Dialogo
-keep public class com.abdullghani.dialogo.** {
    public protected *;
}

# Keep listener interfaces
-keep interface com.abdullghani.dialogo.model.** { *; }
-keep interface com.abdullghani.dialogo.core.** { *; }

# Keep Params/Result/FormField (used via Bundle serialization)
-keep class com.abdullghani.dialogo.model.DialogoParams { *; }
-keep class com.abdullghani.dialogo.model.DialogoResult { *; }
-keep class com.abdullghani.dialogo.model.FormField { *; }
-keep class com.abdullghani.dialogo.model.DialogoAction { *; }
-keep class com.abdullghani.dialogo.model.DialogoPosition { *; }

# Keep animation enums
-keep class com.abdullghani.dialogo.animation.DialogoAnimation { *; }

# Keep enums generally
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Kotlin metadata
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses