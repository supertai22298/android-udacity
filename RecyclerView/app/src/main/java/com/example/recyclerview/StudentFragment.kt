package com.example.recyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.recyclerview.databinding.FragmentStudentBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class StudentFragment : Fragment() {

    private val viewModel: StudentViewModel by lazy {
        ViewModelProviders.of(this).get(StudentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStudentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.studentRecyclerView.adapter = StudentAdapter()

        return binding.root
    }
}