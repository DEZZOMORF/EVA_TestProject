package test.project.eva.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoEntity(

    @SerializedName("urls")
    @Expose
    val urls: PhotoUrls?
)