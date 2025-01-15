package com.msgkatz.ratesapp.presentation.ui.chart2.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.msgkatz.ratesapp.presentation.entities.CandleData;
import com.msgkatz.ratesapp.presentation.entities.ChartType;
import com.msgkatz.ratesapp.presentation.entities.NewVerticalData;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.BaseLoaderActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.LoaderActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.LoaderExtendedActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.NewRightVoidActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.NewVerticalGridActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.SimpleCurveGraphicActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.SimpleHorizontalGridActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.SimpleRealtimeRateDotActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.actors.SimpleRealtimeRateLineActor;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.common.ChartCamera;
import com.msgkatz.ratesapp.presentation.ui.chart2.gdx.prerenderer.PreRenderer;
import com.msgkatz.ratesapp.utils.NumFormatUtil;
import com.msgkatz.ratesapp.utils.gdx.GdxSettings;
import com.msgkatz.ratesapp.utils.gdx.GdxUtil;

import java.util.HashMap;

/**
 * Created by msgkatz on 15/09/2018.
 */

public class SimpleChartStage extends Stage implements GestureDetector.GestureListener, StageControllerListener
{
    private static final String TAG = SimpleChartStage.class.getSimpleName();

    private ShapeRenderer                       shapeRenderer = new ShapeRenderer();
    private PreRenderer controller;
    private ChartType                           chartType;

    /*** Actors ***/
    private SimpleCurveGraphicActor simpleCurveGraphicActor;
    private NewVerticalGridActor newVerticalGridActor;
    private SimpleRealtimeRateDotActor simpleCurrentCursDotActor;
    private SimpleRealtimeRateLineActor simpleCurrentCursLineActor;
    private NewRightVoidActor newRightVoidActor;
    private SimpleHorizontalGridActor simpleHorizontalGridActor;

    private HashMap<String, LoaderActor>        stringLoaderActorHashMap;
    private BaseLoaderActor loaderActor;                // LoaderActor loaderActor;

    /*** Params ***/
    private Array<CandleData>       candleDataArray;
    private float                   zoom;
    private float                   lastZoomDistance;
    private float                   zoomMax;
    private boolean                 isZoomable;

    private float                   priceLowMin1;             // priceLowMin1
    private float                   priceHighMax1;            // priceHighMax1
    private float                   ratioHeightPricesDelta1;  // chartHeight/PriceDelta ratio1
    private float                   priceLowMin2;             // priceLowMin2
    private float                   ratioHeightPricesDelta2;  // chartHeight/PriceDelta ratio2

    private float                   deltaForLowestPrices;     // lowestPricesDelta = low2 - low1
    private float                   deltaForRatios;           // heightPriceRatiosDelta = ratio2 - ratio1
    private boolean                 adjustCurveOnAct;

    private int                     visibleRangeIdxFirst;
    private int                     visibleRangeIdxLast;
    private int                     visibleRangeIdxFirstZeroBased;
    private int                     visibleRangeIdxLastZeroBased;

    private int                     runningIdxByTime;

    private float                   runningX;
    private float                   runningXAlter;
    private float                   runningXNew;
    private float                   firstX;
    private float                   lastX;
    private float                   minHistoricalX;

    private float                   runningY;
    private float                   runningYPrev;
    private float                   runningYDelta;

    private float                   priceClosePrev;

    private boolean                 isTouchedDown;          // touchDown & has primitives
    private boolean                 isPanning;              // pan && !isTouchedDown
    private boolean                 isPanningAlter;         // pan alter
    private boolean                 isFlinging;             // fling

    private float                   velocityX;
    private float                   deltaX;

    private float                   cameraTranslationX;

