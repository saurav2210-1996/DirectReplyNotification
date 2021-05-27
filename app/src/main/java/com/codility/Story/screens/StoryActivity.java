package com.codility.Story.screens;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.ActionMode;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codility.Story.app.StoryApp;
import com.codility.Story.customview.FixedViewPager;
import com.codility.Story.customview.StoryPagerAdapter;
import com.codility.Story.data.Story;
import com.codility.Story.data.StoryUser;
import com.codility.Story.utils.StoryGenerator;
import com.codility.bubble_view_pager.FriendsStoriesAdapter;
import com.codility.custom_view.CustomNonSwipeableViewPager;
import com.codility.custom_view.NoGifEditText;
import com.codility.directreplynotification.R;
import com.codility.retrofit.APIClient;
import com.codility.retrofit.APIInterface;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.saurav.Calculator;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.crypto.Cipher;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import kotlin.random.Random;
import kotlinx.coroutines.GlobalScope;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ClipDescription.MIMETYPE_TEXT_HTML;
import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class StoryActivity extends AppCompatActivity implements PageViewOperator{

    FixedViewPager viewPager;
    private StoryPagerAdapter pagerAdapter;
    private int currentPage = 0;
    public static SparseIntArray progressState = new SparseIntArray();
    ProgressBar progressBar;

    int x= 0;
    Handler handler = new Handler();
    Runnable runnable;
    int size = 2;
    WebView webView;

    CustomNonSwipeableViewPager swipePager;
    int currentPos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        //setUpPager();
        //compressVideo();


        //((EditText)findViewById(R.id.etCompanyName)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //((EditText)findViewById(R.id.etCompanyName)).setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

        //workOnKeyboardType();
        //workOnRecyclerView();
        //workWithWebView();
        //workwithStoryVideoViewPagerOfBubble();
        //playVideo();
        //playvideoOnJzvdStd();
        callApi();
        //getCallDetails();

        Calculator c = new Calculator();
        Log.e("TAG","SUM : "+c.sum(5,6));
    }

    private void getCallDetails() {
        String[] perms = {Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG};
        ActivityCompat.requestPermissions(this, perms, 123);

        JSONArray jsonArray = new JSONArray();

        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        Cursor managedCursor = getContentResolver().query(contacts, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {

            HashMap rowDataCall = new HashMap<String, String>();

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            String callDayTime = new Date(Long.valueOf(callDate)).toString();
            // long timestamp = convertDateToTimestamp(callDayTime);
            String callDuration = managedCursor.getString(duration);
            String callName = managedCursor.getString(name);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            Log.e("TAG","Phone Number:--- "+phNumber +"Call Type:--- " + dir + "Call Date:--- " + callDayTime +"Call duration in sec :--- "+callDuration +" callName : "+callName);
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");

            try {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String stringDate = formatter.format(new Date(Long.valueOf(callDate)));
                Date callDateTime = formatter.parse(stringDate);

                String fromDate = "2021-05-14 05:32:29";
                Date fromDate_ = formatter.parse(fromDate);

                String today = formatter.format(Calendar.getInstance().getTime());
                Date toDate_ = formatter.parse(today);

                /*String toDate = "2021-05-14 15:32:29";
                Date toDate_ = formatter.parse(toDate);*/

                if(callDateTime.after(fromDate_) && callDateTime.before(toDate_)){
                    Log.e("TAG","Check date : "+callDateTime);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type",dir);
                    jsonObject.put("leadName",phNumber);
                    jsonObject.put("callDuration",callDuration);
                    jsonObject.put("date",callDayTime);
                    jsonObject.put("header",callName);

                    jsonArray.put(jsonObject);
                }


            }catch (Exception e){
                Log.e("TAG","ERROR : "+e.getLocalizedMessage());
            }

        }
        Log.e("TAG","data : "+jsonArray.toString());
        managedCursor.close();
        //System.out.println(sb);
    }


        private void callApi() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        /*Call<ResponseBody> call = apiInterface.getData("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7Il9pZCI6IjYwNzU1NDBhOTM2Y2QwMWM0NDMyN2IxOCIsInVzZXJUeXBlIjoiYWRtaW4iLCJwaG9uZSI6IjcwMTYyNDYwMjMifSwiaWF0IjoxNjIwNDAwNzMyfQ.TwuvTVOLNo3qGs5r1FzPWjPK3IrQpi_b0YXE6ewWgoM");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("TAG","response "+response.code());
                try {

                    Log.e("TAG","response "+response.body().byteStream().available());
                    Log.e("TAG","response "+response.body().byteStream().read());
                    //writeToFile(response.body().string(),response.body().bytes(),StoryActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //response.headers().namesAndValues[13]
                //response.headers().get("Content-Disposition")
                //response.body.contentType()
                //response.body.byteStream()
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG","error : "+call.request().body());
            }
        });*/


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        try {
            url = new URL("https://digitaleximapp.com/backup/db");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7Il9pZCI6IjYwNzU1NDBhOTM2Y2QwMWM0NDMyN2IxOCIsInVzZXJUeXBlIjoiYWRtaW4iLCJwaG9uZSI6IjcwMTYyNDYwMjMifSwiaWF0IjoxNjIwNDAwNzMyfQ.TwuvTVOLNo3qGs5r1FzPWjPK3IrQpi_b0YXE6ewWgoM");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            String fileName="";
            String raw = urlConnection.getHeaderField("Content-Disposition");
            if(raw != null && raw.indexOf("=") != -1) {
                fileName = raw.split("=")[1]; //getting value after '='
            }
            Log.e("TAG", "fileName: " + fileName);
            //File path = new File("/sdcard/Test App");
            File path = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS),
                    "Test App"
            );


            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, fileName);

            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();

        } catch (Exception e) {
            Log.e("TAG", "File write failed: " + e.toString());
            e.printStackTrace();
        }
    }

    private void writeToFile(String string,byte[] data,Context context) {


        /*String filename = "digitalExim-2021-05-11-11-29-35.tgz";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data);
            outputStream.close();
            Log.e("apples", "addStat: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "File write failed: " + e.toString());
        }*/

        /*try {
            File path = new File("/sdcard/Test App");
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(path, "digitalExim-2021-05-11-11-29-35.tgz");
            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(data);
            } finally {
                stream.close();
            }
        }
        catch (IOException e) {
            Log.e("TAG", "File write failed: " + e.toString());
        }*/

        /*try {
            File path = new File("/sdcard/Test App");
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(path, "digitalExim-2021-05-11-11-29-35.tgz");

            //if file exist, it will be replace in default
            FileOutputStream os= new FileOutputStream(file, false); //sequence byte of file no buffer
            BufferedOutputStream buffOutStream= new BufferedOutputStream(os, 1024); //buffer for char 1024 char buffer
            //

            buffOutStream.write(data,0, data.length);
            buffOutStream.flush();
            buffOutStream.close();

        } catch (Exception e) {
            Log.e("TAG", "File write failed: " + e.toString());
        }*/

        /*try {
            File path = new File("/sdcard/Test App");
            if (!path.exists()) {
                path.mkdirs();
            }

            File file = new File(path, "digitalExim-2021-05-11-11-29-35.tgz");
            FileWriter writer = new FileWriter(file);
            writer.append(string);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("TAG", "File write failed: " + e.toString());
        }*/
    }

    private void playvideoOnJzvdStd() {
        JzvdStd jzvdStd =  findViewById(R.id.jz_video);

        String proxyUrl = StoryApp.getProxy(this).getProxyUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        JZDataSource jzDataSource = new JZDataSource(proxyUrl, "");
        //jzDataSource.looping = true;
        //jzDataSource.currentUrlIndex = 2;
        jzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_NORMAL);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT/*VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP*/);
        //Jzvd.PROGRESS_DRAG_RATE = 2f;//设置播放进度条手势滑动阻尼系数
        Glide.with(this).load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640").into(jzvdStd.posterImageView);

        //Jzvd.goOnPlayOnResume();
        jzvdStd.startVideo();
    }

    private void playVideo() {
        PlayerView storyDisplayVideo;
        SimpleExoPlayer simpleExoPlayer = null;
        DataSource.Factory mediaDataSourceFactory;

        storyDisplayVideo = findViewById(R.id.storyDisplayVideo);
        storyDisplayVideo.setUseController(true);

        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        } else {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        }

        mediaDataSourceFactory = new CacheDataSourceFactory(
                StoryApp.simpleCache,
                new DefaultHttpDataSourceFactory(
                        Util.getUserAgent(
                                this,
                                Util.getUserAgent(this, getString(R.string.app_name))
                        )
                )
        );
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse("https://d36tfeg9eo7a1o.cloudfront.net/post_images/60924db563133.mp4"));

        simpleExoPlayer.prepare(mediaSource, false, false);
        simpleExoPlayer.setPlayWhenReady(true);

        storyDisplayVideo.setShutterBackgroundColor(Color.BLACK);
        storyDisplayVideo.setPlayer(simpleExoPlayer);

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                com.google.android.exoplayer2.util.Log.e("TAG","onPlayerError : "+error.getLocalizedMessage());
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                com.google.android.exoplayer2.util.Log.e("TAG","isLoading : "+isLoading);
                if (isLoading) {

                } else {

                    /*simpleExoPlayer.seekTo(5);
                    simpleExoPlayer.setPlayWhenReady(true);*/
                }
            }
        });
    }

    private void workwithStoryVideoViewPagerOfBubble() {

        ArrayList<String> videoList = new ArrayList<String>();
        videoList.add("https://d36tfeg9eo7a1o.cloudfront.net/post_images/60924db563133.mp4");
        videoList.add("https://d36tfeg9eo7a1o.cloudfront.net/post_images/60924e96db5ae.mp4");
        videoList.add("https://d36tfeg9eo7a1o.cloudfront.net/post_images/60927d1f2e2f5.mp4");
        videoList.add("https://d36tfeg9eo7a1o.cloudfront.net/post_images/60938e679bb26.mp4");
        StoryGenerator.preLoadVideos(videoList,getApplicationContext());
        swipePager = findViewById(R.id.swipePager);
        FriendsStoriesAdapter mFriendsStoriesAdapter = new FriendsStoriesAdapter(this,videoList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.next){
                    currentPos = currentPos+1;
                    swipePager.setCurrentItem(currentPos);
                }else{
                    currentPos = currentPos-1;
                    swipePager.setCurrentItem(currentPos);
                }
                Log.e("TAG","Click onPageSelected : "+currentPage);
            }
        });

        swipePager.setOffscreenPageLimit(0);
        swipePager.setAdapter(mFriendsStoriesAdapter);
        swipePager.setCurrentItem(0);
    }

    private void workOnKeyboardType() {

        ((EditText)findViewById(R.id.etCompanyName)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        int[] devicesIds = InputDevice.getDeviceIds();
        for (int deviceId : devicesIds) {
            //Check the device you want
            InputDevice device = InputDevice.getDevice(deviceId);
            //device.getName must to have virtual
            //That check the keyboard type
            Log.e("TAG","KeyboardType : "+device.getKeyboardType());
            device.getKeyboardType(); //returns an int
        }

        //check current keyboard typr
        String oldDefaultKeyboard = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        Log.e("TAG","oldDefaultKeyboard : "+oldDefaultKeyboard);

        ///open system config to change keyboard type
        /*InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        imeManager.showInputMethodPicker();*/
    }

    private void workWithWebView() {
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl("http://3.139.190.86:8069/");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e("TAG","shouldOverrideUrlLoading");
            Log.e("TAG","shouldOverrideUrlLoading view : "+view.getOriginalUrl());
            Log.e("TAG","shouldOverrideUrlLoading view : "+view.getTitle());
            Log.e("TAG","shouldOverrideUrlLoading view : "+view.getHitTestResult());
            Log.e("TAG","shouldOverrideUrlLoading request : "+request.getMethod());
            Log.e("TAG","shouldOverrideUrlLoading request : "+request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override public void onPageFinished(WebView view, String url) {
            Log.e("TAG","onPageFinished");
            String cookies = CookieManager.getInstance().getCookie(url);
            Log.e("TAG", "All the cookies in a string:" + cookies);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.e("TAG","onReceivedError");
            super.onReceivedError(view, request, error);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Log.e("TAG","onJsAlert");
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            Log.e("TAG","onJsConfirm");
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                  final JsPromptResult result) {
            Log.e("TAG","onJsPrompt");
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.copyBackForwardList().getCurrentIndex() > 0) {
            webView.goBack();
        }
        else {
            // Your exit alert code, or alternatively line below to finish
            super.onBackPressed(); // finishes activity
        }
    }

    private void workOnRecyclerView() {

        class TempAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

            int size;
            public TempAdapter(int size) {
                this.size = size;
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edittext,parent,false);
                return new MyViewHolder(view,new MyCustomEditTextListener());
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if(holder instanceof MyViewHolder){
                    MyViewHolder holder1 = (MyViewHolder)holder;
                    //holder1.editText
                    holder1.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
                }
            }

            @Override
            public int getItemCount() {
                return this.size;
            }

            class MyViewHolder extends RecyclerView.ViewHolder {
                EditText editText;
                public MyCustomEditTextListener myCustomEditTextListener;
                @RequiresApi(api = Build.VERSION_CODES.M)
                public MyViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener) {
                    super(itemView);
                    editText = itemView.findViewById(R.id.editText);
                    this.myCustomEditTextListener = myCustomEditTextListener;
                    this.editText.addTextChangedListener(myCustomEditTextListener);

                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("TAG","Click : "+getAdapterPosition());
                            LinearLayoutManager layoutManager = (LinearLayoutManager) ((RecyclerView)findViewById(R.id.recyclerView)).getLayoutManager();
                            layoutManager.scrollToPositionWithOffset(getAdapterPosition(), 0);
                            editText.setFocusableInTouchMode(true);
                            Toast.makeText(StoryActivity.this,"Position : "+getAdapterPosition(),Toast.LENGTH_LONG).show();
                            editText.post(() -> {
                                editText.requestFocus();

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                            });
                        }
                    });

                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                editText.setFocusableInTouchMode(false);
                                //UiUtils.hideKeyboard();
                            }
                        }
                    });

                    /*editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                case android.R.id.copy:
                                    Log.e("TAG","copy");
                                    int min = 0;
                                    int max = editText.getText().length();
                                    if (editText.isFocused()) {
                                        final int selStart = editText.getSelectionStart();
                                        final int selEnd = editText.getSelectionEnd();

                                        min = Math.max(0, Math.min(selStart, selEnd));
                                        max = Math.max(0, Math.max(selStart, selEnd));
                                    }
                                    // Perform your definition lookup with the selected text
                                    final CharSequence selectedText = editText.getText()
                                            .subSequence(min, max);
                                    String text = selectedText.toString();
                                    Toast.makeText(getApplicationContext(), "Text Copied",
                                            Toast.LENGTH_SHORT).show();
                                    // Finish and close the ActionMode
                                    mode.finish();
                                    return true;
                                case android.R.id.cut:
                                    Log.e("TAG","cut");
                                    // add your custom code to get cut functionality according
                                    // to your requirement
                                    return true;
                                case android.R.id.paste:
                                    Log.e("TAG","paste");
                                    // add your custom code to get paste functionality according
                                    // to your requirement
                                    return true;

                                default:
                                    break;
                            }
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }
                    });*/

                    editText.setCustomInsertionActionModeCallback(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                case android.R.id.copy:
                                    Log.e("TAG","copy1");
                                    int min = 0;
                                    int max = editText.getText().length();
                                    if (editText.isFocused()) {
                                        final int selStart = editText.getSelectionStart();
                                        final int selEnd = editText.getSelectionEnd();

                                        min = Math.max(0, Math.min(selStart, selEnd));
                                        max = Math.max(0, Math.max(selStart, selEnd));
                                    }
                                    // Perform your definition lookup with the selected text
                                    final CharSequence selectedText = editText.getText()
                                            .subSequence(min, max);
                                    String text = selectedText.toString();
                                    Toast.makeText(getApplicationContext(), "Text Copied",
                                            Toast.LENGTH_SHORT).show();
                                    // Finish and close the ActionMode
                                    mode.finish();
                                    return true;
                                case android.R.id.cut:
                                    Log.e("TAG","cut1");
                                    // add your custom code to get cut functionality according
                                    // to your requirement
                                    return true;
                                case android.R.id.paste:
                                    Log.e("TAG","paste1");

                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    String pasteData = "";
                                    //check if something present in clipboard, and check if it is text
                                    if (clipboard.hasPrimaryClip() && (clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN) || clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_HTML))) {
                                        ClipData.Item item1 = clipboard.getPrimaryClip().getItemAt(0);
                                        pasteData = item1.getText().toString();
                                        editText.setText(pasteData);
                                    }

                                    mode.finish();
                                    return true;

                                default:
                                    break;
                            }
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }
                    });
                }
            }

            class MyCustomEditTextListener implements TextWatcher {
                private int position;

                public void updatePosition(int position) {
                    this.position = position;
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    Log.e("TAG","onTextChanged : "+position);
                    if(size == position+1){
                        //size = size + 1;
                        //notifyItemChanged();
                        //notifyItemInserted(0);
                        //notifyItemChanged(size-1);
                        /*editText.setFocusableInTouchMode(true);
                        editText.post(() -> {
                            editText.requestFocus();

                        });*/
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            }
        }

        ((RecyclerView)findViewById(R.id.recyclerView)).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView)findViewById(R.id.recyclerView)).setHasFixedSize(true);
        ((RecyclerView)findViewById(R.id.recyclerView)).setAdapter(new TempAdapter(size));
    }

    private void setUpPager() {
        viewPager = findViewById(R.id.viewPager);
        progressBar = findViewById(R.id.progressBar);

        //ArrayList<StoryUser> storyUserList = StoryGenerator.generateStories();
        ArrayList<String> storyUserList = getList();
        preLoadStories(storyUserList);

        pagerAdapter = new StoryPagerAdapter(getSupportFragmentManager(), storyUserList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPage);
        //viewPager.setPageTransformer(true, CubeOutTransformer());
        viewPager.addOnPageChangeListener(new PageChangeListener() {
            @Override
            public void onPageScrollCanceled() {
                currentFragment().resumeCurrentStory();
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
            }
        });
    }

    private void preLoadStories(ArrayList<String> storyUserList) {
        ArrayList<String> imageList = new ArrayList<String>();
        ArrayList<String> videoList = new ArrayList<String>();

        /*for(int i = 0;i<storyUserList.size();i++){
            for(int j =0;j<storyUserList.get(i).getStories().size();j++){
                if (storyUserList.get(i).getStories().get(j).isVideo()) {
                    videoList.add(storyUserList.get(i).getStories().get(j).getUrl());
                } else {
                    imageList.add(storyUserList.get(i).getStories().get(j).getUrl());
                }
            }
        }*/
        for(int i = 0;i<storyUserList.size();i++){
            if (storyUserList.get(i).contains(".mp4")) {
                videoList.add(storyUserList.get(i));
            } else {
                imageList.add(storyUserList.get(i));
            }
        }
        StoryGenerator.preLoadVideos(videoList,getApplicationContext());
        preLoadImages(imageList);
    }

    private void preLoadImages(ArrayList<String> imageList) {
        for(int i = 0;i<imageList.size();i++){
            Glide.with(this).load(imageList.get(i)).preload();
        }
    }

    private StoryDisplayFragment currentFragment() {
        return (StoryDisplayFragment)pagerAdapter.findFragmentByPosition(viewPager, currentPage);
    }


    @Override
    public void backPageView() {
        viewPager.setCurrentItem(currentPage-1);
    }

    @Override
    public void nextPageView() {
        viewPager.setCurrentItem(currentPage+1);
    }

    private int prevDragPosition = 0;

    ArrayList<StoryUser> generateStories() {
        ArrayList<String> storyUrls = new ArrayList<String>();
        /*storyUrls.add("https://player.vimeo.com/external/403295268.sd.mp4?s=3446f787cefa52e7824d6ce6501db5261074d479&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/409206405.sd.mp4?s=0bc456b6ff355d9907f285368747bf54323e5532&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/403295710.sd.mp4?s=788b046826f92983ada6e5caf067113fdb49e209&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/394678700.sd.mp4?s=353646e34d7bde02ad638c7308a198786e0dff8f&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/405333429.sd.mp4?s=dcc3bdec31c93d87c938fc6c3ef76b7b1b188580&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/363465031.sd.mp4?s=15b706ccd3c0e1d9dc9290487ccadc7b20fff7f1&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/422787651.sd.mp4?s=ec96f3190373937071ba56955b2f8481eaa10cce&profile_id=165&oauth2_token_id=57447761");*/

        storyUrls.add("https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1122868/pexels-photo-1122868.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1837603/pexels-photo-1837603.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1612461/pexels-photo-1612461.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1591382/pexels-photo-1591382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/53757/pexels-photo-53757.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        storyUrls.add("https://images.pexels.com/photos/134020/pexels-photo-134020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1367067/pexels-photo-1367067.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        storyUrls.add("https://images.pexels.com/photos/2217365/pexels-photo-2217365.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2260800/pexels-photo-2260800.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1719344/pexels-photo-1719344.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/364096/pexels-photo-364096.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/3849168/pexels-photo-3849168.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2953901/pexels-photo-2953901.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/3538558/pexels-photo-3538558.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2458400/pexels-photo-2458400.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");


        ArrayList<String> userProfileUrls = new ArrayList<String>();
        userProfileUrls.add("https://randomuser.me/api/portraits/women/1.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/1.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/2.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/2.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/3.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/3.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/4.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/4.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/5.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/5.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/6.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/6.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/7.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/7.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/8.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/8.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/9.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/9.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/10.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/10.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/women/11.jpg");
        userProfileUrls.add("https://randomuser.me/api/portraits/men/11.jpg");

        ArrayList<StoryUser> storyUserList = new ArrayList<StoryUser>();
        for (int i = 0;i<10 ;i++) {
            ArrayList<Story> stories = new ArrayList<Story>();
            int storySize = 1;
            for (int j = 0 ;j < storySize;j++) {
                stories.add(new Story(storyUrls.get(2), System.currentTimeMillis() - (1 * (24 - j) * 60 * 60 * 1000)));
            }
            storyUserList.add(
                    new StoryUser(
                            "username$i",
                            userProfileUrls.get(1),
                            stories
                    )
            );
        }

        return storyUserList;
    }

    ArrayList<String> getList(){

        ArrayList<String> storyUrls = new ArrayList<String>();
        storyUrls.add("https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        //storyUrls.add("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        storyUrls.add("https://player.vimeo.com/external/403295268.sd.mp4?s=3446f787cefa52e7824d6ce6501db5261074d479&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/409206405.sd.mp4?s=0bc456b6ff355d9907f285368747bf54323e5532&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/403295710.sd.mp4?s=788b046826f92983ada6e5caf067113fdb49e209&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/394678700.sd.mp4?s=353646e34d7bde02ad638c7308a198786e0dff8f&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/405333429.sd.mp4?s=dcc3bdec31c93d87c938fc6c3ef76b7b1b188580&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/363465031.sd.mp4?s=15b706ccd3c0e1d9dc9290487ccadc7b20fff7f1&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://player.vimeo.com/external/422787651.sd.mp4?s=ec96f3190373937071ba56955b2f8481eaa10cce&profile_id=165&oauth2_token_id=57447761");
        storyUrls.add("https://images.pexels.com/photos/1433052/pexels-photo-1433052.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1366630/pexels-photo-1366630.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1067333/pexels-photo-1067333.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1122868/pexels-photo-1122868.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1837603/pexels-photo-1837603.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1612461/pexels-photo-1612461.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1591382/pexels-photo-1591382.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/53757/pexels-photo-53757.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        storyUrls.add("https://images.pexels.com/photos/134020/pexels-photo-134020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1367067/pexels-photo-1367067.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500");
        storyUrls.add("https://images.pexels.com/photos/2217365/pexels-photo-2217365.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2260800/pexels-photo-2260800.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/1719344/pexels-photo-1719344.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/364096/pexels-photo-364096.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/3849168/pexels-photo-3849168.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2953901/pexels-photo-2953901.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/3538558/pexels-photo-3538558.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        storyUrls.add("https://images.pexels.com/photos/2458400/pexels-photo-2458400.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");

        return  storyUrls;
    }

    String ORIGINAL_VIDEO_PATH="";
    private void compressVideo(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permsRequestCode = 200;
        ActivityCompat.requestPermissions(this,perms, permsRequestCode);

        try {
            ORIGINAL_VIDEO_PATH = "/sdcard/Bubble/Video/PostVideos/1724538161.mp4";
            //ORIGINAL_VIDEO_PATH = "/sdcard/Bubble/Video/PostVideos/VID_20210420_123048.738.mp4";
            Log.e("TAG", "ORIGINAL_VIDEO_PATH :"+ORIGINAL_VIDEO_PATH);
            File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            com.codility.video_compress.VideoCompressor.start(getApplicationContext(),
                    Uri.fromFile(new File(ORIGINAL_VIDEO_PATH)),
                    ORIGINAL_VIDEO_PATH,
                    outputFile.getPath()+File.separator+"Xender"+File.separator+"VID_20210420_123048.738.mp4",
                    new com.codility.video_compress.CompressionListener() {
                        @Override
                        public void onStart() {
                            Log.e("TAG", "onStart");
                        }

                        @Override
                        public void onSuccess() {
                            //ORIGINAL_VIDEO_PATH = outputFile.getPath();
                            Log.e("TAG", "After ORIGINAL_VIDEO_PATH :"+outputFile.getPath()+File.separator+"Xender"+File.separator+"1724538161.mp4");
                        }

                        @Override
                        public void onFailure(@NotNull String s) {
                            Log.e("TAG", "onFailure "+s);
                        }

                        @Override
                        public void onProgress(float v) {

                        }

                        @Override
                        public void onCancelled() {
                            Log.e("TAG", "onCancelled");
                        }
                    }, com.codility.video_compress.VideoQuality.MEDIUM,false,false
            );
        }catch (Exception e){

        }
    }

}
