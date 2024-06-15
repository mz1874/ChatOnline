package cn.org.bugcreator.util

import android.content.Context
import android.util.Log
import cn.org.bugcreator.App
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

object OkHttpTool {


    private val client = OkHttpClient()

    @Throws(IOException::class)
    fun get(url: String): String {
        val session = getSession()
        Log.i("SESSION", session.toString())

        val cookieHeader = "JSESSIONID=$session"

        val requestBuilder = Request.Builder()
            .url(url)
            .header("Cookie", cookieHeader)

        val request = requestBuilder.build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string() ?: ""
        }
    }


    fun saveSession(sessionId: String) {
        val sharedPreferences = App.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("sessionId", sessionId).apply()
    }

    fun getSession(): String? {
        val sharedPreferences = App.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("sessionId", null)
    }
    @Throws(IOException::class)
    fun post(url: String, jsonBody: String, token: String?): String {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = RequestBody.create(mediaType, jsonBody)
        val requestBuilder = Request.Builder()
            .url(url)
            .post(requestBody)

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        // Add Cookie header only if session is available
        getSession()?.let { session ->
            val cookieHeader = "JSESSIONID=$session"
            requestBuilder.addHeader("Cookie", cookieHeader)
        }

        val request = requestBuilder.build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.string() ?: ""
        }
    }




}
