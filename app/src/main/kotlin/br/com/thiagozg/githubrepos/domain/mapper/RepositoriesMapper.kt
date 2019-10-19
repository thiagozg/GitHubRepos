package br.com.thiagozg.githubrepos.domain.mapper

import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import br.com.thiagozg.githubrepos.domain.model.RepositoryBO

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
object RepositoriesMapper {

    fun map(repositoriesResponse: RepositoriesResponse): List<RepositoryBO> {
        return repositoriesResponse.items.map {
            RepositoryBO(
                id = it.id,
                name = it.name,
                starsCount = it.stargazersCount,
                forkCount = it.forksCount,
                photoUrl = it.owner.avatarUrl,
                authorName = it.owner.login
            )
        }
    }

}