    public SimpleChartStage(Batch batch, PreRenderer preRenderer)
    {
        super(new ScreenViewport(new ChartCamera((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight())), batch);
        ShaderProgram.pedantic = false;
        this.controller = preRenderer;
        this.stringLoaderActorHashMap = new HashMap();

        this.zoom = 1.0f;
        this.isZoomable = false;

        InputProcessor inputMultiplexer = new InputMultiplexer(new GestureDetector(this), this);
        if (Gdx.input != null) {
            Gdx.input.setInputProcessor(inputMultiplexer);
        }

        if (controller.getCandleDataArray() != null && controller.getCandleDataArray().size > 0) {
            processDataArray(controller.getCandleDataArray(), true);
        }
    }

    public void init()
    {
        Gdx.app.log(TAG, "gdx stage init");
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        getBatch().setProjectionMatrix(getCamera().combined);
        this.controller.setStageControllerListener((StageControllerListener) this);
        InputProcessor inputMultiplexer = new InputMultiplexer(new GestureDetector(this), this);
        if (Gdx.input != null) {
            Gdx.input.setInputProcessor(inputMultiplexer);
        }

        if (this.candleDataArray != null && this.candleDataArray.size > 0) {
            this.runningXAlter = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
            this.runningX = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
            this.runningXNew = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
        }

        this.velocityX = 0.0f;
        this.deltaX = 0.0f;
    }

    @Override
    public void act(float delta) {

//        if (Parameters.DEBUG_GDX_RENDERING_LOGGING)
//            Gdx.app.log(TAG, "<<act>> started for delta=" + delta);

        int i = 0;
        if (this.candleDataArray != null && this.candleDataArray.size > 0)
        {
            this.visibleRangeIdxFirst = (int) ((getCamera().position.x - (getCamera().viewportWidth / 2.0f)) / GdxSettings.chartBlockWidthDensityEffective);
            if (this.visibleRangeIdxFirst < ((CandleData) this.candleDataArray.first()).getIdxByTime()) {
                this.visibleRangeIdxFirst = ((CandleData) this.candleDataArray.first()).getIdxByTime();
            }
            this.visibleRangeIdxLast = (this.visibleRangeIdxFirst + GdxSettings.chartBlockWidthReal) + 1;
            if (this.visibleRangeIdxLast > ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) {
                this.visibleRangeIdxLast = ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime();
            }
            this.visibleRangeIdxFirstZeroBased = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + this.visibleRangeIdxFirst;
            this.visibleRangeIdxLastZeroBased = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + this.visibleRangeIdxLast;

            /*** Camera IF Block Start ***/
            if (this.isPanning) {
                getCamera().translate(-this.velocityX, 0.0f, 0.0f);
            } else if (!this.isFlinging) {
                if (this.velocityX != 0.0f) {
                    getCamera().translate((-this.velocityX) * delta, 0.0f, 0.0f);
                    if (this.velocityX > 0.0f) {
                        this.velocityX -= this.velocityX * delta;
                        if (Math.round(this.velocityX) == 0) {
                            this.velocityX = 0.0f;
                        }
                        if (this.velocityX < 0.0f) {
                            this.velocityX = 0.0f;
                        }
                    } else {
                        this.velocityX -= this.velocityX * delta;
                        if (Math.round(this.velocityX) == 0) {
                            this.velocityX = 0.0f;
                        }
                        if (this.velocityX > 0.0f) {
                            this.velocityX = 0.0f;
                        }
                    }
                }
                if (this.controller.getCandleDataArray() != null && this.controller.getCandleDataArray().size > 0 && this.isPanningAlter) {
                    recalcOnDataItem(this.visibleRangeIdxFirst, this.visibleRangeIdxLast);
                    if (this.simpleCurveGraphicActor != null) {
                        this.simpleCurveGraphicActor.updateLastPrices(this.priceLowMin1, this.ratioHeightPricesDelta1);
                    }

                    this.velocityX = 0.0f;
                    this.isPanningAlter = false;
                }
            } else if (this.deltaX > 0.0f) {
                if (this.velocityX < 0.0f) {
                    getCamera().translate((this.velocityX * delta) * 2.0f, 0.0f, 0.0f);
                    this.velocityX += (this.deltaX * 4.0f) * 3.0f;
                    if ((getCamera().position.x - (getCamera().viewportWidth / 2.0f)) + ((this.velocityX * delta) * 2.0f) <= this.firstX) {
                        this.firstX -= GdxSettings.chartBlockWidthDensityEffective * 600.0f;
                        //this.controller.loadHistoryBlock();
                        getCamera().translate((this.velocityX * delta) * 2.0f, 0.0f, 0.0f);
                        this.velocityX += (this.deltaX * 4.0f) * 3.0f;
                    } else {
                        getCamera().translate((this.velocityX * delta) * 2.0f, 0.0f, 0.0f);
                        this.velocityX += (this.deltaX * 4.0f) * 3.0f;
                    }
                } else {
                    this.isFlinging = false;
                    this.velocityX = 0.0f;
                    recalcOnDataItem(this.visibleRangeIdxFirst, this.visibleRangeIdxLast);
                }
            } else if (this.velocityX > 0.0f) {
                getCamera().translate((this.velocityX * delta) * 2.0f, 0.0f, 0.0f);
                this.velocityX += (this.deltaX * 4.0f) * 3.0f;
                if (getCamera().position.x + ((this.velocityX * delta) * 2.0f) > this.lastX) {
                    getCamera().translate(((this.velocityX * delta) * 2.0f) - ((getCamera().position.x + ((this.velocityX * delta) * 2.0f)) - this.lastX), 0.0f, 0.0f);
                    this.velocityX = 0.0f;
                } else {
                    getCamera().translate((this.velocityX * delta) * 2.0f, 0.0f, 0.0f);
                    this.velocityX += (this.deltaX * 4.0f) * 3.0f;
                }
            } else {
                this.isFlinging = false;
                this.velocityX = 0.0f;
                recalcOnDataItem(this.visibleRangeIdxFirst, this.visibleRangeIdxLast);
            }

            /*** Camera IF Block End ***/


            if (this.newVerticalGridActor != null) {
//                if (Parameters.DEBUG_GDX_RENDERING_LOGGING)
//                    Gdx.app.log(TAG, "newVerticalGridActor != null, size=" + Integer.toString(controller.getVerticalDataArray().size));
                this.newVerticalGridActor.updateVisibleRange(this.visibleRangeIdxFirst, this.visibleRangeIdxLast);
            }

            if (this.simpleCurveGraphicActor != null) {
                this.simpleCurveGraphicActor.updateVisibleRange(this.visibleRangeIdxFirstZeroBased, this.visibleRangeIdxLastZeroBased);
            }
            this.runningIdxByTime = (int) (getCamera().position.x / GdxSettings.chartBlockWidthDensityEffective);

            if (this.adjustCurveOnAct) {
                int i2;
                this.priceClosePrev = (((this.runningYPrev - GdxSettings.heightMainChartBorder) - GdxSettings.heightExtraChartFull) / this.ratioHeightPricesDelta1) + this.priceLowMin1;
                if (this.priceLowMin1 != this.priceLowMin2) {
                    if (this.deltaForLowestPrices > 0.0f) {
                        this.priceLowMin1 += (this.deltaForLowestPrices * delta) * 4.0f;
                        if (this.priceLowMin1 >= this.priceLowMin2) {
                            this.priceLowMin1 = this.priceLowMin2;
                        }
                    } else {
                        this.priceLowMin1 += (this.deltaForLowestPrices * delta) * 4.0f;
                        if (this.priceLowMin1 <= this.priceLowMin2) {
                            this.priceLowMin1 = this.priceLowMin2;
                        }
                    }
                }
                if (this.deltaForRatios > 0.0f) {
                    this.ratioHeightPricesDelta1 += (this.deltaForRatios * delta) * 4.0f;
                    if (this.ratioHeightPricesDelta1 >= this.ratioHeightPricesDelta2) {
                        this.ratioHeightPricesDelta1 = this.ratioHeightPricesDelta2;
                        this.adjustCurveOnAct = false;
                    }
                } else {
                    this.ratioHeightPricesDelta1 += (this.deltaForRatios * delta) * 4.0f;
                    if (this.ratioHeightPricesDelta1 <= this.ratioHeightPricesDelta2) {
                        this.ratioHeightPricesDelta1 = this.ratioHeightPricesDelta2;
                        this.adjustCurveOnAct = false;
                    }
                }
                if (this.simpleCurveGraphicActor != null) {
                    this.simpleCurveGraphicActor.updateLastPrices(this.priceLowMin1, this.ratioHeightPricesDelta1);
                }

                if (this.simpleHorizontalGridActor != null) {
                    this.simpleHorizontalGridActor.updateGrid(this.priceLowMin1, this.priceHighMax1, this.ratioHeightPricesDelta1);
                }

                this.runningYPrev = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((this.priceClosePrev - this.priceLowMin1) * this.ratioHeightPricesDelta1);
                if (this.candleDataArray != null && this.candleDataArray.size > 0) {
                    this.runningY = ((((float) ((CandleData) this.candleDataArray.peek()).getPriceClose()) - this.priceLowMin1) * this.ratioHeightPricesDelta1) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder);
                }
            }

        }

        if (this.runningYPrev != this.runningY) {
            this.runningYPrev += (this.runningYDelta * delta) * 2.0f;
            if (this.runningYDelta > 0.0f) {
                if (this.runningYPrev > this.runningY) {
                    this.runningYPrev = this.runningY;
                }
            } else if (this.runningYPrev < this.runningY) {
                this.runningYPrev = this.runningY;
            }
            if (this.simpleCurrentCursDotActor != null) {
                this.simpleCurrentCursDotActor.setY(this.runningYPrev);
            }
            if (this.simpleCurrentCursLineActor != null) {
                this.simpleCurrentCursLineActor.setY(this.runningYPrev);
            }
        } else {
            if (this.simpleCurrentCursDotActor != null) {
                this.simpleCurrentCursDotActor.setY(this.runningY);
            }
            if (this.simpleCurrentCursLineActor != null) {
                this.simpleCurrentCursLineActor.setY(this.runningY);
            }
        }
        if (this.runningX != this.runningXNew && this.runningXNew > 0.0f) {
            this.runningX += (GdxSettings.chartBlockWidthDensityEffective * 2.0f) * delta;
            if ((getCamera().position.x + (getCamera().viewportWidth / 2.0f)) - GdxSettings.glyphWidthNormalized > this.runningX) {
                this.cameraTranslationX = (GdxSettings.chartBlockWidthDensityEffective * 2.0f) * delta;
            }
            if (this.runningX > this.runningXNew) {
                this.runningX = this.runningXNew;
                this.cameraTranslationX = 0.0f;
            }
            if (this.simpleCurrentCursDotActor != null) {
                this.simpleCurrentCursDotActor.setX(this.runningX);
            }

            getCamera().translate(this.cameraTranslationX, 0.0f, 0.0f);
        }
        
        super.act(delta);

//        if (Parameters.DEBUG_GDX_RENDERING_LOGGING)
//            Gdx.app.log(TAG, "<<act>> ended");
    }
    
    

