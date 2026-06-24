package com.itamadersomajinc.banglatype.commons.activities

import android.content.Context
import androidx.activity.ComponentActivity
import com.itamadersomajinc.banglatype.commons.extensions.baseConfig
import com.itamadersomajinc.banglatype.commons.helpers.MyContextWrapper
import com.itamadersomajinc.banglatype.commons.helpers.REQUEST_APP_UNLOCK
import com.itamadersomajinc.banglatype.commons.helpers.isTiramisuPlus

abstract class BaseComposeActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        maybeLaunchAppUnlockActivity(REQUEST_APP_UNLOCK)
    }

    override fun attachBaseContext(newBase: Context) {
        if (newBase.baseConfig.useEnglish && !isTiramisuPlus()) {
            super.attachBaseContext(MyContextWrapper(newBase).wrap(newBase, "en"))
        } else {
            super.attachBaseContext(newBase)
        }
    }
}
