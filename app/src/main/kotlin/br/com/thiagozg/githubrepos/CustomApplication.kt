package br.com.thiagozg.githubrepos

import br.com.thiagozg.githubrepos.di.ActivityInjector
import br.com.thiagozg.githubrepos.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class CustomApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        ActivityInjector.setup(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

}
