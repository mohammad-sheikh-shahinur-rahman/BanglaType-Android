package com.itamadersomajinc.banglatype.commons

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.itamadersomajinc.banglatype.commons.extensions.appLockManager
import com.itamadersomajinc.banglatype.commons.extensions.checkUseEnglish

open class FossifyApp : Application() {

    open val isAppLockFeatureAvailable = false

    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
        setupAppLockManager()
    }

    private fun setupAppLockManager() {
        if (isAppLockFeatureAvailable) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(appLockManager)
        }
    }
}
