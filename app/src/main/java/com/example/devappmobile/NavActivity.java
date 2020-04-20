package com.example.devappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , CasesFragment.OnListFragmentInteractionListener , DraftFragment.OnListFragmentInteractionListener {
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://process.isiforge.tn/")
            .addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit= builder.build();
    UserClient userClient= retrofit.create(UserClient.class);
    String token , pro_uid , tas_uid;
    Fragment demandFragment = new  CasesFragment();
    Fragment draftFragment = new DraftFragment();
    final FragmentManager fm = getSupportFragmentManager();
    SharedPreferences prefs;


    // String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs= this.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE).edit();
        editor.putString("just","just");
        editor.apply();

        SharedPreferences prefs= this.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("token", null);
        if (restoredText != null) {
            token = prefs.getString("token", "No token defined");
            // System.out.println(token + "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

        }


        setContentView(R.layout.activity_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fm.beginTransaction().add(R.id.nav_container, demandFragment, "1").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            fm.beginTransaction().replace(R.id.nav_container, demandFragment, "1").commit();


        } else if (id == R.id.nav_gallery) {

            fm.beginTransaction().replace(R.id.nav_container, draftFragment, "2").commit();


        }  else if (id == R.id.nav_share) {
            prefs.edit().remove("token").apply();
            prefs.edit().remove("refresh").apply();
            prefs.edit().remove("Expires_in").apply();
            Intent homeIntent = new Intent(NavActivity.this, MainActivity.class);
            startActivity(homeIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(final ProcessList item) {
        System.out.println(item.getPro_uid());
        System.out.println(item.getTas_uid());
        Call<List<Step>> cal = userClient.getStep(token, item.getPro_uid(),item.getTas_uid());
        cal.enqueue(new Callback<List<Step>>() {
            @Override
            public void onResponse(Call<List<Step>> call, Response<List<Step>> response) {
                Call<Dynaform> cal = userClient.getDynaform(token, item.getPro_uid(),response.body().get(0).step_uid_obj);
                cal.enqueue(new Callback<Dynaform>() {
                    @Override
                    public void onResponse(Call<Dynaform> call, Response<Dynaform> response) {
                        //System.out.println(response.body().getDyn_content()+ "ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

                        Fragment fragment = new FormFragment();
                        Bundle args = new Bundle();
                       // args.putString("dyn_content", response.body().getDyn_content());
                        args.putString("formJsonString", response.body().getDyn_content());
                        args.putString("pro_uid", item.getPro_uid());
                        args.putString("tas_uid", item.getTas_uid());
                        args.putString("token", token);
                        fragment.setArguments(args);

                        fm.beginTransaction().replace(R.id.nav_container, fragment, "3").commit();




                    }

                    @Override
                    public void onFailure(Call<Dynaform> call, Throwable t) {
                        System.out.println("Failednav");

                        // Toast.makeText(getContext(), "scss", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Step>> call, Throwable t) {
                System.out.println(t.toString());

                // Toast.makeText(getContext(), "scss", Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    public void onListDraftFragmentInteraction(Draft item) {

    }
}
