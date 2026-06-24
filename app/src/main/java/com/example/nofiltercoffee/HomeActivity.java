package com.example.nofiltercoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawer;
    NestedScrollView scroll;
    RecyclerView cats, products;
    CategoryAdapter catAdapter;
    SectionedProductAdapter prodAdapter;
    ArrayList<String> categories;
    TextView cartCount, cartTotal, drawerBeans;
    LinearLayout bottomCart;
    boolean categoryButtonScrolling = false;

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
        findViewById(R.id.btnHero).setOnClickListener(v -> scrollToCategory("Iced Coffee"));
        bottomCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        // Search icon click
        findViewById(R.id.txtSearch).setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        setupDrawer();
        // Quick actions removed – no setupQuick() call
        setupCategories();
        setupProducts();
        refreshCart();

        // FCM token registration
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseFirestore.getInstance().collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .update("fcmToken", token);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCart();
    }

    void setupDrawer() {
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
        // Rewards in drawer
        findViewById(R.id.navRewards).setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(this, RewardsActivity.class));
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
            UserSession.logout(this);
            CartManager.clearCart();
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });
    }

    void setupCategories() {
        categories = SampleData.categories();
        cats.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        catAdapter = new CategoryAdapter(categories, this::scrollToCategory);
        cats.setAdapter(catAdapter);
    }

    void setupProducts() {
        products.setLayoutManager(new LinearLayoutManager(this));
        prodAdapter = new SectionedProductAdapter(this, buildList(), this::refreshCart);
        products.setAdapter(prodAdapter);
    }

    ArrayList<ProductListItem> buildList() {
        ArrayList<ProductListItem> res = new ArrayList<>();
        for (String c : categories) {
            if (c.equals("All")) continue;
            res.add(new ProductListItem(c));
            for (Product p : SampleData.products())
                if (p.category.equals(c))
                    res.add(new ProductListItem(p));
        }
        return res;
    }

    void scrollToCategory(String c) {
        catAdapter.setSelectedCategory(c);
        int pos = c.equals("All") ? 0 : findHeader(c);
        cats.smoothScrollToPosition(Math.max(categories.indexOf(c), 0));
        products.post(() -> {
            RecyclerView.ViewHolder h = products.findViewHolderForAdapterPosition(pos);
            if (h == null) {
                products.scrollToPosition(pos);
                products.postDelayed(() -> scrollToCategory(c), 80);
                return;
            }
            int y = products.getTop() + h.itemView.getTop() - 12;
            scroll.smoothScrollTo(0, Math.max(y, 0));
        });
    }

    int findHeader(String c) {
        for (int i = 0; i < prodAdapter.getItemCount(); i++) {
            ProductListItem it = prodAdapter.getItem(i);
            if (it.type == 0 && it.categoryTitle.equals(c)) return i;
        }
        return 0;
    }

    void refreshCart() {
        int count = CartManager.getCount();
        cartCount.setText("" + count);
        cartTotal.setText("Rs. " + CartManager.getTotal());
        drawerBeans.setText("☕ " + UserSession.getCoffeeBeans(this) + " Coffee Beans");
        bottomCart.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
    }
}