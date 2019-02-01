package com.msgkatz.ratesapp.presentation.ui.chart.gdx.common;

import android.app.Activity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.msgkatz.ratesapp.presentation.entities.ToolFormat;
import com.msgkatz.ratesapp.presentation.ui.chart.gdx.prerenderer.PreRenderer;
import com.msgkatz.ratesapp.utils.gdx.GdxUtil;
import com.msgkatz.ratesapp.utils.gdx.ChartShaderUtil;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class ChartGdxGame extends Game {

    final private Activity activity;
    private Screen screen;
    private PreRenderer preRenderer;

    public ChartGdxGame(Activity activity, PreRenderer preRenderer)
    {
        this.activity = activity;
        this.preRenderer = preRenderer;
    }

    @Override
    public void create() {
        Gdx.app.getPreferences(getPrefName()).flush();
        GdxUtil.init(this.activity);
        ChartShaderUtil.init();

        if (getScreen() == null) {
            this.screen = new SimpleChartScreen(this, this.preRenderer);
            //this.screen = new SimpleChartScreenDemo(this);
            setScreen(this.screen);
        }
    }

    public void render() {
        super.render();
    }

    public void resume() {
        super.resume();
    }

    private String getPrefName() {
        return this.activity.getPackageName() + "_preferences";
    }
}
