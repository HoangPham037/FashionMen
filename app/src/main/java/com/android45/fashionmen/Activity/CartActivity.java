package com.android45.fashionmen.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.fashionmen.EventBus.TinhTongEvent;
import com.android45.fashionmen.Adapter.GioHangAdapter;
import com.android45.fashionmen.R;
import com.android45.fashionmen.Util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    TextView txtGioHangTrong, txtTongTien;
    Button btnMuaHang;
    RecyclerView recyclerView;
    Toolbar toolbar;
    GioHangAdapter gioHangAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initControl();
        tinhtongtien();
        btnMuaHang.setOnClickListener(v -> {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            //Thiết lập tiêu đề
            b.setTitle("Xác nhận");
            b.setMessage("Thanh toán thành công");
// Nút Ok
            b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

//Tạo dialog
            AlertDialog al = b.create();
//Hiển thị
            al.show();
            CTSPActivity.badge.setText("0");
        });
    }

    private void tinhtongtien() {
        long tongtiensp = 0;
        for (int i = 0; i< Util.mangGioHang.size(); i++){
            tongtiensp = tongtiensp + (Util.mangGioHang.get(i).getAmount() * Util.mangGioHang.get(i).getPrice());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtiensp) + "VNĐ");
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Util.mangGioHang.size() == 0){
            txtGioHangTrong.setVisibility(View.VISIBLE);
            txtTongTien.setText("0VND");
        }else{
            gioHangAdapter = new GioHangAdapter(Util.mangGioHang,getApplicationContext());
            recyclerView.setAdapter(gioHangAdapter);
        }
    }

    private void initView() {
        txtGioHangTrong = findViewById(R.id.txtGioHangTrong);
        txtTongTien = findViewById(R.id.tvSumPrice);
        toolbar = findViewById(R.id.toolbarCart);
        recyclerView = findViewById(R.id.recyclerCart);
        btnMuaHang = findViewById(R.id.btnMuaHang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent tongEvent){
        if (tongEvent !=null) {
            tinhtongtien();
        }
    }
}