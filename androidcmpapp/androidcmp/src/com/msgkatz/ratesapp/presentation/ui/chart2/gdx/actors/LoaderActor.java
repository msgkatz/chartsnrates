package com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.msgkatz.ratesapp.utils.gdx.Constants;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;
import com.msgkatz.ratesapp.utils.gdx.GdxUtil;

/**
 * Created by msgkatz on 12/02/2018.
 */

public class LoaderActor extends BaseLoaderActor {

    protected Texture textureMain = GdxUtil.textureLoading;
    protected TextureRegion textureRegion;
    protected Camera camera;
    protected float offset1;
    protected float offset2;
    protected boolean drawBgTexture;
    protected Texture texture2nd;

    public LoaderActor() {}

    public LoaderActor(Camera camera, float f, float f2, float f3, float f4, boolean z) {

        this.textureMain.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.camera = camera;
        this.drawBgTexture = z;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Constants.COLOR_LOADER);
        pixmap.fill();
        this.texture2nd = new Texture(pixmap);
        setX(f);
        setY(f2);
        setWidth(f3);
        setHeight(f4);
        this.textureRegion = new TextureRegion(this.textureMain, (int) f3, (int) f4);
        this.offset1 = this.camera.position.x - (camera.viewportWidth * 1.5f);
        this.offset2 = this.camera.position.x - (camera.viewportWidth / 2.0f);
    }

    public void act(float f) {
        super.act(f);
        this.offset1 += (Gdx.graphics.getDensity() * 5.0f) * f;
        if (this.offset1 >= this.camera.position.x + (this.camera.viewportWidth / 2.0f)) {
            this.offset1 = this.camera.position.x - (this.camera.viewportWidth * 1.5f);
        }
        this.offset2 += (Gdx.graphics.getDensity() * 5.0f) * f;
        if (this.offset2 >= this.camera.position.x + (this.camera.viewportWidth / 2.0f)) {
            this.offset2 = this.camera.position.x - (this.camera.viewportWidth * 1.5f);
        }
        setY(GdxSettings.heightExtraChartFull);
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        batch.end();
        batch.setShader(null);
        batch.begin();
        if (this.drawBgTexture) {
            batch.draw(this.texture2nd, getX(), getY(), getWidth(), getHeight());
        }

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(this.textureRegion, getX(), getY(), getWidth(), getHeight());
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
}