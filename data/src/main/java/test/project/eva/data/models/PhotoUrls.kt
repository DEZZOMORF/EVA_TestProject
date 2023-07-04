package test.project.eva.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoUrls(

    @SerializedName("raw")
    @Expose
    val raw: String?,

    @SerializedName("full")
    @Expose
    val full: String?,

    @SerializedName("regular")
    @Expose
    val regular: String?,

    @SerializedName("small")
    @Expose
    val small: String?,

    @SerializedName("thumb")
    @Expose
    val thumb: String?,
)