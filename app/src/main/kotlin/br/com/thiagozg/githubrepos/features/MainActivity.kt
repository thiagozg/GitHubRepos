package br.com.thiagozg.githubrepos.features

import android.os.Bundle
import android.util.Log
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

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var repositoriesAdapter: RepositoriesListAdapter
    private lateinit var linearLayoutManager: WrapContentLinearLayoutManager
    private var hasRestoredState = false

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
        if (viewModel.repositoriesData.value == null) {
            viewModel.fetchRepositories()
        } else {
            hasRestoredState = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.saveState(linearLayoutManager.findFirstVisibleItemPosition())
    }

    private fun observeRepositoriesResult() {
        viewModel.repositoriesData.observeNonNull(this) { stateResponse ->
            when (stateResponse) {
                is StateSuccess<*> -> handleSuccessState(stateResponse)
                is StateError -> handleErrorState()
                is StateLoading -> handleLoadingState()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleSuccessState(stateResponse: StateSuccess<*>) {
        runOnUiThread {
            rvRepositories.visibility = View.VISIBLE
            pbRepositories.visibility = View.GONE
        }
        val items = stateResponse.data
        if (items is List<*> && items.isNotEmpty() && items[0] is RepositoryVO) {
            rvRepositories.post {
                repositoriesAdapter.addItems(items as List<RepositoryVO>)
                if (hasRestoredState) restoreRecyclerViewState()
            }
        }
    }

    private fun restoreRecyclerViewState() {
        hasRestoredState = false
        val position = viewModel.currentPosition
        linearLayoutManager.scrollToPosition(position)
    }

    private fun handleErrorState() {
        runOnUiThread {
            pbRepositories.visibility = View.GONE
            alert(
                br.com.thiagozg.githubrepos.R.string.error_dialog_msg,
                br.com.thiagozg.githubrepos.R.string.error_dialog_title
            ) {
                yesButton { viewModel.fetchRepositories(isRetry = true) }
                noButton {}
            }.show()
        }
    }

    private fun handleLoadingState() {
        runOnUiThread { pbRepositories.visibility = View.VISIBLE }
    }

    private fun RecyclerView.setup() {
        linearLayoutManager = WrapContentLinearLayoutManager(context)
        layoutManager = linearLayoutManager
        val infiniteScrollListener = InfiniteScrollListener(linearLayoutManager) {
            viewModel.fetchRepositories()
        }
        addOnScrollListener(infiniteScrollListener)
        addItemDecoration(DividerItemDecoration(context, VERTICAL))
        adapter = repositoriesAdapter
    }

}
