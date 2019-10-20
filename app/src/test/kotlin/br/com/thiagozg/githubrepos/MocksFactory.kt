package br.com.thiagozg.githubrepos

import br.com.thiagozg.githubrepos.data.repository.model.ItemResponse
import br.com.thiagozg.githubrepos.data.repository.model.OwnerResponse
import br.com.thiagozg.githubrepos.data.repository.model.RepositoriesResponse

/*
 * Created by Thiago Zagui Giacomini on 19/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
fun createRepositoriesResponse() = RepositoriesResponse(
    listOf(
        ItemResponse(
            111, "fullName111", "github/111", 111, "Kotlin", "name111",
            OwnerResponse("url111", 111, "login111"),
            111.0, 111
        ),
        ItemResponse(
            222, "fullName222", "github/222", 222, "Kotlin", "name222",
            OwnerResponse("url222", 222, "login222"),
            222.0, 222
        )
    )
)