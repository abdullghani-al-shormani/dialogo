package com.abdullghani.dialogo.listeners;

import com.abdullghani.dialogo.core.DialogoController;

public interface OnMultiChoiceClickListener {
    void onClick(DialogoController controller, int position, boolean isChecked);
}