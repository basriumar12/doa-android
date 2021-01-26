package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constans
import doa.ai.utils.Constant

class SavePrefId {

       fun  setPlanID (name : String){
            Hawk.put(Constant().planId, name)
       }

        fun getPlanID () : String{
            val value = Hawk.get<Any>(Constant().planId)
            return value.toString()
        }

        fun deletePlanId (){
            Hawk.delete(Constant().planId)
        }

        fun  setBusinessId (name : String){
            Hawk.put(Constant().bussinessId, name)
       }

        fun getBussinessId () : String{
            val value = Hawk.get<Any>(Constant().bussinessId)
            return value.toString()
        }

        fun deleteBussinessId (){
            Hawk.delete(Constant().bussinessId)
        }

        fun  setMarketId (name : String){
            Hawk.put(Constant().marketId, name)
       }

        fun getMarketId () : String{
            val value = Hawk.get<Any>(Constant().marketId)
            return value.toString()
        }

        fun deleteMarketId (){
            Hawk.delete(Constant().marketId)
        }

    fun  setFinnanceId (name : String){
            Hawk.put(Constant().finnanceId, name)
       }

        fun getFinnanceId () : String{
            val value = Hawk.get<Any>(Constant().finnanceId)
            return value.toString()
        }

        fun deleteFinnanceId (){
            Hawk.delete(Constant().finnanceId)
        }

    fun  setStrategyId (name : String){
            Hawk.put(Constant().strategyId, name)
       }

        fun getStrategyId () : String{
            val value = Hawk.get<Any>(Constant().strategyId)
            return value.toString()
        }

        fun deleteStrategyId (){
            Hawk.delete(Constant().strategyId)
        }

    fun  setSummaryId (name : String){
            Hawk.put(Constant().summaryId, name)
       }

        fun getSummaryId () : String{
            val value = Hawk.get<Any>(Constant().summaryId)
            return value.toString()
        }

        fun deletesummaryId (){
            Hawk.delete(Constant().summaryId)
        }
}
