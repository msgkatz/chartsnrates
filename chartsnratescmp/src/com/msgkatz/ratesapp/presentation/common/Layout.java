package com.msgkatz.ratesapp.presentation.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.LayoutRes;

/**
 * Annotaion for fragment inflation
 *
 * Created by gkostyaev on 18/12/2017.
 */

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Layout {
    @LayoutRes int id();
}