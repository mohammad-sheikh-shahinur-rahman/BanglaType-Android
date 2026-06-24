package com.itamadersomajinc.banglatype.commons.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList
import com.itamadersomajinc.banglatype.commons.compose.extensions.enableEdgeToEdgeSimple
import com.itamadersomajinc.banglatype.commons.compose.screens.FAQScreen
import com.itamadersomajinc.banglatype.commons.compose.theme.AppThemeSurface
import com.itamadersomajinc.banglatype.commons.helpers.APP_FAQ
import com.itamadersomajinc.banglatype.commons.models.FAQItem

class FAQActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                val faqItems = remember { intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem> }
                FAQScreen(
                    goBack = ::finish,
                    faqItems = faqItems.toImmutableList()
                )
            }
        }
    }
}
