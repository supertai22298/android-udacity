package com.taffy.cleanhome.bottomnavandfab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taffy.cleanhome.R

/**
 * A simple [Fragment] subclass.
 */
class BottomNavAndFabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_nav_and_fab, container, false)
    }

}
