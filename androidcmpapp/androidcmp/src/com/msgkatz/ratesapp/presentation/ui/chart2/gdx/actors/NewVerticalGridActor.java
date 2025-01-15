package com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.msgkatz.ratesapp.presentation.entities.NewVerticalData;
import com.msgkatz.ratesapp.utils.gdx.Constants;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;
import com.msgkatz.ratesapp.utils.gdx.GdxUtil;

public class NewVerticalGridActor extends Actor {

    private Camera camera;
    private TextureRegion textureRegion;
    private Array<NewVerticalData> verticalDataArray;
    private float fontY;
    private float textureY;
    private int visibleRangeIdxFirst;
    private int visibleRangeIdxLast;

    public NewVerticalGridActor(Camera camera, Array<NewVerticalData> dataArray) {
        this.camera = camera;
        this.verticalDataArray = dataArray;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Constants.COLOR_VERTICAL_GRID);
        pixmap.fill();
        this.textureRegion = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
    }

    public void act(float f) {
        super.act(f);
        this.fontY = (((this.camera.position.y - (this.camera.viewportHeight / 2.0f)) + GdxSettings.heightExtraChartFull) + Constants.gdxGraphDensity_8_0_vertGrid) + GdxUtil.glyphLayout4.height;
        this.textureY = (((this.camera.position.y - (this.camera.viewportHeight / 2.0f)) + GdxSettings.heightExtraChartFull) + (Constants.gdxGraphDensity_8_0_vertGrid * 2.0f)) + GdxUtil.glyphLayout4.height;
    }

    public void updateVisibleRange(int i, int i2) {
        this.visibleRangeIdxFirst = i;
        this.visibleRangeIdxLast = i2;
    }

    public void draw(Batch batch, float f) {
        super.draw(batch, f);
        if (this.verticalDataArray.size > 0) {
            batch.setShader(null);
            int i = 0;
            while (i < this.verticalDataArray.size) {
                if (((NewVerticalData) this.verticalDataArray.get(i)).getIdxByTime() > this.visibleRangeIdxFirst
                        && ((NewVerticalData) this.verticalDataArray.get(i)).getIdxByTime() < this.visibleRangeIdxLast)
                {
                    batch.draw(this.textureRegion,
                            ((NewVerticalData) this.verticalDataArray.get(i)).getIdxByScaledDensity() - (0.5f * Gdx.graphics.getDensity()),
                            this.textureY,
                            Gdx.graphics.getDensity(),
                            (float) Gdx.graphics.getHeight());
                            //(float) Gdx.graphics.getWidth());

                    GdxUtil.bitmapFont_a.draw(batch,
                            ((NewVerticalData) this.verticalDataArray.get(i)).getText(),
                            ((NewVerticalData) this.verticalDataArray.get(i)).getIdxByScaledDensity() - (GdxUtil.glyphLayout4.width / 2.0f),
                            this.fontY);
                }
                i++;
            }
        }
    }
}
