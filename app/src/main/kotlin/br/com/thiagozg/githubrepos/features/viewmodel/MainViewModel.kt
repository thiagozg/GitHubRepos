package br.com.thiagozg.githubrepos.features.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import br.com.thiagozg.githubrepos.domain.model.StateError
import br.com.thiagozg.githubrepos.domain.model.StateResponse
import br.com.thiagozg.githubrepos.domain.model.StateSuccess
import br.com.thiagozg.githubrepos.features.model.toVO
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainViewModel @Inject constructor(
    private val fetchRepositoriesUseCase: FetchRepositoriesUseCase
) : ViewModel() {

    val repositoriesData = MutableLiveData<StateResponse>()

    private val disposables = CompositeDisposable()
    private var page = 0
    private var perPage = 0

    fun fetchRepositories() {
        val params = getRequestParams()
        disposables.add(
            fetchRepositoriesUseCase(params)
                .subscribe(
                    { bo -> repositoriesData.value = StateSuccess(bo.map { it.toVO() }) },
                    { repositoriesData.value = StateError(it) }
                )
        )
    }

    private fun getRequestParams(): FetchRepositoriesUseCase.Params {
        perPage += PER_PAGE_REQUEST
        page = perPage / PER_PAGE_LIMIT + 1
        return FetchRepositoriesUseCase.Params(page = page, perPage = perPage)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    companion object {
        private const val PER_PAGE_REQUEST = 25
        private const val PER_PAGE_LIMIT = 100
    }

}