    public void processDataArray(Array<CandleData> _candleDataArray, boolean z)
    {
        Gdx.app.log(TAG, "processDataArray:: processing _candleDataArray to GDX Stage, size="
                + (_candleDataArray==null? "null":_candleDataArray.size));

        //Gdx.app.log(TAG, "<<<<< workaround for loaderActor >>>>>>");
//        if (this.loaderActor != null) {
//            this.loaderActor.remove();
//            this.loaderActor = null;
//        }

        int i = 0;
        this.candleDataArray = _candleDataArray;
        if (_candleDataArray != null && _candleDataArray.size > 0)
        {
            int f;
            float g;

            float g2 = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
            if (z) {
                Gdx.app.log(TAG, "processDataArray:: candleData.peek==" + ((CandleData) _candleDataArray.peek()).toString());
                this.runningIdxByTime = ((CandleData) _candleDataArray.peek()).getIdxByTime();
            } else {
                g2 = ((float) this.runningIdxByTime) * GdxSettings.chartBlockWidthDensityEffective;
            }
            int i2 = this.runningIdxByTime - (GdxSettings.chartBlockWidthReal / 2);
            if (i2 < ((CandleData) this.candleDataArray.first()).getIdxByTime()) {
                f = ((CandleData) this.candleDataArray.first()).getIdxByTime();
                //g = (float) ((CandleData) _candleDataArray.get((int) (_candleDataArray.size - 1))).getIdxByScaledDensity();
                //g=g2;

                if ((GdxSettings.chartBlockWidthReal / 2) < _candleDataArray.size) {
                    g = (float) ((CandleData) _candleDataArray.get(GdxSettings.chartBlockWidthReal / 2)).getIdxByScaledDensity();
                    Gdx.app.log(TAG, "count ver.0 g=" + g);
                }
                else if ((GdxSettings.chartBlockWidthReal / 3) < _candleDataArray.size) {
                    g = (float) ((CandleData) _candleDataArray.get(GdxSettings.chartBlockWidthReal / 3)).getIdxByScaledDensity();
                    Gdx.app.log(TAG, "count ver.1 g=" + g);
                }
                else if ((GdxSettings.chartBlockWidthReal / 4) < _candleDataArray.size) {
                    g = (float) ((CandleData) _candleDataArray.get(GdxSettings.chartBlockWidthReal / 4)).getIdxByScaledDensity();
                    Gdx.app.log(TAG, "count ver.2 g=" + g);
                }
                else {
                    g = g2;
                    Gdx.app.log(TAG, "count ver.3 g=" + g);
                }

            } else {
                g = g2;
                f = i2;
                Gdx.app.log(TAG, "count ver.4 g=" + g);
            }
            i2 = (GdxSettings.chartBlockWidthReal / 2) + this.runningIdxByTime;
            if (i2 > ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) {
                i2 = ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime();
            }
            this.visibleRangeIdxFirstZeroBased = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + f;
            this.visibleRangeIdxLastZeroBased = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + i2;
            if (this.visibleRangeIdxLastZeroBased >= this.candleDataArray.size) {
                this.visibleRangeIdxLastZeroBased = this.candleDataArray.size - 1;
            }
            getActors().clear();


            this.minHistoricalX = (float) ((CandleData) this.candleDataArray.first()).getIdxByScaledDensity();
            this.firstX = (float) ((CandleData) this.candleDataArray.first()).getIdxByScaledDensity();
            this.lastX = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
            recalcOnDataArray(f, i2);
            this.runningXAlter = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
            this.runningX = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
            this.runningXNew = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
            this.velocityX = 0.0f;
            this.deltaX = 0.0f;
            this.runningYPrev = (float) ((CandleData) _candleDataArray.peek()).getYPriceCloseScaled();
            this.runningY = (float) ((CandleData) _candleDataArray.peek()).getYPriceCloseScaled();

            this.newVerticalGridActor = new NewVerticalGridActor(getCamera(), this.controller.getVerticalDataArray());
            addActor(this.newVerticalGridActor);

            if (this.chartType == ChartType.Curve) {
                this.simpleCurveGraphicActor = new SimpleCurveGraphicActor(_candleDataArray, this.shapeRenderer, this.priceLowMin1, this.ratioHeightPricesDelta1, getCamera());
                addActor(this.simpleCurveGraphicActor);

                this.zoomMax = 9.0f;
            }

            this.simpleCurrentCursDotActor = new SimpleRealtimeRateDotActor((CandleData) _candleDataArray.peek());
            this.simpleCurrentCursDotActor.setX(this.runningX);
            this.simpleCurrentCursDotActor.setY(this.runningYPrev);
            addActor(this.simpleCurrentCursDotActor);

            this.newRightVoidActor = new NewRightVoidActor(this.shapeRenderer, getCamera(), GdxUtil.bitmapFont_f);
            addActor(this.newRightVoidActor);
            this.simpleHorizontalGridActor = new SimpleHorizontalGridActor(getCamera(), this.controller.getToolFormat());
            this.simpleHorizontalGridActor.updateGrid(this.priceLowMin1, this.priceHighMax1, this.ratioHeightPricesDelta1);
            addActor(this.simpleHorizontalGridActor);


            this.simpleCurrentCursLineActor = new SimpleRealtimeRateLineActor((CandleData) _candleDataArray.peek(), getCamera(), GdxUtil.bitmapFont_f, this.chartType, this.controller.getToolFormat());
            addActor(this.simpleCurrentCursLineActor);

            getCamera().translate(g - getCamera().position.x, 0.0f, 0.0f);

            this.isZoomable = true;
        }

    }

