package com.rino.ekky.favoriteapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends CursorAdapter {
    public FavoriteAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView;
        TextView tvJudul, tvDate, tvRating;

        if (cursor != null) {
            imageView = (ImageView) view.findViewById(R.id.img_poster);
            tvJudul = (TextView) view.findViewById(R.id.tv_title);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvRating = (TextView) view.findViewById(R.id.tv_rating);

            Picasso.with(context)
                    .load(DatabaseContract.getColumnString(cursor, DatabaseContract.FavoriteColumns.IMAGE_PATH))
                    .into(imageView);
            tvJudul.setText(DatabaseContract.getColumnString(cursor, DatabaseContract.FavoriteColumns.TITLE));
            tvDate.setText(DatabaseContract.getColumnString(cursor, DatabaseContract.FavoriteColumns.RELEASE_DATE));
            tvRating.setText(DatabaseContract.getColumnString(cursor, DatabaseContract.FavoriteColumns.RATING));
        }
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }
}
