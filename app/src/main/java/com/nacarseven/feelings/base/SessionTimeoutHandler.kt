package com.nacarseven.feelings.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable

class SessionTimeoutHandler(
    private val timeoutNotification: SessionTimeout
) : Application.ActivityLifecycleCallbacks {

    private val disposables = CompositeDisposable()

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityPaused(activity: Activity) {
        disposables.clear()
    }

    override fun onActivityResumed(activity: Activity) {
    }
}