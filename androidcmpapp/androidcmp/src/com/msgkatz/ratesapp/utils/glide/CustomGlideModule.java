package com.msgkatz.ratesapp.utils.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by msgkatz on 14/09/2018.
 */

@GlideModule
public class CustomGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 10; // 10mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(2)
                //.setMemoryCacheScreens(2)
                .build();

        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
