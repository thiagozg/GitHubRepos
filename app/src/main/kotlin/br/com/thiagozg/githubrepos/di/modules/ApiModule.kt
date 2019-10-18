package br.com.thiagozg.githubrepos.di.modules

import br.com.thiagozg.githubrepos.data.GitHubApiDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesGitHubApiDataSource(retrofit: Retrofit) =
        retrofit.create(GitHubApiDataSource::class.java)

}
