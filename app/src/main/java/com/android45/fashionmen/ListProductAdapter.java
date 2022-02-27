package com.android45.fashionmen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.fashionmen.Medel.ListsProducts;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHoder> {
    List<ListsProducts> listProduct;
    Context context;

    public ListProductAdapter(List<ListsProducts> listProduct, Context context) {
        this.listProduct = listProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ic_item_listproduct,parent,false);
        ViewHoder viewHoder = new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final ListsProducts list = listProduct.get(position);
        holder.tvNameList.setText(list.getImg());
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvNameList;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvNameList = itemView.findViewById(R.id.tvNameList);
        }
    }
}
