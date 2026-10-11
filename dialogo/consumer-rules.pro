# ==================== Dialogo Consumer Rules ====================
# These rules are applied to any app that depends on Dialogo.

-keep class com.abdullghani.dialogo.** { *; }
-keep interface com.abdullghani.dialogo.** { *; }

# Keep enums used by consumers
-keepclassmembers enum com.abdullghani.dialogo.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}