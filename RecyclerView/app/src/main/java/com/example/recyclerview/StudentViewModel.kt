package com.example.recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {
    private var _listOfStudent = MutableLiveData<List<Student>>()

    val listOfStudent: LiveData<List<Student>>
        get() = _listOfStudent

    init {
        _listOfStudent.value = listOf<Student>(
            Student("Taffy", "22", "https://kidsland-senior-project.s3-ap-southeast-1.amazonaws.com/Apr-04-2020-637216003294231275.png", "123"),
            Student("Taffy", "22", "https://kidsland-senior-project.s3-ap-southeast-1.amazonaws.com/Apr-04-2020-637216012175259859.png", "124"),
            Student("Taffy", "22", "https://kidsland-senior-project.s3-ap-southeast-1.amazonaws.com/Apr-06-2020-637217697500316476.png", "125"),
            Student("Taffy", "22", "image.png", "126")
        )
    }
}