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
import com.app.plyss.utils.Vars;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private HomeViewModel viewModel;
    NavController navController;
    private Vars vars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        vars = new Vars(this);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

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
            String userEmail = vars.getSignedUserEmail();
            User mUser = viewModel.getUser(userEmail);
            if (mUser != null) {
               viewModel.deleteUser(mUser);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                mAuth.signOut();
                Toasty.info(getApplicationContext(), "You have been logged out!", Toasty.LENGTH_SHORT).show();

                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}
