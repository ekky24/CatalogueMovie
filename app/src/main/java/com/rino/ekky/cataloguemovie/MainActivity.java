package com.rino.ekky.cataloguemovie;

import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>, NavigationView.OnNavigationItemSelectedListener {

    MovieAdapter adapter;
    ListView lvMovie;
    LinearLayout linearMain;
    TextView tvSearchMessage;
    boolean isSearchPhrase = false;
    String searchPhrase = "";

    private static final String EXTRAS_KEYWORD = "extras_keyword";
    public static final String EXTRAS_PAGE_TYPE = "extras_page_type";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lvMovie = findViewById(R.id.lv_movie);
        adapter = new MovieAdapter(this);

        lvMovie.setAdapter(adapter);
        lvMovie.setOnItemClickListener(this);

        tvSearchMessage = findViewById(R.id.tv_search_message);
        linearMain = findViewById(R.id.linear_main);

        preferences = getSharedPreferences("catalogue_movie", Context.MODE_PRIVATE);

        getLoaderManager().initLoader(0, null, this);

        if (savedInstanceState != null) {
            isSearchPhrase = savedInstanceState.getBoolean("search_phrase");
            searchPhrase = savedInstanceState.getString("search_query");
            if (isSearchPhrase) {
                linearMain.removeView(tvSearchMessage);
            }
        }

        if (!preferences.getBoolean("first_time", false)) {
            DailyReminderReceiver receiver = new DailyReminderReceiver();
            receiver.setDailyReminderAlarm(this);
            ReleaseTodayReceiver releaseTodayReceiver = new ReleaseTodayReceiver();
            releaseTodayReceiver.setReleaseTodayAlarm(this);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_time", true);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_movie));
        searchView.setQueryHint(getResources().getString(R.string.enter_keyword));
        searchView.setIconified(false);
        searchView.setQuery(searchPhrase, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean isEmpty = false;

                if (TextUtils.isEmpty(query)) {
                    isEmpty = true;
                }
                if (!isEmpty) {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRAS_KEYWORD, query);

                    getLoaderManager().restartLoader(0, bundle, MainActivity.this);
                    searchPhrase = query;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        String keyword = "";
        if (args != null) {
            keyword = args.getString(EXTRAS_KEYWORD);
            linearMain.removeView(tvSearchMessage);
            isSearchPhrase = true;
        }
        return new MyAsyncTaskLoader(MainActivity.this, keyword, "search");
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieItems selectedMovie = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, selectedMovie);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {

        }
        else if (id == R.id.nav_now_playing) {
            Intent intent = new Intent(MainActivity.this, UpcomingActivity.class);
            intent.putExtra(EXTRAS_PAGE_TYPE, "now_playing");
            startActivity(intent);
        }
        else if (id == R.id.nav_upcoming) {
            Intent intent = new Intent(MainActivity.this, UpcomingActivity.class);
            intent.putExtra(EXTRAS_PAGE_TYPE, "upcoming");
            startActivity(intent);
        }
        else if (id == R.id.nav_favorite) {
            Intent intent = new Intent(MainActivity.this, UpcomingActivity.class);
            intent.putExtra(EXTRAS_PAGE_TYPE, "favorite");
            startActivity(intent);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("search_phrase", isSearchPhrase);
        outState.putString("search_query", searchPhrase);
    }
}
