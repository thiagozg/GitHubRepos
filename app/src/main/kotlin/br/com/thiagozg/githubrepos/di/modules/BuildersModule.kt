package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.features.MainActivity
import br.com.thiagozg.githubrepos.features.viewmodel.MainViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainViewModelModule::class])
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

}
