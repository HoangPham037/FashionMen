package com.android45.fashionmen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.fashionmen.EventBus.TinhTongEvent;
import com.android45.fashionmen.Medel.GioHang;
import com.android45.fashionmen.Interface.IImgClickListiner;
import com.android45.fashionmen.R;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHoder> {

    List<GioHang> listGioHang;
    Context context;

    public GioHangAdapter(List<GioHang> listGioHang, Context context) {
        this.listGioHang = listGioHang;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        GioHang gioHang = listGioHang.get(position);

        holder.itemCartTen.setText(gioHang.getName());
        holder.itemCartSoluong.setText(gioHang.getAmount() + " ");
        Glide.with(context).load(gioHang.getImgSp()).into(holder.itemCartIMG);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.itemCartGiaSp1.setText(decimalFormat.format(gioHang.getPrice()));
        long gia = gioHang.getAmount() * gioHang.getPrice();
        holder.itemCartGiaSp2.setText(decimalFormat.format(gia));
        holder.setClickListiner(new IImgClickListiner() {
            @Override
            public void onClickImg(View view, int pos, int giatri) {
                if (giatri == 1){
                    if (listGioHang.get(pos).getAmount() > 1){
                        int soluongmoi = listGioHang.get(pos).getAmount()- 1;
                        listGioHang.get(pos).setAmount(soluongmoi);
                    }
                }else if(giatri == 2){
                    if (listGioHang.get(pos).getAmount() < 11){
                        int soluongmoi = listGioHang.get(pos).getAmount()+1;
                        listGioHang.get(pos).setAmount(soluongmoi);
                    }
                }
                holder.itemCartSoluong.setText(listGioHang.get(pos).getAmount() + " ");
                long gia = listGioHang.get(pos).getAmount() * listGioHang.get(pos).getPrice();
                holder.itemCartGiaSp2.setText(decimalFormat.format(gia));
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listGioHang.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemCartIMG, itemCart_tru, itemCart_cong, itemCart_bang;
        TextView itemCartGiaSp1, itemCartGiaSp2, itemCartSoluong,itemCartTen;
        IImgClickListiner clickListiner;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            itemCartIMG = itemView.findViewById(R.id.itemCartIMG);
            itemCart_tru= itemView.findViewById(R.id.itemcart_tru);
            itemCart_cong = itemView.findViewById(R.id.itemcart_cong);
            itemCart_bang = itemView.findViewById(R.id.itemCart_bang);
            itemCartGiaSp1 = itemView.findViewById(R.id.itemCart_giaSp1);
            itemCartGiaSp2 = itemView.findViewById(R.id.itemCart_giaSp2);
            itemCartSoluong = itemView.findViewById(R.id.itemCart_sl);
            itemCartTen = itemView.findViewById(R.id.itemcart_name);

            //even click
            itemCart_cong.setOnClickListener(this);
            itemCart_tru.setOnClickListener(this);
        }

        public void setClickListiner(IImgClickListiner clickListiner) {
            this.clickListiner = clickListiner;
        }

        @Override
        public void onClick(View v) {
            if (v == itemCart_tru){
                clickListiner.onClickImg(v,getAdapterPosition(),1);
            }else if(v == itemCart_cong){
                clickListiner.onClickImg(v,getAdapterPosition(),2);
            }
        }
    }
}
