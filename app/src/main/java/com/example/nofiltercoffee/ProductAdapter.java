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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {
    private final Context context;
    private final ArrayList<Product> list;
    private final Runnable cartCallback;

    public ProductAdapter(Context context, ArrayList<Product> list, Runnable cartCallback) {
        this.context = context;
        this.list = list;
        this.cartCallback = cartCallback;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int position) {
        Product p = list.get(position);
        h.name.setText(p.name);
        h.desc.setText(p.description);
        h.price.setText("from Rs. " + p.price);
        h.badge.setText(p.badge);

        int quantity = getProductQuantity(p);
        updateQuantityUI(h, quantity);

        h.root.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductDetailActivity.class);
            i.putExtra("product", p);
            context.startActivity(i);
        });

        // Add button (+)
        h.btnAdd.setOnClickListener(v -> {
            CartManager.add(p);
            if (cartCallback != null) cartCallback.run();
            notifyDataSetChanged();
        });

        // Plus in selector
        h.btnPlus.setOnClickListener(v -> {
            CartManager.add(p);
            if (cartCallback != null) cartCallback.run();
            notifyDataSetChanged();
        });

        // Minus in selector
        h.btnMinus.setOnClickListener(v -> {
            for (CartItem item : CartManager.getCart()) {
                if (item.product.id.equals(p.id)) {
                    CartManager.decrease(item);
                    break;
                }
            }
            if (cartCallback != null) cartCallback.run();
            notifyDataSetChanged();
        });

        // Delete/Trash
        h.btnDelete.setOnClickListener(v -> {
            ArrayList<CartItem> cart = CartManager.getCart();
            ArrayList<CartItem> toRemove = new ArrayList<>();
            for (CartItem item : cart) {
                if (item.product.id.equals(p.id)) {
                    toRemove.add(item);
                }
            }
            for (CartItem item : toRemove) {
                cart.remove(item);
            }
            if (cartCallback != null) cartCallback.run();
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
        return list.size();
    }

    public void update(ArrayList<Product> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        View root;
        TextView name, desc, price, badge;
        TextView btnAdd;
        View quantitySelector;
        ImageView btnDelete;
        TextView btnMinus, btnPlus, tvQuantity;

        VH(View v) {
            super(v);
            root = v.findViewById(R.id.rootProduct);
            name = v.findViewById(R.id.txtName);
            desc = v.findViewById(R.id.txtDescription);
            price = v.findViewById(R.id.txtPrice);
            badge = v.findViewById(R.id.txtBadge);
            btnAdd = v.findViewById(R.id.btnAdd);
            quantitySelector = v.findViewById(R.id.quantitySelector);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnPlus = v.findViewById(R.id.btnPlus);
            tvQuantity = v.findViewById(R.id.tvQuantity);
        }
    }
}