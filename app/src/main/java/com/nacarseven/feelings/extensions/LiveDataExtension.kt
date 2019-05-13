package com.nacarseven.feelings.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, func: (T) -> Unit) =
    observe(owner, Observer<T> { it?.let { func(it) } })