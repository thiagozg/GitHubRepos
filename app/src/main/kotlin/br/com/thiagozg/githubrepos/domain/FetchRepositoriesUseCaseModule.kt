package br.com.thiagozg.githubrepos.domain

import br.com.thiagozg.githubrepos.data.GitHubApiDataSource
import br.com.thiagozg.githubrepos.data.repository.MainRepository
import br.com.thiagozg.githubrepos.data.repository.MainRepositoryImpl
import br.com.thiagozg.githubrepos.domain.mapper.RepositoriesMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
@Module
class FetchRepositoriesUseCaseModule {

    @Singleton
    @Provides
    fun providesUseCase(
        mainRepository: MainRepository,
        repositoriesMapper: RepositoriesMapper
    ) = FetchRepositoriesUseCase(mainRepository, repositoriesMapper)

    @Singleton
    @Provides
    fun providesMainRepository(apiDataSource: GitHubApiDataSource): MainRepository =
        MainRepositoryImpl(apiDataSource)

    @Singleton
    @Provides
    fun providesMapper() = RepositoriesMapper

}