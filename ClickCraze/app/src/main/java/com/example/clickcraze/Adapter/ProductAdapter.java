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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public static List<Product> productList;
    private Context context;

    public ProductAdapter (Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void addProducts(List<Product> products){
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public class
    ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProduct;
        private TextView tvTitle, tvPrice, tvDisc, tvRate, tvStok;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.iv_product);
            tvTitle = itemView.findViewById(R.id.titleProduct);
            tvDisc = itemView.findViewById(R.id.discProduct);
            tvPrice = itemView.findViewById(R.id.priceProduct);
            tvRate = itemView.findViewById(R.id.rateProduct);
            tvStok = itemView.findViewById(R.id.stockProduct);
        }

        public void bind(Product product){
            Glide.with(itemView.getContext())
                    .load(product.getThumbnail())
                    .into(ivProduct);
            tvTitle.setText(product.getTitle());
            tvDisc.setText(String.format("Disc: %.2f%%", product.getDiscountPercentage()));
            tvPrice.setText(String.format("$%.2f", product.getPrice()));
            tvRate.setText(String.format("⭐ %.1f", product.getRating()));
            tvStok.setText("Stok: " + String.valueOf(product.getStock()));

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
