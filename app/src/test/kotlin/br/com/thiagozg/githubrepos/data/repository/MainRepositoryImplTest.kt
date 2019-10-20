package br.com.thiagozg.githubrepos.data.repository

import br.com.thiagozg.githubrepos.createRepositoriesResponse
import br.com.thiagozg.githubrepos.data.repository.datasource.GitHubApiDataSource
import br.com.thiagozg.githubrepos.domain.FetchRepositoriesUseCase
import br.com.thiagozg.githubrepos.domain.mapper.RepositoriesMapper
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleObserveOn
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

/*
 * Created by Thiago Zagui Giacomini on 19/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class MainRepositoryImplTest {

    @get:Rule val rxSchedulersRule = RxSchedulerRule()

    private val apiDataSource = mockk<GitHubApiDataSource>()
    private val mainRepository = MainRepositoryImpl(apiDataSource)

    @Test
    fun `#searchByQuery() should return Single of RepositoriesResponse`() {
        val expectedResult = createRepositoriesResponse()
        every {
            apiDataSource.searchRepositories(any(), any(), any(), any())
        } returns Single.just(createRepositoriesResponse())

        val singleResponse = mainRepository.searchByQuery(
            FetchRepositoriesUseCase.Params(page = 1))
        val testObserver = singleResponse.test()

        assertThat(singleResponse).isNotNull
        assertThat(singleResponse).isExactlyInstanceOf(SingleObserveOn::class.java)
        testObserver.assertTerminated()
            .assertNoErrors()
            .assertValueCount(1)
            .assertComplete()
            .assertResult(expectedResult)
            .dispose()
    }

}