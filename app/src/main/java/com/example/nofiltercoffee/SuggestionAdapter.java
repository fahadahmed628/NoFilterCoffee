package com.example.nofiltercoffee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.VH> {
    Context c;
    ArrayList<Product> list;
    Runnable cb;

    public SuggestionAdapter(Context c, ArrayList<Product> l, Runnable cb) {
        this.c = c;
        list = l;
        this.cb = cb;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_suggestion, p, false));
    }

    @Override
    public void onBindViewHolder(VH h, int p) {
        Product pr = list.get(p);
        h.n.setText(pr.name);
        h.price.setText("Rs. " + pr.price);

        int quantity = getProductQuantity(pr);
        updateQuantityUI(h, quantity);

        // Add button - uses requiresCustomization()
        h.btnAdd.setOnClickListener(v -> {
            if (ProductUtils.requiresCustomization(pr.category)) {
                Intent in = new Intent(c, ProductDetailActivity.class);
                in.putExtra("product", pr);
                c.startActivity(in);
            } else {
                CartManager.add(pr);
                if (cb != null) cb.run();
                notifyDataSetChanged();
            }
        });

        // Plus in selector
        h.btnPlus.setOnClickListener(v -> {
            if (ProductUtils.requiresCustomization(pr.category)) {
                Intent in = new Intent(c, ProductDetailActivity.class);
                in.putExtra("product", pr);
                c.startActivity(in);
            } else {
                CartManager.add(pr);
                if (cb != null) cb.run();
                notifyDataSetChanged();
            }
        });

        // Minus in selector
        h.btnMinus.setOnClickListener(v -> {
            for (CartItem item : CartManager.getCart()) {
                if (item.product.id.equals(pr.id)) {
                    CartManager.decrease(item);
                    break;
                }
            }
            if (cb != null) cb.run();
            notifyDataSetChanged();
        });

        // Delete/Trash
        h.btnDelete.setOnClickListener(v -> {
            ArrayList<CartItem> cart = CartManager.getCart();
            ArrayList<CartItem> toRemove = new ArrayList<>();
            for (CartItem item : cart) {
                if (item.product.id.equals(pr.id)) {
                    toRemove.add(item);
                }
            }
            for (CartItem item : toRemove) {
                cart.remove(item);
            }
            if (cb != null) cb.run();
            notifyDataSetChanged();
        });
    }

    private int getProductQuantity(Product p) {
        int total = 0;
        for (CartItem item : CartManager.getCart()) {
            if (item.product.id.equals(p.id)) {
                total += item.quantity;
            }
        }
        return total;
    }

    private void updateQuantityUI(VH h, int qty) {
        if (qty == 0) {
            h.btnAdd.setVisibility(View.VISIBLE);
            h.quantitySelector.setVisibility(View.GONE);
        } else {
            h.btnAdd.setVisibility(View.GONE);
            h.quantitySelector.setVisibility(View.VISIBLE);
            h.tvQuantity.setText(String.valueOf(qty));
            if (qty == 1) {
                h.btnDelete.setVisibility(View.VISIBLE);
                h.btnMinus.setVisibility(View.GONE);
            } else {
                h.btnDelete.setVisibility(View.GONE);
                h.btnMinus.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 8);
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView n, price;
        TextView btnAdd;
        View quantitySelector;
        ImageView btnDelete;
        TextView btnMinus, btnPlus, tvQuantity;

        VH(View v) {
            super(v);
            n = v.findViewById(R.id.txtName);
            price = v.findViewById(R.id.txtPrice);
            btnAdd = v.findViewById(R.id.btnAdd);
            quantitySelector = v.findViewById(R.id.quantitySelector);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnPlus = v.findViewById(R.id.btnPlus);
            tvQuantity = v.findViewById(R.id.tvQuantity);
        }
    }
}