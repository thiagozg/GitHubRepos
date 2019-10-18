package br.com.thiagozg.githubrepos.domain

import br.com.thiagozg.githubrepos.data.repository.MainRepository
import br.com.thiagozg.githubrepos.domain.mapper.RepositoriesMapper
import br.com.thiagozg.githubrepos.domain.model.RepositoryBO
import io.reactivex.Single
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class FetchRepositoriesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val repositoriesMapper: RepositoriesMapper
) : UseCase<FetchRepositoriesUseCase.Params, List<RepositoryBO>>() {

    override fun run(params: Params): Single<List<RepositoryBO>> =
        mainRepository.searchByQuery(params)
            .map { repositoriesMapper.map(it) }

    data class Params(
        val language: String = DEFAULT_LANGUAGE,
        val sort: String = DEFAULT_SORT,
        val page: Int,
        val perPage: Int
    )

    companion object {
        private const val DEFAULT_LANGUAGE = "kotlin"
        private const val DEFAULT_SORT = "stars"
    }
}