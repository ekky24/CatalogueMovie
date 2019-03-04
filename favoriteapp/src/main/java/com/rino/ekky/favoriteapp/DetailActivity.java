package com.rino.ekky.favoriteapp;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView posterImage;
    TextView tvTitle, tvReleaseDate, tvLanguage, tvRating, tvVoteCount, tvOverview;
    MovieItems movie;
    ImageButton btnFavorite;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        posterImage = findViewById(R.id.poster_image);
        tvTitle = findViewById(R.id.tv_title);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvLanguage = findViewById(R.id.tv_language);
        tvRating = findViewById(R.id.tv_rating);
        tvVoteCount = findViewById(R.id.tv_vote_count);
        tvOverview = findViewById(R.id.tv_overview);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    movie = new MovieItems(cursor);
                }
                cursor.close();
            }
        }

        Picasso.with(this)
                .load(movie.getImagePathBig())
                .into(posterImage);
        tvTitle.setText(movie.getJudul());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvLanguage.setText(movie.getLanguage());
        tvRating.setText(movie.getRating());
        tvVoteCount.setText(movie.getVoteCount());
        tvOverview.setText(movie.getOverview());

        btnFavorite.setImageResource(R.drawable.ic_star);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favorite) {
            getContentResolver().delete(uri, null, null);
            btnFavorite.setImageResource(R.drawable.ic_star_border);
        }
    }
}
