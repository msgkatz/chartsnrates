package com.msgkatz.ratesapp.presentation.ui.chart2.gdx.common;

import android.app.Activity;
import android.content.Context;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.prerenderer.PreRenderer;
import com.msgkatz.ratesapp.utils.gdx.ChartShaderUtil;
import com.msgkatz.ratesapp.utils.gdx.GdxUtil;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class ChartGdxGame extends Game {

    final private Activity activity;
    final private Context context;
    private Screen screen;
    private PreRenderer preRenderer;

    public ChartGdxGame(Activity activity, PreRenderer preRenderer)
    {
        this.activity = activity;
        this.context = activity;
        this.preRenderer = preRenderer;
    }

    public ChartGdxGame(Context context, PreRenderer preRenderer)
    {
        this.activity = null;
        this.context = context;
        this.preRenderer = preRenderer;
    }

    @Override
    public void create() {
        Gdx.app.getPreferences(getPrefName()).flush();
        //GdxUtil.init(this.activity);
        GdxUtil.init(this.context);
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
        return this.context.getPackageName() + "_preferences";
    }
}
