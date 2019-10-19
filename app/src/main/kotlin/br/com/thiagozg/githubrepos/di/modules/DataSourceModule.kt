package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.data.repository.datasource.GitHubApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun providesGitHubApiDataSource(retrofit: Retrofit) =
        retrofit.create(GitHubApiDataSource::class.java)

}
