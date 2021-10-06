package ru.neosvet.flickr.abs

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.neosvet.flickr.R
import java.net.SocketException
import java.net.UnknownHostException

fun Fragment.showMessageRetry(parent: View, msg: String, event: ((View) -> Unit)) {
    Snackbar.make(
        parent, msg, Snackbar.LENGTH_INDEFINITE
    ).setAction(
        getString(R.string.retry),
        event
    )
        .show()
}

fun Fragment.getTranslateMessage(t: Throwable):String {
    if(t is UnknownHostException || t is SocketException) {
        return getString(R.string.no_connection)
    }
    return t.message.toString()
}

data class MyAction(
    val type: Type,
    val sArg: String,
    val iArg: Int
) {
    enum class Type {
        GALLERY, SEARCH, PHOTO, INFO
    }
}