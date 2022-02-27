package com.android45.fashionmen.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android45.fashionmen.Medel.User;
import com.android45.fashionmen.R;
import com.android45.fashionmen.fragment.Favorite_fragment;
import com.android45.fashionmen.fragment.Home_fragment;
import com.android45.fashionmen.fragment.User_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private BottomNavigationView mBottomNavigationView;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FAVORITE = 1;
    private static final int FRAGMENT_USER = 2;
    private TextView  mEmail;
    private int mCurrentFragment = FRAGMENT_HOME;
    final private User userr = new User();
    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                Uri uri = intent.getData();
                userr.setUri(uri);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        Actionbar();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new Home_fragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        mBottomNavigationView.getMenu().findItem(R.id.nav_bottom_home).setChecked(true);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_bottom_home) {
                    openHomeFragment();
                    navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                } else if (id == R.id.nav_bottom_favorite) {
                    openFavoriteFragment();
                    navigationView.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                }else if (id == R.id.nav_bottom_user) {
                    openUser();
                    navigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                }
                return true;
            }
        });
//        showUserInformation();

    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String email = user.getEmail();


        mEmail.setText(email);
    }

    private void Anhxa() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mBottomNavigationView = findViewById(R.id.bottom_nav);
//        mEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            openHomeFragment();
            mBottomNavigationView.getMenu().findItem(R.id.nav_bottom_home).setChecked(true);

        } else if (id == R.id.nav_favorite) {
            openFavoriteFragment();
            mBottomNavigationView.getMenu().findItem(R.id.nav_bottom_favorite).setChecked(true);
        }if (id == R.id.nav_user) {
            openUser();
            mBottomNavigationView.getMenu().findItem(R.id.nav_bottom_user).setChecked(true);
        }
        else if (id == R.id.nav_sig_out){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openHomeFragment() {
        if (mCurrentFragment != FRAGMENT_HOME) {
            replaceFragment(new Home_fragment());
            mCurrentFragment = FRAGMENT_HOME;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFavoriteFragment() {
        if (mCurrentFragment != FRAGMENT_FAVORITE) {
            replaceFragment(new Favorite_fragment());
            mCurrentFragment = FRAGMENT_FAVORITE;
        }
    }

    private void openUser() {
        if (mCurrentFragment != FRAGMENT_USER) {
            replaceFragment(new User_fragment());
            mCurrentFragment = FRAGMENT_USER;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
    private void Actionbar(){
        ActionBar actionBar = getSupportActionBar();
//        actionBar.getTitle("title");
    }


}