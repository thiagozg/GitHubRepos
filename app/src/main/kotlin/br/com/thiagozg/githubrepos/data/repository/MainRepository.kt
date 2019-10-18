package br.com.thiagozg.githubrepos.data.repository

import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import io.reactivex.Single

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
interface MainRepository {

    fun searchByQuery(
        language: String,
        sort: String,
        page: Int,
        perPage: Int
    ): Single<RepositoriesResponse>

}