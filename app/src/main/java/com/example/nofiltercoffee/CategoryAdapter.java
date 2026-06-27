package com.example.nofiltercoffee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
    public interface Listener {
        void onClick(String c);
    }

    private final ArrayList<String> list;
    private final Listener listener;
    private String selected = "All";

    public CategoryAdapter(ArrayList<String> list, Listener listener) {
        this.list = new ArrayList<>();
        if (list != null) {
            for (String item : list) {
                if (item != null) this.list.add(item);
            }
        }
        if (this.list.isEmpty()) this.list.add("All");
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder == null || holder.textView == null) return;
        if (position < 0 || position >= list.size()) return;

        String category = list.get(position);
        if (category == null) category = "Unknown";
        final String finalCategory = category;

        holder.textView.setText(category);
        boolean isSelected = category.equals(selected);
        holder.textView.setBackgroundResource(
                isSelected ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected
        );
        holder.textView.setTextColor(
                holder.textView.getResources().getColor(
                        isSelected ? R.color.espresso_bg : R.color.cream
                )
        );

        holder.textView.setOnClickListener(v -> {
            if (listener != null && finalCategory != null) {
                listener.onClick(finalCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setSelectedCategory(String category) {
        if (category == null) return;
        this.selected = category;
        notifyDataSetChanged();
    }

    public String getSelectedCategory() {
        return selected != null ? selected : "All";
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView textView;
        VH(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtCategory);
        }
    }
}