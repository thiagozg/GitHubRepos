package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.features.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

}
