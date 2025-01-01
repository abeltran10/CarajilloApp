package com.abeltran10.carajilloapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.abeltran10.carajilloapp.ui.login.LoginFragment;
import com.abeltran10.carajilloapp.ui.main.MainFragment;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_round);

        if (savedInstanceState == null)
            if (mAuth.getCurrentUser() != null) {
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .add(R.id.frame_container, MainFragment.class, null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .add(R.id.frame_container, LoginFragment.class, null)
                        .commit();
            }


    }

}

