package com.app.plyss.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app.plyss.R;
import com.app.plyss.data.model.User;
import com.app.plyss.ui.home.HomeViewModel;
import com.app.plyss.ui.home.HomeViewModelFactory;
import com.app.plyss.ui.login.LoginActivity;
import com.app.plyss.utils.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_data, R.id.navigation_add_form,
                R.id.navigation_add_household, R.id.navigation_household_data,
                R.id.navigation_individual_data)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mAuth = FirebaseAuth.getInstance();

        HomeViewModelFactory factory = new HomeViewModelFactory(this.getApplication());
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            String userEmail = AppUtils.getSignedUserEmail(this);
            User mUser = viewModel.getUser(userEmail);
            if (mUser != null) {
               viewModel.deleteUser(mUser);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                mAuth.signOut();
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
