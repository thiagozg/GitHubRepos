package br.com.thiagozg.githubrepos.features.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase.Companion.PER_PAGE_LIMIT
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase.Companion.PER_PAGE_TO_INCREASED
import br.com.thiagozg.githubrepos.domain.model.RepositoryBO
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

    private val repositoriesFullListData = MutableLiveData<MutableList<RepositoryVO>>().apply {
        value = mutableListOf()
    }
    val repositoriesData = MutableLiveData<StateResponse>()

    var currentPosition = 0
        private set

    private val disposables = CompositeDisposable()
    private var actualPage = 1
    private var previousPage = 0
    private var actualItemsShowingCount = 0
    private var previousItemsShowingCount = 0

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

    fun saveState(currentPosition: Int) {
        this.currentPosition = currentPosition
    }

    private fun updateRepositoriesData(bo: List<RepositoryBO>) {
        repositoriesFullListData.value?.run {
            addAll(bo.map { it.toVO() }.toMutableList())
            repositoriesData.value = StateSuccess(getNewItems())
        }
    }

    private fun MutableList<RepositoryVO>.getNewItems() = subList(0, maxLength())

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
        val newItems = repositoriesFullListData.value?.getNewItems()
        repositoriesData.value = StateSuccess(newItems)
    }

    private fun maxLength(): Int = repositoriesFullListData.value?.size?.takeUnless {
        actualItemsShowingCount < it
    } ?: actualItemsShowingCount

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}