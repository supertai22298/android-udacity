package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){

    init {
        Log.i("GameViewModel", "Create Game View Model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "Destroy Game View Model")
    }
}