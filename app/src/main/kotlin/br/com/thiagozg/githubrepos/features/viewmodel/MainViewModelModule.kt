package br.com.thiagozg.githubrepos.features.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.thiagozg.githubrepos.di.viewmodel.ViewModelFactory
import br.com.thiagozg.githubrepos.di.viewmodel.ViewModelKey
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCaseModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
@Module(includes = [FetchRepositoriesUseCaseModule::class])
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

}