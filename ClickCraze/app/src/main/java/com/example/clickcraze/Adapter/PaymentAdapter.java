package com.example.clickcraze.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clickcraze.Model.Product;
import com.example.clickcraze.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private Context context;
    private List<Product> paymentList;

    public PaymentAdapter(Context context, List<Product> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentAdapter.PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_payment, parent, false);
        return new PaymentAdapter.PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.PaymentViewHolder holder, int position) {
        Product product = paymentList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvTitle, tvPrice;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.ivProductPayment);
            tvTitle = itemView.findViewById(R.id.tv_namaProductPayment);
            tvPrice = itemView.findViewById(R.id.tv_hargaProductPayment);
        }

        public void bind(Product product) {
            Glide.with(context)
                    .load(product.getThumbnail())
                    .into(ivProduct);
            tvTitle.setText(product.getTitle());
            tvPrice.setText(String.format("$%.2f", product.getPrice()));
        }
    }
}
