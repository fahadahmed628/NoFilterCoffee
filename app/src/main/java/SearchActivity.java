package com.example.nofiltercoffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    EditText searchInput;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    ArrayList<Product> allProducts;
    ArrayList<Product> filteredProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.rvSearchResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load all products from SampleData
        allProducts = SampleData.products();
        filteredProducts = new ArrayList<>(allProducts);

        adapter = new ProductAdapter(this, filteredProducts, () -> { /* optional cart refresh */ });
        recyclerView.setAdapter(adapter);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Search filter
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String query) {
        filteredProducts.clear();
        if (query.isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            String lower = query.toLowerCase();
            for (Product p : allProducts) {
                if (p.name.toLowerCase().contains(lower) || p.description.toLowerCase().contains(lower)) {
                    filteredProducts.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}