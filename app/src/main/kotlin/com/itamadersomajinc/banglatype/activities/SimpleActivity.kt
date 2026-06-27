package com.itamadersomajinc.banglatype.activities

import com.itamadersomajinc.banglatype.commons.activities.BaseSimpleActivity
import com.itamadersomajinc.banglatype.commons.helpers.SIDELOADING_FALSE
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.extensions.config

open class SimpleActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        config.appSideloadingStatus = SIDELOADING_FALSE
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        config.appSideloadingStatus = SIDELOADING_FALSE
        super.onResume()
    }

    override fun getAppIconIDs() = arrayListOf(
        R.mipmap.ic_launcher_red,
        R.mipmap.ic_launcher_pink,
        R.mipmap.ic_launcher_purple,
        R.mipmap.ic_launcher_deep_purple,
        R.mipmap.ic_launcher_indigo,
        R.mipmap.ic_launcher_blue,
        R.mipmap.ic_launcher_light_blue,
        R.mipmap.ic_launcher_cyan,
        R.mipmap.ic_launcher_teal,
        R.drawable.logo_banglatype,
        R.mipmap.ic_launcher_light_green,
        R.mipmap.ic_launcher_lime,
        R.mipmap.ic_launcher_yellow,
        R.mipmap.ic_launcher_amber,
        R.mipmap.ic_launcher_orange,
        R.mipmap.ic_launcher_deep_orange,
        R.mipmap.ic_launcher_brown,
        R.mipmap.ic_launcher_blue_grey,
        R.mipmap.ic_launcher_grey_black
    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)

    override fun getRepositoryName() = ""
}
