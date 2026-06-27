package com.example.nofiltercoffee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
    public interface Listener {
        void onCartChanged();
    }

    private final ArrayList<CartItem> list;
    private final Listener listener;

    public CartAdapter(ArrayList<CartItem> list, Listener listener) {
        // Defensive copy – never accept null
        this.list = list != null ? list : new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder == null) return;
        if (position < 0 || position >= list.size()) return;

        CartItem item = list.get(position);
        if (item == null) return;

        // Null‑safe product name
        String productName = (item.product != null && item.product.name != null) ? item.product.name : "Unknown item";
        holder.name.setText(productName);

        // Null‑safe options
        String optionsText = (item.options != null) ? item.options : "Standard";
        holder.options.setText(optionsText);

        // Price – getTotal() returns int, safe
        holder.price.setText("Rs. " + item.getTotal());

        // Quantity
        holder.quantity.setText(String.valueOf(item.quantity));

        // Plus button
        holder.plus.setOnClickListener(v -> {
            CartManager.increase(item);
            notifyDataSetChanged();
            if (listener != null) listener.onCartChanged();
        });

        // Minus button
        holder.minus.setOnClickListener(v -> {
            CartManager.decrease(item);
            notifyDataSetChanged();
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, options, price, quantity, plus, minus;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.txtName);
            options = v.findViewById(R.id.txtOptions);
            price = v.findViewById(R.id.txtPrice);
            quantity = v.findViewById(R.id.txtQty);
            plus = v.findViewById(R.id.btnPlus);
            minus = v.findViewById(R.id.btnMinus);
        }
    }
}