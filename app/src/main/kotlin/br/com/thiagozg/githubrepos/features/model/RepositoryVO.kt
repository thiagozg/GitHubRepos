package br.com.thiagozg.githubrepos.features.model

import br.com.thiagozg.githubrepos.domain.model.RepositoryBO

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class RepositoryVO(
    val name: String,
    val starsCount: Int,
    val forkCount: Int,
    val photoUrl: String,
    val authorName: String
)

fun RepositoryBO.toVO() = RepositoryVO(
    name, starsCount, forkCount, photoUrl, authorName
)