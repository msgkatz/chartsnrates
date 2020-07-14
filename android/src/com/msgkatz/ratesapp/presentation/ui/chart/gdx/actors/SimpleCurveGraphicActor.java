package com.msgkatz.ratesapp.presentation.ui.chart.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

import com.msgkatz.ratesapp.App;
import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.presentation.entities.CandleData;
import com.msgkatz.ratesapp.utils.gdx.ChartShaderUtil;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;

import androidx.core.content.ContextCompat;

/**
 * Created by msgkatz on 29/03/2018.
 */

public class SimpleCurveGraphicActor extends Actor 
{
    private final static String TAG = SimpleCurveGraphicActor.class.getSimpleName();

    private boolean spriteParamInit_isOk;
    private boolean actorInit_isOk;
    private boolean dirty;

    private final Color batchColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private FloatArray floatArray = new FloatArray();
    private ShapeRenderer shapeRenderer;
    private ShaderProgram shaderProgram;
    
    private Texture texture;
    private TextureRegion textureRegion;
    private float textureRegionDU;
    private float textureRegionDV;
    
    private PolygonSprite polygonSprite;
    private PolygonRegion polygonRegion;
    private float[] vertices; // pixel coordinates relative to source image.
    private short[] triangles;
    private float[] polygonVertices;
    private float[] interimArray; //helps to transform vertices to polygonVertices
    private int interimIdx1;
    private int interimIdx2;
    private int interimIdx3;

    private float polygonSpriteX;
    private float polygonSpriteY;
    private float polygonSpriteWRatio;
    private float polygonSpriteHRatio;
    private float polygonSpriteRotCosDeg;
    private float polygonSpriteRotSinDeg;
    private float fX;
    private float fY;

    private Array<CandleData> candleDataArray;
    private float lastCandleDataX;
    private float lastCandleDataY;
    private float lastCandlePriceClose;
    private float ratioHeightPricesDelta;
    private float lowestPrice;
    private float lastCandleDataYDelta;

    private int visibleRangeIdxFirst;
    private int visibleRangeLength;

    private float closePriceEffective;
    private int visibleRangeIdxFirst_FloatArray;
    private int visibleRangeIdxLast_FloatArray;

