package br.com.thiagozg.githubrepos.data.repository

import br.com.thiagozg.githubrepos.data.GitHubApiDataSource
import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import io.reactivex.Single
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainRepositoryImpl @Inject constructor() : MainRepository {

    @Inject
    lateinit var apiDataSource: GitHubApiDataSource

    override fun searchByQuery(
        params: FetchRepositoriesUseCase.Params
    ): Single<RepositoriesResponse> {
        // TODO cache images
        return params.run {
            apiDataSource.searchRepositories(language, sort, page, perPage)
        }
    }

}