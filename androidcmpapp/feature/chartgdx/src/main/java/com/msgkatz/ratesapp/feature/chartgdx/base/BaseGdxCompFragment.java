package com.msgkatz.ratesapp.feature.chartgdx.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;


import com.msgkatz.ratesapp.feature.chartgdx.base.gdx.BaseGdxAppFragment;


import javax.inject.Inject;


public abstract class BaseGdxCompFragment extends BaseGdxAppFragment /** implements BaseView , HasSupportFragmentInjector**/ {

    @Inject
    protected Context activityContext;

    // Note that this should not be used within a child fragment.
//    @Inject
//    @Named(BaseFragmentModule.CHILD_FRAGMENT_MANAGER)
//    protected FragmentManager childFragmentManager;

//    @Inject
//    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here before M, L (API 22) and below because onAttach(Context)
            // is not yet available at L.
            //AndroidSupportInjection.inject(this);
        }
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
            //AndroidSupportInjection.inject(this);
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (getPresenter() != null)
//            getPresenter().setView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Logs.d("onActivityCreated %s", this.getClass().getSimpleName());
        Log.d("onActivityCreated %s", this.getClass().getSimpleName());
        super.onActivityCreated(savedInstanceState);
        //ChartRouter chartRouter = (ChartRouter) getActivity();
        //noinspection unchecked
//        if (getPresenter() != null)
//            getPresenter().setRouter(chartRouter);
    }

    @Override
    public void onStart() {
        //Logs.d("onStart %s", this.getClass().getSimpleName());
        Log.d("onStart %s", this.getClass().getSimpleName());
//        if (getPresenter() != null) {
//            getPresenter().setView(this);
//            getPresenter().onStart();
//        }

        super.onStart();
    }

    @Override
    public void onStop() {
        //Logs.d("onStop %s", this.getClass().getSimpleName());
        Log.d("onStop %s", this.getClass().getSimpleName());
//        if (getPresenter() != null)
//            getPresenter().onStop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        //Logs.d("onDestroyView %s", this.getClass().getSimpleName());
        Log.d("onDestroyView %s", this.getClass().getSimpleName());

//        if (getPresenter() != null) {
//            getPresenter().setView(null);
//            getPresenter().setRouter(null);
//        }
        super.onDestroyView();
    }


//    @Override
//    public AndroidInjector<Fragment> supportFragmentInjector() {
//        return childFragmentInjector;
//    }

    //public abstract BasePresenter getPresenter();
}
