package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String> get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean> get() = _eventGameFinish

    init {
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
        _word.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "Destroy Game View Model")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        nextWord()
    }
    fun onGameFinished() {
        _eventGameFinish.value = false
    }

    private fun nextWord() {
        if (wordList.isEmpty()) {
            _eventGameFinish.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }
    }


}