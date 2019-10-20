package br.com.thiagozg.githubrepos.domain

import br.com.thiagozg.githubrepos.createRepositoriesResponse
import br.com.thiagozg.githubrepos.data.repository.MainRepositoryImpl
import br.com.thiagozg.githubrepos.data.repository.datasource.GitHubApiDataSource
import br.com.thiagozg.githubrepos.domain.mapper.RepositoriesMapper
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleMap
import io.reactivex.internal.operators.single.SingleObserveOn
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test


/*
 * Created by Thiago Zagui Giacomini on 19/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class FetchRepositoriesUseCaseTest {

    @get:Rule val rxSchedulersRule = RxSchedulerRule()

    private val mainRepository = mockk<MainRepositoryImpl>()
    private val useCase = FetchRepositoriesUseCase(mainRepository, RepositoriesMapper)

    @Test
    fun `when receive Repositories Response should return Single of RepositoriesBO`() {
        // given
        val expectedResult = RepositoriesMapper.map(createRepositoriesResponse())
        every {
            mainRepository.searchByQuery(any())
        } returns Single.just(createRepositoriesResponse())

        // when
        val singleResponse = useCase(
            FetchRepositoriesUseCase.Params(page = 1))
        val testObserver = singleResponse.test()

        // then
        assertThat(singleResponse).isNotNull
        assertThat(singleResponse).isExactlyInstanceOf(SingleMap::class.java)
        testObserver.assertTerminated()
            .assertNoErrors()
            .assertValueCount(1)
            .assertComplete()
            .assertResult(expectedResult)
            .dispose()
    }

}