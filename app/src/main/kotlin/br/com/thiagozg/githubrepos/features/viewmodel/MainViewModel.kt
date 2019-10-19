package br.com.thiagozg.githubrepos.features.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase.Companion.PER_PAGE_LIMIT
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase.Companion.PER_PAGE_TO_INCREASED
import br.com.thiagozg.githubrepos.domain.model.*
import br.com.thiagozg.githubrepos.features.model.*
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
    private var actualPage = 1
    private var previousPage = 0
    private var actualItemsShowingCount = 0
    private var previousItemsShowingCount = 0
    private var repositoriesListVo = mutableListOf<RepositoryVO>()

    fun fetchRepositories(isRetry: Boolean = false) {
        if (isRetry.not()) increasePage()
        if (actualPage > previousPage || isRetry) {
            previousPage = actualPage
            val params = FetchRepositoriesUseCase.Params(page = actualPage)
            disposables.add(
                fetchRepositoriesUseCase(params)
                    .doOnSubscribe { repositoriesData.value = StateLoading }
                    .subscribe(
                        { updateRepositoriesData(it) },
                        { repositoriesData.value = StateError(it) }
                    )
            )
        }
    }

    private fun updateRepositoriesData(bo: List<RepositoryBO>) {
        val startPosition = repositoriesListVo.size
        repositoriesListVo.addAll(bo.map { it.toVO() }.toMutableList())
        val newItems = repositoriesListVo.subList(startPosition, actualItemsShowingCount)
        repositoriesData.value = StateSuccess(newItems)
    }

    private fun increasePage() {
        previousItemsShowingCount = actualItemsShowingCount
        actualPage = actualItemsShowingCount / PER_PAGE_LIMIT + 1
        if (actualPage > previousPage) {
            actualItemsShowingCount += PER_PAGE_TO_INCREASED
        } else {
            actualItemsShowingCount += PER_PAGE_TO_INCREASED
            increaseShowingItems()
        }
    }

    private fun increaseShowingItems() {
        val maxLength = if (actualItemsShowingCount < repositoriesListVo.size) {
            actualItemsShowingCount
        } else repositoriesListVo.size
        val newItems = repositoriesListVo.subList(
            previousItemsShowingCount, maxLength)
        repositoriesData.value = StateSuccess(newItems)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}