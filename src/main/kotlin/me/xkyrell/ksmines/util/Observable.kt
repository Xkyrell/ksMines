package me.xkyrell.ksmines.util

interface Observable {

    fun onObserve() : Result

    enum class Result {
        IDLE,
        REFILLED,
        ROTATED
    }
}