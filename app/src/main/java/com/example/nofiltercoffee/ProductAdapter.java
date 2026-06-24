package com.example.nofiltercoffee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        h.badge.setText(p.badge);

        if (p.isDeal()) {
            h.originalPrice.setVisibility(View.VISIBLE);
            h.originalPrice.setText("Rs. " + p.originalPrice);
            h.originalPrice.setPaintFlags(h.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            h.price.setText("Rs. " + p.price);
            h.discountBadge.setVisibility(View.VISIBLE);
            h.discountBadge.setText("Save " + p.discountPercent + "%");
        } else {
            h.originalPrice.setVisibility(View.GONE);
            h.discountBadge.setVisibility(View.GONE);
            h.price.setText("from Rs. " + p.price);
        }

        h.root.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductDetailActivity.class);
            i.putExtra("product", p);
            context.startActivity(i);
        });

        h.add.setOnClickListener(v -> {
            CartManager.add(p);
            cartCallback.run();
        });
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
        TextView name, desc, price, badge, add, originalPrice, discountBadge;
        VH(View v) {
            super(v);
            root = v.findViewById(R.id.rootProduct);
            name = v.findViewById(R.id.txtName);
            desc = v.findViewById(R.id.txtDescription);
            price = v.findViewById(R.id.txtPrice);
            badge = v.findViewById(R.id.txtBadge);
            add = v.findViewById(R.id.btnAdd);
            originalPrice = v.findViewById(R.id.txtOriginalPrice);
            discountBadge = v.findViewById(R.id.txtDiscountBadge);
        }
    }
}