package com.lisandro.adsmanager

import android.content.Context

object AdsManager {

    private const val DEFAULT_URL =
        "https://tuusuario.github.io/ads-stock/ads.json"

    private lateinit var repository: AdsRepository
    private var initialized = false

    fun init(
        context: Context,
        adsUrl: String = DEFAULT_URL,
        cacheTtlMs: Long = 6 * 60 * 60 * 1000L
    ) {
        repository  = AdsRepository(context.applicationContext, adsUrl, cacheTtlMs)
        initialized = true
    }

    suspend fun getBanner(): Ad?      = getAd(AdType.BANNER)
    suspend fun getInterstitial(): Ad? = getAd(AdType.INTERSTITIAL)

    suspend fun refresh(): List<Ad> {
        checkInit()
        return repository.getAds()
    }

    private suspend fun getAd(type: AdType): Ad? {
        checkInit()
        return AdSelector.pick(repository.getAds(), type)
    }

    private fun checkInit() {
        check(initialized) {
            "AdsManager no fue inicializado. Llama AdsManager.init(context) primero."
        }
    }
}
