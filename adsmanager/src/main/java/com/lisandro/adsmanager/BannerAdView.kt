package com.lisandro.adsmanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.LifecycleCoroutineScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

/**
 * View listo para usar en XML.
 *
 * XML:
 *   <com.lisandro.adsmanager.BannerAdView
 *       android:id="@+id/bannerAd"
 *       android:layout_width="match_parent"
 *       android:layout_height="50dp" />
 *
 * Kotlin:
 *   bannerAd.load(lifecycleScope)
 */
class BannerAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val imageView: ImageView

    init {
        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            scaleType      = ImageView.ScaleType.FIT_XY
            adjustViewBounds = true
        }
        addView(imageView)
        visibility = GONE
    }

    fun load(scope: LifecycleCoroutineScope) {
        scope.launch {
            val ad = AdsManager.getBanner()
            ad?.let { bind(it) }
        }
    }

    private fun bind(ad: Ad) {
        Glide.with(this)
            .load(ad.imageUrl)
            .into(imageView)

        setOnClickListener {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(ad.actionUrl))
            )
        }
        visibility = VISIBLE
    }
}
