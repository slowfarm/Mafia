package com.eva.inc.mafia.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import com.eva.inc.mafia.databinding.ActivityMainBinding
import com.eva.inc.mafia.ui.App
import com.eva.inc.mafia.ui.activity.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    private val domainRepository = App.get().domainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnNewGame.setOnClickListener { GameActivity.Companion.start(this) }

        if (domainRepository.loadSnapshotFromPrefs() != null) MovesActivity.start(this)
    }
}
