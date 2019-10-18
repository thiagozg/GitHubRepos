package br.com.thiagozg.githubrepos.domain

import io.reactivex.Single

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
abstract class UseCase<in Params, BO> {

    abstract fun run(params: Params): Single<BO>

    operator fun invoke(params: Params) = run(params)

}