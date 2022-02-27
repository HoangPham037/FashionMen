package com.android45.fashionmen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android45.fashionmen.ChangeFavorite;
import com.android45.fashionmen.ChangeListFavoriteEvent;
import com.android45.fashionmen.Medel.GioHang;
import com.android45.fashionmen.Medel.MyFavoriteModel;
import com.android45.fashionmen.Medel.Products;
import com.android45.fashionmen.R;
import com.android45.fashionmen.RateDialog;
import com.android45.fashionmen.Util.Util;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.HashMap;

public class CTSPActivity extends AppCompatActivity {


    TextView NameCTSP, GiaCTSP, MotaSTSP;
    ImageView imgCTSP, imgCart;
    Button addCart,rate;
    Toolbar toolbar;
    Spinner spinner;
    Products products;
    public static NotificationBadge badge;
    ImageButton fvrt_btn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    boolean checkFav = false;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    MyFavoriteModel model = new MyFavoriteModel();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctspactivity);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        anhxa();
        initData();
        initControl();
        ActionToolbar();
    }

    private void initControl() {
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
                Toast.makeText(CTSPActivity.this, "Thêm giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void themGioHang() {
        if (Util.mangGioHang.size() > 0) {
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Util.mangGioHang.size(); i++) {
                if (Util.mangGioHang.get(i).getProductID() == products.getId()) {
                    Util.mangGioHang.get(i).setAmount(soluong + Util.mangGioHang.get(i).getAmount());
                    long gia = products.getPrice() * Util.mangGioHang.get(i).getAmount();
                    Util.mangGioHang.get(i).setPrice(gia);
                    flag = true;
                }
            }
            if (flag == false) {
                long gia = products.getPrice() * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setAmount(soluong);
                gioHang.setPrice(gia);
                gioHang.setName(products.getName());
                gioHang.setImgSp(products.getImage());
                Util.mangGioHang.add(gioHang);
            }

        } else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = products.getPrice() * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setAmount(soluong);
            gioHang.setPrice(gia);
            gioHang.setName(products.getName());
            gioHang.setImgSp(products.getImage());
            Util.mangGioHang.add(gioHang);
        }
        int totalItem = 0;
        for (int i = 0; i < Util.mangGioHang.size(); i++) {
            totalItem = totalItem + Util.mangGioHang.get(i).getAmount();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        products = (Products) getIntent().getSerializableExtra("product");
        NameCTSP.setText(products.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        GiaCTSP.setText("Giá: " + decimalFormat.format(products.getPrice()) + "VNĐ");
        MotaSTSP.setText(products.getDescribe());
        Glide.with(getApplicationContext()).load(products.getImage()).into(imgCTSP);
        Integer[] sl = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sl);
        spinner.setAdapter(adapter);
    }

    private void anhxa() {
        rate = findViewById(R.id.rate);
        NameCTSP = findViewById(R.id.tvNameCTSP);
        GiaCTSP = findViewById(R.id.tvPriceSp);
        imgCTSP = findViewById(R.id.imgCTSP);
        MotaSTSP = findViewById(R.id.MoTaCTSP);
        toolbar = findViewById(R.id.toolbar);
        spinner = findViewById(R.id.spinnerSoLuong);
        addCart = findViewById(R.id.addCart);
        imgCart = findViewById(R.id.imgCart);
        badge = findViewById(R.id.menu_amount);
        fvrt_btn = findViewById(R.id.fvrt_f2_item);
        FrameLayout frameLayout = findViewById(R.id.frameCart);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(i);
            }
        });
        if (Util.mangGioHang != null) {
            int totalItem = 0;
            for (int i = 0; i < Util.mangGioHang.size(); i++) {
                totalItem = totalItem + Util.mangGioHang.get(i).getAmount();
            }
            badge.setText(String.valueOf(totalItem));
        }
        fvrt_btn.setOnClickListener(v -> {
            if (checkFav) {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Favorite")
                        .document(model.getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                checkFav = false;
                                changeFav();
                                EventBus.getDefault().postSticky(new ChangeListFavoriteEvent());
                            }
                        });

            } else {
                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productImg", products.getImage());
                cartMap.put("productName", products.getName());
                cartMap.put("productPrice", products.getPrice());

                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Favorite").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(CTSPActivity.this, "Đã thêm vào sản phẩm yêu thích", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().postSticky(new ChangeFavorite());
                        EventBus.getDefault().postSticky(new ChangeListFavoriteEvent());
                    }
                });
            }
        });
        rate.setOnClickListener(v -> {
            RateDialog rateDialog = new RateDialog(CTSPActivity.this);
            rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            rateDialog.setCancelable(false);
            rateDialog.show();
        });
    }
    private void changeFav(){
        if (!checkFav){
            fvrt_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            fvrt_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);

        }
    }

    private void checkFavorite(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference yourCollRef = rootRef.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("Favorite");
        Query query = yourCollRef.whereEqualTo("productName", products.getName());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        model.setDocumentID(document.getId());
                        checkFav = true;
                        changeFav();
                    }
                } else {
                    checkFav = false;
                    changeFav();
                }
            }
        });

    }
    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventChangeFavorite(ChangeFavorite event) {
        if (event != null) {
            checkFavorite();
        }
    }
}