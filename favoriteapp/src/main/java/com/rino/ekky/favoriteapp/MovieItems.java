package com.rino.ekky.favoriteapp;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieItems implements Parcelable {
    private String id;
    private String imagePath;
    private String imagePathBig;
    private String judul;
    private String releaseDate;
    private String language;
    private String rating;
    private String voteCount;
    private String overview;

    public MovieItems() {

    }

    public MovieItems(Cursor c) {
        this.id = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns._ID);
        this.imagePath = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.IMAGE_PATH);
        this.imagePathBig = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.IMAGE_PATH_BIG);
        this.judul = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.TITLE);
        this.releaseDate = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.RELEASE_DATE);
        this.language = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.LANGUAGE);
        this.rating = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.RATING);
        this.voteCount = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.VOTE_COUNT);
        this.overview = DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.OVERVIEW);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImagePathBig() {
        return imagePathBig;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImagePathBig(String imagePathBig) {
        this.imagePathBig = imagePathBig;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imagePath);
        dest.writeString(this.imagePathBig);
        dest.writeString(this.judul);
        dest.writeString(this.releaseDate);
        dest.writeString(this.language);
        dest.writeString(this.rating);
        dest.writeString(this.voteCount);
        dest.writeString(this.overview);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readString();
        this.imagePath = in.readString();
        this.imagePathBig = in.readString();
        this.judul = in.readString();
        this.releaseDate = in.readString();
        this.language = in.readString();
        this.rating = in.readString();
        this.voteCount = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<MovieItems> CREATOR = new Parcelable.Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}
