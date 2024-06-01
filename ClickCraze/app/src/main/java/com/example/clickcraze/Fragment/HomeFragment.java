package com.example.clickcraze.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.clickcraze.API.ApiService;
import com.example.clickcraze.API.ProductResponse;
import com.example.clickcraze.API.RetrofitClient;
import com.example.clickcraze.Adapter.ProductAdapter;
import com.example.clickcraze.Model.Product;
import com.example.clickcraze.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ApiService apiService;
    private ProductAdapter productAdapter;
    ProgressBar pbHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerView = view.findViewById(R.id.rv_product);
        pbHome = view.findViewById(R.id.pbHome);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        loadData();
    }

    private void loadData(){
        pbHome.setVisibility(View.VISIBLE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<ProductResponse> call = apiService.getProducts();
                call.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pbHome.setVisibility(View.GONE);

                                if (response.isSuccessful() && response.body() != null) {
                                    List<Product> products = response.body().getProducts();
                                    productAdapter.addProducts(products);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pbHome.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
    }
}