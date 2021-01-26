package doa.ai.main.notes.ideation.model

import com.google.gson.annotations.SerializedName

data class ResponseGetIdea(


        @field:SerializedName("results")
        val results: MutableList<ResultsItem>,

        @field:SerializedName("status")
        val status: Int? = null,
        val message : String,
        val page_size : Int,
        val count : Int,
        val current_page : Int

)
