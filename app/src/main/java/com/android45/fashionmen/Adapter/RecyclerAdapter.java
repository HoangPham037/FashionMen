package com.android45.fashionmen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.fashionmen.Activity.CTSPActivity;
import com.android45.fashionmen.Medel.Products;
import com.android45.fashionmen.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHoder> {
    List<Products> listProduct;
    Context context;

    public RecyclerAdapter(List<Products> listProduct, Context context) {
        this.listProduct = listProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ic_item_recycler, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRecyclerItemClick((RecyclerView) parent, v);
            }
        });
        return new ViewHoder(view);
    }

    public void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildAdapterPosition(itemView);
        Products products = this.listProduct.get(itemPosition);
        Intent intent = new Intent(context, CTSPActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", products);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void searchSp(String str){
        str = str.toLowerCase();
        int k = 0;
        for (int i = 0; i < listProduct.size(); i++){
            Products products = listProduct.get(i);
            String tenSP = products.getName().toLowerCase();
            if(tenSP.indexOf(str) >= 0 ){
                listProduct.set(i, listProduct.get(k));
                listProduct.set(k,products);
                k++;
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final Products products = listProduct.get(position);
        holder.name.setText(products.getName());
        holder.describe.setText(products.getDescribe());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText("Giá: " + decimalFormat.format(products.getPrice()) + "VND");
        Glide.with(context)
                .load(listProduct.get(position).getImage())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, describe;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageSp);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
            describe = itemView.findViewById(R.id.tvDescribe);
        }
    }
}
