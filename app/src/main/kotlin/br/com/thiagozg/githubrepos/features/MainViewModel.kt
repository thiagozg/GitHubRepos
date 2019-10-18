package br.com.thiagozg.githubrepos.features

import androidx.lifecycle.ViewModel
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 18/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainViewModel @Inject constructor(
    private val fetchRepositoriesUseCase: FetchRepositoriesUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}