package com.example.e_koteapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.e_koteapplication.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private boolean isAdmin = false;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        boolean isLoggedIn = intent.getBooleanExtra("isLoggedIn",false);
        String role = intent.getStringExtra("roles");

        if (role != null && role.equalsIgnoreCase("Admin")) {
            isAdmin = true;
        }

        setSupportActionBar(binding.appBarMain.toolbar);


        binding.appBarMain.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show()

        );

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
              this,drawer,binding.appBarMain.toolbar,
              R.string.navigation_drawer_open,
              R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_helpline,R.id.nav_dashboard,
                R.id.nav_issued_return,R.id.nav_issued_return,R.id.maintenance)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateMenuItems(navigationView);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

                if (id ==R.id.nav_home){
                    navController.navigate(R.id.nav_home);}
                else if (id == R.id.nav_about){
                    navController.navigate(R.id.nav_about);}
                else if (id == R.id.nav_helpline){
                    navController.navigate(R.id.nav_helpline);}
                else if (id == R.id.nav_dashboard){
                    navController.navigate(R.id.nav_dashboard);}
                else if (id == R.id.nav_issued_return){
                    navController.navigate(R.id.nav_issued_return);}
                else if (id == R.id.nav_inventory_management){
                    navController.navigate(R.id.nav_inventory_management);}
                else if (id == R.id.nav_maintenance){
                    navController.navigate(R.id.nav_maintenance);}
                else if (id == R.id.logout){
                    handleLogout();}

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        if (isLoggedIn) {
           navigateBasedOnRole(role, navController);
            }else {
               navigateToLogin();
            }
        }



private void navigateToLogin() {
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    startActivity(intent);
    finish();
}

private void navigateBasedOnRole(String role, NavController navController) {
    if ("Admin".equalsIgnoreCase(role)){
        navController.navigate(R.id.nav_dashboard);
    } else {
        // Regular user should be directed to the Home fragment
        navController.navigate(R.id.nav_home);
    }

}


private void handleLogout() {
        isLoggedIn = false;
        isAdmin = false;

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        updateMenuItems(binding.navView);
    }

    private void updateMenuItems(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        if (menu != null) {
            MenuItem dashboardItem = menu.findItem(R.id.nav_dashboard);
            MenuItem issuedReturnItem = menu.findItem(R.id.nav_issued_return);
            MenuItem inventoryManagementItem = menu.findItem(R.id.nav_inventory_management);
            MenuItem maintenanceItem = menu.findItem(R.id.nav_maintenance);

            if (dashboardItem != null) {
                dashboardItem.setVisible(isAdmin);
            }
            if (issuedReturnItem != null) {
                issuedReturnItem.setVisible(isAdmin);
            }
            if (inventoryManagementItem != null) {
                inventoryManagementItem.setVisible(isAdmin);
            }
            if (maintenanceItem != null) {
                maintenanceItem.setVisible(isAdmin);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        DrawerLayout drawerLayout = binding.drawerLayout;
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}