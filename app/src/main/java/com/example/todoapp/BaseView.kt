package com.example.todoapp

interface BaseView<in T> {

    fun setPresenter(presenter: T)

}
