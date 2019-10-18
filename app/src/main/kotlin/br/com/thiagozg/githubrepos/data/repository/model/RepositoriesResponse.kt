package br.com.thiagozg.githubrepos.data.repository.model


import com.google.gson.annotations.SerializedName

data class RepositoriesResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<ItemResponse>,
    @SerializedName("total_count")
    val totalCount: Int
)
