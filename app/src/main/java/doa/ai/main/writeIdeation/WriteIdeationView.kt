package doa.ai.main.writeIdeation

import doa.ai.main.notes.ideation.model.Result

interface WriteIdeationView {

    fun showSucces(data : Result)
    fun failed()
    fun message(message : String)
    fun showLoad()
    fun hideLoad()
}