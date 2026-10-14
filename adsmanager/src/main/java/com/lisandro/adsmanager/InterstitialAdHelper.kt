package com.lisandro.adsmanager

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleCoroutineScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

/**
 * Muestra un interstitial como Dialog de pantalla completa.
 *
 * USO:
 *   override fun onBackPressed() {
 *       if (AdFrequency.shouldShow()) {
 *           InterstitialAdHelper.show(this, lifecycleScope)
 *       }
 *       super.onBackPressed()
 *   }
 */
object InterstitialAdHelper {

    fun show(context: Context, scope: LifecycleCoroutineScope) {
        scope.launch {
            val ad = AdsManager.getInterstitial() ?: return@launch
            showDialog(context, ad)
        }
    }

    private fun showDialog(context: Context, ad: Ad) {
        val dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        // Botón cerrar alineado a la derecha
        val closeBtn = ImageButton(context).apply {
            setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            setBackgroundColor(Color.TRANSPARENT)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.gravity = Gravity.END }
            setOnClickListener { dialog.dismiss() }
        }

        // Imagen del anuncio
        val imageView = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(320, 480)
            scaleType      = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
            setOnClickListener {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(ad.actionUrl))
                )
                dialog.dismiss()
            }
        }

        Glide.with(context).load(ad.imageUrl).into(imageView)

        container.addView(closeBtn)
        container.addView(imageView)
        dialog.setContentView(container)
        dialog.show()
    }
}
