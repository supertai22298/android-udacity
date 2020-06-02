package com.taffy.cleanhome.getstarted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.taffy.cleanhome.R
import com.taffy.cleanhome.databinding.FragmentGetStartedBinding
import kotlinx.android.synthetic.main.fragment_get_started.*
import kotlinx.android.synthetic.main.fragment_get_started.view.*

/**
 * A simple [Fragment] subclass.
 */
class GetStartedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGetStartedBinding.inflate(inflater)
        // Inflate the layout for this fragment
        binding.getStartedButton.setOnClickListener {
            this.findNavController().navigate(GetStartedFragmentDirections.actionGetStartedFragmentToYourPlanFragment())
        }
        return binding.root
    }

}
