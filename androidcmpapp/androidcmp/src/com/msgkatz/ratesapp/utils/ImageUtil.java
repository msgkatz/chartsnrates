package com.msgkatz.ratesapp.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by msgkatz on 14/09/2018.
 */

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getName();

    public void load(ImageView imageView, String url)
    {
        loadCustomSize(imageView, imageView.getLayoutParams().width, imageView.getLayoutParams().height, url);
    }

    public void loadWithoutResize(ImageView imageView, String url)
    {
        if (url == null) {
            return;
        }
        String decodeUrl;
        try {
            decodeUrl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            decodeUrl = url;
        }
        loadImage(imageView, decodeUrl);
    }

    public void loadImage(ImageView imageView, String url)
    {
        if (url == null || url.isEmpty()) {
            return;
        }

        Glide.with(imageView.getContext())
                .asDrawable()
                //.transition(withCrossFade())
                .load(url)
                //.placeholder(new ColorDrawable(Color.WHITE))
                //.transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

//    Glide.with(imageView.getContext())
//          .load(new TheQGlideUrl(url))
//          .transition(withCrossFade())
//          .into(imageView);
    }

    public void loadCustomSize(ImageView imageView, int width, int height, String url)
    {
        if (url == null || url.isEmpty()) {
            return;
        }

        loadImage(imageView, url);
    }

    public void loadLocalResource(ImageView imageView, int res)
    {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(res)
                .into(imageView);
    }

    public void loadSpecial(ImageView imageView, int res)
    {
        Glide.with(imageView.getContext())

                .asBitmap()
                .load(res)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
