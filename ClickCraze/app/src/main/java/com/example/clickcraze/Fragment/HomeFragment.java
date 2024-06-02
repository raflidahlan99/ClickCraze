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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
    LottieAnimationView loading;
    LinearLayout llNoInt;
    TextView tvNoInt;
    ImageView ivNoInt;
    Button btnNoInt;


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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loading = view.findViewById(R.id.loadinghome);
        llNoInt = view.findViewById(R.id.llNoIntHome);
        tvNoInt = view.findViewById(R.id.tvNoIntHome);
        ivNoInt = view.findViewById(R.id.ivNoIntHome);
        btnNoInt = view.findViewById(R.id.btnNoIntHome);

        btnNoInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                llNoInt.setVisibility(View.GONE);
                loadData();
            }
        });

        productAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        loading.setVisibility(View.VISIBLE);
        loadData();
    }

    private void loadData(){

        // Tampilkan loading indicator sebelum melakukan pemanggilan API
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE); // Sembunyikan RecyclerView sementara

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
                                // Sembunyikan loading indicator setelah pemanggilan API selesai
                                loading.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE); // Tampilkan kembali RecyclerView

                                if (response.isSuccessful() && response.body() != null) {
                                    List<Product> products = response.body().getProducts();
                                    productAdapter.addProducts(products);
                                } else {
                                    llNoInt.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Sembunyikan loading indicator jika terjadi kesalahan saat pemanggilan API
                                llNoInt.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
    }
}