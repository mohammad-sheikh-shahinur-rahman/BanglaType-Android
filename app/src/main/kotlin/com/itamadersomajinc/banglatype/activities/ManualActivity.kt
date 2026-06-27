package com.itamadersomajinc.banglatype.activities

import android.os.Bundle
import com.itamadersomajinc.banglatype.commons.helpers.NavigationIcon
import com.itamadersomajinc.banglatype.databinding.ActivityManualBinding

class ManualActivity : SimpleActivity() {
    private lateinit var binding: ActivityManualBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(padBottomSystem = listOf(binding.manualNestedScrollview))
    }

    override fun onResume() {
        super.onResume()
        setupTopAppBar(binding.manualAppbar, NavigationIcon.Arrow)
    }
}
