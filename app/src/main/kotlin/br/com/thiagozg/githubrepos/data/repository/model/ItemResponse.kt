package br.com.thiagozg.githubrepos.data.repository.model

import com.google.gson.annotations.SerializedName

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
data class ItemResponse(
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: OwnerResponse,
    @SerializedName("score")
    val score: Double,
    @SerializedName("stargazers_count")
    val stargazersCount: Int
)