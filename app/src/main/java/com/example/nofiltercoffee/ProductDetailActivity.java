package com.example.nofiltercoffee;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    Product product;
    TextView name, desc, size, sweet, ice, food, warn, btn, title;
    String selectedSize = "", selectedSweet = "", selectedIce = "";
    int finalPrice;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_product_detail);

        product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            finish();
            return;
        }

        name = findViewById(R.id.txtName);
        desc = findViewById(R.id.txtDesc);
        size = findViewById(R.id.txtSize);
        sweet = findViewById(R.id.txtSweetness);
        ice = findViewById(R.id.txtIce);
        food = findViewById(R.id.txtFoodNote);
        warn = findViewById(R.id.txtRequiredWarning);
        btn = findViewById(R.id.btnAddToCart);
        title = findViewById(R.id.txtCustomizeTitle);

        name.setText(product.name);
        desc.setText(product.description);
        finalPrice = product.price;

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        setup();
        btn.setOnClickListener(v -> {
            if (ProductUtils.isDrink(product.category)) {
                if (selectedSize.isEmpty() || selectedSweet.isEmpty() || selectedIce.isEmpty()) {
                    warn.setVisibility(View.VISIBLE);
                    return;
                }
                CartManager.add(product, finalPrice, selectedSize + " • " + selectedSweet + " sweetness • " + selectedIce + " ice");
            } else {
                CartManager.add(product);
            }
            finish();
        });
        updateBtn();
    }

    private void setup() {
        if (ProductUtils.isDrink(product.category)) {
            title.setVisibility(View.VISIBLE);
            size.setVisibility(View.VISIBLE);
            sweet.setVisibility(View.VISIBLE);
            ice.setVisibility(View.VISIBLE);
            food.setVisibility(View.GONE);
            size.setText("Select size");
            sweet.setText("Select sweetness");
            ice.setText("Select ice");
            size.setOnClickListener(v -> showSize());
            sweet.setOnClickListener(v -> showSweet());
            ice.setOnClickListener(v -> showIce());
        } else {
            title.setVisibility(View.GONE);
            size.setVisibility(View.GONE);
            sweet.setVisibility(View.GONE);
            ice.setVisibility(View.GONE);
            food.setVisibility(View.VISIBLE);
        }
    }

    private void showSize() {
        String[] a = {"Small (- Rs. 100)", "Regular (+ Rs. 0)", "Large (+ Rs. 150)"};
        new AlertDialog.Builder(this)
                .setTitle("Select size")
                .setItems(a, (d, w) -> {
                    selectedSize = w == 0 ? "Small" : w == 1 ? "Regular" : "Large";
                    size.setText("Size: " + a[w]);
                    warn.setVisibility(View.GONE);
                    updateBtn();
                })
                .show();
    }

    private void showSweet() {
        String[] a = {"No sugar", "Less sweet", "Normal", "Extra sweet"};
        new AlertDialog.Builder(this)
                .setTitle("Select sweetness")
                .setItems(a, (d, w) -> {
                    selectedSweet = a[w];
                    sweet.setText("Sweetness: " + a[w]);
                    warn.setVisibility(View.GONE);
                })
                .show();
    }

    private void showIce() {
        String[] a = {"No ice", "Less ice", "Normal ice", "Extra ice"};
        new AlertDialog.Builder(this)
                .setTitle("Select ice")
                .setItems(a, (d, w) -> {
                    selectedIce = a[w].replace(" ice", "");
                    ice.setText("Ice: " + a[w]);
                    warn.setVisibility(View.GONE);
                })
                .show();
    }

    private void updateBtn() {
        finalPrice = product.price;
        if (selectedSize.equals("Small")) {
            finalPrice = ProductUtils.getSmallPrice(product.price);
        } else if (selectedSize.equals("Large")) {
            finalPrice = ProductUtils.getLargePrice(product.price);
        }
        btn.setText("Add to cart • Rs. " + finalPrice);
    }
}