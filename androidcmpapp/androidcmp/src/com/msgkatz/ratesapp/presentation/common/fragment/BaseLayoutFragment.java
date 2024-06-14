package com.msgkatz.ratesapp.presentation.common.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.utils.Logs;

import java.lang.annotation.Annotation;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@Deprecated
public abstract class BaseLayoutFragment extends Fragment {

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logs.d("onCreateView %s", this.getClass().getSimpleName());
        Class cls = getClass();
        if (!cls.isAnnotationPresent(Layout.class)) {
            return null;
        }
        Annotation annotation = cls.getAnnotation(Layout.class);
        Layout layout = (Layout) annotation;

        try
        {
            View view = inflater.inflate(layout.id(), null);
            unbinder = ButterKnife.bind(this, view);
            return view;
        }
        catch (Exception ex)
        {
            Logs.e("INFLATE WTF", ex.getMessage() + "\n" + ex.getCause());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        Logs.d("onDestroyView %s", this.getClass().getSimpleName());
        if (unbinder != null)
            unbinder.unbind();

        super.onDestroyView();
    }
}
