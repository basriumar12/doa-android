package doa.ai.main.notes.ideation.model

import com.google.gson.annotations.SerializedName

class Result(

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("deleted_at")
        val deletedAt: Any? = null,

        @field:SerializedName("labels")
        val labels: List<String?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)
