package com.rino.ekky.cataloguemovie;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Bitmap> widgetItems;
    private Context context;
    private int appWidgetId;

    public FavoriteRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        widgetItems = new ArrayList<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        Cursor c = context.getContentResolver().query(DatabaseContract.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);

        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            do {
                Bitmap bmp = null;
                try {
                    bmp = Glide.with(context)
                            .asBitmap()
                            .load(DatabaseContract.getColumnString(c, DatabaseContract.FavoriteColumns.IMAGE_PATH_BIG))
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    widgetItems.add(bmp);

                }catch (InterruptedException | ExecutionException e){
                    Log.d("Widget Load Error","error");
                }
            }
            while(c.moveToNext());
            c.close();
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        remoteViews.setImageViewBitmap(R.id.favorite_image_view_widget, widgetItems.get(position));

        Bundle bundle = new Bundle();
        bundle.putInt(FavoriteBannerWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.favorite_image_view_widget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
