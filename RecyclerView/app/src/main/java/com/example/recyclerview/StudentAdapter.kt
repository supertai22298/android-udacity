package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.databinding.ListStudentItemBinding


class StudentAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Student, StudentViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        var layoutInflater = ListStudentItemBinding.inflate(LayoutInflater.from(parent.context))

        return StudentViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        var student = getItem(position)
        holder.bind(student)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(student)
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

    }
}

class StudentViewHolder(private var binding: ListStudentItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(student: Student) {
        binding.student = student
        binding.executePendingBindings()
    }
}

class OnClickListener(val clickListener: (student: Student) -> Unit) {
    fun onClick(student: Student) = clickListener(student)
}