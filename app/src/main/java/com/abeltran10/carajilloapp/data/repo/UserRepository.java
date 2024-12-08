package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.RepositoryCallback;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.User;
import com.abeltran10.carajilloapp.utils.Cipher;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
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

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public Result<User> login(String email, String password) {

        Result<User> result = null;
        User user = null;


        Task<QuerySnapshot> queryDocumentSnapshotTask = db.collection("users")
                .whereEqualTo("email", email)
                .get(Source.SERVER);

        try {
            QuerySnapshot querySnapshot = Tasks.await(queryDocumentSnapshotTask);
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                user = new User();
                user.setId(documentSnapshot.getId());
                user.setEmail(documentSnapshot.getString("email"));
                user.setPassword(documentSnapshot.getString("password"));
                user.setUsername(documentSnapshot.getString("username"));

            }

            if (user != null) {
                setUser(user);

                String decrypted = Cipher.decrypt(this.user.getPassword());

                 if (password.equals(decrypted)) {
                     result = new Result.Success<User>(this.user);
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

    public void asyncLogin(String email, String password, RepositoryCallback<User> callback) {
        Runnable runnable = () -> {
            try {
                Result<User> result = login(email, password);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<User> errorResult = new Result.Error(new IOException("Error al recuperar el usuario"));
                callback.onComplete(errorResult);
            }
        };

        new Thread(runnable).start();
    }

    public Result<User> create(String username, String email, String password) {

        Result<User> result = null;
        User user = null;


        Task<QuerySnapshot> queryDocumentSnapshotTask = db.collection("users")
                .where(Filter.or(Filter.equalTo("username", username), Filter.equalTo("email", email)))
                .get(Source.SERVER);

        try {
            QuerySnapshot querySnapshot = Tasks.await(queryDocumentSnapshotTask);

            if (!querySnapshot.getDocuments().isEmpty())
                result = new Result.Error(new Exception("El usuario ya existe"));
            else {
                user.setEmail(email);
                user.setPassword(Cipher.encrypt(password));
                user.setUsername(username);

                DocumentReference documentReference = Tasks.await(db.collection("users").add(user));

                if (documentReference.getId() != null && !documentReference.getId().isEmpty()) {
                    user.setId(documentReference.getId());
                    setUser(user);

                    result = new Result.Success<User>(this.user);
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public void asyncCreate(String username, String email, String password, RepositoryCallback<User> callback) {
        Runnable runnable = () -> {
            try {
                Result<User> result = create(username, email, password);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<User> errorResult = new Result.Error(new IOException("Error al crear el usuario"));
                callback.onComplete(errorResult);
            }
        };

        new Thread(runnable).start();
    }

}