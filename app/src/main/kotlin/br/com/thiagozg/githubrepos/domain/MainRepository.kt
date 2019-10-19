package br.com.thiagozg.githubrepos.domain

import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import io.reactivex.Single

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
interface MainRepository {

    fun searchByQuery(
        params: FetchRepositoriesUseCase.Params
    ): Single<RepositoriesResponse>

}