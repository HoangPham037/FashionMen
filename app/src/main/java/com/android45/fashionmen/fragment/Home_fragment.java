package com.android45.fashionmen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android45.fashionmen.Activity.CapActivity;
import com.android45.fashionmen.Activity.CartActivity;
import com.android45.fashionmen.Activity.AoActivity;
import com.android45.fashionmen.Activity.MuActivity;
import com.android45.fashionmen.Activity.QuanActivity;
import com.android45.fashionmen.Medel.Products;
import com.android45.fashionmen.R;
import com.android45.fashionmen.Adapter.RecyclerAdapter;
import com.android45.fashionmen.Medel.Slide;
import com.android45.fashionmen.Adapter.SliderPagerAdapter;
import com.android45.fashionmen.Util.Util;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Home_fragment extends Fragment {
    ImageView ao,quan,cap,mu;
    List<Products> list,list1;
    RecyclerView recyclerView,recyclerView2;
    FrameLayout frameLayout;
    NotificationBadge badgee;
    EditText search;
    RecyclerAdapter productAdapter, productAdapter1;
    FirebaseDatabase database, database1;
    DatabaseReference reference, reference1;
    private ViewPager slideViewPager;
    private List<Slide> listSlides;
    private TabLayout indicator;
    private SliderPagerAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ao = view.findViewById(R.id.ao);
        quan = view.findViewById(R.id.quan);
        cap = view.findViewById(R.id.cap);
        mu = view.findViewById(R.id.mu);
        recyclerView = view.findViewById(R.id.recycler2);
        recyclerView2 = view.findViewById(R.id.recycler3);
        frameLayout = view.findViewById(R.id.frameCartHome);
        search = view.findViewById(R.id.edtSearch);
        badgee = view.findViewById(R.id.menu_amountt);
        slideViewPager = view.findViewById(R.id.slider_pager);
        indicator = view.findViewById(R.id.indicator);

        ao.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AoActivity.class);
            startActivity(intent);
        });
        quan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuanActivity.class);
            startActivity(intent);
        });
        cap.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CapActivity.class);
            startActivity(intent);
        });
        mu.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MuActivity.class);
            startActivity(intent);
        });

        //banner
        listSlides = new ArrayList<>();
        listSlides.add(new Slide(R.drawable.banner2));
        listSlides.add(new Slide(R.drawable.banner3));
        listSlides.add(new Slide(R.drawable.banner4));
        adapter = new SliderPagerAdapter(getActivity(),listSlides);
        slideViewPager.setAdapter(adapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,5000);
        indicator.setupWithViewPager(slideViewPager,true);
                frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = search.getText().toString();
                productAdapter.searchSp(str);
            }
        });

        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        productAdapter = new RecyclerAdapter(list,getContext());
        recyclerView.setAdapter(productAdapter);


        list1 = new ArrayList<>();
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        productAdapter1 = new RecyclerAdapter(list1,getContext());
        recyclerView2.setAdapter(productAdapter1);

        if (Util.mangGioHang == null){
            Util.mangGioHang = new ArrayList<>();
        }else{
            int totalItem = 0;
            for (int i = 0; i< Util.mangGioHang.size(); i++){
                totalItem = totalItem  + Util.mangGioHang.get(i).getAmount();
            }
            badgee.setText(String.valueOf(totalItem));
        }
        getListProductRclRealtimeDatabase();
        getListProductRclRealtimeDatabaseNew();
    }
    class SliderTimer extends TimerTask{
        @Override
        public void run() {
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (slideViewPager.getCurrentItem()<listSlides.size()-1){
                        slideViewPager.setCurrentItem(slideViewPager.getCurrentItem()+1);
                    }
                    else {
                        slideViewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }


    public  void getListProductRclRealtimeDatabase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("sanphamphobien");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Products products = dataSnapshot.getValue(Products.class);
                    list.add(products);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //moi cap nhap
    public  void getListProductRclRealtimeDatabaseNew(){
        database1 = FirebaseDatabase.getInstance();
        reference1 = database1.getReference("moicapnhap");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Products products = dataSnapshot.getValue(Products.class);
                    list1.add(products);
                }
                productAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
