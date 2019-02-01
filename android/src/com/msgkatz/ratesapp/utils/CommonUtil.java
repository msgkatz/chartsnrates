package com.msgkatz.ratesapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by msgkatz on 09/09/2018.
 */

public class CommonUtil {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float convertPixelsToDp(float px, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static android.graphics.drawable.Drawable setTint(android.graphics.drawable.Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    public static int getScreenWidth()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        return (int)Resources.getSystem().getDisplayMetrics().widthPixels;

    }

    public static int getScreenHeight()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        return (int)Resources.getSystem().getDisplayMetrics().heightPixels;

    }

    public static void showSoftKeyboard(final Context context, final EditText editText) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    public static void closeSoftKeyboard(Activity activity) {

        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }

    public static int getColor(Context context, @ColorRes int resId) throws Resources.NotFoundException {
        return ContextCompat.getColor(context, resId);
    }

    /**
     * Gets a reference to a given drawable and prepares it for use with tinting through.
     *
     * @param resId the resource id for the given drawable
     * @return a wrapped drawable ready fo use
     * with {@link android.support.v4.graphics.drawable.DrawableCompat}'s tinting methods
     * @throws Resources.NotFoundException
     */
    public static Drawable getWrappedDrawable(Context context, @DrawableRes int resId) throws Resources.NotFoundException {
        return DrawableCompat.wrap(ResourcesCompat.getDrawable(context.getResources(),
                resId, null));
    }
}
