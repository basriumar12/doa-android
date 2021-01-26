package doa.ai.main.notes.ideation.model

import com.google.gson.annotations.SerializedName

data class BodyIdea(

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("labels")
        val labels: List<String?>? = null
)

class BodyIdeaDelete(
        @field:SerializedName("id")
        val id : Int? = null
)
class BodyIdeaDeleteFull(

        val ideas : MutableList<Int>
)

class BodyIdeaPinned(
        @field:SerializedName("id")
        val id : MutableList<Int>
)

class BodyIdeaUpdateStatus(
        val ideas : MutableList<Int>,
        val status : String
)

class BodyCopyIdea(
        val ideas : MutableList<Int>
)


