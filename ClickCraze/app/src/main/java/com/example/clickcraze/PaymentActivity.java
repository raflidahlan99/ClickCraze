package com.example.clickcraze;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clickcraze.Adapter.CartAdapter;
import com.example.clickcraze.Adapter.PaymentAdapter;
import com.example.clickcraze.Fragment.CartFragment;
import com.example.clickcraze.Model.Product;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalPriceTextView;
    private RecyclerView rvPayment;
    Button btnPay;
    ImageView backPayment;
    private PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        totalPriceTextView = findViewById(R.id.total);
        rvPayment = findViewById(R.id.rv_payment);
        btnPay = findViewById(R.id.btnPay);
        backPayment = findViewById(R.id.backPayment);

        backPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        rvPayment.setLayoutManager(new LinearLayoutManager(this));

        // Ambil data cartList dari intent
        List<Product> paymentList = getIntent().getParcelableArrayListExtra("CART_LIST");

        // Buat dan atur adapter untuk RecyclerView
        paymentAdapter = new PaymentAdapter(this, paymentList);
        rvPayment.setAdapter(paymentAdapter);

        // Hitung total harga dari semua produk di dalam cartList
        double totalPrice = calculateTotalPrice(paymentList);

        // Tampilkan total harga di TextView
        totalPriceTextView.setText(String.format("Total Price : $%.2f", totalPrice));
    }

    // Method untuk menghitung total harga dari semua produk di dalam cartList
    private double calculateTotalPrice(List<Product> cartList) {
        double totalPrice = 0.0;
        if (cartList != null) {
            for (Product product : cartList) {
                totalPrice += product.getPrice();
            }
        }
        return totalPrice;
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Konfirmasi Pembayaran");
        builder.setMessage("Apakah Anda yakin ingin melanjutkan pembayaran?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Menghapus semua data dari keranjang dan menampilkan pesan
                clearCart();
                Toast.makeText(PaymentActivity.this, "Terima kasih telah berbelanja", Toast.LENGTH_SHORT).show();
                // Kembali ke HomeActivity setelah menghapus keranjang
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void clearCart() {
        // Menghapus semua data dari SharedPreferences (keranjang)
        SharedPreferences sharedPreferences = getSharedPreferences("CART_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        finish();
    }
}