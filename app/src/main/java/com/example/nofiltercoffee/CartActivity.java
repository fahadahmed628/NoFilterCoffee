package com.example.nofiltercoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity {
    TextView total, summary, empty, checkout, walletTxt, beansTxt, promo;
    Switch swWallet, swBeans;
    RecyclerView rv;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_cart);
        total = findViewById(R.id.txtTotal);
        summary = findViewById(R.id.txtSummary);
        empty = findViewById(R.id.txtEmptyCart);
        checkout = findViewById(R.id.btnCheckout);
        walletTxt = findViewById(R.id.txtWalletBalance);
        beansTxt = findViewById(R.id.txtBeansBalance);
        promo = findViewById(R.id.txtPromo);
        swWallet = findViewById(R.id.switchWallet);
        swBeans = findViewById(R.id.switchBeans);
        rv = findViewById(R.id.rvCart);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        checkout.setOnClickListener(v -> {
            if (CartManager.getCount() > 0) startActivity(new Intent(this, CheckoutActivity.class));
        });

        // Promo click: if applied, remove it; otherwise go to PromoActivity
        promo.setOnClickListener(v -> {
            if (CartManager.isPromoApplied()) {
                CartManager.removePromo();
                refresh();
            } else {
                startActivity(new Intent(this, PromoActivity.class));
            }
        });

        swWallet.setChecked(CartManager.isUseWallet());
        swBeans.setChecked(CartManager.isUseCoffeeBeans());

        swWallet.setOnCheckedChangeListener((b1, c) -> {
            CartManager.setUseWallet(c);
            refresh();
        });
        swBeans.setOnCheckedChangeListener((b1, c) -> {
            CartManager.setUseCoffeeBeans(c);
            refresh();
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(CartManager.getCart(), this::refresh);
        rv.setAdapter(adapter);

        RecyclerView sug = findViewById(R.id.rvSuggestions);
        sug.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        sug.setAdapter(new SuggestionAdapter(this, SampleData.products(), this::refresh));

        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    void refresh() {
        if (adapter != null) adapter.notifyDataSetChanged();
        int w = UserSession.getWalletBalance(this), beans = UserSession.getCoffeeBeans(this);
        walletTxt.setText("Use Wallet: Rs. " + w);
        beansTxt.setText("Use Coffee Beans: " + beans);

        int subtotal = CartManager.getSubtotal();
        int tax = CartManager.getTax16Percent();
        int promoDisc = CartManager.getPromoDiscount();
        int wallet = CartManager.getWalletDiscount(w, false);
        int beanDisc = CartManager.getCoffeeBeansDiscount(beans, w, false);
        int pay = CartManager.getPayableTotal(w, beans, false);
        int earned = CartManager.getCoffeeBeansEarned();

        String s = "Subtotal: Rs. " + subtotal + "\nTax 16%: Rs. " + tax;
        if (CartManager.isPromoApplied()) {
            s += "\nFIRSTORDER voucher: - Rs. " + promoDisc;
            promo.setText("✓ Voucher applied – tap to remove");
        } else if (CartManager.isPromoRequested()) {
            s += "\nFIRSTORDER voucher selected: add Rs. " + CartManager.getPromoAmountNeeded() + " more";
            promo.setText("Voucher pending – tap to remove");
        } else {
            promo.setText("+ Apply promo");
        }

        if (wallet > 0) s += "\nWallet used: - Rs. " + wallet;
        if (beanDisc > 0) s += "\nCoffee Beans used: - " + beanDisc;
        s += "\nCoffee Beans earned: " + earned + "\nGrand Total: Rs. " + pay;
        summary.setText(s);
        total.setText("Total: Rs. " + pay);

        empty.setVisibility(CartManager.getCount() == 0 ? View.VISIBLE : View.GONE);
        rv.setVisibility(CartManager.getCount() == 0 ? View.GONE : View.VISIBLE);
        checkout.setAlpha(CartManager.getCount() == 0 ? .45f : 1f);
    }
}