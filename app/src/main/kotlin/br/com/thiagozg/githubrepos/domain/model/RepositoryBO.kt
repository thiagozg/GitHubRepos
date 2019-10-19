package br.com.thiagozg.githubrepos.domain.model

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class RepositoryBO(
    val id: Int,
    val name: String,
    val starsCount: Int,
    val forkCount: Int,
    val photoUrl: String,
    val authorName: String
)