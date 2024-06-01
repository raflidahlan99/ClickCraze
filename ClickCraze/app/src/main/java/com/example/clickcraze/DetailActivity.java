package com.example.clickcraze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.clickcraze.API.ApiService;
import com.example.clickcraze.Model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ImageView back, productImage, cart;
    TextView nama, disc, harga, desc;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        back = findViewById(R.id.back);
        productImage = findViewById(R.id.iv_productDetail);
        cart = findViewById(R.id.cartDetail);
        nama = findViewById(R.id.titleProductDetail);
        disc = findViewById(R.id.discProductDetail);
        harga = findViewById(R.id.priceProductDetail);
        desc = findViewById(R.id.descProductDetail);

        Product product = getIntent().getParcelableExtra("PRODUCT");
        if (product != null) {
            updateUI(product);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart(product);
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateUI(Product product) {
        nama.setText(product.getTitle());
        disc.setText(String.format("Disc: %.2f%%", product.getDiscountPercentage()));
        harga.setText(String.format("$%.2f", product.getPrice()));
        desc.setText(product.getDescription());

        Glide.with(this)
                .load(product.getThumbnail())
                .into(productImage);
    }

    private void addProductToCart(Product product) {
        SharedPreferences sharedPreferences = getSharedPreferences("CART_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("CART_LIST", null);
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        List<Product> cartList = gson.fromJson(json, type);

        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        cartList.add(product);
        String updatedJson = gson.toJson(cartList);
        editor.putString("CART_LIST", updatedJson);
        editor.apply();

        Toast.makeText(this, "Produk Telah di tambahkan ke Cart", Toast.LENGTH_SHORT).show();
    }
}