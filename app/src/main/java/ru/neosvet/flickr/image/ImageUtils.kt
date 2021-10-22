package ru.neosvet.flickr.image

object ImageUtils {
    fun findSeparator(resolution: String): Int {
        var i = resolution.indexOf("x")
        if (i > -1)
            return i
        i = 0
        while (resolution[i].isDigit()) {
            i++
            if (i == resolution.length)
                return -1
        }
        return i
    }

    fun isFitRatio(size1: Size, size2: Size): Boolean {
        val w = (size1.width.toFloat() / size2.width).rounded()
        val h = (size1.height.toFloat() / size2.height).rounded()
        return w == h
    }
}

fun Float.rounded() = String.format("%.2f", this)

data class Size(
    val width: Int,
    val height: Int
)
