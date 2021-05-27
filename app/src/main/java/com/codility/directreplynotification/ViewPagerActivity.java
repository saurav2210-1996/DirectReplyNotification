package com.codility.directreplynotification;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codility.CubeTransformer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViewPagerActivity extends AppCompatActivity {

    Handler handler = new Handler();
    ProgressBar progressBar;
    ProgressBar progressBar1;
    Runnable runnable;
    int i = 0;
    Boolean first = true;
    List<View> view = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        progressBar1 = findViewById(R.id.progressBar);
        view.clear();

        storageCheck();

        final MyViewPagerAdapter adapter = new MyViewPagerAdapter();
        final /*CustomViewPager*/ViewPager pager = findViewById(R.id.pager);
        pager.setPageTransformer(true, new CubeTransformer());
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //pager.setCurrentItem(3);
            }
        },1000);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (first && positionOffset == 0 && positionOffsetPixels == 0){
                    onPageSelected(0);
                    first = false;
                }
            }

            @Override
            public void onPageSelected(final int position) {
                Log.e("TAG","position = "+position);
                //pager.setCurrentItem(position);

                if(position < view.size() && view.get(position) != null){
                    adapter.initiate(view.get(position));
                }


                if((pager.getChildAt(position) != null)){
                    ((ProgressBar)pager.getChildAt(position).findViewById(R.id.progressBar)).setProgress(0);
                }

                i = 0;
                handler.removeCallbacks(runnable);
                runnable = new Runnable() {
                    @Override
                    public void run() {
//                        getActiveView(pager);
                        /*if((pager.getChildAt(position) != null)){
                            ((ProgressBar)pager.getChildAt(position).findViewById(R.id.progressBar)).setProgress(50 - i);
                        }else{
                            Log.e("TAG","null = "+position);
                            ((ProgressBar)getActiveView(pager).findViewById(R.id.progressBar)).setProgress(50 - i);
                        }*/
                        if(getActiveView(pager) != null){
                            ((ProgressBar)getActiveView(pager).findViewById(R.id.progressBar)).setProgress(50 - i);
                        }

                        //progressBar.setProgress(100 - i);
                        progressBar1.setProgress(50 - i);
                        handler.postDelayed(this,2000);
                        i++;
                    }
                };
                handler.post(runnable);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(ViewPagerActivity.this);
            View linearLayout = /*(RelativeLayout) */inflater.inflate(R.layout.view_pager_item, container, false);

            if(position == 0 && view.size() == 0){
                for(int i = 0;i<10;i++){
                    view.add(null);
                }
            }
            if(view.get(position) == null){
                view.add(position,linearLayout);
                container.addView(linearLayout);
                return linearLayout;
            }else{
                // index exists
                /*if(container.getChildAt(position) != null ){
                    container.removeViewAt(position);
                }*/

                container.addView(view.get(position));
                return view.get(position);
            }


            /*Log.e("TAG","load : "+position);
            container.addView(linearLayout);
            return linearLayout;*/
        }

        public void initiate(View linearLayout){
            progressBar = linearLayout.findViewById(R.id.progressBar);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public static View getActiveView(final ViewPager viewPager) {
        final PagerAdapter adapter = viewPager.getAdapter();
        if (null == adapter || adapter.getCount() == 0 || viewPager.getChildCount() == 0) {
            return null;
        }

        int position;
        final int currentPosition = viewPager.getCurrentItem();

        for (int i = 0; i < viewPager.getChildCount(); i++) {
            final View child = viewPager.getChildAt(i);
            final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();
            if (layoutParams.isDecor) {
                continue;
            }
            final Field positionField;
            try {
                positionField = ViewPager.LayoutParams.class.getDeclaredField("position");
                positionField.setAccessible(true);
                position = positionField.getInt(layoutParams);
            } catch (NoSuchFieldException e) {
                break;
            } catch (IllegalAccessException e) {
                break;
            }
            if (position == currentPosition) {
                return child;
            }
        }
        return null;
    }

    private void storageCheck() {
        //File appSpecificExternalDir = new File(this.getExternalFilesDir(), "Mayur");

        //To determine whether your app has been granted the MANAGE_EXTERNAL_STORAGE permission, call Environment.isExternalStorageManager().
        //https://developer.android.com/training/data-storage/manage-all-files
        //https://github.com/pg598595/ScopedStorageDemo

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Set<String> volumeNames = MediaStore.getExternalVolumeNames(this);
            String firstVolumeName = volumeNames.iterator().next();

        }


        File[] externalStorageVolumes = ContextCompat.getExternalFilesDirs(this, null);

        getFilesDir(); //get path of like(/data/user/0/com.codility.directreplynotification/files)
        getExternalFilesDir("Mayur"); //return or create file(/storage/emulated/0/Android/data/com.codility.directreplynotification/files/Mayur)
        getCacheDir();//return cache dir of internal storage(/data/user/0/com.codility.directreplynotification/cache)
        getExternalCacheDir();//return cache dir of primary external storage(/storage/emulated/0/Android/data/com.codility.directreplynotification/cache)



        /*---------*/

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Mayur");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }

    }


}
