package com.msgkatz.ratesapp.presentation.ui.chart.gdx.common;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class ChartCamera extends OrthographicCamera {
    private final Vector3 vector3 = new Vector3();

    public ChartCamera(float f, float f2) {
        this.viewportWidth = f;
        this.viewportHeight = f2;
        this.near = 0.0f;
        update();
    }

    public void update (boolean updateFrustum) {
        projection.setToOrtho(
                zoom * -viewportWidth / 2,
                zoom * (viewportWidth / 2),
                -(viewportHeight / 2),
                viewportHeight / 2

                , near, far);
        view.setToLookAt(position, vector3.set(position).add(direction), up);
        combined.set(projection);
        Matrix4.mul(combined.val, view.val);

        if (updateFrustum) {
            invProjectionView.set(combined);
            Matrix4.inv(invProjectionView.val);
            frustum.update(invProjectionView);
        }
    }
}
