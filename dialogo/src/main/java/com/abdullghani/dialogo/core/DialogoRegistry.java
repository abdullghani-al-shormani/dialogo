package com.abdullghani.dialogo.core;

import android.util.SparseArray;

import com.abdullghani.dialogo.model.DialogoParams;

/**
 * سجل مؤقت يحفظ الـ DialogoParams بين الـ Builder والـ Fragment.
 * يحل مشكلة أن Params تحتوي على Listeners و Views لا يمكن وضعها في Bundle.
 *
 * الاستخدام: Fragment يقرأ params بواسطة الـ ID المخزّن في الـ Bundle.
 */
public final class DialogoRegistry {

    private static final SparseArray<DialogoParams> REGISTRY = new SparseArray<>();
    private static int nextId = 1;

    private DialogoRegistry() { }

    /** يسجّل Params ويُرجع ID فريد. */
    public static synchronized int register(DialogoParams params) {
        int id = nextId++;
        REGISTRY.put(id, params);
        return id;
    }

    /** يجلب Params بواسطة ID. */
    public static synchronized DialogoParams get(int id) {
        return REGISTRY.get(id);
    }

    /** يحذف Params بعد العرض (لتفادي تسرّب الذاكرة). */
    public static synchronized void unregister(int id) {
        REGISTRY.remove(id);
    }
}