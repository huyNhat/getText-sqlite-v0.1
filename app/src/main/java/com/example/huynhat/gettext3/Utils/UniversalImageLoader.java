package com.example.huynhat.gettext3.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.huynhat.gettext3.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by huynhat on 2017-11-22.
 */

public class UniversalImageLoader {

    private static final int defaultImage = R.drawable.ic_camera;

    private Context context;

    public UniversalImageLoader(Context context){
        this.context= context;
    }

    public ImageLoaderConfiguration getConfig(){
        //Universal Image Loader Setup
        DisplayImageOptions defaulOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration= new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaulOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024)
                .build();

        return configuration;
    }

    public static void setImage(String imgURL, ImageView imageView){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgURL,imageView);

    }
}