    public void processDataHistoryArray(Array<CandleData> _candleDataArray, String historicalBlockName)
    {
        int i = 0;
        Gdx.app.log(TAG, "processDataHistoryArray:: processing historical _candleDataArray to GDX Stage, size="
                + (_candleDataArray==null? "null":_candleDataArray.size));

        Gdx.app.log(TAG, "<<<<< workaround for loaderActor >>>>>>");
        if (this.loaderActor != null) {
            this.loaderActor.remove();
            this.loaderActor = null;
        }
        if (this.stringLoaderActorHashMap != null && this.stringLoaderActorHashMap.size() > 0) {
            for (String str2 : this.stringLoaderActorHashMap.keySet()) {
                if (str2.equals(historicalBlockName)) {
                    ((LoaderActor) this.stringLoaderActorHashMap.remove(str2)).remove();
                    break;
                }
            }
        }
        this.candleDataArray = _candleDataArray;
        int i2 = (int) ((getCamera().position.x - (getCamera().viewportWidth / 2.0f)) / GdxSettings.chartBlockWidthDensityEffective);
        if (i2 <= ((CandleData) _candleDataArray.first()).getIdxByTime()) {
            i2 = ((CandleData) _candleDataArray.first()).getIdxByTime();
        }
        final int i3 = i2 + GdxSettings.chartBlockWidthReal;
        recalcOnDataArray(i2, i3);
        if (this.simpleCurveGraphicActor != null) {
            this.simpleCurveGraphicActor.fillFloatArray((Array) _candleDataArray);
        }

        this.minHistoricalX = (float) ((CandleData) this.candleDataArray.first()).getIdxByScaledDensity();

        this.firstX = (float) ((CandleData) this.candleDataArray.first()).getIdxByScaledDensity();

        this.runningXAlter = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
        this.runningX = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
        this.runningXNew = (float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity();
        this.velocityX = 0.0f;
        this.deltaX = 0.0f;
        this.runningYPrev = (float) ((CandleData) _candleDataArray.peek()).getYPriceCloseScaled();
        this.runningY = (float) ((CandleData) _candleDataArray.peek()).getYPriceCloseScaled();

        if (this.simpleHorizontalGridActor != null)
        {
            this.simpleHorizontalGridActor.updateGrid(this.priceLowMin1, this.priceHighMax1, this.ratioHeightPricesDelta1);
        }

        if (this.simpleCurrentCursDotActor != null) {
            this.simpleCurrentCursDotActor.setX((float) ((CandleData) _candleDataArray.peek()).getIdxByScaledDensity());
        }


    }

    @Override
    public void processDataItemMerged(CandleData candleData, float f) {
        int i;
        int i2 = 0;
        if (this.candleDataArray != null) {
            ((CandleData) this.candleDataArray.peek()).setYPriceCloseScaled(((double) (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)) + ((((CandleData) this.candleDataArray.peek()).getPriceClose() - ((double) this.priceLowMin1)) * ((double) this.ratioHeightPricesDelta1)));
            if (this.chartType == ChartType.Candle) {
                ((CandleData) this.candleDataArray.peek()).setYPriceOpenScaled(((double) (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)) + ((((CandleData) this.candleDataArray.peek()).getPriceOpen() - ((double) this.priceLowMin1)) * ((double) this.ratioHeightPricesDelta1)));
                ((CandleData) this.candleDataArray.peek()).setYPriceLowScaled(((double) (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)) + ((((CandleData) this.candleDataArray.peek()).getPriceLow() - ((double) this.priceLowMin1)) * ((double) this.ratioHeightPricesDelta1)));
                ((CandleData) this.candleDataArray.peek()).setYPriceHighScaled(((double) (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)) + ((((CandleData) this.candleDataArray.peek()).getPriceHigh() - ((double) this.priceLowMin1)) * ((double) this.ratioHeightPricesDelta1)));
            }
        }
        this.runningY = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((((float) candleData.getPriceClose()) - this.priceLowMin1) * this.ratioHeightPricesDelta1);
        this.runningYPrev = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f - this.priceLowMin1) * this.ratioHeightPricesDelta1);
        this.runningYDelta = this.runningY - this.runningYPrev;
        this.runningX = (float) candleData.getIdxByScaledDensity();
        if (this.simpleCurveGraphicActor != null) {
            this.simpleCurveGraphicActor.updateLastY(this.runningY, this.runningYPrev);
        }

