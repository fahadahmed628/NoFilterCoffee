package com.example.nofiltercoffee;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView searchResults;
    private ProductAdapter adapter;
    private ArrayList<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        searchInput = findViewById(R.id.edtSearch);
        searchResults = findViewById(R.id.rvSearchResults);

        // Load all products from SampleData
        allProducts = SampleData.products();

        // Setup RecyclerView
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, new ArrayList<>(allProducts), () -> {});
        searchResults.setAdapter(adapter);

        // Search listener
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String query) {
        ArrayList<Product> filtered = new ArrayList<>();
        if (query.trim().isEmpty()) {
            filtered.addAll(allProducts);
        } else {
            String lowerQuery = query.toLowerCase(Locale.getDefault());
            for (Product p : allProducts) {
                if (p.name.toLowerCase(Locale.getDefault()).contains(lowerQuery) ||
                        p.description.toLowerCase(Locale.getDefault()).contains(lowerQuery) ||
                        p.category.toLowerCase(Locale.getDefault()).contains(lowerQuery)) {
                    filtered.add(p);
                }
            }
        }
        adapter.update(filtered);
    }
}