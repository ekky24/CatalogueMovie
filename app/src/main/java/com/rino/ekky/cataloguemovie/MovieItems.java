package com.rino.ekky.cataloguemovie;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieItems implements Parcelable {
    private int id;
    private String imagePath;
    private String imagePathBig;
    private String judul;
    private String releaseDate;
    private String language;
    private String rating;
    private String voteCount;
    private String overview;
    private String releaseDateGeneric;

    public MovieItems() {

    }

    public MovieItems(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.imagePath = "https://image.tmdb.org/t/p/w92" + object.getString("poster_path");
            this.imagePathBig = "https://image.tmdb.org/t/p/w342" + object.getString("poster_path");
            this.judul = object.getString("original_title");
            this.overview = object.getString("overview");
            this.rating = object.getString("vote_average");
            this.voteCount = object.getString("vote_count");
            String dateTemp = object.getString("release_date");
            String lanTemp = object.getString("original_language");
            this.releaseDateGeneric = dateTemp;

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateTemp);
            DateFormat dateFormat1 = new SimpleDateFormat("EEEE, dd MMM yyyy");
            this.releaseDate = dateFormat1.format(date);

            if (lanTemp.equals("id")) {
                this.language = "Indonesia";
            }
            else if(lanTemp.equals("en")) {
                this.language = "English";
            }
            else {
                this.language = "Other";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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

    public String getReleaseDateGeneric() {
        return releaseDateGeneric;
    }

    public void setReleaseDateGeneric(String releaseDateGeneric) {
        this.releaseDateGeneric = releaseDateGeneric;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.imagePath);
        dest.writeString(this.imagePathBig);
        dest.writeString(this.judul);
        dest.writeString(this.releaseDate);
        dest.writeString(this.language);
        dest.writeString(this.rating);
        dest.writeString(this.voteCount);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDateGeneric);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.imagePath = in.readString();
        this.imagePathBig = in.readString();
        this.judul = in.readString();
        this.releaseDate = in.readString();
        this.language = in.readString();
        this.rating = in.readString();
        this.voteCount = in.readString();
        this.overview = in.readString();
        this.releaseDateGeneric = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
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
