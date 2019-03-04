package com.rino.ekky.cataloguemovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView posterImage;
    TextView tvTitle, tvReleaseDate, tvLanguage, tvRating, tvVoteCount, tvOverview;
    MovieItems movie;
    ImageButton btnFavorite;
    FavoriteHelper helper;
    boolean isFavorite = false;
    public static final String EXTRA_MOVIE = "extra_movie";
    public static int REQUEST_FAVORITE = 100;
    public static int RESULT_FAVORITE = 101;

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
        btnFavorite =  findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        helper = new FavoriteHelper(this);
        helper.open();

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        Picasso.with(this)
                .load(movie.getImagePathBig())
                .into(posterImage);
        tvTitle.setText(movie.getJudul());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvLanguage.setText(movie.getLanguage());
        tvRating.setText(movie.getRating());
        tvVoteCount.setText(movie.getVoteCount());
        tvOverview.setText(movie.getOverview());

        isFavorite = helper.checkFavorite(String.valueOf(movie.getId()));
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_star);
        }
        else {
            btnFavorite.setImageResource(R.drawable.ic_star_border);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_FAVORITE);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_FAVORITE);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favorite) {
            if (isFavorite) {
                helper.delete(movie.getId());
                btnFavorite.setImageResource(R.drawable.ic_star_border);
            }
            else {
                helper.insert(movie);
                btnFavorite.setImageResource(R.drawable.ic_star);
            }
            isFavorite = helper.checkFavorite(String.valueOf(movie.getId()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.close();
        }
    }
}
