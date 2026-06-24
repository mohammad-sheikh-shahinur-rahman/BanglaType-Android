package com.itamadersomajinc.banglatype.dialogs

import android.content.Intent
import android.net.Uri
import com.itamadersomajinc.banglatype.commons.activities.BaseSimpleActivity
import com.itamadersomajinc.banglatype.commons.extensions.getAlertDialogBuilder
import com.itamadersomajinc.banglatype.commons.extensions.launchViewIntent
import com.itamadersomajinc.banglatype.commons.extensions.setupDialogStuff
import com.itamadersomajinc.banglatype.R
import com.itamadersomajinc.banglatype.databinding.DialogAboutBinding

class AboutDialog(val activity: BaseSimpleActivity, val versionName: String) {
    init {
        val binding = DialogAboutBinding.inflate(activity.layoutInflater).apply {
            aboutVersion.text = activity.getString(R.string.about_version, versionName)

            aboutWebsite.setOnClickListener {
                activity.launchViewIntent(R.string.about_website_url)
            }

            aboutEmail.setOnClickListener {
                val address = activity.getString(R.string.about_email)
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$address")
                    activity.startActivity(this)
                }
            }

            aboutFacebook.setOnClickListener {
                activity.launchViewIntent(R.string.about_facebook_url)
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.about)
            }
    }
}
