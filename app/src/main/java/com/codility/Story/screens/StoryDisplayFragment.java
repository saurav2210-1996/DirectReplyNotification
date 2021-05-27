package com.codility.Story.screens;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codility.Story.app.StoryApp;
import com.codility.Story.customview.StoriesProgressView;
import com.codility.Story.data.Story;
import com.codility.Story.data.StoryUser;
import com.codility.Story.utils.OnSwipeTouchListener;
import com.codility.directreplynotification.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.jzvd.JzvdStd;

public class StoryDisplayFragment extends Fragment implements StoriesProgressView.StoriesListener{

    private static String EXTRA_POSITION = "EXTRA_POSITION";
    private static String EXTRA_STORY_USER = "EXTRA_STORY_USER";

    private SimpleExoPlayer simpleExoPlayer = null;
    private DataSource.Factory mediaDataSourceFactory;
    private PageViewOperator pageViewOperator = null;
    private int counter = 0;
    private long pressTime = 0L;
    private long limit = 500L;
    private boolean onResumeCalled = false;
    private boolean onVideoPrepared = false;

    PlayerView storyDisplayVideo;
    AppCompatImageView storyDisplayImage,storyDisplayProfilePicture;
    ProgressBar storyDisplayVideoProgress;
    View previous,next;
    LinearLayout storyDisplayProfile;
    TextView storyDisplayNick,storyDisplayTime;
    StoriesProgressView storiesProgressView;
    FrameLayout storyOverlay;
    ImageView ivBackground;

