package com.rino.ekky.cataloguemovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static com.rino.ekky.cataloguemovie.DatabaseContract.TABLE_FAVORITE;

public class FavoriteHelper {
    private static String DATABASE_TABLE = TABLE_FAVORITE;
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public FavoriteHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public ArrayList<MovieItems> query() {
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null,
                null, null, DatabaseContract.FavoriteColumns._ID + " DESC", null);
        ArrayList<MovieItems> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        MovieItems movieItems;

        if (cursor.getCount() > 0) {
            do {
                int temp_id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID));
                String temp_title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TITLE));
                String temp_img_path = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.IMAGE_PATH));
                String temp_img_path_big = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.IMAGE_PATH_BIG));
                String temp_release_date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.RELEASE_DATE));
                String temp_language = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LANGUAGE));
                String temp_rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.RATING));
                String temp_vote_count = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.VOTE_COUNT));
                String temp_overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.OVERVIEW));

                movieItems = new MovieItems();
                movieItems.setId(temp_id);
                movieItems.setJudul(temp_title);
                movieItems.setImagePath(temp_img_path);
                movieItems.setImagePathBig(temp_img_path_big);
                movieItems.setLanguage(temp_language);
                movieItems.setReleaseDate(temp_release_date);
                movieItems.setRating(temp_rating);
                movieItems.setVoteCount(temp_vote_count);
                movieItems.setOverview(temp_overview);

                arrayList.add(movieItems);
                cursor.moveToNext();
            }
            while(!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean checkFavorite(String id) {
        Cursor cursor = db.query(DATABASE_TABLE, null, DatabaseContract.FavoriteColumns._ID + "= ?",
                new String[]{id}, null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public long insert(MovieItems item) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.FavoriteColumns._ID, item.getId());
        values.put(DatabaseContract.FavoriteColumns.TITLE, item.getJudul());
        values.put(DatabaseContract.FavoriteColumns.IMAGE_PATH, item.getImagePath());
        values.put(DatabaseContract.FavoriteColumns.IMAGE_PATH_BIG, item.getImagePathBig());
        values.put(DatabaseContract.FavoriteColumns.LANGUAGE, item.getLanguage());
        values.put(DatabaseContract.FavoriteColumns.RELEASE_DATE, item.getReleaseDate());
        values.put(DatabaseContract.FavoriteColumns.RATING, item.getRating());
        values.put(DatabaseContract.FavoriteColumns.VOTE_COUNT, item.getVoteCount());
        values.put(DatabaseContract.FavoriteColumns.OVERVIEW, item.getOverview());
        return db.insert(DATABASE_TABLE, null, values);
    }

    public int delete(int id) {
        return db.delete(DatabaseContract.TABLE_FAVORITE, DatabaseContract.FavoriteColumns._ID + "= '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return db.query(DATABASE_TABLE, null, DatabaseContract.FavoriteColumns._ID + "= ?",
                new String[]{id}, null, null, null, null);
    }

    public Cursor queryProvider() {
        return db.query(DATABASE_TABLE, null, null, null, null,
                null, DatabaseContract.FavoriteColumns._ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return db.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return db.delete(DATABASE_TABLE, DatabaseContract.FavoriteColumns._ID + " = ?", new String[]{id});
    }
}
