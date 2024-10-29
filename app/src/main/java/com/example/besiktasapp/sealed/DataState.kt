package com.example.besiktasapp.sealed

import com.example.besiktasapp.models.Player

open class DataState {
    class Success(val data: MutableList<Player>) : DataState()
    class Failure(val message: String) : DataState()
    object Loading : DataState()
    object Empty : DataState()
}