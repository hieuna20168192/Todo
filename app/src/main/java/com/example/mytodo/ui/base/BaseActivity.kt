package com.example.mytodo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        initComponent()
        initData(savedInstanceState)
    }

    protected abstract fun initComponent()

    protected abstract fun initData(savedInstanceState: Bundle?)

}

