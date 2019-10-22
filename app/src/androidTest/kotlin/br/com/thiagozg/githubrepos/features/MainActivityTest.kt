package br.com.thiagozg.githubrepos.features

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.intercepting.SingleActivityFactory
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import br.com.thiagozg.githubrepos.features.model.StateResponse
import br.com.thiagozg.githubrepos.features.recycleradapter.RepositoriesListAdapter
import br.com.thiagozg.githubrepos.features.viewmodel.MainViewModel
import br.com.thiagozg.githubrepos.util.*
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/*
 * Created by Thiago Zagui Giacomini on 21/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule val archRule = InstantTaskExecutorRule()

    @get:Rule val activityRule = ActivityTestRule(injectedFactory(), false, false)

    private lateinit var viewModel: MainViewModel
    private val repositoriesData = MutableLiveData<StateResponse>()

    @Test
    fun when_receive_state_success_should_show_data_recycler_view() {
        MainActivityRobot.launchActivity(activityRule)
            .fetchStateSuccessDataHalfPage(repositoriesData)
            .checkRecyclerViewIsShowingWithExpectedData()
    }

    @Test
    fun when_receive_state_error_should_show_retry_dialog() {
        MainActivityRobot.launchActivity(activityRule)
            .fetchStateError(repositoriesData)
            .checkIsShowingAlertDialogError()
            .clickOnRetry()
            .checkHadRetry(viewModel)
    }

    @Test
    fun when_receive_state_loading_should_show_progress_bar() {
        MainActivityRobot.launchActivity(activityRule)
            .fetchStateLoading(repositoriesData)
            .checkIsShowingProgressBar()
    }

    @Test
    fun when_is_almost_showing_full_list_should_fetch_more_data() {
        MainActivityRobot.launchActivity(activityRule)
            .fetchStateSuccessDataHalfPage(repositoriesData)
            .scrollToPosition(20)
            .fetchStateSuccessDataFullPage(repositoriesData)
            .checkIfRecyclerViewDataHasIncreased(viewModel)
    }

    @Test
    fun when_user_change_screen_orientation_should_keep_recycler_view_state() {
        val position = 20
        MainActivityRobot.launchActivity(activityRule)
            .fetchStateSuccessDataHalfPage(repositoriesData)
            .scrollToPosition(position)
            .changeScreenOrientation(viewModel, position)
            .checkIfKeepStateOfRecyclerView(position)
    }

    private fun injectedFactory() = object : SingleActivityFactory<MainActivity>(MainActivity::class.java) {
        override fun create(intent: Intent): MainActivity {
            val mainActivity = MainActivity()
            mockDaggerInjections(mainActivity)
            return mainActivity
        }
    }

    private fun mockDaggerInjections(mainActivity: MainActivity) {
        val useCase = mockk<FetchRepositoriesUseCase>()
        viewModel = spyk(MainViewModel(useCase), recordPrivateCalls = true)
        mainActivity.run {
            viewModelFactory = createViewModelFactory(viewModel)
            repositoriesAdapter = RepositoriesListAdapter()
        }

        every { viewModel.repositoriesData } returns repositoriesData
        every { viewModel.fetchRepositories(any()) } just Runs
    }

}