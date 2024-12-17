package com.abeltran10.carajilloapp.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.User;
import com.abeltran10.carajilloapp.data.repo.UserRepository;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private UserRepository userRepository;

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();

    public RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void create(String username, String email, String password) {
        // can be launched in a separate asynchronous job

        userRepository.asyncCreate(username, email, password, result -> {
            if (result instanceof Result.Success) {
                User data = ((Result.Success<User>) result).getData();
                registerResult.postValue(new RegisterResult(new RegisterView(data.getUsername())));
            } else {
                registerResult.postValue(new RegisterResult(((Result.Error)result).getError().getMessage()));
            }
        });
    }

    public void loginDataChanged(String username, String email, String password, String repeatPassword) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        } else if (!isRepeatPasswordValid(password, repeatPassword)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_repeated_password));
        } else if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_email, null, null));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isRepeatPasswordValid(String password, String repeatPassword) {
        return repeatPassword.equals(password);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        return username.length() >= 4 && !username.trim().isEmpty();
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return !email.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 4;

    }
}