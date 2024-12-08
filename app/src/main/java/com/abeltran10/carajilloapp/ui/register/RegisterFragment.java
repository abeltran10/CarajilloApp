package com.abeltran10.carajilloapp.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.databinding.FragmentLoginBinding;
import com.abeltran10.carajilloapp.databinding.FragmentRegisterBinding;
import com.abeltran10.carajilloapp.ui.login.LoginViewModel;
import com.abeltran10.carajilloapp.ui.login.LoginViewModelFactory;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);
    }
}