        if (this.simpleCurrentCursLineActor != null) {
            this.simpleCurrentCursLineActor.updateRate(candleData.getPriceClose());
        }

        if (((double) ((getCamera().position.x + (getCamera().viewportWidth / 2.0f)) - GdxSettings.glyphWidthNormalized)) <= candleData.getIdxByScaledDensity()) {
            return;
        }
        if (candleData.getPriceLow() < ((double) this.priceLowMin1) || candleData.getPriceHigh() > ((double) this.priceHighMax1)) {
            i = (int) (getCamera().position.x / GdxSettings.chartBlockWidthDensityEffective);
            int i3 = i - (GdxSettings.chartBlockWidthReal / 2);
            if (i3 >= 0) {
                i2 = i3;
            }
            i += GdxSettings.chartBlockWidthReal / 2;
            if (i > ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) {
                i3 = ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime();
            } else {
                i3 = i;
            }
            recalcOnDataItem(i2, i3);
        }
    }

    @Override
    public void processDataItem(CandleData candleData, float f, float f2, float f3) {
        int i;
        int i2 = 0;
        this.lastX = f2;
        this.runningY = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((((float) candleData.getPriceClose()) - this.priceLowMin1) * this.ratioHeightPricesDelta1);
        this.runningYPrev = (GdxSettings.heightMainChartBorder + GdxSettings.heightExtraChartFull) + ((f3 - this.priceLowMin1) * this.ratioHeightPricesDelta1);
        this.runningYDelta = this.runningY - this.runningYPrev;
        this.runningXNew = (float) candleData.getIdxByScaledDensity();
        this.runningX = (float) ((CandleData) this.candleDataArray.get(this.candleDataArray.size - 2)).getIdxByScaledDensity();
        if (this.simpleCurveGraphicActor != null) {
            this.simpleCurveGraphicActor.updateLastCandleData(candleData, this.runningYPrev);
        }

        if (this.simpleCurrentCursLineActor != null) {
            this.simpleCurrentCursLineActor.updateRate(candleData.getPriceClose());
            if (this.chartType == ChartType.Candle) {
                this.simpleCurrentCursLineActor.updateSecondLine("00:00");
            }
        }

        if (((double) getCamera().position.x) < candleData.getIdxByScaledDensity()) {
            this.runningXAlter = (float) candleData.getIdxByScaledDensity();
        }
        if (((double) ((getCamera().position.x + (getCamera().viewportWidth / 2.0f)) - GdxSettings.glyphWidthNormalized)) <= candleData.getIdxByScaledDensity()) {
            return;
        }
        if (candleData.getPriceLow() < ((double) this.priceLowMin1) || candleData.getPriceHigh() > ((double) this.priceHighMax1)) {
            i = (int) (getCamera().position.x / GdxSettings.chartBlockWidthDensityEffective);
            int i3 = i - (GdxSettings.chartBlockWidthReal / 2);
            if (i3 >= 0) {
                i2 = i3;
            }
            i += GdxSettings.chartBlockWidthReal / 2;
            if (i > ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) {
                i3 = ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime();
            } else {
                i3 = i;
            }
            recalcOnDataItem(i2, i3);
        }
    }

    @Override
    public void initLoader(boolean z, String str) {
        if (this.loaderActor != null) {
            this.loaderActor.remove();
            this.loaderActor = null;
        }
        this.isZoomable = z;
        if (z) {
            LoaderActor actor = new LoaderActor(getCamera(), this.firstX, GdxSettings.heightExtraChartFull, GdxSettings.chartBlockWidthDensityEffective * 600.0f, GdxSettings.heightMainChartFull, false);
            this.stringLoaderActorHashMap.put(str, actor);
            addActor(actor);
            return;
        }
        //this.loaderActor = new LoaderActor(getCamera(), getCamera().position.x - (getCamera().viewportWidth / 2.0f), GdxSettings.heightExtraChartFull, getCamera().viewportWidth, GdxSettings.heightMainChartFull, true);
        this.loaderActor = new LoaderExtendedActor(getCamera(), getCamera().position.x - (getCamera().viewportWidth / 2.0f), GdxSettings.heightExtraChartFull, getCamera().viewportWidth, GdxSettings.heightMainChartFull, true);
        addActor(this.loaderActor);
    }


    // region GestureListener
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        this.isTouchedDown = false;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (this.candleDataArray != null && this.candleDataArray.size > 0 && this.isZoomable) {
            this.isFlinging = true;
            this.isPanning = false;
            this.velocityX = -velocityX;
            this.deltaX = (float) (Math.sqrt((double) Math.abs(this.velocityX)) / 4.0d);
            if (this.velocityX > 0.0f) {
                this.deltaX = -1.0f * this.deltaX;
            }
        }
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (this.candleDataArray != null && this.candleDataArray.size > 0 && this.isZoomable) {
            if (this.isTouchedDown) {
                this.isPanning = false;
                this.isFlinging = false;
                float f5 = (getCamera().position.x - (getCamera().viewportWidth / 2.0f)) + x;

            } else if (Math.abs(deltaX) > 3.0f) {
                this.isPanning = true;
                this.isFlinging = false;
                if (deltaX > 0.0f) {
                    if (getCamera().position.x - getCamera().viewportWidth <= this.firstX) {
                        //this.firstX -= GdxSettings.chartBlockWidthDensityEffective * 600.0f;
                        this.firstX -= GdxSettings.chartBlockWidthDensityEffective * 300.0f;
                        this.controller.loadHistoryBlock();

                        /**
                         Delete this block when history loading become OK

                        if (this.firstX <= this.minHistoricalX)
                        {
                            //do nothing
                        }
                        else
                        {
                            float newMinX = this.firstX - GdxSettings.chartBlockWidthDensityEffective * 600.0f;

                            if (this.minHistoricalX <= newMinX) {
                                this.firstX -= GdxSettings.chartBlockWidthDensityEffective * 600.0f;
                                //this.controller.loadHistoryBlock(); //extra loading actor init
                            }

                            if (this.minHistoricalX > newMinX) {
                                this.firstX = this.minHistoricalX;
                                //this.controller.loadHistoryBlock(); //extra loading actor init
                            }
                        }
                         **
                         */
                    }
                    this.velocityX = deltaX;
                    recalcOnDataItem(this.visibleRangeIdxFirst, this.visibleRangeIdxLast);
                }
                if (deltaX < 0.0f) {
                    if (getCamera().position.x - deltaX > ((float) ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) * GdxSettings.chartBlockWidthDensityEffective) {
                        getCamera().position.set(((float) ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) * GdxSettings.chartBlockWidthDensityEffective, getCamera().position.y, getCamera().position.z);
                        this.velocityX = ((getCamera().position.x - deltaX) - (((float) ((CandleData) this.controller.getCandleDataArray().peek()).getIdxByTime()) * GdxSettings.chartBlockWidthDensityEffective)) + deltaX;
                    } else {
                        this.velocityX = deltaX;
                    }
                    recalcOnDataItem(this.visibleRangeIdxFirst, this.visibleRangeIdxLast);
                }
            } else {
                this.isFlinging = false;
                this.isPanning = false;
                if (this.velocityX != 0.0f) {
                    this.isPanningAlter = true;
                }
                this.velocityX = 0.0f;
            }
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if (this.candleDataArray != null && this.candleDataArray.size > 0 && this.isZoomable) {

            this.isPanning = false;
            this.velocityX = 0.0f;
        }
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (this.candleDataArray != null && this.candleDataArray.size > 0 && this.isZoomable) {
            if (Math.abs(distance - this.lastZoomDistance) > 3.0f) {
                if (this.lastZoomDistance == 0.0f) {
                    this.lastZoomDistance = distance;
                } else {
                    if (this.lastZoomDistance < distance) {
                        if (this.zoom < this.zoomMax) {
                            if (this.zoom + 0.025f > this.zoomMax) {
                                this.zoom = this.zoomMax;
                            } else {
                                this.zoom += 0.025f;
                            }
                        }
                    } else if (this.zoom > 1.0f) {
                        if (this.zoom - 0.025f < 1.0f) {
                            this.zoom = 1.0f;
                        } else {
                            this.zoom -= 0.025f;
                        }
                    }
                    this.lastZoomDistance = distance;
                    Gdx.app.postRunnable(new zoomRunnable(this));
                }
            }
            this.isPanning = false;
            this.isFlinging = false;
        }
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
        this.lastZoomDistance = 0.0f;
    }

    class zoomRunnable implements Runnable
    {
        final /* synthetic */ SimpleChartStage simpleGraphicStage;

        zoomRunnable(SimpleChartStage ngStage) {
            this.simpleGraphicStage = ngStage;
        }

        public void run() {
            int i = 0;
            int abs = Math.abs(((CandleData) this.simpleGraphicStage.candleDataArray.first()).getIdxByTime())
                    + ((int) (this.simpleGraphicStage.getCamera().position.x / GdxSettings.chartBlockWidthDensityEffective));
            GdxSettings.init(this.simpleGraphicStage.chartType, this.simpleGraphicStage.zoom, this.simpleGraphicStage.controller.getExtraChartCount());
            this.simpleGraphicStage.refreshCandleData();
            if (abs >= 0)
            {
                int i2;
                this.simpleGraphicStage.getCamera().position.set((float) ((CandleData) this.simpleGraphicStage.candleDataArray.get(abs)).getIdxByScaledDensity(), this.simpleGraphicStage.getCamera().position.y, this.simpleGraphicStage.getCamera().position.z);
                this.simpleGraphicStage.getCamera().update();
                if (this.simpleGraphicStage.simpleCurrentCursDotActor != null) {
                    this.simpleGraphicStage.simpleCurrentCursDotActor.updateValue((CandleData) this.simpleGraphicStage.candleDataArray.peek());
                }
                if (this.simpleGraphicStage.simpleCurveGraphicActor != null) {
                    this.simpleGraphicStage.simpleCurveGraphicActor.updateData();
                }
            }
        }
    }

    // endregion
    
    // region Helper methods

    private void toFront(Actor actor) {
        Gdx.app.log(TAG, "toFront:: actor to front...");
        if (actor != null) {
            actor.toFront();
        }
    }

    /**
     * Recalculates stage params on new data item processing or on new frame's delta
     *
     * Params represents range [t1, t2] of candleDataArray elements that corresponds to visible data range on X axis
     * @param i - index t1
     * @param i2 - index t2
     */
    private void recalcOnDataItem(int i, int i2) {
        int abs = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + i;
        if (abs >= 0) {
            int i3;
            int abs2 = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + i2;
            if (abs2 >= this.candleDataArray.size - 1) {
                i3 = this.candleDataArray.size - 1;
            } else {
                i3 = abs2;
            }
            this.adjustCurveOnAct = true;
            this.priceLowMin2 = Float.MAX_VALUE;
            this.priceHighMax1 = Float.MIN_VALUE;
            while (abs <= i3) {
                if (((CandleData) this.candleDataArray.get(abs)).getPriceHigh() > ((double) this.priceHighMax1)) {
                    this.priceHighMax1 = (float) ((CandleData) this.candleDataArray.get(abs)).getPriceHigh();
                }
                if (((CandleData) this.candleDataArray.get(abs)).getPriceLow() < ((double) this.priceLowMin2)) {
                    this.priceLowMin2 = (float) ((CandleData) this.candleDataArray.get(abs)).getPriceLow();
                }
                abs++;
            }

            if ((this.priceHighMax1 - this.priceLowMin2) == 0.0f)
            {
                this.priceLowMin2 = this.priceLowMin2 - NumFormatUtil.getFractial(controller.getToolFormat().getMaxFractionDigits(), false);
            }

            this.ratioHeightPricesDelta2 = ((this.priceHighMax1 - this.priceLowMin2) == 0.0f)
                    ? GdxSettings.heightMainChartBody2
                    : GdxSettings.heightMainChartBody2 / (this.priceHighMax1 - this.priceLowMin2);
            this.deltaForRatios = this.ratioHeightPricesDelta2 - this.ratioHeightPricesDelta1;
            this.deltaForLowestPrices = this.priceLowMin2 - this.priceLowMin1;
            this.runningY = ((((float) ((CandleData) this.candleDataArray.peek()).getPriceClose()) - this.priceLowMin2) * this.ratioHeightPricesDelta2) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder);
            this.runningYDelta = this.runningY - this.runningYPrev;

        }
    }

    /**
     * Recalculates stage params on new data array processing
     * Params represents range [t1, t2] of candleDataArray elements that corresponds to visible data range on X axis
     * @param i - index t1
     * @param i2 - index t2
     */
    private void recalcOnDataArray(int i, int i2) {
        int abs = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + i;
        if (abs >= 0) {
            int i3;
            int abs2 = Math.abs(((CandleData) this.candleDataArray.first()).getIdxByTime()) + i2;
            if (abs2 >= this.candleDataArray.size) {
                i3 = this.candleDataArray.size - 1;
            } else {
                i3 = abs2;
            }
            this.priceLowMin1 = Float.MAX_VALUE;
            this.priceHighMax1 = Float.MIN_VALUE;
            while (abs <= i3) {
                if (((CandleData) this.candleDataArray.get(abs)).getPriceLow() < ((double) this.priceLowMin1)) {
                    this.priceLowMin1 = (float) ((CandleData) this.candleDataArray.get(abs)).getPriceLow();
                }
                if (((CandleData) this.candleDataArray.get(abs)).getPriceHigh() > ((double) this.priceHighMax1)) {
                    this.priceHighMax1 = (float) ((CandleData) this.candleDataArray.get(abs)).getPriceHigh();
                }
                abs++;
            }

            if ((this.priceHighMax1 - this.priceLowMin1) == 0.0f)
            {
                this.priceLowMin1 = this.priceLowMin1 - NumFormatUtil.getFractial(controller.getToolFormat().getMaxFractionDigits(), false);
            }

            this.ratioHeightPricesDelta1 = ((this.priceHighMax1 - this.priceLowMin1) == 0.0f)
                    ? GdxSettings.heightMainChartBody
                    : GdxSettings.heightMainChartBody / (this.priceHighMax1 - this.priceLowMin1);
            for (abs = 0; abs < this.candleDataArray.size; abs++) {
                ((CandleData) this.candleDataArray.get(abs)).setYPriceCloseScaled((double) (((((float) ((CandleData) this.candleDataArray.get(abs)).getPriceClose()) - this.priceLowMin1) * this.ratioHeightPricesDelta1) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)));
                ((CandleData) this.candleDataArray.get(abs)).setYPriceOpenScaled((double) (((((float) ((CandleData) this.candleDataArray.get(abs)).getPriceOpen()) - this.priceLowMin1) * this.ratioHeightPricesDelta1) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)));
                ((CandleData) this.candleDataArray.get(abs)).setYPriceLowScaled((double) (((((float) ((CandleData) this.candleDataArray.get(abs)).getPriceLow()) - this.priceLowMin1) * this.ratioHeightPricesDelta1) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)));
                ((CandleData) this.candleDataArray.get(abs)).setYPriceHighScaled((double) (((((float) ((CandleData) this.candleDataArray.get(abs)).getPriceHigh()) - this.priceLowMin1) * this.ratioHeightPricesDelta1) + (GdxSettings.heightExtraChartFull + GdxSettings.heightMainChartBorder)));
            }
        }
    }

    private void refreshCandleData() {
        int i = 0;
        for (int i2 = 0; i2 < this.candleDataArray.size; i2++) {
            ((CandleData) this.candleDataArray.get(i2)).setIdxByScaledDensity((double) (((float) ((CandleData) this.candleDataArray.get(i2)).getIdxByTime()) * GdxSettings.chartBlockWidthDensityEffective));
        }

        while (i < this.controller.getVerticalDataArray().size) {
            ((NewVerticalData) this.controller.getVerticalDataArray().get(i)).setIdxByScaledDensity(((float) ((NewVerticalData) this.controller.getVerticalDataArray().get(i)).getIdxByTime()) * GdxSettings.chartBlockWidthDensityEffective);
            i++;
        }

        this.firstX = (float) ((CandleData) this.candleDataArray.first()).getIdxByScaledDensity();
        this.lastX = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
        this.runningXNew = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
        this.runningX = (float) ((CandleData) this.candleDataArray.peek()).getIdxByScaledDensity();
    }
    
    // endregion


    // region Getters && Setters

    @Override
    public void setChartType(ChartType type)
    {
        this.chartType = type;
    }

    public float getZoom() {
        return zoom;
    }


    // endregion
}
