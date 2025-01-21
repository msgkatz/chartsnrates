package com.msgkatz.ratesapp.feature.chartgdx.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

import com.msgkatz.ratesapp.feature.chartgdx.entities.ToolFormat;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.ChartShaderUtil;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.Constants;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.GdxSettings;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.GdxUtil;
import com.msgkatz.ratesapp.utils.NumFormatUtil;
import com.msgkatz.ratesapp.utils.Parameters;


import java.text.DecimalFormat;

/**
 * Created by msgkatz on 11/04/2018.
 */

public class SimpleHorizontalGridActor extends Actor {

    private Camera camera;
    private ShaderProgram shaderProgram;
    private Texture texture;
    private float lineCount;
    private float textPositionX;
    private float interValueStep;
    private ToolFormat toolFormat;
    private FloatArray floatArray = new FloatArray();
    private Array<String> stringArray = new Array();

    private float prevF = 0;
    private float prevF2 = 0;
    private float prevF3 = 0;

    public SimpleHorizontalGridActor(Camera camera, ToolFormat toolFormat) {
        this.camera = camera;
        this.toolFormat = toolFormat;
        this.lineCount = camera.viewportWidth / (5.0f * GdxSettings.chartBlockWidthDensityEffective);
        this.texture = new Texture((int) ((camera.viewportWidth - (2.0f * Constants.gdxGraphDensity_8_0_horizGrid)) - GdxUtil.glyphLayout2Width), (int) (Gdx.graphics.getDensity() * 1.0f), Pixmap.Format.RGBA8888);
        this.shaderProgram = ChartShaderUtil.shaderProgram_d;
        setHeight(Gdx.graphics.getDensity() * 1.0f);
        setWidth(GdxSettings.widthFull);

        prevF = 0;
        prevF2 = 0;
        prevF3 = 0;
    }

    public void act(float f) {
        super.act(f);
        setX(this.camera.position.x - (this.camera.viewportWidth / 2.0f));
        this.textPositionX = ((this.camera.position.x + (this.camera.viewportWidth / 2.0f)) - Constants.gdxGraphDensity_8_0_horizGrid) - GdxUtil.glyphLayout2Width;
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        if (this.floatArray.size > 0) {
            for (int i = 0; i < this.floatArray.size; i++) {
                batch.setShader(this.shaderProgram);
                this.shaderProgram.setUniformf("u_dash_count", Constants.DASH_COUNT);
                this.shaderProgram.setUniformf("u_lineCount", this.lineCount);
                batch.draw(this.texture, getX(), this.floatArray.get(i), (float) this.texture.getWidth(), (float) this.texture.getHeight());
                batch.setShader(null);
                GdxUtil.bitmapFont_a.draw(batch, (CharSequence) this.stringArray.get(i), this.textPositionX, this.floatArray.get(i) + (GdxUtil.glyphLayout2.height / 2.0f));
            }
        }
    }

    /**
     *
     * @param f - minPrice
     * @param f2 - maxPrice
     * @param f3 - ratio of chart's height and price delta
     */
    public void updateGrid(float f, float f2, float f3) {

        if (prevF == f && prevF2 == f2 && prevF3 == f3
                //&& floatArray.size > 0
                )
            return;

        prevF = f;
        prevF2 = f2;
        prevF3 = f3;

        if ((f2 - f) == 0.0f) {
            updateGrid_last(f, f2, f3);
        }
        else {
            updateGrid_prev_actual(f, f2, f3);
        }
    }

