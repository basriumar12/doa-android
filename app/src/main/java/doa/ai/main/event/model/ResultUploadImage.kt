package doa.ai.main.event.model

import com.google.gson.annotations.SerializedName

data class ResultUploadImage(

        @field:SerializedName("image_name")
        val imageName: String? = null,

        @field:SerializedName("image_url")
        val imageUrl: String? = null,

        val file_name: String? = null,
        val file_url: String? = null
)