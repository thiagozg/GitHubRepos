package br.com.thiagozg.githubrepos.domain.model

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
sealed class StateResponse
class StateSuccess<T>(val data: T) : StateResponse()
class StateError(val error: Throwable) : StateResponse()