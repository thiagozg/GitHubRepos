package br.com.thiagozg.githubrepos.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.thiagozg.githubrepos.createRepositoriesResponse
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import br.com.thiagozg.githubrepos.domain.mapper.RepositoriesMapper
import br.com.thiagozg.githubrepos.domain.model.RepositoryBO
import br.com.thiagozg.githubrepos.features.model.*
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.mockk.*
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/*
 * Created by Thiago Zagui Giacomini on 19/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainViewModelTest {

    @get:Rule val rxSchedulersRule = RxSchedulerRule()

    @get:Rule val archRule = InstantTaskExecutorRule()

    private val useCase = mockk<FetchRepositoriesUseCase>()
    private val viewModel = spyk(MainViewModel(useCase), recordPrivateCalls = true)
    private val observerState = spyk<Observer<StateResponse>>()

    @Before
    fun setUp() {
        viewModel.repositoriesData.observeForever(observerState)
    }

    @After
    fun tearDown() {
        viewModel.repositoriesData.removeObserver(observerState)
    }

    @Test
    fun `when receive Success Response should return StateSuccess with List of RepositoryVO`() {
        // given
        val repositoriesBO = mockSuccessResponse()

        // when
        viewModel.fetchRepositories(false)

        // then
        verifyPrivateFunctions(repositoriesBO)
        verifySuccessResponseData()
    }

    @Test(expected = IOException::class)
    fun `when receive Success Response should return StateError with Throwable`() {
        // given
        val ioException = IOException()
        every {
            useCase(any())
        } throws ioException

        // when
        viewModel.fetchRepositories(false)

        // then
        verifyErrorData()
    }

    @Test
    fun `when request without retry flag should increase page`() {
        // given
        mockSuccessResponse()

        // when
        viewModel.fetchRepositories(false)

        // then
        verifyOrder {
            viewModel.fetchRepositories(false)
            viewModel["increasePage"]()
        }
    }

    @Test
    fun `when request with retry flag should not increase page`() {
        // given
        mockSuccessResponse()

        // when
        viewModel.fetchRepositories(true)

        // then
        verify(exactly = 0) {
            viewModel["increasePage"]()
        }
    }

    @Test
    fun `when request less thant storage list should not request more network data`() {
        // given
        mockSuccessResponse()

        // when
        viewModel.fetchRepositories(false)
        viewModel.fetchRepositories(false)

        // then
        verifyOrder {
            viewModel["increasePage"]()
            viewModel["increaseShowingItems"]()
        }
    }

    @Test
    fun `#saveState() should record #currentPosition property`() {
        // when
        viewModel.saveState(10)

        // then
        assertThat(viewModel.currentPosition).isEqualTo(10)
    }

    private fun mockSuccessResponse(): MutableList<RepositoryBO> {
        val mockRepositoriesBO = mutableListOf<RepositoryBO>()
        for (i in 0 until 100) {
            val repositoriesBO = RepositoriesMapper.map(createRepositoriesResponse())
            mockRepositoriesBO.addAll(repositoriesBO)
        }
        val successResponse = Single.just(mockRepositoriesBO.toList())
        every {
            useCase(any())
        } returns successResponse
        return mockRepositoriesBO
    }

    private fun verifySuccessResponseData() {
        assertThat(viewModel.repositoriesData).isNotNull
        assertThat(viewModel.repositoriesData).isExactlyInstanceOf(MutableLiveData::class.java)
        val stateResponse = viewModel.repositoriesData.value
        assertThat(stateResponse).isExactlyInstanceOf(StateSuccess::class.java)
        val data = (stateResponse as StateSuccess<List<RepositoryVO>>).data
        assertThat(data.size).isEqualTo(25)
        data.forEach { repositoryVO ->
            assertThat(repositoryVO).isExactlyInstanceOf(RepositoryVO::class.java)
            assertThat(repositoryVO.id).isNotNull()
            assertThat(repositoryVO.name).isNotNull()
            assertThat(repositoryVO.starsCount).isNotNull()
            assertThat(repositoryVO.forkCount).isNotNull()
            assertThat(repositoryVO.authorName).isNotNull()
            assertThat(repositoryVO.photoUrl).isNotNull()
        }
    }

    private fun verifyPrivateFunctions(repositoriesBO: List<RepositoryBO>) {
        verifyOrder {
            viewModel["increasePage"]()
            observerState.onChanged(StateLoading)
            viewModel["updateRepositoriesData"](repositoriesBO)
            viewModel["maxLength"]()
        }
    }

    private fun verifyErrorData() {
        assertThat(viewModel.repositoriesData).isNotNull
        assertThat(viewModel.repositoriesData).isExactlyInstanceOf(MutableLiveData::class.java)
        val stateResponse = viewModel.repositoriesData.value
        assertThat(stateResponse).isExactlyInstanceOf(StateError::class.java)
        val data = (stateResponse as StateError).error
        assertThat(data).isExactlyInstanceOf(IOException::class.java)
    }

}