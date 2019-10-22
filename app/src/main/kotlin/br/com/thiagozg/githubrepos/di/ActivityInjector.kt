package br.com.thiagozg.githubrepos.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import br.com.thiagozg.githubrepos.CustomApplication
import dagger.android.AndroidInjection

object ActivityInjector {

    fun setup(customApplication: CustomApplication) {
        DaggerAppComponent.builder().create(customApplication)
        customApplication.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    AndroidInjection.inject(activity)
                }
                override fun onActivityStarted(activity: Activity) = Unit
                override fun onActivityResumed(activity: Activity) = Unit
                override fun onActivityPaused(activity: Activity) = Unit
                override fun onActivityStopped(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit
                override fun onActivityDestroyed(activity: Activity) = Unit
            }
        )
    }
}
