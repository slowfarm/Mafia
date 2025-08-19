package com.eva.inc.mafia.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentMafiaMeetUpBinding
import com.eva.inc.mafia.ui.fragment.base.BaseFragment

class MafiaMeetUpFragment : BaseFragment<FragmentMafiaMeetUpBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMafiaMeetUpBinding =
        FragmentMafiaMeetUpBinding::inflate

    override fun onDestroyView() {
        binding.countdownTimer.cancel()
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): MafiaMeetUpFragment = MafiaMeetUpFragment()
    }
}
