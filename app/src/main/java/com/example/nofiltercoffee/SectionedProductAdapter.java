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

public class SectionedProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    ArrayList<ProductListItem> list;
    Runnable cb;

    public SectionedProductAdapter(Context c, ArrayList<ProductListItem> l, Runnable cb) {
        this.c = c;
        list = l;
        this.cb = cb;
    }

    public int getItemViewType(int p) {
        return list.get(p).type;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p, int t) {
        if (t == 0) {
            return new HH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_section_header, p, false));
        } else {
            return new PH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_product, p, false));
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder h, int p) {
        ProductListItem it = list.get(p);
        if (h instanceof HH) {
            ((HH) h).t.setText(it.categoryTitle);
            return;
        }

        Product pr = it.product;
        PH x = (PH) h;
        x.n.setText(pr.name);
        x.d.setText(pr.description);
        x.price.setText("from Rs. " + pr.price);
        x.badge.setText(pr.badge);

        int quantity = getProductQuantity(pr);
        updateQuantityUI(x, quantity);

        x.root.setOnClickListener(v -> open(pr));

        // Add button (+) - uses requiresCustomization()
        x.btnAdd.setOnClickListener(v -> {
            if (ProductUtils.requiresCustomization(pr.category)) {
                open(pr);
            } else {
                CartManager.add(pr);
                if (cb != null) cb.run();
                notifyDataSetChanged();
            }
        });

        // Plus in selector
        x.btnPlus.setOnClickListener(v -> {
            if (ProductUtils.requiresCustomization(pr.category)) {
                open(pr);
            } else {
                CartManager.add(pr);
                if (cb != null) cb.run();
                notifyDataSetChanged();
            }
        });

        // Minus in selector
        x.btnMinus.setOnClickListener(v -> {
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
        x.btnDelete.setOnClickListener(v -> {
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

    private void updateQuantityUI(PH x, int qty) {
        if (qty == 0) {
            x.btnAdd.setVisibility(View.VISIBLE);
            x.quantitySelector.setVisibility(View.GONE);
        } else {
            x.btnAdd.setVisibility(View.GONE);
            x.quantitySelector.setVisibility(View.VISIBLE);
            x.tvQuantity.setText(String.valueOf(qty));
            if (qty == 1) {
                x.btnDelete.setVisibility(View.VISIBLE);
                x.btnMinus.setVisibility(View.GONE);
            } else {
                x.btnDelete.setVisibility(View.GONE);
                x.btnMinus.setVisibility(View.VISIBLE);
            }
        }
    }

    void open(Product p) {
        Intent in = new Intent(c, ProductDetailActivity.class);
        in.putExtra("product", p);
        c.startActivity(in);
    }

    public int getItemCount() {
        return list.size();
    }

    public ProductListItem getItem(int p) {
        return list.get(p);
    }

    static class HH extends RecyclerView.ViewHolder {
        TextView t;
        HH(View v) {
            super(v);
            t = v.findViewById(R.id.txtSectionTitle);
        }
    }

    static class PH extends RecyclerView.ViewHolder {
        View root;
        TextView n, d, price, badge;
        TextView btnAdd;
        View quantitySelector;
        ImageView btnDelete;
        TextView btnMinus, btnPlus, tvQuantity;

        PH(View v) {
            super(v);
            root = v.findViewById(R.id.rootProduct);
            n = v.findViewById(R.id.txtName);
            d = v.findViewById(R.id.txtDescription);
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