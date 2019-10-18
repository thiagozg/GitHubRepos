package br.com.thiagozg.githubrepos.di.modules

import android.content.Context
import br.com.thiagozg.githubrepos.CustomApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun provideContext(application: CustomApplication): Context = application.applicationContext

}
