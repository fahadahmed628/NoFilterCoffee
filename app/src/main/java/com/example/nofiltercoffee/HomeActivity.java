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
import java.util.HashMap;
import java.util.Map;

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

    // ── Scroll-sync guards ──────────────────────────────────────────────────
    // True while a chip tap is animating the product list — suppresses scrollspy.
    private boolean isTapScrolling = false;

    // ── Scrollspy: cached Y-offsets for each category header ───────────────
    // Populated after the product RecyclerView has been laid out.
    private final Map<String, Integer> categoryOffsets = new HashMap<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home);

        drawer      = findViewById(R.id.drawerLayout);
        scroll      = findViewById(R.id.homeScrollView);
        cats        = findViewById(R.id.rvCategories);
        products    = findViewById(R.id.rvProducts);
        cartCount   = findViewById(R.id.txtCartCount);
        cartTotal   = findViewById(R.id.txtCartTotal);
        bottomCart  = findViewById(R.id.bottomCart);
        drawerBeans = findViewById(R.id.drawerBeans);

        findViewById(R.id.txtMenu).setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
        findViewById(R.id.drawerClose).setOnClickListener(v -> drawer.closeDrawer(Gravity.LEFT));
        findViewById(R.id.btnHero).setOnClickListener(v -> scrollToCategory("COFFEE"));
        bottomCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        findViewById(R.id.txtSearch).setOnClickListener(v ->
                Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show());

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

    // ── Drawer ──────────────────────────────────────────────────────────────

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

    // ── Category chips ──────────────────────────────────────────────────────

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

    // ── Product list ────────────────────────────────────────────────────────

    private void setupProducts() {
        productListItems = buildProductList();
        if (productListItems == null) productListItems = new ArrayList<>();

        products.setLayoutManager(new LinearLayoutManager(this));
        prodAdapter = new SectionedProductAdapter(this, productListItems, this::refreshCart);
        products.setAdapter(prodAdapter);

        // Build Y-offset cache after layout is complete, then attach scrollspy.
        products.getViewTreeObserver().addOnGlobalLayoutListener(this::buildOffsetCache);

        attachScrollspy();
    }

    /**
     * Walk every child view of the product RecyclerView and record the top-Y
     * (relative to the NestedScrollView) for every category-header item.
     *
     * We add products.getTop() because the RecyclerView sits below the hero
     * section inside the NestedScrollView's content LinearLayout.
     */
    private void buildOffsetCache() {
        categoryOffsets.clear();
        LinearLayoutManager lm = (LinearLayoutManager) products.getLayoutManager();
        if (lm == null || productListItems == null) return;

        int rvTop = products.getTop(); // offset of RecyclerView inside NestedScrollView content

        for (int i = 0; i < productListItems.size(); i++) {
            ProductListItem item = productListItems.get(i);
            if (item == null || item.type != ProductListItem.TYPE_HEADER
                    || item.categoryTitle == null) continue;

            // Force-measure the item view if it isn't attached yet.
            View child = lm.findViewByPosition(i);
            if (child != null) {
                categoryOffsets.put(item.categoryTitle, rvTop + child.getTop());
            } else {
                // Fallback: estimate offset from adapter position heights.
                // We'll refresh the cache once the view scrolls into existence.
            }
        }
    }

    /**
     * Attach a scroll listener to the NestedScrollView (the true scroll container).
     * The product RecyclerView has nestedScrollingEnabled=false, so all scroll
     * events originate here — NOT on the RecyclerView.
     */
    private void attachScrollspy() {
        scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                    // While a chip tap is programmatically scrolling, ignore scroll events.
                    if (isTapScrolling) return;
                    if (catAdapter == null || categories == null) return;

                    // Rebuild offset cache on every scroll pass to capture newly laid-out
                    // headers (handles the initial render where not all items are visible yet).
                    buildOffsetCache();

                    String activeCategory = resolveActiveCategory(scrollY);
                    if (activeCategory == null) return;

                    if (!activeCategory.equals(catAdapter.getSelectedCategory())) {
                        catAdapter.setSelectedCategory(activeCategory);
                        int chipIndex = categories.indexOf(activeCategory);
                        if (chipIndex >= 0) {
                            cats.smoothScrollToPosition(chipIndex);
                        }
                    }
                });
    }

    /**
     * Given the current NestedScrollView scroll offset, return the category
     * whose header is at or above the top of the viewport.
     *
     * We walk the categories in reverse order and return the first one whose
     * cached Y-offset is ≤ scrollY + a small look-ahead (48 dp) so the chip
     * activates just before the header reaches the very top.
     */
    private String resolveActiveCategory(int scrollY) {
        if (categories == null || categoryOffsets.isEmpty()) return "All";

        int lookAhead = (int) (48 * getResources().getDisplayMetrics().density);
        String best = "All";

        for (int i = categories.size() - 1; i >= 0; i--) {
            String cat = categories.get(i);
            if (cat == null || cat.equals("All")) continue;
            Integer offset = categoryOffsets.get(cat);
            if (offset != null && offset <= scrollY + lookAhead) {
                best = cat;
                break;
            }
        }
        return best;
    }

    // ── Smooth scroll anchor (chip tap → product list) ──────────────────────

    /**
     * Called when the user taps a category chip.
     * Updates the chip highlight, scrolls the chip row, then smoothly scrolls
     * the NestedScrollView to the target category section.
     */
    private void scrollToCategory(String categoryName) {
        if (categoryName == null) categoryName = "All";
        final String finalCategory = categoryName; // effectively-final capture for lambdas

        // Update chip immediately for instant visual feedback.
        if (catAdapter != null) {
            catAdapter.setSelectedCategory(categoryName);
            int chipIndex = categories != null ? categories.indexOf(categoryName) : -1;
            if (chipIndex >= 0) cats.smoothScrollToPosition(chipIndex);
        }

        if (categoryName.equals("All")) {
            // "All" scrolls back to the very top of the page.
            isTapScrolling = true;
            scroll.smoothScrollTo(0, 0);
            clearTapLock(500);
            return;
        }

        // Rebuild the offset cache in case it's stale, then scroll.
        buildOffsetCache();
        Integer targetY = categoryOffsets.get(categoryName);

        if (targetY != null) {
            isTapScrolling = true;
            scroll.smoothScrollTo(0, targetY);
            clearTapLock(600);
        } else {
            // Offset not yet in cache — scroll to the adapter position as a fallback
            // and retry after layout.
            int targetPos = findHeaderPosition(finalCategory);
            if (targetPos >= 0) {
                products.scrollToPosition(targetPos);
                // Give the layout pass time to happen, then rebuild cache and scroll.
                new Handler().postDelayed(() -> {
                    buildOffsetCache();
                    Integer y = categoryOffsets.get(finalCategory);
                    if (y != null) {
                        isTapScrolling = true;
                        scroll.smoothScrollTo(0, y);
                        clearTapLock(600);
                    }
                }, 100);
            }
        }
    }

    /** Releases the tap-scroll guard after [delayMs] milliseconds. */
    private void clearTapLock(long delayMs) {
        new Handler().postDelayed(() -> isTapScrolling = false, delayMs);
    }

    /** Returns the adapter position of the header for [categoryName], or -1. */
    private int findHeaderPosition(String categoryName) {
        if (productListItems == null || categoryName == null) return -1;
        for (int i = 0; i < productListItems.size(); i++) {
            ProductListItem item = productListItems.get(i);
            if (item != null && item.type == ProductListItem.TYPE_HEADER
                    && categoryName.equals(item.categoryTitle)) {
                return i;
            }
        }
        return -1;
    }

    // ── Data helpers ────────────────────────────────────────────────────────

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

    private void refreshCart() {
        int count = CartManager.getCount();
        cartCount.setText(String.valueOf(count));
        cartTotal.setText("Rs. " + CartManager.getTotal());
        drawerBeans.setVisibility(View.GONE);
        bottomCart.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
    }
}