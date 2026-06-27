package com.example.nofiltercoffee;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrdersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.simple_page);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        ((TextView) findViewById(R.id.txtTitle)).setText("My Orders");

        String data = UserSession.getOrders(this);
        StringBuilder sb = new StringBuilder();

        if (data == null || data.isEmpty()) {
            sb.append("No orders yet.");
        } else {
            long now = System.currentTimeMillis();
            String[] rows = data.split(";;");
            for (int i = rows.length - 1; i >= 0; i--) {
                if (rows[i] == null || rows[i].trim().isEmpty()) continue;
                String[] parts = rows[i].split("\\|");
                if (parts.length >= 4) {
                    String id = parts[0] != null ? parts[0] : "";
                    String pickup = parts[1] != null ? parts[1] : "";
                    String pickupMillisStr = parts[2] != null ? parts[2] : "";
                    String totalStr = parts[3] != null ? parts[3] : "0";

                    long pickupMillis = 0;
                    try {
                        if (!pickupMillisStr.isEmpty()) pickupMillis = Long.parseLong(pickupMillisStr);
                    } catch (NumberFormatException ignored) {}

                    int total = 0;
                    try {
                        if (!totalStr.isEmpty()) total = Integer.parseInt(totalStr);
                    } catch (NumberFormatException ignored) {}

                    if (id.isEmpty() && pickup.isEmpty() && total == 0) continue;

                    sb.append(pickupMillis > now ? "CURRENT ORDER\n" : "PAST ORDER\n");
                    sb.append("Order ID: ").append(id).append("\n");
                    sb.append("Pickup: ").append(pickup).append("\n");
                    sb.append("Total paid: Rs. ").append(total).append("\n\n");
                }
            }
            if (sb.length() == 0) sb.append("No valid orders found.");
        }

        ((TextView) findViewById(R.id.txtBody)).setText(sb.toString());
    }
}