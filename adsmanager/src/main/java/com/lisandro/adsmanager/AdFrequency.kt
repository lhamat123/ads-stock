package com.lisandro.adsmanager

object AdFrequency {
    private var lastShownAt = 0L
    private const val MIN_INTERVAL_MS = 5 * 60 * 1000L  // 5 minutos

    fun shouldShow(): Boolean {
        val now = System.currentTimeMillis()
        return if (now - lastShownAt > MIN_INTERVAL_MS) {
            lastShownAt = now
            true
        } else false
    }

    fun reset() { lastShownAt = 0L }
}
