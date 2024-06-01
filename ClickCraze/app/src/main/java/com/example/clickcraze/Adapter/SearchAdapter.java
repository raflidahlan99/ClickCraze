package com.example.clickcraze.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clickcraze.DetailActivity;
import com.example.clickcraze.Model.Product;
import com.example.clickcraze.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Product> productList;
    private Context context;

    public SearchAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newProductList) {
        productList = newProductList;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvHarga;
        private ImageView ivSearch;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_namaProduct);
            tvHarga = itemView.findViewById(R.id.tv_hargaProduct);
            ivSearch = itemView.findViewById(R.id.iv_productsearch);
        }

        public void bind(Product product) {
            tvNama.setText(product.getTitle());
            tvHarga.setText(String.format("$%.2f", product.getPrice()));
            Glide.with(itemView.getContext())
                    .load(product.getThumbnail())
                    .into(ivSearch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("PRODUCT", product);
                    context.startActivity(intent);
                }
            });
        }
    }
}
