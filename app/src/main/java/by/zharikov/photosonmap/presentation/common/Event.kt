package by.zharikov.photosonmap.presentation.common

interface Event<E> {
    fun onEvent(event: E)
}