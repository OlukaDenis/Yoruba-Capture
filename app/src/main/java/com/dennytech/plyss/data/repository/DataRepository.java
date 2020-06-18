package com.dennytech.plyss.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dennytech.plyss.data.api.ApiClient;
import com.dennytech.plyss.data.api.ApiService;
import com.dennytech.plyss.data.db.AppDatabase;
import com.dennytech.plyss.data.db.UserDao;
import com.dennytech.plyss.data.local.LocalDataSource;
import com.dennytech.plyss.data.model.Form;
import com.dennytech.plyss.data.model.State;
import com.dennytech.plyss.data.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dennytech.plyss.utils.AppGlobals.CAPTURES;
import static com.dennytech.plyss.utils.AppGlobals.USERS;

public class DataRepository {
    private static final String TAG = "DataRepository";
    private AppDatabase database;
    private UserDao userDao;
    private FirebaseFirestore db;

    public DataRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        userDao = database.userDao();
        db = FirebaseFirestore.getInstance();
        captureCount();
        getStates();
    }

    public void addUser(User user){
        userDao.addUser(user);
    }

    public User getUser(String email) {
        return userDao.getUser(email);
    }

    public void deleteUser(User user){
        userDao.deleteUser(user);
    }

    public LiveData<List<User>> allUsers() {
        return userDao.getAllUsers();
    }

    public Task<Void> addDataCapture(Form form){
        String uid = UUID.randomUUID().toString();
        DocumentReference docRef = db.collection(CAPTURES).document(uid);
        return docRef.set(form);
    }

    public Task<Void> saveUserToFirestore(User user){
        String uid = UUID.randomUUID().toString();
        DocumentReference docRef = db.collection(USERS).document(uid);
        return docRef.set(user);
    }

    public List<Form> getAllCaptures() {
        List<Form> captures = new ArrayList<>();

        db.collection(CAPTURES)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Form form = document.toObject(Form.class);
                            captures.add(form);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error while fetching form data: ", e );
                });
        Log.d(TAG, "getAllCaptures: "+captures.size());
        return captures;
    }

    public int captureCount() {
        final int[] count = new int[1];
        db.collection(CAPTURES).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Captured Data: ", task.getResult().size() + "");
                count[0] = task.getResult().size();
            } else {
                Log.d(TAG, "Error getting captures: ", task.getException());
            }
        });

        return count[0];
    }

    private void getStates() {
        //Fetching states
        ApiService service = ApiClient.getApiService(ApiService.class);
        Call<List<State>> call = service.getAllStates();

        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {

                if (response.isSuccessful()) {
                    List<State> stateList = response.body();
                    List<String> strings = new ArrayList<>();

                    for (int i = 0; i < stateList.size(); i++) {
                        strings.add(stateList.get(i).getName());
//                            Log.i(TAG, "State at "+ i +":" +stateList.get(i).getName());
                    }

                    LocalDataSource.ALL_STATES = strings;
//                        Log.d(TAG, "States List: "+strings);
                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Log.e(TAG, "States onFailure: ", t);

            }
        });
    }
}
