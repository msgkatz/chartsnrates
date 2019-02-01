package com.msgkatz.ratesapp.utils.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class ChartShaderUtil {

    private static final String SHADER_PATH = "shaders/";
    private static final String SHADER_CURVE_V = SHADER_PATH + "curve_line_shader.vert";
    private static final String SHADER_CURVE_F = SHADER_PATH + "curve_line_shader.frag";

    private static final String SHADER_HORIZ_GRID_V = SHADER_PATH + "dash_line_horizontal_grid.vert";
    private static final String SHADER_HORIZ_GRID_F = SHADER_PATH + "dash_line_horizontal_grid.frag";

    private static final String SHADER_CIRCLE_V = SHADER_PATH + "new_circle_shader.vert";
    private static final String SHADER_CIRCLE_F = SHADER_PATH + "new_circle_shader.frag";

    private static final String SHADER_LOADER_V = SHADER_PATH + "loader_shader.vert"; // loader_shader_new.vert
    private static final String SHADER_LOADER_F = SHADER_PATH + "loader_shader.frag"; // loader_shader_new.frag

    public static ShaderProgram shaderProgram_c; //SimpleCurveGraphicActor
    public static ShaderProgram shaderProgram_d; //SimpleHorizontalGridActor
    public static ShaderProgram shaderProgram_h; //SimpleRealtimeRateDotActor
    public static ShaderProgram shaderProgram_i; //LoaderExtendedActor

    public static void init() {
        ShaderProgram.pedantic = false;

        shaderProgram_c = new ShaderProgram(Gdx.files.internal(SHADER_CURVE_V), Gdx.files.internal(SHADER_CURVE_F));
        if (shaderProgram_c.isCompiled()) {
            shaderProgram_d = new ShaderProgram(Gdx.files.internal(SHADER_HORIZ_GRID_V), Gdx.files.internal(SHADER_HORIZ_GRID_F));
            if (shaderProgram_d.isCompiled()) {
                shaderProgram_h = new ShaderProgram(Gdx.files.internal(SHADER_CIRCLE_V), Gdx.files.internal(SHADER_CIRCLE_F));
                if (shaderProgram_h.isCompiled()) {
                    shaderProgram_i = new ShaderProgram(Gdx.files.internal(SHADER_LOADER_V), Gdx.files.internal(SHADER_LOADER_F));
                    if (!shaderProgram_i.isCompiled()) {
                        throw new GdxRuntimeException("Couldn't compile shader: " + shaderProgram_i.getLog());
                    }
                    return;
                }
                throw new GdxRuntimeException("Couldn't compile shader: " + shaderProgram_h.getLog());
            }
            throw new GdxRuntimeException("Couldn't compile shader: " + shaderProgram_d.getLog());
        }
        throw new GdxRuntimeException("Couldn't compile shader: " + shaderProgram_c.getLog());

    }
}
