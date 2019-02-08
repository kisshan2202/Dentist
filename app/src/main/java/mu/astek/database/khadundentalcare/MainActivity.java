package mu.astek.database.khadundentalcare;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import mu.astek.database.khadundentalcare.Activities.AddAppointmentActivity;
import mu.astek.database.khadundentalcare.Activities.AddPatientActivity;
import mu.astek.database.khadundentalcare.Activities.AppointmentFragment;
import mu.astek.database.khadundentalcare.Activities.PatientFragment;
import mu.astek.database.khadundentalcare.Database.DatabaseService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        databaseService = new DatabaseService(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        openFrag(new AppointmentFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            openFrag(new AppointmentFragment());
        } else if (id == R.id.nav_gallery) {
            openFrag(new PatientFragment());
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(this, AddAppointmentActivity.class));
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(this, AddPatientActivity.class));
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openFrag(final Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        final android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
    }
}
