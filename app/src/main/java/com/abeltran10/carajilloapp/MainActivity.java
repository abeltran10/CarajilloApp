package com.abeltran10.carajilloapp;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.abeltran10.carajilloapp.ui.login.LoginFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.frame_container, LoginFragment.class, null)
                    .commit();
    }

}

