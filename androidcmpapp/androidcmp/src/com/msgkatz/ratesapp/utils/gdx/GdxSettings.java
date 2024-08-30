package com.msgkatz.ratesapp.utils.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.msgkatz.ratesapp.presentation.entities.ChartType;

/**
 * Created by msgkatz on 16/09/2018.
 *
 * Scaled density usage example:
 *     //According to LibGDX documentation; getDensity() returns a scalar value for 160dpi.
 *     float dpi = 160 * Gdx.graphics.getDensity();
 *     float widthInches = Gdx.graphics.getWidth() / dpi;
 *     float heightInches = Gdx.graphics.getHeight() / dpi;
 *
 */

public class GdxSettings {

    public static ChartType chartType;
    public static float widthFull;
    public static float heightFull;
    public static float heightMainChartBody2;
    public static float heightMainChartBorder;
    public static float chartBlockWidthDensityEffective;
    public static int chartBlockWidthReal;
    public static float heightMainChartBody;
    public static float heightMainChartFull;
    public static float heightExtraChartFull;
    public static float heightExtraChartPerItem;
    public static float glyphWidthNormalized;
    public static float glyphHeightNormalized;
    private static GlyphLayout glyphLayout;

    public static void init(ChartType chartType, float zoomWidth, int extraChartCount) {
        GdxSettings.chartType = chartType;
        heightExtraChartFull = 0.0f;
        heightExtraChartPerItem = 0.0f;
        switch (extraChartCount) {
            case 0:
                heightExtraChartFull = 0.0f;
                heightExtraChartPerItem = 0.0f;
                break;
            case 1:
                heightExtraChartFull = ((float) Gdx.graphics.getHeight()) * 0.3f;
                heightExtraChartPerItem = heightExtraChartFull;
                break;
            case 2:
                heightExtraChartFull = ((float) Gdx.graphics.getHeight()) * 0.4f;
                heightExtraChartPerItem = heightExtraChartFull / 2.0f;
                break;
            case 3:
                heightExtraChartFull = ((float) Gdx.graphics.getHeight()) * 0.45f;
                heightExtraChartPerItem = heightExtraChartFull / 3.0f;
                break;
        }
        heightMainChartBorder = (((float) Gdx.graphics.getHeight()) - heightExtraChartFull) * 0.15f;
        heightMainChartBody = (((float) Gdx.graphics.getHeight()) - heightExtraChartFull) - (heightMainChartBorder * 2.0f);
        heightMainChartFull = ((float) Gdx.graphics.getHeight()) - heightExtraChartFull;
        widthFull = (float) Gdx.graphics.getWidth();
        heightFull = (float) Gdx.graphics.getHeight();
        heightMainChartBody2 = (((float) Gdx.graphics.getHeight()) - heightExtraChartFull) - (heightMainChartBorder * 2.0f);
        glyphLayout = new GlyphLayout(GdxUtil.bitmapFont_f, "1.11111");
        glyphWidthNormalized = glyphLayout.width + (18.0f * Gdx.graphics.getDensity());
        glyphHeightNormalized = (heightExtraChartFull + glyphLayout.height) + (12.0f * Gdx.graphics.getDensity());
        switch (chartType) {
            case Curve:
                int a = (int) (((float) Gdx.graphics.getWidth()) / (Constants.densityCurve * zoomWidth));
                int i2 = (int) (450.0f / zoomWidth);
                if (a >= i2) {
                    a = i2;
                }
                chartBlockWidthReal = a; //or chartBlockWidthSize - blockSize
                chartBlockWidthDensityEffective = ((float) Gdx.graphics.getWidth()) / ((float) chartBlockWidthReal);
                return;
            case Candle:
                chartBlockWidthDensityEffective = Constants.densityCandle * zoomWidth; //efffective width density
                chartBlockWidthReal = (int) ((((float) Gdx.graphics.getWidth()) / Constants.densityCandle) / zoomWidth);
                return;
            default:
                return;
        }
    }
}
