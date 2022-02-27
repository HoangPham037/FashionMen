package com.android45.fashionmen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android45.fashionmen.Activity.MapsActivity;
import com.android45.fashionmen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_fragment extends Fragment {
    TextView tvMaps,tvUer, tvTen, tvEmail,feedback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvMaps = view.findViewById(R.id.tvAddress);
        tvUer = view.findViewById(R.id.tvFixProfile);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvTen = view.findViewById(R.id.tvName);
        feedback = view.findViewById(R.id.tvfeedback);
        setUserInformation();

        tvMaps.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            startActivity(intent);
        });
        feedback.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Hiện tại không thể phản hồi", Toast.LENGTH_SHORT).show();
        });
    }
    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        tvEmail.setText(user.getEmail());
    }
    
}
