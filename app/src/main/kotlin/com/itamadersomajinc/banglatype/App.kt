package com.itamadersomajinc.banglatype

import android.app.Application
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.itamadersomajinc.banglatype.helpers.Config
import com.itamadersomajinc.banglatype.commons.helpers.SIDELOADING_FALSE

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = Config.newInstance(this)
        config.appSideloadingStatus = SIDELOADING_FALSE
        config.hadThankYouInstalled = true
        config.appRunCount = 100
        setupEmojiCompat()
    }

    private fun setupEmojiCompat() {
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}
