package br.com.thiagozg.githubrepos.data.repository

import br.com.thiagozg.githubrepos.data.GitHubApiDataSource
import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import io.reactivex.Single
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainRepositoryImpl @Inject constructor() {

    @Inject
    lateinit var apiDataSource: GitHubApiDataSource

    fun searchByQuery(
        language: String = DEFAULT_LANGUAGE,
        sort: String = DEFAULT_SORT,
        page: Int,
        perPage: Int
    ): Single<RepositoriesResponse> {
        // TODO cache images
        return apiDataSource.searchRepositories(language, sort, page, perPage)
    }

    companion object {
        private const val DEFAULT_LANGUAGE = "kotlin"
        private const val DEFAULT_SORT = "stars"
    }
}