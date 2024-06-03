package com.msgkatz.ratesapp.presentation.ui.chart.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.msgkatz.ratesapp.utils.gdx.Constants;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;


public class NewRightVoidActor extends Actor {

    private static final float gdxGraphDensity_18_0 = (18.0f * Gdx.graphics.getDensity());
    private ShapeRenderer shapeRenderer;
    private Camera camera;
    private float actorWidth;

    public NewRightVoidActor(ShapeRenderer shapeRenderer, Camera camera, BitmapFont bitmapFont) {
        this.shapeRenderer = shapeRenderer;
        this.camera = camera;
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bitmapFont, String.valueOf(1.11111111f));
        this.actorWidth = glyphLayout.width + gdxGraphDensity_18_0;
        setBounds((camera.position.x + (camera.viewportWidth / 2.0f)) - this.actorWidth,
                (camera.position.y - (camera.viewportHeight / 2.0f)) + GdxSettings.heightExtraChartFull,
                this.actorWidth,
                GdxSettings.heightMainChartFull);
    }

    public void act(float f) {
        super.act(f);
        setBounds((this.camera.position.x + (this.camera.viewportWidth / 2.0f)) - this.actorWidth,
                (this.camera.position.y - (this.camera.viewportHeight / 2.0f)) + GdxSettings.heightExtraChartFull,
                this.actorWidth,
                GdxSettings.heightMainChartFull);
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        batch.end();
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        this.shapeRenderer.setAutoShapeType(true);
        this.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        this.shapeRenderer.translate(getOriginX(), getOriginY(), 0.0f);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(Constants.COLOR_VOID);
        this.shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        this.shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        batch.begin();
    }
}
