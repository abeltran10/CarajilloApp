package com.abeltran10.carajilloapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abeltran10.carajilloapp.ui.login.LoginFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.mipmap.ic_launcher_round);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.frame_container, LoginFragment.class, null)
                    .commit();
    }

}

