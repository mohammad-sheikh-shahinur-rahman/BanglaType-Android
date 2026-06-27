package com.itamadersomajinc.banglatype.activities

import android.os.Bundle
import com.itamadersomajinc.banglatype.commons.extensions.updateTextColors
import com.itamadersomajinc.banglatype.commons.extensions.viewBinding
import com.itamadersomajinc.banglatype.commons.helpers.NavigationIcon
import com.itamadersomajinc.banglatype.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : SimpleActivity() {
    private val binding by viewBinding(ActivityPrivacyPolicyBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        setupEdgeToEdge(padBottomSystem = listOf(binding.privacyNestedScrollview))
        setupMaterialScrollListener(binding.privacyNestedScrollview, binding.privacyAppbar)
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.privacyAppbar, NavigationIcon.Arrow)
        updateTextColors(binding.privacyNestedScrollview)
    }
}
