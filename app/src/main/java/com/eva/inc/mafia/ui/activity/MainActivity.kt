package com.eva.inc.mafia.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import com.eva.inc.mafia.databinding.ActivityMainBinding
import com.eva.inc.mafia.ui.activity.game.GameActivity
import com.eva.inc.mafia.ui.activity.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnNewGame.setOnClickListener { GameActivity.Companion.start(this) }
    }
}