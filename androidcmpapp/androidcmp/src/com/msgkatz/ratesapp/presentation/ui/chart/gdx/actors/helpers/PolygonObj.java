package com.msgkatz.ratesapp.presentation.ui.chart.gdx.actors.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by msgkatz on 16/09/2018.
 *
 * Analogous to gdx.graphics.g2d.PolygonSprite
 */

public class PolygonObj {

    private final float[] interimArray;
    private final float[] vertices;
    private final short[] triangles;
    private final TextureRegion textureRegion;
    private float polyRegW;
    private float polyRegH;
    private float polyRegX;
    private float polyRegY;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float polyRegCalcX;
    private float polyRegCalcY;
    private float degrees;
    private final Color batchColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    private boolean dirty;
    private float[] polygonVertices;
    private int interimIdx2;
    private int interimIdx3;
    private float polyObjX;
    private float polyObjY;
    private float polyObjWRatio;
    private float polyObjHRatio;
    private float polygonObjRotCosDeg;
    private float polygonObjRotSinDeg;
    private float fX;
    private float fY;

    public PolygonObj(TextureRegion _textureRegion, float[] fArr, short[] sArr) {
        this.textureRegion = _textureRegion;
        this.vertices = fArr;
        this.triangles = sArr;
        float[] fArr2 = new float[fArr.length];
        this.interimArray = fArr2;
        float b = _textureRegion.getU();
        float c = _textureRegion.getV();
        float d = _textureRegion.getU2() - b;
        float e = _textureRegion.getV2() - c;
        int f = _textureRegion.getRegionWidth();
        int g = _textureRegion.getRegionHeight();
        for (int i = 0; i < fArr.length; i += 2) {
            fArr2[i] = ((fArr[i] / ((float) f)) * d) + b;
            fArr2[i + 1] = ((1.0f - (fArr[i + 1] / ((float) g))) * e) + c;
        }
        setRegion();
        setSize((float) _textureRegion.getRegionWidth(), (float) _textureRegion.getRegionHeight());
        setBounds(this.polyRegW / 2.0f, this.polyRegH / 2.0f);
        this.polyObjX = this.polyRegCalcX + this.polyRegX;
        this.polyObjY = this.polyRegCalcY + this.polyRegY;
        this.polyObjWRatio = this.polyRegW / ((float) _textureRegion.getRegionWidth());
        this.polyObjHRatio = this.polyRegH / ((float) _textureRegion.getRegionHeight());
        this.polygonObjRotCosDeg = MathUtils.cosDeg(this.degrees);
        this.polygonObjRotSinDeg = MathUtils.sinDeg(this.degrees);
    }

    private void setSize(float f, float f2) {
        this.polyRegW = f;
        this.polyRegH = f2;
        this.dirty = true;
    }

    private void setBounds(float f, float f2) {
        this.polyRegX = f;
        this.polyRegY = f2;
        this.dirty = true;
    }

    private void setRegion() {
        if (this.polygonVertices == null) {
            this.polygonVertices = new float[((this.vertices.length / 2) * 5)];
        }

        // Set the color and UVs in this polygon's vertices.
        float floatColor = this.batchColor.toFloatBits();
        this.interimIdx2 = 0;
        this.interimIdx3 = 2;
        int length = this.vertices.length;
        while (this.interimIdx2 < length) {
            this.polygonVertices[this.interimIdx3] = floatColor; // this.batchColor.toFloatBits(); //m5427b == toFloatBits
            this.polygonVertices[this.interimIdx3 + 1] = this.interimArray[this.interimIdx2];
            this.polygonVertices[this.interimIdx3 + 2] = this.interimArray[this.interimIdx2 + 1];
            this.interimIdx2 += 2;
            this.interimIdx3 += 5;
        }
        this.dirty = true;
    }

    public void setX(float f) {
        translateX(f - this.polyRegCalcX);
    }

    public void setY(float f) {
        translateY(f - this.polyRegCalcY);
    }

    public void translateX(float f) {
        this.polyRegCalcX += f;
        if (!this.dirty) {
            float[] fArr = this.polygonVertices;
            for (int i = 0; i < fArr.length; i += 5) {
                fArr[i] = fArr[i] + f;
            }
        }
    }

    public void translateY(float f) {
        this.polyRegCalcY += f;
        if (!this.dirty) {
            float[] fArr = this.polygonVertices;
            for (int i = 1; i < fArr.length; i += 5) {
                fArr[i] = fArr[i] + f;
            }
        }
    }

    public void setScale(float f) {
        this.scaleX = f;
        this.scaleY = f;
        this.dirty = true;
    }

    public short[] getTriangles() {
        return this.triangles;
    }

    public TextureRegion getRegion() {
        return this.textureRegion;
    }

    /** Returns the packed vertices, colors, and texture coordinates for this polygonal object. */
    public float[] getVertices() {
        if (!this.dirty) {
            return this.polygonVertices;
        }
        this.dirty = false;
        this.polyObjX = this.polyRegCalcX + this.polyRegX;
        this.polyObjY = this.polyRegCalcY + this.polyRegY;
        this.polyObjWRatio = this.polyRegW / ((float) this.textureRegion.getRegionWidth());
        this.polyObjHRatio = this.polyRegH / ((float) this.textureRegion.getRegionHeight());
        this.polygonObjRotCosDeg = MathUtils.cosDeg(this.degrees);
        this.polygonObjRotSinDeg = MathUtils.sinDeg(this.degrees);
        this.interimIdx2 = 0;
        this.interimIdx3 = 0;
        int length = this.vertices.length;
        while (this.interimIdx2 < length) {
            this.fX = ((this.vertices[this.interimIdx2] * this.polyObjWRatio) - this.polyRegX) * this.scaleX;
            this.fY = ((this.vertices[this.interimIdx2 + 1] * this.polyObjHRatio) - this.polyRegY) * this.scaleY;
            this.polygonVertices[this.interimIdx3] = ((this.polygonObjRotCosDeg * this.fX) - (this.polygonObjRotSinDeg * this.fY)) + this.polyObjX;
            this.polygonVertices[this.interimIdx3 + 1] = ((this.polygonObjRotSinDeg * this.fX) + (this.polygonObjRotCosDeg * this.fY)) + this.polyObjY;
            this.interimIdx2 += 2;
            this.interimIdx3 += 5;
        }
        return this.polygonVertices;
    }
}
