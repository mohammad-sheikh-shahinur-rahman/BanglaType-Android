package com.itamadersomajinc.banglatype.commons.interfaces

import com.itamadersomajinc.banglatype.commons.activities.BaseSimpleActivity

interface RenameTab {
    fun initTab(activity: BaseSimpleActivity, paths: ArrayList<String>)

    fun dialogConfirmed(useMediaFileExtension: Boolean, callback: (success: Boolean) -> Unit)
}
