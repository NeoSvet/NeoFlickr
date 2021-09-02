package ru.neosvet.flickr.photo

interface IInfoItemView {
    var pos: Int
    fun setText(titleId: Int, value: String)
}