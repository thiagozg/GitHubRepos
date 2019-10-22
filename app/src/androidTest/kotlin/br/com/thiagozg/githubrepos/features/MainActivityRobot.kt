package br.com.thiagozg.githubrepos.features

import android.content.Intent
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import br.com.thiagozg.githubrepos.R
import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse
import br.com.thiagozg.githubrepos.domain.mapper.RepositoriesMapper
import br.com.thiagozg.githubrepos.features.model.*
import br.com.thiagozg.githubrepos.features.recycleradapter.RepositoriesListAdapter
import br.com.thiagozg.githubrepos.features.viewmodel.MainViewModel
import br.com.thiagozg.githubrepos.util.*
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import java.io.IOException


/*
 * Created by Thiago Zagui Giacomini on 22/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
object MainActivityRobot {

    private const val VISIBLE_ITEMS = 4
    private val context = getInstrumentation().context

    fun launchActivity(activityRule: ActivityTestRule<*>) = apply {
        activityRule.launchActivity(Intent())
    }

    fun fetchStateSuccessDataHalfPage(repositoriesData: MutableLiveData<StateResponse>) = apply {
        val repositoriesResponse = getJsonObject<RepositoriesResponse>(
            context, "50_results_page_1.json"
        )
        val repositoriesVo = RepositoriesMapper.map(repositoriesResponse)
            .map { it.toVO() }
            .subList(0, 25)
        repositoriesData.value = StateSuccess(repositoriesVo)
    }

    fun fetchStateSuccessDataFullPage(repositoriesData: MutableLiveData<StateResponse>) = apply {
        val repositoriesResponse = getJsonObject<RepositoriesResponse>(
            context, "50_results_page_1.json"
        )
        val repositoriesVo = RepositoriesMapper.map(repositoriesResponse)
            .map { it.toVO() }
        repositoriesData.value = StateSuccess(repositoriesVo)
    }

    fun checkRecyclerViewIsShowingWithExpectedData() = apply {
        sleep(400L)
        viewBy(R.id.rvRepositories).check(RecyclerViewItemCountAssertion(25))
        val recyclerMatcher = RecyclerViewMatcher(R.id.rvRepositories)
        onView(recyclerMatcher.atPosition(0))
            .check(matches(allOf(
                hasDescendant(withText("architecture-samples")),
                hasDescendant(withText("Stars: 34524")),
                hasDescendant(withText("Forks: 9614")),
                hasDescendant(withText("Author: android"))
            )))


        onView(recyclerMatcher.atPosition(1))
            .check(matches(allOf(
                hasDescendant(withText("kotlin")),
                hasDescendant(withText("Stars: 29390")),
                hasDescendant(withText("Forks: 3470")),
                hasDescendant(withText("Author: JetBrains"))
            )))
    }

    fun fetchStateError(repositoriesData: MutableLiveData<StateResponse>) = apply {
        Looper.prepare()
        repositoriesData.value = StateError(IOException())
    }

    fun checkIsShowingAlertDialogError() = apply {
        onView(isRoot()).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    fun fetchStateLoading(repositoriesData: MutableLiveData<StateResponse>) = apply {
        repositoriesData.value = StateLoading
    }

    fun checkIsShowingProgressBar() = apply {
        viewBy(R.id.pbRepositories).isDisplayed()
    }

    fun scrollToPosition(position: Int) = apply {
        viewBy(R.id.rvRepositories).perform(
            scrollToPosition<RepositoriesListAdapter.RepositoryHolder>(position)
        )
    }

    fun checkIfRecyclerViewDataHasIncreased(viewModel: MainViewModel) {
        verify { viewModel.fetchRepositories(any()) }
        viewBy(R.id.rvRepositories).check(RecyclerViewItemCountAssertion(50))
    }

    fun changeScreenOrientation(viewModel: MainViewModel, position: Int) = apply {
        every { viewModel.currentPosition } returns position
        val device = UiDevice.getInstance(getInstrumentation())
        device.setOrientationLeft()
        sleep(400L)
    }

    fun checkIfKeepStateOfRecyclerView(position: Int) = apply {
        val firstVisibleItemPosition = position - VISIBLE_ITEMS
        val recyclerMatcher = RecyclerViewMatcher(R.id.rvRepositories)
        onView(recyclerMatcher.atPosition(firstVisibleItemPosition))
            .check(matches(allOf(
                hasDescendant(withText("okio")),
                hasDescendant(withText("Stars: 6611")),
                hasDescendant(withText("Forks: 952")),
                hasDescendant(withText("Author: square"))
            )))
    }

    fun clickOnRetry() = apply {
        onView(withText(context.getString(android.R.string.ok))).perform(click())
    }

    fun checkHadRetry(viewModel: MainViewModel) = apply {
        verify { viewModel.fetchRepositories(true) }
    }

}