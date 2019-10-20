package br.com.thiagozg.githubrepos.domain.mapper

import br.com.thiagozg.githubrepos.createRepositoriesResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/*
 * Created by Thiago Zagui Giacomini on 19/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
class RepositoriesMapperTest {

    @Test
    fun `validate if is doing the correct mapping`() {
        // given
        val mockRepositoriesResponse = createRepositoriesResponse()

        // when
        val repositoriesBO = RepositoriesMapper.map(mockRepositoriesResponse)

        // then
        assertThat(repositoriesBO).isNotEmpty
        assertThat(repositoriesBO.size).isEqualTo(mockRepositoriesResponse.items.size)
        repositoriesBO.forEachIndexed { index, repositoryBO ->
            val itemResponse = mockRepositoriesResponse.items[index]
            assertThat(repositoryBO.id).isEqualTo(itemResponse.id)
            assertThat(repositoryBO.name).isEqualTo(itemResponse.name)
            assertThat(repositoryBO.starsCount).isEqualTo(itemResponse.stargazersCount)
            assertThat(repositoryBO.forksCount).isEqualTo(itemResponse.forksCount)
            assertThat(repositoryBO.photoUrl).isEqualTo(itemResponse.owner.avatarUrl)
            assertThat(repositoryBO.authorName).isEqualTo(itemResponse.owner.login)
        }
    }

}