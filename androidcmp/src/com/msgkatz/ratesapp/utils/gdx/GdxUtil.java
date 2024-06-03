package com.msgkatz.ratesapp.utils.gdx;

import android.content.Context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.msgkatz.ratesapp.R;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class GdxUtil {

    private static FreeTypeFontGenerator freeTypeFontGenerator;
    private static FreeTypeFontGenerator freeTypeFontGeneratorFirst;
    private static FreeTypeFontGenerator freeTypeFontGeneratorFirstNew;
    private static FreeTypeFontGenerator freeTypeFontGeneratorChartLabels;
    private static FreeTypeFontGenerator freeTypeFontGeneratorChartLabelsNew;
    public static BitmapFont bitmapFont_a;
    public static BitmapFont bitmapFont_b;
    public static BitmapFont bitmapFont_c;
    public static BitmapFont bitmapFont_d;
    public static BitmapFont bitmapFont_e;
    public static BitmapFont bitmapFont_f;
    public static BitmapFont bitmapFont_g;
    public static BitmapFont bitmapFont_h;
    public static BitmapFont bitmapFont_i;
    public static BitmapFont bitmapFont_j;
    public static BitmapFont bitmapFont_k;
    public static BitmapFont bitmapFont_l;
    public static GlyphLayout glyphLayout1;
    public static Texture textureLoading;
    public static GlyphLayout glyphLayout2;
    public static float glyphLayout2Width;
    public static GlyphLayout glyphLayout3;
    public static float glyphLayout3Size;
    public static GlyphLayout glyphLayout4;
    public static String glyphLayout1Text;

    public static void init(Context context) {

        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/normativepro_medium.otf"));
        freeTypeFontGeneratorFirst = new FreeTypeFontGenerator(Gdx.files.internal("fonts/normativepro_medium.otf"));
        freeTypeFontGeneratorFirstNew = new FreeTypeFontGenerator(Gdx.files.internal("fonts/normativepro_medium.otf"));
        freeTypeFontGeneratorChartLabels = new FreeTypeFontGenerator(Gdx.files.internal("fonts/normativepro_medium.otf"));
        freeTypeFontGeneratorChartLabelsNew = new FreeTypeFontGenerator(Gdx.files.internal("fonts/normativepro_medium.otf"));

        glyphLayout1Text = context.getResources().getString(R.string.screen_chart_empty_space);
        bitmapFont_a = GdxUtil.genFont_a();
        bitmapFont_b = GdxUtil.genFont_b();
        glyphLayout1 = new GlyphLayout();
        glyphLayout1.setText(bitmapFont_b, glyphLayout1Text);
        bitmapFont_c = GdxUtil.genFont_c();
        bitmapFont_d = GdxUtil.genFont_d();
        bitmapFont_e = GdxUtil.genFont_e();
        bitmapFont_f = GdxUtil.genFont_f();
        bitmapFont_g = GdxUtil.genFont_g();
        bitmapFont_h = GdxUtil.genFont_h();
        bitmapFont_i = GdxUtil.genFont_i();
        bitmapFont_j = GdxUtil.genFont_j();
        bitmapFont_k = GdxUtil.genFont_k();
        bitmapFont_l = GdxUtil.genFont_l();
        glyphLayout2 = new GlyphLayout(bitmapFont_a, "9.99999999");
        glyphLayout2Width = glyphLayout2.width;
        glyphLayout3 = new GlyphLayout(bitmapFont_f, "66.66666");
        glyphLayout3Size = (glyphLayout3.width + Constants.gdxGraphDensity_10_0_gdxUtil) + glyphLayout3.height;
        glyphLayout4 = new GlyphLayout(bitmapFont_a, "66/66\n66:66");

        textureLoading = new Texture(Gdx.files.internal("loading_texture.png"));

        freeTypeFontGenerator.dispose();
        freeTypeFontGeneratorFirst.dispose();
        freeTypeFontGeneratorFirstNew.dispose();
        freeTypeFontGeneratorChartLabels.dispose();
        freeTypeFontGeneratorChartLabelsNew.dispose();
    }

    private static BitmapFont genFont_a() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (10.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_A; // TODO: !!! fonts for vert&horizon actors -- chage it
        return freeTypeFontGeneratorChartLabelsNew.generateFont(freeTypeFontParameter);
        //return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_b() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (10.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_B;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_c() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (9.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_C;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_d() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (9.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_D;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_e() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) Constants.gdxGraphDensity_9_0_gdxUtil;
        freeTypeFontParameter.color = Constants.COLOR_FONT_E;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_f() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (12.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_F;
        return freeTypeFontGeneratorFirstNew.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_g() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (12.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_G;
        return freeTypeFontGeneratorFirst.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_i() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (8.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_I;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_j() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (8.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_J;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_k() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (8.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_K;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_h() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (10.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_H;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static BitmapFont genFont_l() {
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZกขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบปผฝพฟภมยรฤลฦวศษสหฬอฮ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>₽";
        freeTypeFontParameter.size = (int) (10.0f * Gdx.graphics.getDensity());
        freeTypeFontParameter.color = Constants.COLOR_FONT_L;
        return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
    }

    private static String getFolderByDensity(float density) {
        if (density <= 1.0f) {
            return "mdpi/";
        }
        if (density <= 1.5f) {
            return "hdpi/";
        }
        if (density <= 2.0f) {
            return "xhdpi/";
        }
        if (density <= 3.0f) {
            return "xxhdpi/";
        }
        return "xxxhdpi/";
    }
}
