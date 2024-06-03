package com.msgkatz.ratesapp.utils.gdx;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.R;

/**
 * Created by msgkatz on 16/09/2018.
 */

public class Constants {

    /** Density **/
    public static final float densityCandle                 = (Gdx.graphics.getDensity() * 6.5f);       //worldSettings
    public static final float densityCurve                  = (Gdx.graphics.getDensity() * 1.0f);       //worldSettings

    public static final float gdxGraphDensity_1_0_curCursA  = (Gdx.graphics.getDensity() * 1.0f);
    public static final float gdxGraphDensity_3_0_curCurs   = (Gdx.graphics.getDensity() * 3.0f);
    public static final float gdxGraphDensity_5_0_curCurs   = (Gdx.graphics.getDensity() * 5.0f);
    public static final float gdxGraphDensity_39_0_curCurs  = (Gdx.graphics.getDensity() * 39.0f);
    public static final float gdxGraphDensity_1_0_curCursB  = (Gdx.graphics.getDensity() * 1.0f);
    public static final float gdxGraphDensity_8_0_vertGrid  = (Gdx.graphics.getDensity() * 8.0f);       //vertGrid
    public static final float gdxGraphDensity_10_0_gdxUtil  = (Gdx.graphics.getDensity() * 10.0f);      //GdxUtil
    public static final float gdxGraphDensity_3_0_inner     = (Gdx.graphics.getDensity() * 3.0f);       //inner -
    public static final float gdxGraphDensity_8_0_horizGrid = (Gdx.graphics.getDensity() * 8.0f);       //endDeal, horizontalGrid
    public static final float gdxGraphDensity_9_0_gdxUtil   = (Gdx.graphics.getDensity() * 9.0f);       //GdxUtil

    public static final float DASH_COUNT_VERT               = (((float) Gdx.graphics.getHeight()) / (gdxGraphDensity_3_0_inner * 2.0f));
    public static final float DASH_COUNT                    = (((float) Gdx.graphics.getWidth()) / (gdxGraphDensity_3_0_inner * 2.0f)); //horizontalGrid

    /** Colors #AARRGGBB **/
    public static final Color COLOR_CLEAR           = toGDXColor(R.color.chart_screen_clear_color);
    public static final Color COLOR_RATE_DOT_1      = toGDXColor(R.color.chart_color_rate_dot_1);
    public static final Color COLOR_RATE_DOT_2      = toGDXColor(R.color.chart_color_rate_dot_2);
    public static final Color COLOR_RATE_DOT_3      = toGDXColor(R.color.chart_color_rate_dot_3);
    public static final Color COLOR_RATE_LINE       = toGDXColor(R.color.chart_color_rate_line);        //curCurs, wordUtils
    public static final Color COLOR_LOADER          = toGDXColor(R.color.chart_color_loader);           //loader, rightvoid, calcActors, wordUtils
    public static final Color COLOR_VOID            = toGDXColor(R.color.chart_color_void);
    public static final Color COLOR_VERTICAL_GRID   = toGDXColor(R.color.chart_color_vertical_grid);    //verticalGrid, wordUtils

    public static final Color COLOR_FONT_A          = toGDXColor(R.color.chart_color_font_a);
    public static final Color COLOR_FONT_B          = toGDXColor(R.color.chart_color_font_b);           //endDeal, wordUtils
    public static final Color COLOR_FONT_C          = toGDXColor(R.color.chart_color_font_c);
    public static final Color COLOR_FONT_D          = toGDXColor(R.color.chart_color_font_d);           //wordUtils
    public static final Color COLOR_FONT_E          = toGDXColor(R.color.chart_color_font_e);
    public static final Color COLOR_FONT_F          = toGDXColor(R.color.chart_color_font_f);           //wordUtils
    public static final Color COLOR_FONT_G          = toGDXColor(R.color.chart_color_font_g);
    public static final Color COLOR_FONT_H          = toGDXColor(R.color.chart_color_font_h);
    public static final Color COLOR_FONT_I          = toGDXColor(R.color.chart_color_font_i);
    public static final Color COLOR_FONT_J          = toGDXColor(R.color.chart_color_font_j);
    public static final Color COLOR_FONT_K          = toGDXColor(R.color.chart_color_font_k);
    public static final Color COLOR_FONT_L          = toGDXColor(R.color.chart_color_font_l);

    private static Color toGDXColor(@ColorRes int id) {
        // Color int value as AARRGGBB
        int value = ContextCompat.getColor(App.getInstance(), id) & 0xffffffff;
        return Color.valueOf(toRGBA(value));
    }

    private static String toRGBA(int value) {
        return String.format("#%06x%02x", value & 0xffffff, ((value >>> 24) & 0xff));
    }
}
