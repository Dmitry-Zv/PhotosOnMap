package by.zharikov.photosonmap.domain.model

import com.google.gson.annotations.SerializedName

data class PostAuthentication(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
)