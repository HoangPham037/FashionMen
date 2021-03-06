package com.android45.fashionmen.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android45.fashionmen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigUpActivity extends AppCompatActivity {

    private TextView logIn;
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button signUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_up);
        anhXa();
        onClickButton();
    }
    private void anhXa() {
        mEmail = findViewById(R.id.edtEmailSignIn);
        mPassword = findViewById(R.id.edtPasswordSignIn);
        mConfirmPassword = findViewById(R.id.edtChangPasswordSignIn);
        logIn = findViewById(R.id.tvLogIn);
        signUp = findViewById(R.id.btnSignIn);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void onClickButton(){
        signUp.setOnClickListener(v -> {
            Register();
        });

        logIn.setOnClickListener(v -> {
            Intent i = new Intent(SigUpActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
    private void Register() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPass = mConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            mEmail.setError("?? n??y kh??ng ???????c b??? tr???ng");
            mEmail.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(password)){
            mPassword.setError("?? n??y kh??ng ???????c b??? tr???ng");
            mPassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(confirmPass)){
            mConfirmPassword.setError("?? n??y kh??ng ???????c b??? tr???ng");
            mConfirmPassword.requestFocus();
            return;
        }
        else if (!password.equals(confirmPass)){
            mConfirmPassword.setError("M???t kh???u kh??ng tr??ng");
            mConfirmPassword.requestFocus();
            return;
        }
        else if (mPassword.length() < 3){
            mPassword.setError("M???t kh???u ??t nh???t 5 k?? t???");
            mPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Vui l??ng ?????i...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SigUpActivity.this, "????ng k?? th??nh c??ng!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SigUpActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(SigUpActivity.this, "????ng k?? th???t b???i!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}