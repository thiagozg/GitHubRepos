package br.com.thiagozg.githubrepos.features.model

import br.com.thiagozg.githubrepos.domain.model.RepositoryBO

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
data class RepositoryVO(
    val id: Int,
    val name: String,
    val starsCount: Int,
    val forkCount: Int,
    val photoUrl: String,
    val authorName: String
)

fun RepositoryBO.toVO() = RepositoryVO(
    id, name, starsCount, forksCount, photoUrl, authorName
)