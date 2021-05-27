package com.codility.Story.app;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

public class StoryApp extends Application {


    public static SimpleCache simpleCache = null;

    //JzVideo
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();

        LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024);
        DatabaseProvider databaseProvider  = new ExoDatabaseProvider(this);

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, databaseProvider);
        }
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        StoryApp app = (StoryApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