    public SimpleCurveGraphicActor(Array<CandleData> candleDataArray, ShapeRenderer shapeRenderer, float lowestPrice, float ratioHeightPricesDelta, Camera camera)
    {
//        Gdx.app.log(TAG, "initialisation started...");
        this.candleDataArray = candleDataArray;
        this.ratioHeightPricesDelta = ratioHeightPricesDelta;
        this.lowestPrice = lowestPrice;
        this.lastCandlePriceClose = (float) ((CandleData) candleDataArray.peek()).getPriceClose();
        fillFloatArrayInternal(candleDataArray);

        this.shapeRenderer = shapeRenderer;
        this.shaderProgram = ChartShaderUtil.shaderProgram_c;
        this.shaderProgram.setUniformf("u_resolution", camera.viewportWidth, camera.viewportHeight);

        Pixmap pixmap = new Pixmap((int) GdxSettings.chartBlockWidthDensityEffective, Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(this.batchColor);
        pixmap.fill();

        this.texture = new Texture(pixmap);
        this.textureRegion = new TextureRegion(this.texture);
        this.textureRegionDU = this.textureRegion.getU2() - this.textureRegion.getU();
        this.textureRegionDV = this.textureRegion.getV2() - this.textureRegion.getV();

        this.triangles = new short[]{(short) 3, (short) 0, (short) 1, (short) 1, (short) 2, (short) 3};
        this.vertices = new float[]{this.floatArray.get(0), GdxSettings.heightExtraChartFull, this.floatArray.get(0), this.floatArray.get(1), this.floatArray.get(2), this.floatArray.get(3), this.floatArray.get(2), GdxSettings.heightExtraChartFull};
        this.interimArray = new float[8];
        this.polygonVertices = new float[20];
        this.spriteParamInit_isOk = true;

        try {
            this.polygonRegion = new PolygonRegion(this.textureRegion, this.vertices, this.triangles);
            this.polygonSprite = new PolygonSprite(this.polygonRegion);
            this.polygonSpriteX = this.polygonSprite.getX() + this.polygonSprite.getOriginX();
            this.polygonSpriteY = this.polygonSprite.getY() + this.polygonSprite.getOriginY();
            this.polygonSpriteWRatio = this.polygonSprite.getWidth() / ((float) this.polygonSprite.getRegion().getRegion().getRegionWidth());
            this.polygonSpriteHRatio = this.polygonSprite.getHeight() / ((float) this.polygonSprite.getRegion().getRegion().getRegionHeight());
            this.polygonSpriteRotCosDeg = MathUtils.cosDeg(this.polygonSprite.getRotation());
            this.polygonSpriteRotSinDeg = MathUtils.sinDeg(this.polygonSprite.getRotation());
        } catch (ArrayIndexOutOfBoundsException e) {
            this.spriteParamInit_isOk = false;
        }
        this.actorInit_isOk = true;

//        Gdx.app.log(TAG, "initialization ended...");
    }

    @Override
    public void act(float delta) 
    {
        super.act(delta);
//        if (Parameters.DEBUG_GDX_RENDERING_LOGGING)
//            Gdx.app.log(TAG, "<<act>> started for f=" + delta);
        if (this.lastCandleDataX > 0.0f && this.lastCandleDataX != this.floatArray.get(this.floatArray.size - 2)) 
        {
            setY(this.floatArray.get(this.floatArray.size - 1) + ((this.lastCandleDataYDelta * 2.0f) * delta));
            setX(this.floatArray.get(this.floatArray.size - 2) + ((GdxSettings.chartBlockWidthDensityEffective * 2.0f) * delta));
            if (getX() > this.lastCandleDataX) {
                setX(this.lastCandleDataX);
            }
            if (this.lastCandleDataYDelta > 0.0f) {
                if (getY() > this.lastCandleDataY) {
                    setY(this.lastCandleDataY);
                }
            } else if (getY() < this.lastCandleDataY) {
                setY(this.lastCandleDataY);
            }
            this.floatArray.set(this.floatArray.size - 1, getY());
            this.floatArray.set(this.floatArray.size - 2, getX());
        } 
        else if (this.floatArray.get(this.floatArray.size - 1) != this.lastCandleDataY) 
        {
            setY(this.floatArray.get(this.floatArray.size - 1) + ((this.lastCandleDataYDelta * 2.0f) * delta));
            if (this.lastCandleDataYDelta > 0.0f) {
                if (getY() > this.lastCandleDataY) {
                    setY(this.lastCandleDataY);
                }
            } else if (getY() < this.lastCandleDataY) {
                setY(this.lastCandleDataY);
            }
            this.floatArray.set(this.floatArray.size - 1, getY());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
//        if (Parameters.DEBUG_GDX_RENDERING_LOGGING)
//            Gdx.app.log(TAG, "<<draw>> started for parentAlpha=" + parentAlpha);
        
        Gdx.gl20.glLineWidth(1.0f * Gdx.graphics.getDensity());
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        this.shapeRenderer.translate(getOriginX(), getOriginY(), 0.0f);
        this.shapeRenderer.begin();
        this.shapeRenderer.set(ShapeRenderer.ShapeType.Line);

        //this.shapeRenderer.setColor(Constants.f15452c);
        //Color cl = new Color(ContextCompat.getColor(App.getInstance(), R.color.graph_line));
        //Color cl = Color.valueOf("#1edd78");
        //Color cl = Color.valueOf('#' + Integer.toHexString(ContextCompat.getColor(App.getInstance(), R.color.graph_line)));
        Color cl = Color.valueOf(String.format("#%06x", ContextCompat.getColor(App.getInstance(), R.color.shader_curve_border) & 0xffffff));
        this.shapeRenderer.setColor(cl);

        if (this.floatArray.size > 2 && this.visibleRangeLength > 4 && this.actorInit_isOk) {
            this.shapeRenderer.polyline(this.floatArray.items, this.visibleRangeIdxFirst * 2, this.visibleRangeLength * 2);
        }
        this.shapeRenderer.end();
        batch.begin();
        batch.setColor(this.batchColor);
        batch.setShader(this.shaderProgram);
        if (this.spriteParamInit_isOk && this.visibleRangeLength > 4 && this.actorInit_isOk) {
            this.vertices[0] = this.floatArray.get(this.visibleRangeIdxFirst * 2);
            this.vertices[1] = GdxSettings.heightExtraChartFull;
            this.vertices[2] = this.floatArray.get(this.visibleRangeIdxFirst * 2);
            this.vertices[3] = this.floatArray.get((this.visibleRangeIdxFirst * 2) + 1);
            this.vertices[4] = this.floatArray.get((this.visibleRangeIdxFirst * 2) + 2);
            this.vertices[5] = this.floatArray.get((this.visibleRangeIdxFirst * 2) + 3);
            this.vertices[6] = this.floatArray.get((this.visibleRangeIdxFirst * 2) + 2);
            this.vertices[7] = GdxSettings.heightExtraChartFull;
            this.shaderProgram.setUniformf("a_firstPosition", this.vertices[2], this.vertices[3]);
            this.shaderProgram.setUniformf("a_secondPosition", this.vertices[4], this.vertices[5]);
            updateRegion_stepOne();
            updateRegion_stepTwo();
            ((PolygonSpriteBatch) batch).draw(this.textureRegion.getTexture(), getPolygonVertices(), 0, this.polygonVertices.length, this.triangles, 0, this.triangles.length);
            for (int i = (this.visibleRangeIdxFirst * 2) + 2; i < ((this.visibleRangeIdxFirst * 2) + (this.visibleRangeLength * 2)) - 2; i += 2) {
                this.vertices[0] = this.floatArray.get(i);
                this.vertices[1] = GdxSettings.heightExtraChartFull;
                this.vertices[2] = this.floatArray.get(i);
                this.vertices[3] = this.floatArray.get(i + 1);
                this.vertices[4] = this.floatArray.get(i + 2);
                this.vertices[5] = this.floatArray.get(i + 3);
                this.vertices[6] = this.floatArray.get(i + 2);
                this.vertices[7] = GdxSettings.heightExtraChartFull;
                this.shaderProgram.setUniformf("a_firstPosition", this.vertices[2], this.vertices[3]);
                this.shaderProgram.setUniformf("a_secondPosition", this.vertices[4], this.vertices[5]);
                updateRegion_stepOne();
                updateRegion_stepTwo();
                ((PolygonSpriteBatch) batch).draw(this.textureRegion.getTexture(), getPolygonVertices(), 0, this.polygonVertices.length, this.triangles, 0, this.triangles.length);
            }
        }
        batch.setShader(null);
        Gdx.gl20.glLineWidth(1.0f * Gdx.graphics.getDensity());
    }

    private void fillFloatArrayInternal(Array<CandleData> data)
    {
        this.floatArray.clear();
        for (int i = 0; i < data.size; i++) {
            this.floatArray.add((float) ((CandleData) data.get(i)).getIdxByScaledDensity());
            this.floatArray.add((float) ((CandleData) data.get(i)).getYPriceCloseScaled());
        }

        setX((float) ((CandleData) data.peek()).getIdxByScaledDensity());
        setY((float) ((CandleData) data.peek()).getYPriceCloseScaled());

        this.lastCandleDataX = (float) ((CandleData) data.peek()).getIdxByScaledDensity();
        this.lastCandleDataY = (float) ((CandleData) data.peek()).getYPriceCloseScaled();
    }

    public void fillFloatArray(Array<CandleData> data) {
        this.candleDataArray = data;
        this.actorInit_isOk = false;
        this.floatArray.clear();
        for (int i = 0; i < data.size; i++) {
            this.floatArray.add((float) ((CandleData) data.get(i)).getIdxByScaledDensity());
            this.floatArray.add((float) ((CandleData) data.get(i)).getYPriceCloseScaled());
        }
        setY((float) ((CandleData) data.peek()).getYPriceCloseScaled());
        setX((float) ((CandleData) data.peek()).getIdxByScaledDensity());
        this.lastCandleDataY = (float) ((CandleData) data.peek()).getYPriceCloseScaled();
        this.lastCandleDataX = (float) ((CandleData) data.peek()).getIdxByScaledDensity();
        this.actorInit_isOk = true;
    }

    public void updateLastY(float f, float f2) {
        this.lastCandlePriceClose = (float) ((CandleData) this.candleDataArray.peek()).getPriceClose();
        this.lastCandleDataY = f;
        this.lastCandleDataYDelta = f - f2;
        this.floatArray.set(this.floatArray.size - 2, (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity());
    }

    public void updateLastCandleData(CandleData candleData, float f) {
        this.lastCandleDataX = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
        this.lastCandleDataY = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((((float) candleData.getPriceClose()) - this.lowestPrice) * this.ratioHeightPricesDelta);
        this.lastCandleDataYDelta = this.lastCandleDataY - f;
        this.lastCandlePriceClose = (float) candleData.getPriceClose();
        float g = (float) (((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity() - ((double) GdxSettings.chartBlockWidthDensityEffective));
        this.floatArray.set(this.floatArray.size - 2, g);
        this.floatArray.set(this.floatArray.size - 1, f);
        this.floatArray.add(g);
        this.floatArray.add(f);
    }

    public void updateLastPrices(float f, float f2) {
        this.closePriceEffective = (((getY() - GdxSettings.heightMainChartBorder) - GdxSettings.heightExtraChartFull) / this.ratioHeightPricesDelta) + this.lowestPrice;
        this.lowestPrice = f;
        this.ratioHeightPricesDelta = f2;
        this.visibleRangeIdxFirst_FloatArray = (this.visibleRangeIdxFirst - (GdxSettings.chartBlockWidthReal / 2)) * 2;
        if (this.visibleRangeIdxFirst_FloatArray <= 0) {
            this.visibleRangeIdxFirst_FloatArray = 0;
        }
        this.visibleRangeIdxLast_FloatArray = this.visibleRangeIdxFirst_FloatArray + (this.visibleRangeLength * 4);
        if (this.visibleRangeIdxLast_FloatArray >= this.floatArray.size) {
            this.visibleRangeIdxLast_FloatArray = this.floatArray.size - 1;
        }
        for (int i = this.visibleRangeIdxFirst_FloatArray + 1; i < this.visibleRangeIdxLast_FloatArray - 1; i += 2) {
            this.floatArray.set(i, ((((float) ((CandleData) this.candleDataArray.get((i - 1) / 2)).getPriceClose()) - f) * f2) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder));
        }
        this.floatArray.set(this.floatArray.size - 1, (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((this.closePriceEffective - f) * f2));
        this.lastCandleDataY = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((this.lastCandlePriceClose - f) * f2);
        this.lastCandleDataYDelta = this.lastCandleDataY - this.floatArray.peek();
    }

    public void updateData() {
        for (int i = 0; i < this.floatArray.size; i += 2) {
            this.floatArray.set(i, (float) ((CandleData) this.candleDataArray.get(i / 2)).getIdxByScaledDensity());
        }
        this.lastCandleDataX = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
    }



    private void updateRegion_stepOne() {
        this.interimIdx1 = 0;
        int length = this.vertices.length;
        while (this.interimIdx1 < length) {
            this.interimArray[this.interimIdx1] = this.textureRegion.getU() + (this.textureRegionDU * (this.vertices[this.interimIdx1] / ((float) this.textureRegion.getRegionWidth())));
            this.interimIdx1++;
            this.interimArray[this.interimIdx1] = this.textureRegion.getV() + (this.textureRegionDV * (1.0f - (this.vertices[this.interimIdx1] / ((float) this.textureRegion.getRegionHeight()))));
            this.interimIdx1++;
        }
    }

    private void updateRegion_stepTwo() {
        if (this.polygonVertices == null) {
            this.polygonVertices = new float[((this.vertices.length / 2) * 5)];
        }
        this.interimIdx2 = 0;
        this.interimIdx3 = 2;
        int length = this.vertices.length;
        while (this.interimIdx2 < length) {
            this.polygonVertices[this.interimIdx3] = this.batchColor.toFloatBits();
            this.polygonVertices[this.interimIdx3 + 1] = this.interimArray[this.interimIdx2];
            this.polygonVertices[this.interimIdx3 + 2] = this.interimArray[this.interimIdx2 + 1];
            this.interimIdx2 += 2;
            this.interimIdx3 += 5;
        }
        this.dirty = true;
    }

    private float[] getPolygonVertices() {
        if (!this.dirty) {
            return this.polygonVertices;
        }
        this.dirty = false;
        this.interimIdx2 = 0;
        this.interimIdx3 = 0;
        int length = this.vertices.length;
        while (this.interimIdx2 < length) {
            this.fX = ((this.vertices[this.interimIdx2] * this.polygonSpriteWRatio) - this.polygonSprite.getOriginX()) * this.polygonSprite.getScaleX();
            this.fY = ((this.vertices[this.interimIdx2 + 1] * this.polygonSpriteHRatio) - this.polygonSprite.getOriginY()) * this.polygonSprite.getScaleY();
            this.polygonVertices[this.interimIdx3] = ((this.polygonSpriteRotCosDeg * this.fX) - (this.polygonSpriteRotSinDeg * this.fY)) + this.polygonSpriteX;
            this.polygonVertices[this.interimIdx3 + 1] = ((this.polygonSpriteRotSinDeg * this.fX) + (this.polygonSpriteRotCosDeg * this.fY)) + this.polygonSpriteY;
            this.interimIdx2 += 2;
            this.interimIdx3 += 5;
        }
        return this.polygonVertices;
    }

    public void updateVisibleRange(int x1, int x2) {
        this.visibleRangeIdxFirst = x1;
        this.visibleRangeLength = GdxSettings.chartBlockWidthReal;
        if ((this.visibleRangeLength + this.visibleRangeIdxFirst) * 2 >= this.floatArray.size) {
            this.visibleRangeLength = Math.abs((this.floatArray.size - (this.visibleRangeIdxFirst * 2)) / 2);
        }
    }
}
