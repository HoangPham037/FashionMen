package com.android45.fashionmen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.fashionmen.Medel.ListsProducts;
import com.android45.fashionmen.R;

import java.util.List;

public class ListsProductsAdapter extends RecyclerView.Adapter<ListsProductsAdapter.ViewHorder> {

    List<ListsProducts> list;
    Context context;

    public ListsProductsAdapter(List<ListsProducts> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHorder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_product,parent,false);
        return new ViewHorder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHorder holder, int position) {
        ListsProducts products = list.get(position);
        holder.imgList.setImageResource(products.getImg());
        holder.tvNameList.setText(products.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHorder extends RecyclerView.ViewHolder {
        ImageView imgList;
        TextView tvNameList;
        public ViewHorder(@NonNull View itemView) {
            super(itemView);
            imgList= itemView.findViewById(R.id.img_list);
            tvNameList = itemView.findViewById(R.id.tvNameList);
        }
    }
}
