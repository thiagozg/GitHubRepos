package br.com.thiagozg.githubrepos.data.repository

import br.com.thiagozg.githubrepos.base.handleSchedulers
import br.com.thiagozg.githubrepos.data.GitHubApiDataSource
import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import io.reactivex.Single

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainRepositoryImpl(
    private val apiDataSource: GitHubApiDataSource
) : MainRepository {

    override fun searchByQuery(
        params: FetchRepositoriesUseCase.Params
    ): Single<RepositoriesResponse> {
        // TODO cache images
        return params.run {
            apiDataSource
                .searchRepositories(language, sort, page, perPage)
                .handleSchedulers()
        }
    }

}