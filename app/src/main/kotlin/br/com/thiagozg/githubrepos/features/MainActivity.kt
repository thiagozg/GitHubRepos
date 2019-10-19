package br.com.thiagozg.githubrepos.features

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.LinearLayout.VERTICAL
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.com.thiagozg.githubrepos.R
import br.com.thiagozg.githubrepos.base.BaseActivity
import br.com.thiagozg.githubrepos.base.observeNonNull
import br.com.thiagozg.githubrepos.features.model.RepositoryVO
import br.com.thiagozg.githubrepos.features.model.StateError
import br.com.thiagozg.githubrepos.features.model.StateLoading
import br.com.thiagozg.githubrepos.features.model.StateSuccess
import br.com.thiagozg.githubrepos.features.recycleradapter.InfiniteScrollListener
import br.com.thiagozg.githubrepos.features.recycleradapter.RepositoriesListAdapter
import br.com.thiagozg.githubrepos.features.recycleradapter.WrapContentLinearLayoutManager
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

    private var listState: Parcelable? = null
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var repositoriesAdapter: RepositoriesListAdapter

    private val viewModel by viewModels<MainViewModel> {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvRepositories.setup()
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
            rvRepositories.post {
                repositoriesAdapter.addItems(items as List<RepositoryVO>)
            }
        }
    }

    private fun handleErrorState() {
        pbRepositories.visibility = View.GONE
        alert(R.string.error_dialog_msg, R.string.error_dialog_title) {
            yesButton { viewModel.fetchRepositories(isRetry = true) }
            noButton {}
        }.show()
    }

    private fun RecyclerView.setup() {
        val linearLayoutManager = WrapContentLinearLayoutManager(context)
        layoutManager = linearLayoutManager
        val infiniteScrollListener =
            InfiniteScrollListener(linearLayoutManager) {
                viewModel.fetchRepositories()
            }
        addOnScrollListener(infiniteScrollListener)
        addItemDecoration(DividerItemDecoration(context, VERTICAL))
        adapter = repositoriesAdapter
    }

    companion object {
        private const val LIST_STATE_KEY = "listStateKey"
    }

}
