package com.dflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import com.dflow.databinding.ActivityMainBinding
import com.dflow.utils.Navigator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>() {


    override fun inflate(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Navigator.showFragment(supportFragmentManager, MainFragment.newInstance(), MainFragment.TAG, true)
    }

    override fun onBackPressed() {
        if(Navigator.getCurrentFragment(supportFragmentManager) !is MainFragment){
            super.onBackPressed()
        }else
            finish()
    }
}

