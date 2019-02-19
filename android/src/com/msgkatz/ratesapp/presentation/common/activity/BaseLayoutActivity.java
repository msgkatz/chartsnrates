package com.msgkatz.ratesapp.presentation.common.activity;

import android.os.Bundle;
import com.msgkatz.ratesapp.presentation.common.Layout;
import com.msgkatz.ratesapp.utils.Logs;

import java.lang.annotation.Annotation;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by msgkatz on 15/08/2018.
 */

public abstract class BaseLayoutActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logs.d("onCreate %s", this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        Class cls = getClass();
        if (!cls.isAnnotationPresent(Layout.class)) {
            return;
        }
        Annotation annotation = cls.getAnnotation(Layout.class);
        Layout layout = (Layout) annotation;
        setContentView(layout.id());
        unbinder = ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        Logs.d("onDestroy %s", this.getClass().getSimpleName());
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
