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
        return t == 0
                ? new HH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_section_header, p, false))
                : new PH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_product, p, false));
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
        x.badge.setText(pr.badge);  // "Deal" for deals, or original badge

        // Handle discount
        if (pr.isDeal()) {
            x.originalPrice.setVisibility(View.VISIBLE);
            x.originalPrice.setText("Rs. " + pr.originalPrice);
            x.originalPrice.setPaintFlags(x.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            x.price.setText("Rs. " + pr.price);
            x.discountBadge.setVisibility(View.VISIBLE);
            x.discountBadge.setText("Save " + pr.discountPercent + "%");
        } else {
            x.originalPrice.setVisibility(View.GONE);
            x.discountBadge.setVisibility(View.GONE);
            x.price.setText("from Rs. " + pr.price);
        }

        x.root.setOnClickListener(v -> open(pr));
        x.add.setOnClickListener(v -> {
            if (ProductUtils.isDrink(pr.category)) {
                open(pr);
            } else {
                CartManager.add(pr);
                if (cb != null) cb.run();
            }
        });
    }

    void open(Product p) {
        Intent in = new Intent(c, ProductDetailActivity.class);
        in.putExtra("product", p);
        c.startActivity(in);
    }

    public int getItemCount() { return list.size(); }
    public ProductListItem getItem(int p) { return list.get(p); }

    static class HH extends RecyclerView.ViewHolder {
        TextView t;
        HH(View v) { super(v); t = v.findViewById(R.id.txtSectionTitle); }
    }

    static class PH extends RecyclerView.ViewHolder {
        View root;
        TextView n, d, price, badge, add, originalPrice, discountBadge;
        PH(View v) {
            super(v);
            root = v.findViewById(R.id.rootProduct);
            n = v.findViewById(R.id.txtName);
            d = v.findViewById(R.id.txtDescription);
            price = v.findViewById(R.id.txtPrice);
            badge = v.findViewById(R.id.txtBadge);
            add = v.findViewById(R.id.btnAdd);
            originalPrice = v.findViewById(R.id.txtOriginalPrice);
            discountBadge = v.findViewById(R.id.txtDiscountBadge);
        }
    }
}