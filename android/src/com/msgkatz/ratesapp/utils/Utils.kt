package com.msgkatz.ratesapp.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
//import com.msgkatz.ratesapp.BuildConfig
import com.msgkatz.ratesapp.R

fun isTablet(context: Context): Boolean {
    return context.resources.getBoolean(R.bool.isTablet)
}

fun isSystemUiVisible(w: Window?): Boolean {
    return (w?.decorView?.systemUiVisibility ?: 0 and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0
}

fun setSystemUiHidden(isHidden: Boolean, activity: Activity) {
    //Functions.log("setSystemUiHidden( $isHidden )")
    if(isHidden) {
        var flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        flag += View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        flag += View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
        flag += View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        flag += View.SYSTEM_UI_FLAG_FULLSCREEN // hide code bar
        flag += View.SYSTEM_UI_FLAG_LOW_PROFILE // nav buttons as dots
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flag += View.SYSTEM_UI_FLAG_IMMERSIVE
        }
        activity.window.decorView.systemUiVisibility = flag
    } else {
        activity.window.decorView.systemUiVisibility = 0
    }
}

@TargetApi(Build.VERSION_CODES.KITKAT)
fun setSystemUiHiddenSticky(isHidden: Boolean, activity: Activity) {
//    if (BuildConfig.DEBUG)
//        Log.e("setSystemUiHiddenSticky", "isHidden = ${isHidden}")
    if (!isHidden) {
        activity.window.decorView.systemUiVisibility = 0
        return
    }

    /** no nedd for this, as for now
    // BEGIN_INCLUDE (get_current_ui_flags)
    // The UI options currently enabled are represented by a bitfield.
    // getSystemUiVisibility() gives us that bitfield.
    val uiOptions = activity.window.decorView.systemUiVisibility
    // END_INCLUDE (get_current_ui_flags)
    // BEGIN_INCLUDE (toggle_ui_flags)
    val isImmersiveModeEnabled = ((uiOptions.or(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions))
     **/

    var newUiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    newUiOptions += View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    newUiOptions += View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //newUiOptions += View.SYSTEM_UI_FLAG_LOW_PROFILE // nav buttons as dots

    // Navigation bar hiding:  Backwards compatible to ICS.
    if (Build.VERSION.SDK_INT >= 14)
        newUiOptions += View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
    // Status bar hiding: Backwards compatible to Jellybean
    if (Build.VERSION.SDK_INT >= 16)
        newUiOptions += View.SYSTEM_UI_FLAG_FULLSCREEN // hide code bar

    // Immersive mode: Backward compatible to KitKat.
    // Note that this flag doesn't do anything by itself, it only augments the behavior
    // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
    // all three flags are being toggled together.
    // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
    // Sticky immersive mode differs in that it makes the navigation and status bars
    // semi-transparent, and the UI flag does not get cleared when the user interacts with
    // the screen.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        newUiOptions += View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY


    activity.window.decorView.systemUiVisibility = newUiOptions
    //END_INCLUDE (set_ui_flags)
}
