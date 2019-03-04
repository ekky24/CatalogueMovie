package com.rino.ekky.cataloguemovie;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class UpcomingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {
    RecyclerView rvMovieItem;
    CardViewMovieAdapter adapter;
    private ArrayList<MovieItems> movieList;
    private String pageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rvMovieItem = (RecyclerView) findViewById(R.id.rv_sub2);
        rvMovieItem.setHasFixedSize(true);

        pageType = getIntent().getStringExtra(MainActivity.EXTRAS_PAGE_TYPE);

        if(pageType.equals("now_playing")) {
            getSupportActionBar().setTitle(getString(R.string.now_playing));
        }
        else if(pageType.equals("upcoming")) {
            getSupportActionBar().setTitle(getString(R.string.upcoming));
        }
        else if(pageType.equals("favorite")) {
            getSupportActionBar().setTitle(getString(R.string.favorite));
        }

        movieList = new ArrayList<>();
        adapter = new CardViewMovieAdapter(this);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DetailActivity.REQUEST_FAVORITE) {
            if (resultCode == DetailActivity.RESULT_FAVORITE) {
                getLoaderManager().restartLoader(0, null, this);
            }
        }
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
        getMenuInflater().inflate(R.menu.upcoming, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(UpcomingActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_now_playing) {
            pageType = "now_playing";
            getSupportActionBar().setTitle(getString(R.string.now_playing));
            getLoaderManager().restartLoader(0, null, this);
        }
        else if (id == R.id.nav_upcoming) {
            pageType = "upcoming";
            getSupportActionBar().setTitle(getString(R.string.upcoming));
            getLoaderManager().restartLoader(0, null, this);
        }
        else if (id == R.id.nav_favorite) {
            pageType = "favorite";
            getSupportActionBar().setTitle(getString(R.string.favorite));
            getLoaderManager().restartLoader(0, null, this);
        }
        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        if (pageType.equals("now_playing"))
            return new MyAsyncTaskLoader(UpcomingActivity.this, "", "now_playing");
        else if (pageType.equals("upcoming"))
            return new MyAsyncTaskLoader(UpcomingActivity.this, "", "upcoming");
        else if (pageType.equals("favorite"))
            return new MyAsyncTaskLoader(UpcomingActivity.this, "", "favorite");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        showRecycler(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        showRecycler(null);
    }

    private void showRecycler(ArrayList<MovieItems> data) {
        rvMovieItem.setLayoutManager(new LinearLayoutManager(this));
        adapter.setListMovie(data);
        rvMovieItem.setAdapter(adapter);
    }
}
