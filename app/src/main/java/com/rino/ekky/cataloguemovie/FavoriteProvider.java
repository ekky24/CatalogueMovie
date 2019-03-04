package com.rino.ekky.cataloguemovie;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoriteProvider extends ContentProvider {
    private static final int FAVORITE = 1;
    private static final int FAVORITE_ID = 2;
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper helper;

    static {
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.TABLE_FAVORITE, FAVORITE);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.TABLE_FAVORITE + "/#", FAVORITE_ID);
    }

    @Override
    public boolean onCreate() {
        helper = new FavoriteHelper(getContext());
        helper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (matcher.match(uri)) {
            case FAVORITE:
                cursor = helper.queryProvider();
                break;
            case FAVORITE_ID:
                cursor = helper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        switch (matcher.match(uri)) {
            case FAVORITE:
                added = helper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(DatabaseContract.CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (matcher.match(uri)) {
            case FAVORITE_ID:
                deleted = helper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
