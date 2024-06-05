package com.example.clickcraze.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clickcraze.Activity.DetailActivity;
import com.example.clickcraze.Model.Product;
import com.example.clickcraze.R;
import com.google.gson.Gson;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Product> cartList;

    public CartAdapter(Context context, List<Product> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
        saveCartList();
    }

    private void saveCartList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CART_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartList);
        editor.putString("CART_LIST", json);
        editor.apply();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct, ivSampah;
        private TextView tvTitle, tvPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProductCart);
            tvTitle = itemView.findViewById(R.id.tv_namaProductCart);
            tvPrice = itemView.findViewById(R.id.tv_hargaProductCart);
            ivSampah = itemView.findViewById(R.id.ivSampah);

            ivSampah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteConfirmationDialog(position);
                    }
                }
            });
        }

        public void bind(Product product) {
            Glide.with(context)
                    .load(product.getThumbnail())
                    .into(ivProduct);
            tvTitle.setText(product.getTitle());
            tvPrice.setText(String.format("$%.2f", product.getPrice()));

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

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Hapus Item");
        builder.setMessage("Apakah Anda yakin ingin menghapus item ini dari keranjang?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(position);
                showToast("Item berhasil dihapus");
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
