package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.RepositoryCallback;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.LoggedInUser;

import com.abeltran10.carajilloapp.utils.Cypher;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository() {
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result<LoggedInUser> login(String email, String password) {

        Result<LoggedInUser> result = null;
        LoggedInUser loggedInUser = null;


        Task<QuerySnapshot> queryDocumentSnapshotTask = db.collection("users")
                .whereEqualTo("email", email)
                .get(Source.SERVER);

        try {
            QuerySnapshot querySnapshot = Tasks.await(queryDocumentSnapshotTask);
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                loggedInUser = new LoggedInUser();
                loggedInUser.setId(documentSnapshot.getId());
                loggedInUser.setEmail(documentSnapshot.getString("email"));
                loggedInUser.setPassword(documentSnapshot.getString("password"));
                loggedInUser.setUsername(documentSnapshot.getString("username"));

            }

            if (loggedInUser != null) {
                setLoggedInUser(loggedInUser);

                String decrypted = Cypher.decrypt(user.getPassword());

                 if (password.equals(decrypted)) {
                     result = new Result.Success<LoggedInUser>(user);
                 } else {
                     result = new Result.Error(new Exception("Usuario o contraseña incorrectos"));
                 }

            } else {
                result = new Result.Error(new Exception("Usuario o contraseña incorrectos"));
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public void asyncLogin(String email, String password, RepositoryCallback<LoggedInUser> callback) {
        Runnable runnable = () -> {
            try {
                Result<LoggedInUser> result = login(email, password);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<LoggedInUser> errorResult = new Result.Error(new IOException("Error al recuperar el usuario"));
                callback.onComplete(errorResult);
            }
        };

        new Thread(runnable).start();
    }

}