package br.com.thiagozg.githubrepos.di

import br.com.thiagozg.githubrepos.CustomApplication
import br.com.thiagozg.githubrepos.di.modules.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AndroidModule::class,
    NetworkModule::class,
    RetrofitModule::class,
    DataSourceModule::class,
    BuildersModule::class]
)
interface AppComponent : AndroidInjector<CustomApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<CustomApplication>()

}
