package com.codility.bubble_view_pager;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codility.Story.app.StoryApp;
import com.codility.Story.screens.StoryActivity;
import com.codility.directreplynotification.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class FriendsStoriesAdapter extends PagerAdapter {

    LayoutInflater mLayoutInflater;
    Context context;
    PlayerView storyDisplayVideo;
    private SimpleExoPlayer simpleExoPlayer = null;
    private DataSource.Factory mediaDataSourceFactory;
    ProgressBar storyDisplayVideoProgress;
    int currentPos = 0;
    ArrayList<String> videoList;
    View previous,next;
    View.OnClickListener onClickListener;

    public FriendsStoriesAdapter(Context context,ArrayList<String> videoList,View.OnClickListener onClickListener){
        this.videoList = videoList;
        this.context = context;
        this.onClickListener = onClickListener;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.item_video_view,container,false);
        currentPos = position;
        initializeViews(view);
        container.addView(view);
        return view;
    }

    private void initializeViews(View view) {
        storyDisplayVideo = view.findViewById(R.id.storyDisplayVideo);
        previous = view.findViewById(R.id.previous);
        next = view.findViewById(R.id.next);
        storyDisplayVideoProgress = view.findViewById(R.id.storyDisplayVideoProgress);
        storyDisplayVideo.setUseController(true);
        initializePlayer();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(next);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(previous);
            }
        });
    }

    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        } else {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        }

        mediaDataSourceFactory = new CacheDataSourceFactory(
                StoryApp.simpleCache,
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(
                                context,
                                Util.getUserAgent(context, context.getString(R.string.app_name))
                        )
                )
        );
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(videoList.get(currentPos)));

        simpleExoPlayer.prepare(mediaSource, false, false);
        simpleExoPlayer.setPlayWhenReady(true);

        storyDisplayVideo.setShutterBackgroundColor(Color.WHITE);
        storyDisplayVideo.setPlayer(simpleExoPlayer);

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e("TAG","onPlayerError : "+error.getLocalizedMessage());
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.e("TAG","isLoading : "+isLoading);
                if (isLoading) {
                    storyDisplayVideoProgress.setVisibility(View.VISIBLE);
                } else {
                    storyDisplayVideoProgress.setVisibility(View.GONE);
                    simpleExoPlayer.seekTo(5);
                    simpleExoPlayer.setPlayWhenReady(true);
                }
            }
        });
    }


}
