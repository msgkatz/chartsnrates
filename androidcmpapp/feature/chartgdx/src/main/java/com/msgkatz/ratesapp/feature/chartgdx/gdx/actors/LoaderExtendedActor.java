package com.msgkatz.ratesapp.feature.chartgdx.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.ChartShaderUtil;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.Constants;
import com.msgkatz.ratesapp.feature.chartgdx.utils.gdx.GdxSettings;


/**
 * Created by msgkatz on 12/10/2018.
 */

public class LoaderExtendedActor extends BaseLoaderActor {

    private ShaderProgram shaderProgram = ChartShaderUtil.shaderProgram_i;
    private Texture textureMain = new Texture((int) Constants.gdxGraphDensity_3_0_curCurs, (int) Constants.gdxGraphDensity_3_0_curCurs, Pixmap.Format.RGBA8888);

    //protected Texture textureMain = GdxUtil.textureLoading;
    protected TextureRegion textureRegion;
    protected Camera camera;
    protected float offset1;
    protected float offset2;
    protected boolean drawBgTexture;
    protected Texture texture2nd;

    private float timeStart = 0.0f;
    private int timeStartInt = 0;

    public LoaderExtendedActor(Camera camera, float f, float f2, float f3, float f4, boolean z) {
        //super(camera, f, f2, f3, f4, z);

        textureMain = new Texture((int) f3, (int) f4, Pixmap.Format.RGBA8888);
        //this.textureMain.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
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

    @Override
    public void act(float f) {
        super.act(f);

        if (timeStart > 7)
            timeStart = 1.0f;
        this.timeStart += f;
        this.timeStartInt++;

        // Gdx.app.log(this.getClass().getSimpleName(), "::f=" + f + ", tomeStart=" + timeStart + ", timeStartInt=" + timeStartInt);

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

    @Override
    public void draw(Batch batch, float f) {
        super.draw(batch, f);

        batch.end();
        //batch.setShader(null);

//        if (this.drawBgTexture) {
//            batch.draw(this.texture2nd, getX(), getY(), getWidth(), getHeight());
//        }

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();
        batch.setShader(this.shaderProgram);
        this.shaderProgram.setUniformf("u_time", timeStart);
        this.shaderProgram.setUniformf("u_time_int", timeStartInt);
        batch.draw(this.textureMain, getX(), getY(), getWidth(), getHeight());
        //batch.draw(this.textureMain, getX() + offset1, getY(), getWidth(), getHeight());
        batch.setShader(null);
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
}
