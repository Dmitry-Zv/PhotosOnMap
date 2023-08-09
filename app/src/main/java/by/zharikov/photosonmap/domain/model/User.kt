package by.zharikov.photosonmap.domain.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class User(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("status")
    val status: Int = 0
) {
    @Parcelize
    data class Data(
        @SerializedName("login")
        val login: String = "",
        @SerializedName("token")
        val token: String = "",
        @SerializedName("userId")
        val userId: Int = 0
    ) : Parcelable
}