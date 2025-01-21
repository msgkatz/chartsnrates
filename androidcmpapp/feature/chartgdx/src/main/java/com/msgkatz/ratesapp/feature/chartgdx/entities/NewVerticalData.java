package com.msgkatz.ratesapp.feature.chartgdx.entities;


import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.GdxSettings;

/**
 * Created by msgkatz on 17/09/2018.
 */

public class NewVerticalData {
    private float idxByScaledDensity;
    private long time;
    private String textValue;
    private int idxByTime;

    public NewVerticalData(float x, long time, String text, int idxByTime) {
        this.idxByScaledDensity = x;
        this.time = time;
        this.textValue = text;
        this.idxByTime = idxByTime;
    }

    public void updateX() {
        this.idxByScaledDensity = ((float) this.idxByTime) * GdxSettings.chartBlockWidthDensityEffective;
    }

    public float getIdxByScaledDensity() {
        return this.idxByScaledDensity;
    }

    public void setIdxByScaledDensity(float f) {
        this.idxByScaledDensity = f;
    }

    public String getText() {
        return this.textValue;
    }

    public int getIdxByTime() {
        return this.idxByTime;
    }
}
