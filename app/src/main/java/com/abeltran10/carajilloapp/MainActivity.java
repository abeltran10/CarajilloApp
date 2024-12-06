package com.abeltran10.carajilloapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abeltran10.carajilloapp.ui.login.LoginFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.frame_container, LoginFragment.class, null)
                    .commit();
    }

}

