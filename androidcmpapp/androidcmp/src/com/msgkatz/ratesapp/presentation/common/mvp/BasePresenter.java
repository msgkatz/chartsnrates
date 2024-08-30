package com.msgkatz.ratesapp.presentation.common.mvp;

import com.msgkatz.ratesapp.presentation.common.mvp.router.EmptyRouter;

/**
 * Base Presenter entity
 *
 * Created by gkostyaev on 28/12/2017.
 */
public abstract class BasePresenter<View extends BaseView, Router extends EmptyRouter>
{
    private View view;

    private Router router;

    /** Action on View start */
    public abstract void onStart();

    public abstract void onResume();

    public abstract void onPause();

    /** Action on View stop */
    public abstract void onStop();

    /**
     * Gets related View entity
     * @return View
     */
    public View getView() {
        return view;
    }

    /**
     * Sets related View entity
     * @param view view
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Gets related Router entity
     * @return Router
     */
    public Router getRouter() {
        return router;
    }

    /**
     * Sets related Router entity
     * @param router router
     */
    public void setRouter(Router router) {
        this.router = router;
    }

}
