package com.example.nofiltercoffee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawer;
    NestedScrollView scroll;
    RecyclerView cats, products;
    CategoryAdapter catAdapter;
    SectionedProductAdapter prodAdapter;
    ArrayList<String> categories;
    ArrayList<ProductListItem> productListItems;
    TextView cartCount, cartTotal, drawerBeans;
    LinearLayout bottomCart;
    private boolean isScrollingProgrammatically = false;
    private boolean isUpdatingFromScroll = false;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawerLayout);
        scroll = findViewById(R.id.homeScrollView);
        cats = findViewById(R.id.rvCategories);
        products = findViewById(R.id.rvProducts);
        cartCount = findViewById(R.id.txtCartCount);
        cartTotal = findViewById(R.id.txtCartTotal);
        bottomCart = findViewById(R.id.bottomCart);
        drawerBeans = findViewById(R.id.drawerBeans);

        findViewById(R.id.txtMenu).setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
        findViewById(R.id.drawerClose).setOnClickListener(v -> drawer.closeDrawer(Gravity.LEFT));
        findViewById(R.id.btnHero).setOnClickListener(v -> scrollToCategory("COFFEE"));
        bottomCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        findViewById(R.id.txtSearch).setOnClickListener(v -> {
            Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show();
        });

        setupDrawer();
        setupCategories();
        setupProducts();
        refreshCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCart();
    }

    private void setupDrawer() {
        findViewById(R.id.navOrders).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, OrdersActivity.class));
        });
        findViewById(R.id.navPayment).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, PaymentMethodsActivity.class));
        });
        findViewById(R.id.navProfile).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, ProfileActivity.class));
        });
        findViewById(R.id.navRewards).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, RewardsActivity.class));
        });
        // ✅ Wallet click listener
        findViewById(R.id.navWallet).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, WalletActivity.class));
        });
        findViewById(R.id.navSupport).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, HelpActivity.class));
        });
        findViewById(R.id.navAbout).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, AboutActivity.class));
        });
        findViewById(R.id.navLogout).setOnClickListener(v -> {
            CartManager.clearCart();
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });
    }

    private Product findProductByName(String name) {
        if (name == null) return null;
        for (Product p : SampleData.products()) {
            if (p != null && p.name != null && p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    private void setupCategories() {
        categories = SampleData.categories();
        if (categories == null) categories = new ArrayList<>();
        if (categories.isEmpty()) {
            categories.add("All");
            categories.add("COFFEE");
        }
        if (!categories.contains("All")) categories.add(0, "All");
        cats.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        catAdapter = new CategoryAdapter(categories, this::scrollToCategory);
        cats.setAdapter(catAdapter);
        catAdapter.setSelectedCategory("All");
    }

    private void setupProducts() {
        productListItems = buildProductList();
        if (productListItems == null) productListItems = new ArrayList<>();
        products.setLayoutManager(new LinearLayoutManager(this));
        prodAdapter = new SectionedProductAdapter(this, productListItems, this::refreshCart);
        products.setAdapter(prodAdapter);

        products.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrollingProgrammatically || isUpdatingFromScroll) return;
                if (catAdapter == null) return;

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePos = layoutManager.findFirstVisibleItemPosition();
                if (firstVisiblePos == RecyclerView.NO_POSITION) return;

                String currentCategory = findCategoryAtPosition(firstVisiblePos);
                if (currentCategory == null) currentCategory = "All";

                String selected = catAdapter.getSelectedCategory();
                if (!currentCategory.equals(selected)) {
                    isUpdatingFromScroll = true;
                    catAdapter.setSelectedCategory(currentCategory);
                    int index = categories != null ? categories.indexOf(currentCategory) : -1;
                    if (index >= 0) cats.smoothScrollToPosition(index);
                    isUpdatingFromScroll = false;
                }
            }
        });
    }

    private String findCategoryAtPosition(int position) {
        if (position < 0 || productListItems == null || position >= productListItems.size()) return "All";
        ProductListItem item = productListItems.get(position);
        if (item == null) return "All";
        if (item.type == ProductListItem.TYPE_HEADER) {
            return item.categoryTitle != null ? item.categoryTitle : "All";
        }
        for (int i = position - 1; i >= 0; i--) {
            ProductListItem prev = productListItems.get(i);
            if (prev != null && prev.type == ProductListItem.TYPE_HEADER) {
                return prev.categoryTitle != null ? prev.categoryTitle : "All";
            }
        }
        for (ProductListItem p : productListItems) {
            if (p != null && p.type == ProductListItem.TYPE_HEADER && p.categoryTitle != null) {
                return p.categoryTitle;
            }
        }
        return "All";
    }

    private ArrayList<ProductListItem> buildProductList() {
        ArrayList<ProductListItem> res = new ArrayList<>();
        if (categories == null) return res;
        for (String c : categories) {
            if (c == null || c.equals("All")) continue;
            res.add(new ProductListItem(c));
            for (Product p : SampleData.products()) {
                if (p != null && p.category != null && p.category.equals(c)) {
                    res.add(new ProductListItem(p));
                }
            }
        }
        return res;
    }

    private void scrollToCategory(String categoryName) {
        if (categoryName == null) categoryName = "All";
        if (categoryName.equals("All")) {
            isScrollingProgrammatically = true;
            products.smoothScrollToPosition(0);
            new Handler().postDelayed(() -> isScrollingProgrammatically = false, 400);
            return;
        }
        if (catAdapter == null) return;

        isScrollingProgrammatically = true;
        int targetPos = 0;
        for (int i = 0; i < productListItems.size(); i++) {
            ProductListItem item = productListItems.get(i);
            if (item != null && item.type == ProductListItem.TYPE_HEADER && categoryName.equals(item.categoryTitle)) {
                targetPos = i;
                break;
            }
        }
        catAdapter.setSelectedCategory(categoryName);
        int index = categories != null ? categories.indexOf(categoryName) : -1;
        if (index >= 0) cats.smoothScrollToPosition(index);
        final int finalPos = targetPos;
        products.post(() -> {
            ((LinearLayoutManager) products.getLayoutManager()).scrollToPositionWithOffset(finalPos, 0);
            new Handler().postDelayed(() -> isScrollingProgrammatically = false, 400);
        });
    }

    private void refreshCart() {
        int count = CartManager.getCount();
        cartCount.setText(String.valueOf(count));
        cartTotal.setText("Rs. " + CartManager.getTotal());
        drawerBeans.setVisibility(View.GONE);
        bottomCart.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
    }
}