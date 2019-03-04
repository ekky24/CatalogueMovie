package com.rino.ekky.cataloguemovie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService {
    public StackWidgetService() {
    }

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
