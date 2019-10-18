package br.com.thiagozg.githubrepos.features

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout.VERTICAL
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagozg.githubrepos.R
import br.com.thiagozg.githubrepos.base.BaseActivity
import br.com.thiagozg.githubrepos.base.observeNonNull
import br.com.thiagozg.githubrepos.domain.model.StateError
import br.com.thiagozg.githubrepos.domain.model.StateLoading
import br.com.thiagozg.githubrepos.domain.model.StateSuccess
import br.com.thiagozg.githubrepos.features.model.RepositoryVO
import br.com.thiagozg.githubrepos.features.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import javax.inject.Inject

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var repositoriesAdapter: RepositoriesListAdapter

    private val viewModel by viewModels<MainViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvRepositories.bind()
        observeRepositoriesResult()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchRepositories()
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeRepositoriesResult() {
        viewModel.repositoriesData.observeNonNull(this) { stateResponse ->
            when (stateResponse) {
                is StateSuccess<*> -> handleSuccessState(stateResponse)
                is StateError -> handleErrorState()
                is StateLoading -> pbRepositories.visibility = View.VISIBLE
            }
        }
    }

    private fun handleSuccessState(stateResponse: StateSuccess<*>) {
        rvRepositories.visibility = View.VISIBLE
        pbRepositories.visibility = View.GONE
        val items = stateResponse.data
        if (items is List<*> && items.isNotEmpty() && items[0] is RepositoryVO) {
            repositoriesAdapter.addItems(items as List<RepositoryVO>)
        }
    }

    private fun handleErrorState() {
        pbRepositories.visibility = View.GONE
        alert(R.string.error_dialog_msg, R.string.error_dialog_title) {
            yesButton { viewModel.fetchRepositories() }
            noButton {}
        }.show()
    }

    private fun RecyclerView.bind() {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(DividerItemDecoration(context, VERTICAL))
        adapter = repositoriesAdapter
    }

}
