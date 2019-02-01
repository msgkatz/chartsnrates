package com.msgkatz.ratesapp.presentation.ui.chart.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.FloatArray;

import com.msgkatz.ratesapp.presentation.entities.CandleData;
import com.msgkatz.ratesapp.presentation.entities.ChartType;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.actors.helpers.PolygonObj;
import com.msgkatz.ratesapp.utils.gdx.Constants;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;

import java.text.DecimalFormat;

/**
 * Created by msgkatz on 11/04/2018.
 */

public class SimpleRealtimeRateLineActor extends Actor {

    private static final float gdxGraphDensity_6_0  = (Gdx.graphics.getDensity() * 6.0f);
    private static final float gdxGraphDensity_2_0a = (Gdx.graphics.getDensity() * 2.0f);
    private static final float gdxGraphDensity_2_0b = (Gdx.graphics.getDensity() * 2.0f);
    private static final float gdxGraphDensity_10_0 = (Gdx.graphics.getDensity() * 10.0f);
    private static final float gdxGraphDensity_20_0 = (Gdx.graphics.getDensity() * 20.0f);
    private static final float gdxGraphDensity_1_0  = (Gdx.graphics.getDensity() * 1.0f);
    private static final float gdxGraphDensity_0_5  = (gdxGraphDensity_1_0 / 2.0f);

    private ChartType chartType;
    private DecimalFormat decimalFormat;
    private Texture texture;
    private Camera camera;
    private BitmapFont bitmapFont;

    private PolygonObj polygonObj1;
    private PolygonObj polygonObj2;
    private GlyphLayout glyphLayout1 = new GlyphLayout();
    private GlyphLayout glyphLayout2;
    private float actorY;
    private float polygonX;
    private float glyphLayout1X;
    private float glyphLayout2X;
    private float glyphLayout2Y;
    private float glyphLayout2WidthWithBorder;
    private float glyphLayout2HeightWithBorder;

    public SimpleRealtimeRateLineActor(CandleData candleData, Camera camera, BitmapFont bitmapFont, ChartType chartType, ToolFormat toolFormat) {

        this.camera = camera;
        this.chartType = chartType;
        FloatArray floatArray = new FloatArray();
        FloatArray floatArray2 = new FloatArray();
        short[] sArr = new short[]{(short) 4, (short) 0, (short) 1, (short) 4, (short) 1, (short) 2, (short) 2, (short) 3, (short) 4};
        setX((float) candleData.getIdxByScaledDensity());
        setY((float) candleData.getYPriceCloseScaled());

        int maxDigits = toolFormat.getMaxFractionDigits();
        this.decimalFormat = toolFormat.getQuotesFormat(maxDigits);

        this.bitmapFont = bitmapFont;
        //this.glyphLayout1.setText(bitmapFont, this.decimalFormat.format(100000.109375d));
        this.glyphLayout1.setText(bitmapFont, this.decimalFormat.format(toolFormat.getPriceExample()));
        this.glyphLayout2 = new GlyphLayout();
        this.glyphLayout2.setText(bitmapFont, (CharSequence) "11:11");
        this.glyphLayout2WidthWithBorder = (this.glyphLayout2.width + gdxGraphDensity_6_0) + gdxGraphDensity_2_0a;
        this.glyphLayout2HeightWithBorder = this.glyphLayout2.height + (gdxGraphDensity_2_0b * 2.0f);

        setWidth((this.glyphLayout1.width + gdxGraphDensity_6_0) + gdxGraphDensity_2_0a);

        this.glyphLayout1.setText(bitmapFont, String.valueOf(candleData.getPriceClose()));
        this.glyphLayout1X = (camera.position.x + (camera.viewportWidth / 2.0f)) - getWidth();
        this.polygonX = this.glyphLayout1X - gdxGraphDensity_10_0;
        this.actorY = GdxSettings.heightExtraChartFull;

        floatArray.addAll(gdxGraphDensity_10_0,
                0.0f,
                0.0f,
                gdxGraphDensity_20_0 / 2.0f,
                gdxGraphDensity_10_0,
                gdxGraphDensity_20_0,
                gdxGraphDensity_10_0 + getWidth(),
                gdxGraphDensity_20_0,
                gdxGraphDensity_10_0 + getWidth(),
                0.0f);

        int i = (int) (((gdxGraphDensity_10_0 + gdxGraphDensity_6_0) + this.glyphLayout1.width) + gdxGraphDensity_2_0a);
        int i2 = (int) gdxGraphDensity_20_0;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        this.polygonObj1 = new PolygonObj(new TextureRegion(new Texture(pixmap), i, i2), floatArray.toArray(), sArr);
        int i3 = (int) (((gdxGraphDensity_10_0 + gdxGraphDensity_6_0) + this.glyphLayout1.width) + gdxGraphDensity_2_0a);

        floatArray2.addAll(gdxGraphDensity_10_0 + ((gdxGraphDensity_10_0 / (gdxGraphDensity_20_0 / 2.0f)) * this.glyphLayout2HeightWithBorder),
                0.0f,
                0.0f,
                ((float) i) - (gdxGraphDensity_20_0 / 2.0f),
                gdxGraphDensity_10_0,
                (float) i,
                gdxGraphDensity_10_0 + getWidth(),
                (float) ((int) ((gdxGraphDensity_20_0 + this.glyphLayout2.height) + (gdxGraphDensity_2_0a * 2.0f))),
                gdxGraphDensity_10_0 + getWidth(),
                0.0f);

        pixmap.setColor(Constants.COLOR_RATE_LINE);
        pixmap.fill();
        this.polygonObj2 = new PolygonObj(new TextureRegion(new Texture(pixmap), i3, i), floatArray2.toArray(), sArr);
        pixmap.dispose();
    }

