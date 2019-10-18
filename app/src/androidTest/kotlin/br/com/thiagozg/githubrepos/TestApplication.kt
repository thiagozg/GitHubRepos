package br.com.thiagozg.githubrepos

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import br.com.thiagozg.githubrepos.di.DaggerTestAppComponent

class TestApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.builder().create(this)
    }

}
