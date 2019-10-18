package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.TestApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAndroidModule {

    @Provides
    @Singleton
    fun provideContext(application: TestApplication) = application.applicationContext

}
