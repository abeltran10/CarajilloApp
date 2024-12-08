package com.abeltran10.carajilloapp.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.User;
import com.abeltran10.carajilloapp.data.repo.UserRepository;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private UserRepository userRepository;

    public RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(String username, String email, String password) {
        // can be launched in a separate asynchronous job

        userRepository.asyncCreate(username, email, password, result -> {
            if (result instanceof Result.Success) {
                User data = ((Result.Success<User>) result).getData();
                registerResult.postValue(new RegisterResult(new RegisterView(data.getUsername())));
            } else {
                registerResult.postValue(new RegisterResult(R.string.register_failed));
            }
        });
    }
    // TODO: Implement the ViewModel
}