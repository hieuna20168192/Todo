package com.example.mytodo.data.source.remote.dao

import com.example.mytodo.data.model.Task
import org.json.JSONArray
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class RequestHandler : RequestDao {

    private val GET: String = "GET"
    private val POST: String = "POST"
    private val BASE_URL: String = "https://jsonplaceholder.typicode.com/todos/"

    @Throws(IOException::class)
    override fun getTasks(): List<Task> {
        val obj = URL(BASE_URL)
        val con = obj.openConnection() as HttpURLConnection
        val text: String

        con.requestMethod = GET
        val responseCode = con.responseCode
        println("Response Code :: $responseCode")
        return if (responseCode == HttpURLConnection.HTTP_OK) {
            text = con.inputStream.use {
                it.reader().use { reader -> reader.readText() }
            }
            handleJson(text)
        } else {
            listOf()
        }
    }

    override fun getTaskById(taskId: String): Task? {
        val newUrl = BASE_URL + "?${Task.ID}=$taskId"
        val obj = URL(newUrl)
        val con = obj.openConnection() as HttpURLConnection
        val text: String
        con.requestMethod = GET
        val responseCode = con.responseCode
        println("Response Code :: $responseCode")
        return if (responseCode == HttpURLConnection.HTTP_OK) {
            text = con.inputStream.use {
                it.reader().use { reader -> reader.readText() }
            }
            handleJson(text)[0]
        } else {
            null
        }
    }

    private fun handleJson(jsonString: String?): List<Task> {

        val jsonArray = JSONArray(jsonString)

        val list = ArrayList<Task>()
        var x = 0
        while (x < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(x)

            list.add(
                Task(
                    id = jsonObject.getString(Task.ID),
                    title = jsonObject.getString(Task.TITLE),
                    isCompleted = if (jsonObject.getBoolean(Task.IS_COMPLETED)) 1 else 0
                )
            )
            x++
        }
        return list
    }


    companion object {
        private var instance: RequestHandler? = null

        fun getInstance() =
            instance ?: RequestHandler().also {
                instance = it
            }
    }
}

