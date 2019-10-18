package br.com.thiagozg.githubrepos.di

import br.com.thiagozg.githubrepos.TestApplication
import br.com.thiagozg.githubrepos.di.modules.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    TestAndroidModule::class,
    NetworkModule::class,
    TestRetrofitModule::class,
    DataSourceModule::class,
    BuildersModule::class]
)
interface TestAppComponent : AndroidInjector<TestApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<TestApplication>()

}
