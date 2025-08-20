package com.eva.inc.mafia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentRoleAssignmentBinding
import com.eva.inc.mafia.ui.entity.Move
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.getParcelableCompat

class RoleAssignmentFragment : BaseFragment<FragmentRoleAssignmentBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRoleAssignmentBinding =
        FragmentRoleAssignmentBinding::inflate

    private val roleAssignment: Move.RoleAssignment by lazy {
        arguments.getParcelableCompat(ARG_ROLE_ASSIGNMENT)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPlayer.text = "${roleAssignment.player.name} получает роль"
        binding.tvRole.setText(roleAssignment.player.role.title)
        binding.imgIcon.setImageResource(roleAssignment.player.role.drawable)
    }

    companion object {
        private const val ARG_ROLE_ASSIGNMENT = "arg_role_assignment"

        fun newInstance(roleAssignment: Move.RoleAssignment): RoleAssignmentFragment =
            RoleAssignmentFragment().apply {
                arguments = Bundle().apply { putParcelable(ARG_ROLE_ASSIGNMENT, roleAssignment) }
            }
    }
}
