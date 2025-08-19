package com.eva.inc.mafia.ui.fragment.roleassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentRoleAssignmentBinding
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.getParcelableCompat

class RoleAssignmentFragment : BaseFragment<FragmentRoleAssignmentBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRoleAssignmentBinding =
        FragmentRoleAssignmentBinding::inflate

    private val roleAssignment: Moves.RoleAssignment by lazy {
        arguments.getParcelableCompat(ARG_ROLE_ASSIGNMENT)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text =
            "${roleAssignment.player.name} получает роль: ${getString(roleAssignment.player.role.title)}"
        binding.imageView.setImageResource(roleAssignment.player.role.drawable)
    }

    companion object {
        private const val ARG_ROLE_ASSIGNMENT = "arg_role_assignment"

        fun newInstance(roleAssignment: Moves.RoleAssignment): RoleAssignmentFragment =
            RoleAssignmentFragment().apply {
                arguments = Bundle().apply { putParcelable(ARG_ROLE_ASSIGNMENT, roleAssignment) }
            }
    }
}
