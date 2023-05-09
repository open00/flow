package com.dflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint

abstract class BindingFragment<T : ViewBinding> : Fragment() {

    abstract fun inflate(inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): T

    private var _binding: T? = null

    val binding: T
        get() = _binding as T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?  {
        _binding = inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}