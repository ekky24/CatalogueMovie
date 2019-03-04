package com.rino.ekky.cataloguemovie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReleaseTodayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ReleaseTodayAsync async = new ReleaseTodayAsync(context);
        async.execute();
    }

    public void setReleaseTodayAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private class ReleaseTodayAsync extends AsyncTask<Void, Void, ArrayList<MovieItems>> {
        Context context;

        public ReleaseTodayAsync(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<MovieItems> doInBackground(Void... voids) {
            String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.MOVIE_API_KEY + "&language=en-US";
            SyncHttpClient client = new SyncHttpClient();
            final ArrayList<MovieItems> movieItemses = new ArrayList<>();

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

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject movie = list.getJSONObject(i);
                            MovieItems movieItems = new MovieItems(movie);
                            movieItemses.add(movieItems);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
            return movieItemses;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItems> movieItems) {
            super.onPostExecute(movieItems);
            int i = 1;
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayDate = dateFormat.format(date);

            for (MovieItems movie : movieItems) {
                if (movie.getReleaseDateGeneric().equals(todayDate)) {
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(movie.getJudul())
                            .setContentText(movie.getJudul() + " " + context.getString(R.string.release_today_text))
                            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setAutoCancel(true);
                    manager.notify(i, builder.build());
                    i++;
                }
            }
        }
    }
}
