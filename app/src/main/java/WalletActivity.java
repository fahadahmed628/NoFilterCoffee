package com.example.nofiltercoffee;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity {

    private TextView txtBalance, txtAmountError, txtCardError;
    private EditText edtAmount, edtCardHolder, edtCardNumber, edtExpiry, edtCvv;
    private TextView btnTopUp;
    private int currentBalance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Initialize views
        txtBalance = findViewById(R.id.txtWalletBalance);
        edtAmount = findViewById(R.id.edtTopUpAmount);
        txtAmountError = findViewById(R.id.txtAmountError);
        edtCardHolder = findViewById(R.id.edtCardHolder);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtExpiry = findViewById(R.id.edtExpiry);
        edtCvv = findViewById(R.id.edtCvv);
        txtCardError = findViewById(R.id.txtCardError);
        btnTopUp = findViewById(R.id.btnTopUp);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Load current balance from UserSession
        currentBalance = UserSession.getWalletBalance(this);
        txtBalance.setText("Rs. " + currentBalance);

        // Card formatting (same as CheckoutActivity)
        setupCardFormatters();

        // Top Up button
        btnTopUp.setOnClickListener(v -> performTopUp());
    }

    private void setupCardFormatters() {
        // Card number formatting (add spaces every 4 digits)
        edtCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable e) {
                if (isFormatting) return;
                isFormatting = true;
                String raw = e.toString().replaceAll("\\s", "");
                if (raw.length() > 16) raw = raw.substring(0, 16);
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i > 0 && i % 4 == 0) formatted.append(" ");
                    formatted.append(raw.charAt(i));
                }
                edtCardNumber.setText(formatted.toString());
                edtCardNumber.setSelection(edtCardNumber.getText().length());
                isFormatting = false;
            }
        });

        // Expiry formatting (MM/YY)
        edtExpiry.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable e) {
                if (isFormatting) return;
                isFormatting = true;
                String raw = e.toString().replace("/", "");
                if (raw.length() > 4) raw = raw.substring(0, 4);
                String formatted = raw.length() > 2 ? raw.substring(0, 2) + "/" + raw.substring(2) : raw;
                edtExpiry.setText(formatted);
                edtExpiry.setSelection(edtExpiry.getText().length());
                isFormatting = false;
            }
        });
    }

    private void performTopUp() {
        // Validate amount
        String amountStr = edtAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            showAmountError("Please enter an amount.");
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            showAmountError("Please enter a valid number.");
            return;
        }
        if (amount < 100 || amount > 10000) {
            showAmountError("Amount must be between Rs. 100 and Rs. 10,000.");
            return;
        }

        // Validate card details
        if (!validCard()) return;

        // Simulate payment processing (optional)
        // Update wallet balance
        int newBalance = currentBalance + amount;
        UserSession.setWalletBalance(this, newBalance);
        currentBalance = newBalance;
        txtBalance.setText("Rs. " + currentBalance);
        Toast.makeText(this, "Wallet topped up successfully!", Toast.LENGTH_SHORT).show();

        // Clear form
        edtAmount.setText("");
        edtCardHolder.setText("");
        edtCardNumber.setText("");
        edtExpiry.setText("");
        edtCvv.setText("");
        txtCardError.setVisibility(View.GONE);
        txtAmountError.setVisibility(View.GONE);
    }

    private void showAmountError(String msg) {
        txtAmountError.setText(msg);
        txtAmountError.setVisibility(View.VISIBLE);
    }

    private boolean validCard() {
        String holder = edtCardHolder.getText().toString().trim();
        String number = edtCardNumber.getText().toString().replaceAll("\\s", "");
        String expiry = edtExpiry.getText().toString();
        String cvv = edtCvv.getText().toString();

        if (holder.length() < 3) {
            txtCardError.setText("Enter valid card holder name.");
            txtCardError.setVisibility(View.VISIBLE);
            return false;
        }
        if (!number.matches("\\d{16}")) {
            txtCardError.setText("Card number must be exactly 16 digits.");
            txtCardError.setVisibility(View.VISIBLE);
            return false;
        }
        if (!expiry.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            txtCardError.setText("Expiry must be like 07/27.");
            txtCardError.setVisibility(View.VISIBLE);
            return false;
        }
        if (!cvv.matches("\\d{3}")) {
            txtCardError.setText("CVV must be 3 digits.");
            txtCardError.setVisibility(View.VISIBLE);
            return false;
        }
        txtCardError.setVisibility(View.GONE);
        return true;
    }
}