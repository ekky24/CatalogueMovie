package com.rino.ekky.cataloguemovie;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {
    private boolean hasResult = false;
    private ArrayList<MovieItems> data;
    private String keyword;
    private String type;
    private static final String SUCCESS_TAG = "success_tag";
    private Context context;

    public MyAsyncTaskLoader(Context context, String keyword, String type) {
        super(context);

        onContentChanged();
        this.keyword = keyword;
        this.type = type;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
        else if (hasResult) {
            deliverResult(data);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            onReleaseResources(data);
            data = null;
            hasResult = false;
        }
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data) {
        this.data = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        String url = "";
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MovieItems> movieItemses = new ArrayList<>();

        if (type.equals("favorite")) {
            ArrayList<MovieItems> favoriteItems = new ArrayList<>();
            FavoriteHelper helper = new FavoriteHelper(context);
            helper.open();
            favoriteItems = helper.query();

            for (MovieItems movieItems: favoriteItems) {
                movieItemses.add(movieItems);
            }
            helper.close();
        }
        else {
            if (type.equals("search"))
                url = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.MOVIE_API_KEY + "&language=en-US&query=" + keyword;
            else if (type.equals("now_playing"))
                url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.MOVIE_API_KEY + "&language=en-US";
            else if (type.equals("upcoming"))
                url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.MOVIE_API_KEY + "&language=en-US";

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    setUseSynchronousMode(true);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject response = new JSONObject(result);
                        JSONArray list = response.getJSONArray("results");

                        for (int i=0; i<list.length(); i++) {
                            JSONObject movie = list.getJSONObject(i);
                            MovieItems movieItems = new MovieItems(movie);
                            movieItemses.add(movieItems);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
        return movieItemses;
    }

    protected void onReleaseResources(ArrayList<MovieItems> data) {
        // do nothing
    }
}
