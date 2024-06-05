package com.example.clickcraze.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.clickcraze.Adapter.CartAdapter;
import com.example.clickcraze.Model.Product;
import com.example.clickcraze.Activity.PaymentActivity;
import com.example.clickcraze.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Product> cartList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvNoCart = view.findViewById(R.id.tvNoCart);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);
        recyclerView = view.findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCartItems(tvNoCart, btnCheckout);

        cartAdapter = new CartAdapter(getContext(), cartList);
        recyclerView.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartList != null && !cartList.isEmpty()) {
                    // Kirim data cartList ke PaymentActivity
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("CART_LIST", new ArrayList<>(cartList));
                    startActivity(intent);
                }
            }
        });
    }

    private void loadCartItems(TextView tvNoCart, Button btnCheckout) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CART_PREF", MODE_PRIVATE);
        String json = sharedPreferences.getString("CART_LIST", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        cartList = gson.fromJson(json, type);

        if (cartList == null || cartList.isEmpty()) {
            cartList = new ArrayList<>();
            tvNoCart.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.GONE);
        } else {
            tvNoCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnCheckout.setVisibility(View.VISIBLE);
        }
    }
}