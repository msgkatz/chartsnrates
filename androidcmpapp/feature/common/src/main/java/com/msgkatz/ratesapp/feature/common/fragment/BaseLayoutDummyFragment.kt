package com.msgkatz.ratesapp.feature.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.msgkatz.ratesapp.feature.common.utils.Logs


abstract class BaseLayoutDummyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logs.d("onCreateView %s", this.javaClass.simpleName)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        Logs.d("onDestroyView %s", this.javaClass.simpleName)
        super.onDestroyView()
    }
}