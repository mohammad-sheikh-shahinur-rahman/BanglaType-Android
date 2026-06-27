package com.itamadersomajinc.banglatype.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.itamadersomajinc.banglatype.commons.helpers.FontHelper
import com.itamadersomajinc.banglatype.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Fullscreen mode
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashText.typeface = FontHelper.getTypeface(this)

        animateSplash()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3500)
    }

    private fun animateSplash() {
        val logo = binding.splashLogo
        val title = binding.splashTitle
        val text = binding.splashText
        val glow = binding.splashGlow
        val loader = binding.splashLoader

        // Initial states
        logo.alpha = 0f
        logo.scaleX = 0f
        logo.scaleY = 0f

        title.alpha = 0f
        title.translationY = 60f

        text.alpha = 0f
        text.translationY = 80f

        glow.alpha = 0f
        glow.scaleX = 0.5f
        glow.scaleY = 0.5f

        // Logo Animation
        val logoAlpha = ObjectAnimator.ofFloat(logo, View.ALPHA, 0f, 1f).apply { duration = 800 }
        val logoScaleX = ObjectAnimator.ofFloat(logo, View.SCALE_X, 0f, 1.1f, 1f).apply {
            duration = 1200
            interpolator = OvershootInterpolator(1.5f)
        }
        val logoScaleY = ObjectAnimator.ofFloat(logo, View.SCALE_Y, 0f, 1.1f, 1f).apply {
            duration = 1200
            interpolator = OvershootInterpolator(1.5f)
        }

        // Glow Animation
        val glowAlpha = ObjectAnimator.ofFloat(glow, View.ALPHA, 0f, 0.15f).apply {
            duration = 1500
            startDelay = 400
        }
        val glowScaleX = ObjectAnimator.ofFloat(glow, View.SCALE_X, 0.5f, 1.5f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val glowScaleY = ObjectAnimator.ofFloat(glow, View.SCALE_Y, 0.5f, 1.5f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        // Title Animation
        val titleAlpha = ObjectAnimator.ofFloat(title, View.ALPHA, 0f, 1f).apply {
            duration = 900
            startDelay = 800
        }
        val titleTranslationY = ObjectAnimator.ofFloat(title, View.TRANSLATION_Y, 60f, 0f).apply {
            duration = 1000
            startDelay = 800
            interpolator = OvershootInterpolator(1f)
        }

        // Text Animation
        val textAlpha = ObjectAnimator.ofFloat(text, View.ALPHA, 0f, 1f).apply {
            duration = 1000
            startDelay = 1000
        }
        val textTranslationY = ObjectAnimator.ofFloat(text, View.TRANSLATION_Y, 80f, 0f).apply {
            duration = 1200
            startDelay = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        // Loader Animation
        val loaderAlpha = ObjectAnimator.ofFloat(loader, View.ALPHA, 0f, 1f).apply {
            duration = 500
            startDelay = 1800
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            logoAlpha, logoScaleX, logoScaleY,
            glowAlpha, glowScaleX, glowScaleY,
            titleAlpha, titleTranslationY,
            textAlpha, textTranslationY,
            loaderAlpha
        )
        animatorSet.start()
    }
}
