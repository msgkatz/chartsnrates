package com.msgkatz.ratesapp.feature.chartgdx.gdx.common;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.msgkatz.ratesapp.feature.chartgdx.entities.ChartType;
import com.msgkatz.ratesapp.feature.chartgdx.gdx.SimpleChartStage;
import com.msgkatz.ratesapp.feature.chartgdx.gdx.prerenderer.PreRenderer;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.Constants;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.GdxSettings;


/**
 * Created by msgkatz on 15/09/2018.
 */

public class SimpleChartScreen implements Screen {

    private Stage stage;
    private FPSLogger fpsLogger = new FPSLogger();
    private Game game;
    private PreRenderer preRenderer;

    public SimpleChartScreen(Game game, PreRenderer preRenderer)
    {
        this.game = game;
        this.preRenderer = preRenderer;
        this.stage = new SimpleChartStage(new PolygonSpriteBatch(), this.preRenderer);
        this.preRenderer.setStageControllerListener((SimpleChartStage) this.stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT
                | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        Gdx.gl20.glClearColor(Constants.COLOR_CLEAR.r, Constants.COLOR_CLEAR.g, Constants.COLOR_CLEAR.b, Constants.COLOR_CLEAR.a);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glEnable(GL20.GL_BLEND_COLOR);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        this.stage.act(delta);
        this.stage.draw();


    }

    @Override
    public void resize(int width, int height) {

        if (this.preRenderer == null) {
            GdxSettings.init(ChartType.Curve, 1.0f, 0);
        } else if (this.preRenderer.getChartType() == ChartType.Curve) {
            GdxSettings.init(ChartType.Curve, ((SimpleChartStage) this.stage).getZoom(), this.preRenderer.getExtraChartCount());
        } else {
            GdxSettings.init(ChartType.Candle, ((SimpleChartStage) this.stage).getZoom(), this.preRenderer.getExtraChartCount());
        }

        ((SimpleChartStage) this.stage).init();
        this.preRenderer.reInitChart();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        ((SimpleChartStage) this.stage).init();
        this.preRenderer.reInitChart();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
