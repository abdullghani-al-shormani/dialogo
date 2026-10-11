package com.abdullghani.dialogo.listeners;

import com.abdullghani.dialogo.core.DialogoController;

public interface OnItemClickListener {
    void onItemClick(DialogoController controller, int position, CharSequence text);
}