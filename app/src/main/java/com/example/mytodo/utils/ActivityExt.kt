package com.example.mytodo.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun FragmentActivity.replaceFragment(
    id: Int, fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = null
) {
    supportFragmentManager.beginTransaction().replace(id, fragment).apply {
        if (addToBackStack) addToBackStack(tag)
    }.commit()
}

fun FragmentActivity.addFragment(
    id: Int, fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = null
) {
    supportFragmentManager.beginTransaction().add(id, fragment).apply {
        if (addToBackStack) addToBackStack(tag)
    }.commit()
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}


