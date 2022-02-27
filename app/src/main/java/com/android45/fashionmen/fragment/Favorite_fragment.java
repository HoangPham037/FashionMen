package com.android45.fashionmen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.fashionmen.Activity.CTSPActivity;
import com.android45.fashionmen.ChangeListFavoriteEvent;
import com.android45.fashionmen.Adapter.MyFavoriteAdapter;
import com.android45.fashionmen.Medel.MyFavoriteModel;
import com.android45.fashionmen.Interface.OnClickItemFavorite;
import com.android45.fashionmen.Medel.Products;
import com.android45.fashionmen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class Favorite_fragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore db;

    RecyclerView rvFavorite;
    MyFavoriteAdapter favoriteAdapter;
    List<MyFavoriteModel> favoriteModels;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvFavorite = view.findViewById(R.id.rvFavorite);
        favoriteModels = new ArrayList<>();
        favoriteAdapter = new MyFavoriteAdapter(getActivity(), favoriteModels, new OnClickItemFavorite() {
            @Override
            public void onClickItemVav(MyFavoriteModel model) {
                goToDetailFromFav(model);
            }
        });

        GridLayoutManager layoutManager02 = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        rvFavorite.setLayoutManager(layoutManager02);
        rvFavorite.setAdapter(favoriteAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Favorite").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        MyFavoriteModel model = documentSnapshot.toObject(MyFavoriteModel.class);
                        model.setDocumentID(documentId);
                        favoriteModels.add(model);
                        favoriteAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void goToDetailFromFav(MyFavoriteModel model) {
        Intent intent = new Intent(getActivity(), CTSPActivity.class);
        Bundle bundle = new Bundle();

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference yourCollRef = rootRef.collection("Products");
        Query query = yourCollRef.whereEqualTo("name", model.getProductName());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Products herbal = document.toObject(Products.class);

                        bundle.putSerializable("Herbal", herbal);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {

                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventSetFav(ChangeListFavoriteEvent event) {
        if (event != null) {
            favoriteAdapter.notifyDataSetChanged();
        }
    }
}