    public void act(float f) {

        this.glyphLayout1X = (this.camera.position.x + (this.camera.viewportWidth / 2.0f)) - getWidth();
        this.polygonX = this.glyphLayout1X - gdxGraphDensity_10_0;

        if (getY() < this.actorY) {
            setY(this.actorY);
        }

        if (this.chartType == ChartType.Candle) {
            this.glyphLayout2X = (this.camera.position.x + (this.camera.viewportWidth / 2.0f)) - this.glyphLayout2WidthWithBorder;
            this.glyphLayout2Y = (getY() - (gdxGraphDensity_20_0 / 2.0f)) - (this.glyphLayout2HeightWithBorder / 2.0f);
            this.polygonObj2.setX(this.polygonX);
            this.polygonObj2.setY((getY() - (gdxGraphDensity_20_0 / 2.0f)) - this.glyphLayout2HeightWithBorder);
        }

        this.polygonObj1.setX(this.polygonX);
        this.polygonObj1.setY(getY() - (gdxGraphDensity_20_0 / 2.0f));
        super.act(f);
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);

        batch.end();
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.setShader(null);

        batch.draw(this.texture,
                    this.camera.position.x - (this.camera.viewportWidth / 2.0f),
                    getY() - gdxGraphDensity_0_5,
                    this.camera.viewportWidth,
                    gdxGraphDensity_1_0);

        Gdx.gl20.glDisable(GL20.GL_BLEND);
        batch.setShader(null);

        if (this.chartType == ChartType.Candle) {
            batch.setColor(Constants.COLOR_RATE_LINE);
            ((PolygonSpriteBatch) batch).draw(this.polygonObj2.getRegion().getTexture(),
                                            this.polygonObj2.getVertices(),
                                            0,
                                            this.polygonObj2.getVertices().length,
                                            this.polygonObj2.getTriangles(),
                                            0,
                                            this.polygonObj2.getTriangles().length);
        }

        batch.setColor(Color.WHITE);
        ((PolygonSpriteBatch) batch).draw(this.polygonObj1.getRegion().getTexture(),
                                        this.polygonObj1.getVertices(),
                                        0,
                                        this.polygonObj1.getVertices().length,
                                        this.polygonObj1.getTriangles(),
                                        0,
                                        this.polygonObj1.getTriangles().length);

        this.bitmapFont.draw(batch, this.glyphLayout1, this.glyphLayout1X + gdxGraphDensity_2_0a, getY() + (this.glyphLayout1.height / 2.0f));
        if (this.chartType == ChartType.Candle) {
            this.bitmapFont.draw(batch, this.glyphLayout2, this.glyphLayout2X + gdxGraphDensity_2_0a, this.glyphLayout2Y + (this.glyphLayout2.height / 2.0f));
        }
    }

    public void updateRate(double d) {
        this.glyphLayout1.setText(this.bitmapFont, this.decimalFormat.format(d));
    }

    public void updateSecondLine(String str) {
        this.glyphLayout2.setText(this.bitmapFont, (CharSequence) str);
    }
}
