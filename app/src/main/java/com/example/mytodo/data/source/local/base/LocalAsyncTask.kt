package com.example.mytodo.data.source.local.base

import android.os.AsyncTask

class LocalAsyncTask<P, T>(
    private val callback: OnDataLoadedCallback<T>,
    private val handle: (P) -> T
) : AsyncTask<P, Void, T?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: P): T? =
        try {
            handle(params.first()) ?: throw Exception()
        } catch (e: Exception) {
            exception = e
            null
        }

    override fun onPostExecute(result: T?) {
        result?.let(callback::onSuccess) ?: (callback::onFailure)
    }
}

object EmptyInput