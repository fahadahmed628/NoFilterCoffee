package com.example.nofiltercoffee;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PromoActivity extends AppCompatActivity {
    TextView status, btn, removeBtn;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_promo);
        status = findViewById(R.id.txtStatus);
        btn = findViewById(R.id.btnApply);
        removeBtn = findViewById(R.id.btnRemovePromo);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btn.setOnClickListener(v -> {
            CartManager.requestPromo();
            update();
            // Return to cart after applying
            finish();
        });

        removeBtn.setOnClickListener(v -> {
            CartManager.removePromo();
            update();
            finish(); // go back to cart
        });

        update();
    }

    void update() {
        if (CartManager.isPromoApplied()) {
            status.setText("Voucher applied. Rs. 500 will be deducted.");
            btn.setVisibility(TextView.GONE);
            removeBtn.setVisibility(TextView.VISIBLE);
            removeBtn.setText("Remove Voucher");
        } else if (CartManager.isPromoRequested()) {
            status.setText("Voucher selected. Add Rs. " + CartManager.getPromoAmountNeeded() + " more to activate it automatically.");
            btn.setVisibility(TextView.GONE);
            removeBtn.setVisibility(TextView.VISIBLE);
            removeBtn.setText("Remove Voucher");
        } else {
            status.setText("Current subtotal: Rs. " + CartManager.getSubtotal() + " – minimum Rs. 1500 required.");
            btn.setVisibility(TextView.VISIBLE);
            removeBtn.setVisibility(TextView.GONE);
        }
    }
}