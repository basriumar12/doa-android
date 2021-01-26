package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constant

class SavePrefSegmentStartegy {
    fun  setStrategyGoals (name : String){
        Hawk.put(Constant().goals, name)
    }

    fun getStrategyGoals () : String{
        val value = Hawk.get<Any>(Constant().goals)
        return value.toString()
    }

    fun deleteStrategyGoals (){
        Hawk.delete(Constant().goals)
    }

    fun  setStrategyOportunity (name : String){
        Hawk.put(Constant().opportunities, name)
    }

    fun getStrategyOportunity () : String{
        val value = Hawk.get<Any>(Constant().opportunities)
        return value.toString()
    }

    fun deleteStrategyOportunity (){
        Hawk.delete(Constant().opportunities)
    }

    fun  setStrategyStep (name : String){
        Hawk.put(Constant().steps, name)
    }

    fun getStrategyStep () : String{
        val value = Hawk.get<Any>(Constant().steps)
        return value.toString()
    }

    fun deleteStrategyStep (){
        Hawk.delete(Constant().steps)
    }
}