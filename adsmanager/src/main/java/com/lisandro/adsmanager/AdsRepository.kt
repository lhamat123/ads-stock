package com.lisandro.adsmanager

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

internal class AdsRepository(
    context: Context,
    private val adsJsonUrl: String,
    private val cacheTtlMs: Long = 6 * 60 * 60 * 1000L
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ads_stock_cache", Context.MODE_PRIVATE)

    private val KEY_JSON      = "cached_json"
    private val KEY_TIMESTAMP = "cached_at"

    suspend fun getAds(): List<Ad> = withContext(Dispatchers.IO) {
        val rawJson = if (isCacheValid()) {
            prefs.getString(KEY_JSON, null)
        } else {
            fetchRemote() ?: prefs.getString(KEY_JSON, null)
        }
        rawJson?.let { parseAds(it) } ?: emptyList()
    }

    private fun isCacheValid(): Boolean {
        val cachedAt = prefs.getLong(KEY_TIMESTAMP, 0L)
        return (System.currentTimeMillis() - cachedAt) < cacheTtlMs
            && prefs.contains(KEY_JSON)
    }

    private fun fetchRemote(): String? {
        return try {
            val conn = (URL(adsJsonUrl).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5_000
                readTimeout    = 5_000
                requestMethod  = "GET"
            }
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val body = conn.inputStream.bufferedReader().readText()
                prefs.edit()
                    .putString(KEY_JSON, body)
                    .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
                    .apply()
                body
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun parseAds(json: String): List<Ad> {
        return try {
            val root    = JSONObject(json)
            val baseUrl = root.optString("base_url", "")
            val apps    = root.getJSONArray("apps")
            val result  = mutableListOf<Ad>()

            for (i in 0 until apps.length()) {
                val app = apps.getJSONObject(i)
                if (!app.optBoolean("active", true)) continue

                val actionUrl = app.optString("download_url")
                    .ifBlank { app.optString("website_url") }
                val appName   = app.optString("name")
                val adsArray  = app.optJSONArray("ads") ?: continue

                for (j in 0 until adsArray.length()) {
                    val adObj = adsArray.getJSONObject(j)
                    if (!adObj.optBoolean("active", true)) continue
                    result.add(
                        Ad(
                            id        = adObj.getString("id"),
                            app       = appName,
                            type      = AdType.from(adObj.optString("type", "banner")),
                            imageUrl  = "$baseUrl/${adObj.getString("image")}",
                            actionUrl = actionUrl,
                            weight    = adObj.optInt("weight", 1),
                            active    = true
                        )
                    )
                }
            }
            result
        } catch (e: Exception) {
            emptyList()
        }
    }
}
