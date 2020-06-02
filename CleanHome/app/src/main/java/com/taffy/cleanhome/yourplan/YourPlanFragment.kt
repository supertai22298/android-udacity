package com.taffy.cleanhome.yourplan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController


class YourPlanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = com.taffy.cleanhome.databinding.FragmentYourPlanBinding.inflate(inflater)
        binding.companyButton.setOnClickListener {
            this.findNavController().navigate(YourPlanFragmentDirections.actionYourPlanFragmentToBottomNavAndFabFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}
