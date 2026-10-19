package com.abdullghani.dialogo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdullghani.dialogo.R;
import com.abdullghani.dialogo.core.DialogoController;
import com.abdullghani.dialogo.model.DialogoParams;

import java.util.List;

public class DialogoListAdapter extends RecyclerView.Adapter<DialogoListAdapter.ItemViewHolder> {

    private final List<CharSequence> items;
    private final List<Integer> icons;
    private final boolean isSingleChoice;
    private final boolean isMultiChoice;
    private final DialogoParams params;
    private final DialogoController controller;

    private int selectedPosition;
    private final boolean[] checkedItems;
    private final boolean hasPositiveButton;

    public DialogoListAdapter(DialogoParams params, DialogoController controller) {
        this.params = params;
        this.controller = controller;
        this.items = params.listItems;
        this.icons = params.listIcons;
        this.isSingleChoice = params.listSingleChoice;
        this.isMultiChoice = params.listMultiChoice;
        this.selectedPosition = params.listSelectedIndex;
        this.hasPositiveButton = params.positiveAction != null;

        if (params.listCheckedItems != null) {
            this.checkedItems = params.listCheckedItems.clone();
        } else {
            this.checkedItems = new boolean[items != null ? items.size() : 0];
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialogo_item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CharSequence item = items.get(position);
        holder.tvText.setText(item);

        // ==================== Icon ====================
        if (holder.ivIcon != null) {
            if (icons != null && position < icons.size() && icons.get(position) != 0) {
                holder.ivIcon.setImageResource(icons.get(position));
                holder.ivIcon.setVisibility(View.VISIBLE);
                if (params.listIconTint != null) {
                    holder.ivIcon.setColorFilter(params.listIconTint);
                }
            } else {
                holder.ivIcon.setVisibility(View.GONE);
            }
        }

        // Reset
        holder.radioButton.setVisibility(View.GONE);
        holder.checkBox.setVisibility(View.GONE);

        // ==================== Single Choice ====================
        if (isSingleChoice) {
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.radioButton.setChecked(position == selectedPosition);

            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getBindingAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                int previousSelected = selectedPosition;
                selectedPosition = pos;

                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);

                if (params.onListItemSelected != null) {
                    params.onListItemSelected.onItemClick(selectedPosition, item, controller);
                }

                if (!hasPositiveButton && params.autoDismiss) {
                    if (params.onListItemConfirmed != null) {
                        params.onListItemConfirmed.onItemClick(selectedPosition, item, controller);
                    }
                    controller.dismiss();
                }
            });

            // ==================== Multi Choice ====================
        } else if (isMultiChoice) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(checkedItems[position]);

            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getBindingAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                checkedItems[pos] = !checkedItems[pos];
                holder.checkBox.setChecked(checkedItems[pos]);
            });

            // ==================== Plain List ====================
        } else {
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getBindingAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                if (params.onListItemClick != null) {
                    params.onListItemClick.onItemClick(pos, items.get(pos), controller);
                }
                if (params.autoDismiss) {
                    controller.dismiss();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public boolean[] getCheckedItems() {
        return checkedItems;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        RadioButton radioButton;
        CheckBox checkBox;
        ImageView ivIcon;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.dialogo_tv_item_text);
            radioButton = itemView.findViewById(R.id.dialogo_radio_item);
            checkBox = itemView.findViewById(R.id.dialogo_checkbox_item);
            ivIcon = itemView.findViewById(R.id.dialogo_item_icon);
        }
    }
}