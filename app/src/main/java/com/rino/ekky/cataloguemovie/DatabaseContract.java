package com.rino.ekky.cataloguemovie;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_FAVORITE = "favorite";

    static final class FavoriteColumns implements BaseColumns {
        static String TITLE = "title";
        static String IMAGE_PATH = "image_path";
        static String IMAGE_PATH_BIG = "image_path_big";
        static String RELEASE_DATE = "release_date";
        static String LANGUAGE = "language";
        static String RATING = "rating";
        static String VOTE_COUNT = "vote_count";
        static String OVERVIEW = "overview";
    }

    public static final String AUTHORITY = "com.rino.ekky.cataloguemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE)
            .build();

    public static final String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static final int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName){
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
