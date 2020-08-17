package com.example.mytodo.data.source.local.base

interface OnDataLoadedCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(exception: Exception = Exception())
}