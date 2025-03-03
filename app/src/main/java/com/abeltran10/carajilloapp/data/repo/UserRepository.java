package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.ExecutionException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private User user = null;

    // private constructor : singleton access
    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public Result login(String email, String password) {
        Result result = null;
        User user = null;


        try {
            AuthResult authResult = Tasks.await(mAuth.signInWithEmailAndPassword(email, password));
            if (authResult.getUser() != null) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                user = new User();
                user.setId(firebaseUser.getUid());
                user.setEmail(firebaseUser.getEmail());
                user.setUsername(firebaseUser.getDisplayName());

                setUser(user);
                result = new Result.Success<User>(this.user);
            } else {
                result = new Result.Error(new Exception("Email o password incorrectes"));
            }

        } catch (ExecutionException | InterruptedException e) {
            result = new Result.Error(new Exception("Email o password incorrectes"));
        }

        return result;
    }

    public Result create(String username, String email, String password) {
        Result result = null;
        User user = null;

        try {
            AuthResult authResult = Tasks.await(mAuth.createUserWithEmailAndPassword(email, password));
            if (authResult.getUser() != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();
                Tasks.await(authResult.getUser().updateProfile(profileUpdates));
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                user = new User();
                user.setId(firebaseUser.getUid());
                user.setEmail(firebaseUser.getEmail());
                user.setUsername(firebaseUser.getDisplayName());

                setUser(user);
                result = new Result.Success<User>(this.user);
            } else {
                result = new Result.Error(new Exception("Error al crear el compte"));
            }

        } catch (ExecutionException | InterruptedException e) {
            result = new Result.Error(new Exception("El email ja està registrat"));
        }

        return result;
    }

}