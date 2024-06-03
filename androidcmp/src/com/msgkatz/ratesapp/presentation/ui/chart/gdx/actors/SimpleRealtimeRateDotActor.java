package com.msgkatz.ratesapp.presentation.ui.chart.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.msgkatz.ratesapp.presentation.entities.CandleData;
import com.msgkatz.ratesapp.utils.gdx.ChartShaderUtil;
import com.msgkatz.ratesapp.utils.gdx.Constants;

/**
 * Created by msgkatz on 11/04/2018.
 */

public class SimpleRealtimeRateDotActor extends Actor {

    private ShaderProgram shaderProgram = ChartShaderUtil.shaderProgram_h;
    private Texture textureMain = new Texture((int) Constants.gdxGraphDensity_3_0_curCurs, (int) Constants.gdxGraphDensity_3_0_curCurs, Pixmap.Format.RGBA8888);
    private Texture texture2nd = new Texture((int) Constants.gdxGraphDensity_5_0_curCurs, (int) Constants.gdxGraphDensity_5_0_curCurs, Pixmap.Format.RGBA8888);
    private Color color = Constants.COLOR_RATE_DOT_3;

    private float currentSize = Constants.gdxGraphDensity_39_0_curCurs;
    private float runningValue = 0.0f;
    private float normalSize = (Constants.gdxGraphDensity_39_0_curCurs / 0.3f);
    private float normalAlfa = 3.3333333f;

    public SimpleRealtimeRateDotActor(CandleData candleData) {
        setX((float) candleData.getIdxByScaledDensity());
        setY((float) candleData.getYPriceCloseScaled());
        setWidth(Constants.gdxGraphDensity_1_0_curCursA);
    }

    public void act(float f) {
        super.act(f);
        this.runningValue += f;
        if (this.runningValue > 1.3f) {
            this.runningValue = 0.0f;
        } else if (this.runningValue <= 0.3f) {
            this.currentSize = this.normalSize * this.runningValue;

            //set
            this.color.set(Constants.COLOR_RATE_DOT_3.r, Constants.COLOR_RATE_DOT_3.g, Constants.COLOR_RATE_DOT_3.b, 1.0f - (this.normalAlfa * this.runningValue));
        } else {
            this.currentSize = Constants.gdxGraphDensity_1_0_curCursB;
            this.color = Constants.COLOR_RATE_DOT_3;
        }
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        batch.end();
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        batch.setColor(this.color);
        batch.setShader(this.shaderProgram);
        this.shaderProgram.setUniformf("disc_radius", 0.5f);
        this.shaderProgram.setUniformf("border_size", 0.1f);
        this.shaderProgram.setUniformf("disc_center", 0.5f, 0.5f);
        this.shaderProgram.setUniformf("disc_color", this.color);
        batch.draw(this.texture2nd, getX() - (this.currentSize / 2.0f), getY() - (this.currentSize / 2.0f), this.currentSize, this.currentSize);
        batch.setShader(null);
        batch.setShader(this.shaderProgram);
        batch.setColor(Constants.COLOR_RATE_DOT_1);
        this.shaderProgram.setUniformf("disc_radius", 0.5f);
        this.shaderProgram.setUniformf("border_size", 0.1f);
        this.shaderProgram.setUniformf("disc_center", 0.5f, 0.5f);
        this.shaderProgram.setUniformf("disc_color", Constants.COLOR_RATE_DOT_1);
        batch.draw(this.texture2nd, getX() - ((float) (this.texture2nd.getWidth() / 2)), getY() - ((float) (this.texture2nd.getWidth() / 2)));
        batch.setShader(null);
        batch.setShader(this.shaderProgram);
        batch.setColor(Constants.COLOR_RATE_DOT_2);
        this.shaderProgram.setUniformf("disc_radius", 0.5f);
        this.shaderProgram.setUniformf("border_size", 0.1f);
        this.shaderProgram.setUniformf("disc_center", 0.5f, 0.5f);
        this.shaderProgram.setUniformf("disc_color", Constants.COLOR_RATE_DOT_2);
        batch.draw(this.textureMain, getX() - ((float) (this.textureMain.getWidth() / 2)), getY() - ((float) (this.textureMain.getWidth() / 2)));
        batch.setShader(null);
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }

    public void updateValue(CandleData candleData) {
        setX((float) candleData.getIdxByScaledDensity());
    }
}
