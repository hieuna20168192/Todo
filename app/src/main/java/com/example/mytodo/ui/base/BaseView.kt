package com.example.mytodo.ui.base

interface BaseView<T> {

    var presenter: T

    fun showLoading()

    fun hideLoading()

    fun toast(obj: Any)

}
