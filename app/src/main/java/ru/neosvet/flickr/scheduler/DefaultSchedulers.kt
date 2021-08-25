package ru.neosvet.flickr.scheduler

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

class DefaultSchedulers : Schedulers {
    override fun background(): Scheduler = io.reactivex.rxjava3.schedulers.Schedulers.io()

    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}