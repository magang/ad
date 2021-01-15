package com.chanlin.ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.chanlin.ad.data.Trade;
import com.chanlin.ad.manager.QDSkinManager;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import java.io.File;
import java.io.IOException;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;

/**
 * @author dustforest
 */
public class QDApplication extends MultiDexApplication {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        QMUILog.setDelegete(new QMUILog.QMUILogDelegate() {
            @Override
            public void e(String tag, String msg, Object... obj) {
                Log.e(tag, msg);
            }

            @Override
            public void w(String tag, String msg, Object... obj) {
                Log.w(tag, msg);
            }

            @Override
            public void i(String tag, String msg, Object... obj) {

            }

            @Override
            public void d(String tag, String msg, Object... obj) {

            }

            @Override
            public void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {

            }
        });
        QMUISwipeBackActivityManager.init(this);
        QDSkinManager.install(this);

        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
        AVOSCloud.initialize(this, "gbk2tajd9ed82kag7gpb553q4jg1xsect5pfbgqerieljwzj",
                "wd49cigwgbyq7o3k3iq25zn30tizjqwrzn0u6c46d4epm35r",
                "https://gbk2tajd.lc-cn-n1-shared.com");
        AVObject.registerSubclass(Trade.class);

        try {
            initImageLoader(this);
        } catch (IOException e) {
            Toast.makeText(this, "磁盘空间不足", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if ((newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
//            QDSkinManager.changeSkin(QDSkinManager.SKIN_DARK);
//        } else if (QDSkinManager.getCurrentSkin() == QDSkinManager.SKIN_DARK) {
//            QDSkinManager.changeSkin(QDSkinManager.SKIN_BLUE);
//        }

        QDSkinManager.changeSkin(QDSkinManager.SKIN_BLUE);
    }

    public static void initImageLoader(Context context) throws IOException {

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(defaultOptions)
                .diskCache(new LruDiskCache(cacheDir, new HashCodeFileNameGenerator(), 50 * 1024 * 1024))
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .build();

        ImageLoader.getInstance().init(config);
    }
}