    private int position = 0;
    String storyUser;
    //ArrayList<Story> stories;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        position = bundle.getInt(EXTRA_POSITION);
        storyUser = bundle.getString(EXTRA_STORY_USER);
        //stories = storyUser.getStories();
        Log.e("TAG","position : "+position);
        return inflater.inflate(R.layout.fragment_story_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        storyDisplayVideo.setUseController(false);
        updateStory();
        setUpUi();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.pageViewOperator = (PageViewOperator)activity;
        }catch (Exception e){
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        counter = restorePosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        onResumeCalled = true;
        if (storyUser.contains(".mp4") && !onVideoPrepared) {
            if(simpleExoPlayer != null)simpleExoPlayer.setPlayWhenReady(false);
            return;
        }

        if(simpleExoPlayer != null){
            simpleExoPlayer.seekTo(5);
            simpleExoPlayer.setPlayWhenReady(true);
        }
        if (counter == 0) {
            storiesProgressView.startStories();
            Log.e("TAG","onResume startStories() : "+position);
        } else {
            // restart animation
            int x = 0;
            if(getArguments() == null){
                x = getArguments().getInt(EXTRA_POSITION);
            }
            counter = StoryActivity.progressState.get(x);
            storiesProgressView.startStories(counter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer != null)simpleExoPlayer.setPlayWhenReady(false);
        storiesProgressView.abandon();
    }

    @Override
    public void onNext() {
        if (1 <= counter + 1) {
            return;
        }
        ++counter;
        savePosition(counter);
        updateStory();
    }

    @Override
    public void onPrev() {
        if (counter - 1 < 0){
            return;
        }
        --counter;
        savePosition(counter);
        updateStory();
    }

    @Override
    public void onComplete() {
        if(simpleExoPlayer != null)simpleExoPlayer.release();
        pageViewOperator.nextPageView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(simpleExoPlayer != null)simpleExoPlayer.release();
    }

    private int restorePosition() {
        return StoryActivity.progressState.get(position);
    }

    private void savePosition(int pos) {
        StoryActivity.progressState.put(position, pos);
    }

    private void updateStory() {
        if(simpleExoPlayer != null)simpleExoPlayer.stop();
        if (storyUser.contains(".mp4")) {
            storyDisplayVideo.setVisibility(View.VISIBLE);
            ivBackground.setVisibility(View.VISIBLE);
            storyDisplayImage.setVisibility(View.GONE);
            storyDisplayVideoProgress.setVisibility(View.VISIBLE);
            initializePlayer();
        } else {
            storyDisplayVideo.setVisibility(View.GONE);
            storyDisplayVideoProgress.setVisibility(View.GONE);
            ivBackground.setVisibility(View.GONE);
            storyDisplayImage.setVisibility(View.VISIBLE);

            Glide.with(this).load(storyUser).into(storyDisplayImage);
        }

        storyDisplayTime.setText("Test");
    }

    private void initializePlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext());
        } else {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext());
        }

        mediaDataSourceFactory = new CacheDataSourceFactory(
                StoryApp.simpleCache,
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(
                                getContext(),
                                Util.getUserAgent(requireContext(), getString(R.string.app_name))
                        )
                )
        );
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(storyUser));

        simpleExoPlayer.prepare(mediaSource, false, false);
        if (onResumeCalled) {
            simpleExoPlayer.setPlayWhenReady(true);
        }

        storyDisplayVideo.setShutterBackgroundColor(Color.BLACK);
        storyDisplayVideo.setPlayer(simpleExoPlayer);

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                storyDisplayVideoProgress.setVisibility(View.GONE);
                if (counter == 0) {
                    pageViewOperator.nextPageView();
                } else {
                    storiesProgressView.skip();
                }
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                if (isLoading) {
                    storyDisplayVideoProgress.setVisibility(View.VISIBLE);
                    pressTime = System.currentTimeMillis();
                    pauseCurrentStory();
                } else {
                    storyDisplayVideoProgress.setVisibility(View.GONE);
                    storiesProgressView.getProgressWithIndex(counter).setDuration(simpleExoPlayer.getDuration());
                    onVideoPrepared = true;
                    resumeCurrentStory();
                }
            }
        });
    }

    private void setUpUi() {
        View.OnTouchListener touchListener = new OnSwipeTouchListener(getActivity()){
            @Override
            public void onClick(@NotNull View view) {
                super.onClick(view);
                if(view == next){
                    pageViewOperator.nextPageView();
                }else if(view == previous){
                    pageViewOperator.backPageView();
                }
            }

            @Override
            public void onLongClick() {
                hideStoryOverlay();
            }

            @Override
            public boolean onTouchView(@NotNull View view, @NotNull MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    pressTime = System.currentTimeMillis();
                    pauseCurrentStory();
                    return false;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    showStoryOverlay();
                    resumeCurrentStory();
                    return limit < System.currentTimeMillis() - pressTime;
                }
                return false;
            }
        };

        previous.setOnTouchListener(touchListener);
        next.setOnTouchListener(touchListener);

        storiesProgressView.setStoriesCountDebug(
                1, position = getArguments().getInt(EXTRA_POSITION)
        );
        storiesProgressView.setAllStoryDuration(4000L);
        storiesProgressView.setStoriesListener(this);

        Glide.with(this).load("https://randomuser.me/api/portraits/men/11.jpg").into(storyDisplayProfilePicture);
        storyDisplayNick.setText("Test");
    }

    private void showStoryOverlay() {
        if (storyOverlay == null || storyOverlay.getAlpha() != 0F) {
            return;
        }

                storyOverlay.animate()
                        .setDuration(100)
                        .alpha(1F)
                        .start();
    }

    private void hideStoryOverlay() {
        if (storyOverlay == null || storyOverlay.getAlpha() != 1F){
            return;
        }
                storyOverlay.animate()
                        .setDuration(200)
                        .alpha(0F)
                        .start();
    }

    void pauseCurrentStory() {
        if(simpleExoPlayer != null)simpleExoPlayer.setPlayWhenReady(false);
        storiesProgressView.pause();
        Log.e("TAG","pauseCurrentStory() : "+position);
    }


    private void init(View view) {
        storyDisplayVideo = view.findViewById(R.id.storyDisplayVideo);
        storyDisplayImage = view.findViewById(R.id.storyDisplayImage);
        storyDisplayProfilePicture = view.findViewById(R.id.storyDisplayProfilePicture);
        storyDisplayVideoProgress = view.findViewById(R.id.storyDisplayVideoProgress);
        previous = view.findViewById(R.id.previous);
        next = view.findViewById(R.id.next);
        storyDisplayProfile = view.findViewById(R.id.storyDisplayProfile);
        storyDisplayNick = view.findViewById(R.id.storyDisplayNick);
        storyDisplayTime = view.findViewById(R.id.storyDisplayTime);
        storiesProgressView = view.findViewById(R.id.storiesProgressView);
        storyOverlay = view.findViewById(R.id.storyOverlay);
        ivBackground = view.findViewById(R.id.ivBackground);
    }

    void resumeCurrentStory() {
        if (onResumeCalled) {
            if(simpleExoPlayer != null)simpleExoPlayer.setPlayWhenReady(true);
            showStoryOverlay();
            Log.e("TAG","resumeCurrentStory() : "+position);
            storiesProgressView.resume();
        }
    }

    public static StoryDisplayFragment newInstance(int position,String story) {
        StoryDisplayFragment fragment = new StoryDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_POSITION, position);
        bundle.putString(EXTRA_STORY_USER, story);
        fragment.setArguments(bundle);
        return fragment;
    }

}