    private void updateGrid_last(float f, float f2, float f3) {

        float f4 = f2 - f;

        int maxDigits = toolFormat.getMaxFractionDigits();
        DecimalFormat quoteFormat = toolFormat.getQuotesFormat(maxDigits);

        //float step = f4 / 8f;

        this.interValueStep = NumFormatUtil.getFractial(maxDigits, false); //0.1f;
        int i = (int) (f4 / this.interValueStep);

        if (Parameters.DEBUG_GDX_RENDERING_LOGGING) {
            Gdx.app.log(this.getClass().getSimpleName() + "::updateGrid_last",
                    String.format("calling updateGrid_last(%1$s,%2$s,%3$s)", f, f2, f3));
            Gdx.app.log(this.getClass().getSimpleName() + "::updateGrid_last",
                    String.format("interValueStep = %1$s, i = %2$s, f4 = %3$s", interValueStep, i, f4));
        }

        while (i > 8) {
            this.interValueStep += NumFormatUtil.getFractial(maxDigits, true); //this.pairModel.m18495p();
            i = (int) (f4 / this.interValueStep);

            if (Parameters.DEBUG_GDX_RENDERING_LOGGING) {
                Gdx.app.log(this.getClass().getSimpleName() + "::updateGrid_last",
                        String.format("while (i > 8) step:: new interValueStep = %1$s, new i = %2$s", interValueStep, i));
            }
        }
        this.floatArray.clear();
        this.stringArray.clear();
        f4 = this.interValueStep + (((float) ((int) (f / this.interValueStep))) * this.interValueStep);
        this.floatArray.add((GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f4 - f) * f3));

        this.stringArray.add(quoteFormat.format((double) f4));

        for (int i2 = 1; i2 <= i; i2++) {
            f4 += this.interValueStep;
            this.floatArray.add((GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f4 - f) * f3));
            this.stringArray.add(quoteFormat.format((double) f4));
        }
    }

    private void updateGrid_prev_actual(float f, float f2, float f3) {

        float f4 = f2 - f;
        int maxDigits = toolFormat.getMaxFractionDigits();
        DecimalFormat quoteFormat = toolFormat.getQuotesFormat(maxDigits);

        float step = f4 / 8f;

        this.interValueStep = step; //getFractial(maxDigits, false); //0.1f;
        int i = (int) (f4 / this.interValueStep);

        if (Parameters.DEBUG_GDX_RENDERING_LOGGING) {
            Gdx.app.log(this.getClass().getSimpleName() + "::updateGrid_prev_actual",
                    String.format("calling updateGrid_prev_actual(%1$s,%2$s,%3$s)", f, f2, f3));
            Gdx.app.log(this.getClass().getSimpleName() + "::updateGrid_prev_actual",
                    String.format("interValueStep = %1$s, i = %2$s, f4 = %3$s", interValueStep, i, f4));
        }

        while (i > 8) {
            this.interValueStep += step/2; //maxDigits;
            i = (int) (f4 / this.interValueStep);

            if (Parameters.DEBUG_GDX_RENDERING_LOGGING) {
                Gdx.app.log(this.getClass().getSimpleName() + "::updateGrid_prev_actual",
                        String.format("while (i > 8) step:: new interValueStep = %1$s, new i = %2$s", interValueStep, i));
            }
        }
        this.floatArray.clear();
        this.stringArray.clear();
        f4 = this.interValueStep + (((float) ((int) (f / this.interValueStep))) * this.interValueStep);
        this.floatArray.add((GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f4 - f) * f3));

        this.stringArray.add(quoteFormat.format((double) f4));

        for (int i2 = 1; i2 <= i; i2++) {
            f4 += this.interValueStep;
            this.floatArray.add((GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f4 - f) * f3));
            this.stringArray.add(quoteFormat.format((double) f4));
        }
    }

    @Deprecated
    public void updateGrid_old(float f, float f2, float f3) {

        float f4 = f2 - f;

        int maxDigits = toolFormat.getMaxFractionDigits();
        DecimalFormat quoteFormat = toolFormat.getQuotesFormat(maxDigits);

        this.interValueStep = NumFormatUtil.getFractial(maxDigits, false); //0.1f;
        int i = (int) (f4 / this.interValueStep);
        while (i > 8) {
            this.interValueStep += maxDigits;
            i = (int) (f4 / this.interValueStep);
        }
        this.floatArray.clear();
        this.stringArray.clear();
        f4 = this.interValueStep + (((float) ((int) (f / this.interValueStep))) * this.interValueStep);
        this.floatArray.add((GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f4 - f) * f3));

        this.stringArray.add(quoteFormat.format((double) f4));

        for (int i2 = 1; i2 <= i; i2++) {
            f4 += this.interValueStep;
            this.floatArray.add((GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f4 - f) * f3));
            this.stringArray.add(quoteFormat.format((double) f4));
        }
    }


}
