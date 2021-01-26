package doa.ai.main.home

import doa.ai.database.modelDB.PinnedEntry
import doa.ai.main.notes.ideation.model.ResultsItem

data class HomeMenu(val id: Int
                    ,val text: String
                    ,val urlProfile: Int
                    ,val url: String
                    ,val desc: String
                    ,val title: String
                    ,val starDate: String
                    ,val endDate: String
                    ,val place: String
                    ,val homeIdea: MutableList<HomeIdea>
                   // ,val pinnedIdea: MutableList<PinnedEntry>?
                    ,val pinnedIdea: MutableList<ResultsItem>?
                    ,val sampleBusiness: MutableList<HomeSample>
                    ,val type: Int
                    ,val bPlanEvent : Boolean
                    ,val documentEvent : Boolean
                    ,val checkRegisterUser : Boolean
                    )

data class HomeIdea(val id: Int,val textIdea: String,val urlImageHome: Int)

data class HomeSample(val textSample: String,val urlSample: String)