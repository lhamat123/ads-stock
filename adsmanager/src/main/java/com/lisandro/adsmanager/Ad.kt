package com.lisandro.adsmanager

import org.json.JSONObject

data class Ad(
    val id: String,
    val app: String,
    val type: AdType,
    val imageUrl: String,
    val actionUrl: String,
    val weight: Int = 1,
    val active: Boolean = true,
    val tags: List<String> = emptyList()
) {
    companion object {
        fun fromJson(json: JSONObject): Ad? {
            return try {
                Ad(
                    id        = json.getString("id"),
                    app       = json.getString("app"),
                    type      = AdType.from(json.optString("type", "banner")),
                    imageUrl  = json.getString("image_url"),
                    actionUrl = json.getString("action_url"),
                    weight    = json.optInt("weight", 1),
                    active    = json.optBoolean("active", true),
                    tags      = json.optJSONArray("tags")?.let { arr ->
                        List(arr.length()) { arr.getString(it) }
                    } ?: emptyList()
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}

enum class AdType {
    BANNER, INTERSTITIAL, UNKNOWN;

    companion object {
        fun from(value: String) = when (value.lowercase()) {
            "banner"       -> BANNER
            "interstitial" -> INTERSTITIAL
            else           -> UNKNOWN
        }
    }
}
