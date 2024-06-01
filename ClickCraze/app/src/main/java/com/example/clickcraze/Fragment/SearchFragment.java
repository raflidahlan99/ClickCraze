package com.example.clickcraze.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.clickcraze.API.ApiService;
import com.example.clickcraze.API.ProductResponse;
import com.example.clickcraze.API.RetrofitClient;
import com.example.clickcraze.Adapter.SearchAdapter;
import com.example.clickcraze.Model.Product;
import com.example.clickcraze.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private SearchAdapter searchAdapter;
    private ApiService apiService;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.rv_search);
        searchView = view.findViewById(R.id.search_product);
        progressBar = view.findViewById(R.id.pbSearch);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(searchAdapter);

        // Load all products from API
        loadAllProducts();

        // Set up search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return true;
            }
        });
    }

    private void loadAllProducts() {
        progressBar.setVisibility(View.VISIBLE);
        Call<ProductResponse> call = apiService.getProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body().getProducts();
                } else {
                    // Handle the case where the response is not successful
                    allProducts = new ArrayList<>();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                // Handle failure to load data
                allProducts = new ArrayList<>();
            }
        });
    }

    private void searchProducts(String query) {
        if (TextUtils.isEmpty(query)) {
            searchAdapter.updateList(new ArrayList<>());
            recyclerView.setVisibility(View.GONE);
        } else {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : allProducts) {
                if (product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            searchAdapter.updateList(filteredList);
            recyclerView.setVisibility(filteredList.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }
}