package ru.neosvet.flickr.photo

interface IInfoItemView {
    var pos: Int
    fun setText(title_id: Int, value: String)
}