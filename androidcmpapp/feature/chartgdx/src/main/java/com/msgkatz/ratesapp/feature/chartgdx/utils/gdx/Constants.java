package com.msgkatz.ratesapp.feature.chartgdx.utils.gdx;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.msgkatz.ratesapp.feature.chartgdx.R;


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

    /** Colors #AARRGGBB
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

     **/
    public static Color COLOR_CLEAR;//           = toGDXColor(R.color.chart_screen_clear_color);
    public static Color COLOR_RATE_DOT_1;//      = toGDXColor(R.color.chart_color_rate_dot_1);
    public static Color COLOR_RATE_DOT_2;//      = toGDXColor(R.color.chart_color_rate_dot_2);
    public static Color COLOR_RATE_DOT_3 ;//     = toGDXColor(R.color.chart_color_rate_dot_3);
    public static Color COLOR_RATE_LINE ;//      = toGDXColor(R.color.chart_color_rate_line);        //curCurs, wordUtils
    public static Color COLOR_LOADER;//          = toGDXColor(R.color.chart_color_loader);           //loader, rightvoid, calcActors, wordUtils
    public static Color COLOR_VOID;//            = toGDXColor(R.color.chart_color_void);
    public static Color COLOR_VERTICAL_GRID;//   = toGDXColor(R.color.chart_color_vertical_grid);    //verticalGrid, wordUtils

    public static Color COLOR_FONT_A;//          = toGDXColor(R.color.chart_color_font_a);
    public static Color COLOR_FONT_B;//          = toGDXColor(R.color.chart_color_font_b);           //endDeal, wordUtils
    public static Color COLOR_FONT_C;//          = toGDXColor(R.color.chart_color_font_c);
    public static Color COLOR_FONT_D;//          = toGDXColor(R.color.chart_color_font_d);           //wordUtils
    public static Color COLOR_FONT_E;//          = toGDXColor(R.color.chart_color_font_e);
    public static Color COLOR_FONT_F;//          = toGDXColor(R.color.chart_color_font_f);           //wordUtils
    public static Color COLOR_FONT_G;//          = toGDXColor(R.color.chart_color_font_g);
    public static Color COLOR_FONT_H;//          = toGDXColor(R.color.chart_color_font_h);

    public static Color COLOR_FONT_I;//          = toGDXColor(R.color.chart_color_font_i);
    public static Color COLOR_FONT_J;//          = toGDXColor(R.color.chart_color_font_j);
    public static Color COLOR_FONT_K ;//         = toGDXColor(R.color.chart_color_font_k);
    public static Color COLOR_FONT_L;//          = toGDXColor(R.color.chart_color_font_l);

    public static Color COLOR_SHADER_CURVE;
    public static void initColors(Context context) {
        COLOR_CLEAR           = toGDXColor(context, R.color.chart_screen_clear_color);
        COLOR_RATE_DOT_1      = toGDXColor(context, R.color.chart_color_rate_dot_1);
        COLOR_RATE_DOT_2      = toGDXColor(context, R.color.chart_color_rate_dot_2);
        COLOR_RATE_DOT_3      = toGDXColor(context, R.color.chart_color_rate_dot_3);
        COLOR_RATE_LINE       = toGDXColor(context, R.color.chart_color_rate_line);        //curCurs, wordUtils
        COLOR_LOADER          = toGDXColor(context, R.color.chart_color_loader);           //loader, rightvoid, calcActors, wordUtils
        COLOR_VOID            = toGDXColor(context, R.color.chart_color_void);
        COLOR_VERTICAL_GRID   = toGDXColor(context, R.color.chart_color_vertical_grid);    //verticalGrid, wordUtils

        COLOR_FONT_A          = toGDXColor(context, R.color.chart_color_font_a);
        COLOR_FONT_B          = toGDXColor(context, R.color.chart_color_font_b);           //endDeal, wordUtils
        COLOR_FONT_C          = toGDXColor(context, R.color.chart_color_font_c);
        COLOR_FONT_D          = toGDXColor(context, R.color.chart_color_font_d);           //wordUtils
        COLOR_FONT_E          = toGDXColor(context, R.color.chart_color_font_e);
        COLOR_FONT_F          = toGDXColor(context, R.color.chart_color_font_f);           //wordUtils
        COLOR_FONT_G          = toGDXColor(context, R.color.chart_color_font_g);
        COLOR_FONT_H          = toGDXColor(context, R.color.chart_color_font_h);

        COLOR_FONT_I          = toGDXColor(context, R.color.chart_color_font_i);
        COLOR_FONT_J          = toGDXColor(context, R.color.chart_color_font_j);
        COLOR_FONT_K          = toGDXColor(context, R.color.chart_color_font_k);
        COLOR_FONT_L          = toGDXColor(context, R.color.chart_color_font_l);

        COLOR_SHADER_CURVE    = Color.valueOf(String.format("#%06x", ContextCompat.getColor(context, R.color.shader_curve_border) & 0xffffff));
    }
    private static Color toGDXColor(Context context, @ColorRes int id) {
        // Color int value as AARRGGBB
        int value = ContextCompat.getColor(context, id) & 0xffffffff;
        return Color.valueOf(toRGBA(value));
    }

    private static String toRGBA(int value) {
        return String.format("#%06x%02x", value & 0xffffff, ((value >>> 24) & 0xff));
    }
}
