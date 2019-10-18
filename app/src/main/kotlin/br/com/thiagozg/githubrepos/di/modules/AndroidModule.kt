package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.CustomApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun provideContext(application: CustomApplication) = application.applicationContext

}
