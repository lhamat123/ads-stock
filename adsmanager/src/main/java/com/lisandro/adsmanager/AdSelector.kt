package com.lisandro.adsmanager

import kotlin.random.Random

internal object AdSelector {

    fun pick(ads: List<Ad>, type: AdType): Ad? {
        val pool = ads.filter { it.type == type }
        if (pool.isEmpty()) return null
        if (pool.size == 1) return pool.first()

        val totalWeight = pool.sumOf { it.weight.coerceAtLeast(1) }
        var random = Random.nextInt(totalWeight)

        for (ad in pool) {
            random -= ad.weight.coerceAtLeast(1)
            if (random < 0) return ad
        }
        return pool.last()
    }
}
