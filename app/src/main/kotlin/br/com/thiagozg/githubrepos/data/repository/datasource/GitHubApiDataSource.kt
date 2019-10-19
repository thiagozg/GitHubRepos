package br.com.thiagozg.githubrepos.data.repository.datasource

import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
interface GitHubApiDataSource {

    @GET("/search/repositories")
    fun searchRepositories(
        @Query("q") language: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int?
    ): Single<RepositoriesResponse>

}
