package com.example.ayushsrivastava.arc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private TextView username,emailadd;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String uname,uemail,uid;
    private Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid=user.getUid();
        if(firebaseAuth.getCurrentUser()==null)
        {
            Intent intent = new Intent(this,MainActivityLogin.class);
            startActivity(intent);
            finish();
        }
        //Snackbar.make(v,"No internet connection !",Snackbar.LENGTH_INDEFINITE).setAction("Retry",null).show();
        //Toast.makeText(this, "Hello, Toast.LENGTH_SHORT).show();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        username =(TextView)headerView.findViewById(R.id.username);
        username.setText("Hello !");
        emailadd = (TextView)headerView.findViewById(R.id.emailPro);
        emailadd.setText(firebaseAuth.getCurrentUser().getEmail().toString());
        navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState!=null) {
            savedInstanceState.getString("dateString");
        }
        setFragment(new dataFragment());//init

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Exit? ")
                    .setNegativeButton(android.R.string.no,null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.super.onBackPressed();
                            finish();
                        }
                    }).create().show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Toast.makeText(this, "This is not made yet !", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (id == R.id.nav_Home){
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new dataFragment()).commit();
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_uploads) {
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new uploadsFrag()).commit();
            Toast.makeText(this, "Uploads", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_downloads) {
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new downloadsFrag()).commit();
            Toast.makeText(this, "Downloads", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_contact) {
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new contact()).commit();
            Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_time) {
            FragmentManager frt=getSupportFragmentManager();
            frt.beginTransaction().replace(R.id.content_main,new TimeTable()).commit();
            Toast.makeText(this, "Time Table", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_share) {
            Intent i =new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            String sharebody = "ARC : stands for artificial reperesentative of class,where all the stuff which sometimes we miss are collected at one place i.e, class notes,syllabus,important notices,and much more.All the stuff is uploaded by the respective class representative of the class. Just register and forget about loosing your notes or stuff ! ";
            //i.putExtra(Intent.EXTRA_SUBJECT,sharebody);
            i.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(i,"Share Using :"));
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(id == R.id.nav_logout)
        {
            firebaseAuth.signOut();
            Intent i = new Intent(this,MainActivityLogin.class);
            startActivity(i);
            this.finish();
        }
        return true;
    }
    public void setFragment(Fragment fragment)
    {
        if(fragment!=null) {
            FragmentTransaction frt = getSupportFragmentManager().beginTransaction();
            FragmentTransaction replace = frt.replace(R.id.content_main, fragment);
            replace.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
