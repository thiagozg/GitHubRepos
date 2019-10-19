package br.com.thiagozg.githubrepos.data.repository.model


import com.google.gson.annotations.SerializedName

data class RepositoriesResponse(
    @SerializedName("items")
    val items: List<ItemResponse>
)
