package com.dflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BindingActivity<T : ViewBinding> : AppCompatActivity() {

    abstract fun inflate(inflater: LayoutInflater): T

    private lateinit var _binding: T

    val binding: T
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this._binding = inflate(layoutInflater)
        this.setContentView(this._binding.root)
    }
}