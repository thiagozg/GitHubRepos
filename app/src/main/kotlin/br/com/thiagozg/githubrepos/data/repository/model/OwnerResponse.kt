package br.com.thiagozg.githubrepos.data.repository.model

import com.google.gson.annotations.SerializedName

/*
 * Created by Thiago Zagui Giacomini on 17/10/2019.
 * See thiagozg on GitHub: https://github.com/thiagozg
 */
data class OwnerResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String
)