package com.itamadersomajinc.banglatype.commons.compose.extensions

import android.content.Context
import com.itamadersomajinc.banglatype.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)
