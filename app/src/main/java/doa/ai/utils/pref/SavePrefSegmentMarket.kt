package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constant

class SavePrefSegmentMarket {
    fun  setMarketProduct (name : String){
        Hawk.put(Constant().product_market, name)
    }

    fun getMarketProduct () : String{
        val value = Hawk.get<Any>(Constant().product_market)
        return value.toString()
    }

    fun deleteMarketProduct (){
        Hawk.delete(Constant().product_market)
    }

    fun  setMarketTargetCustomer (name : String){
        Hawk.put(Constant().target_customer, name)
    }

    fun getMarketTargetCustomer () : String{
        val value = Hawk.get<Any>(Constant().target_customer)
        return value.toString()
    }

    fun deleteMarketTargetCustomer (){
        Hawk.delete(Constant().target_customer)
    }

    fun  setMarketGeografic (name : String){
        Hawk.put(Constant().geografic_market, name)
    }

    fun getMarketGeografic () : String{
        val value = Hawk.get<Any>(Constant().geografic_market)
        return value.toString()
    }

    fun deleteMarketGeografic (){
        Hawk.delete(Constant().geografic_market)
    }

    fun  setMarketCompetitive (name : String){
        Hawk.put(Constant().competitive_market, name)
    }

    fun getMarketCompetitive () : String{
        val value = Hawk.get<Any>(Constant().competitive_market)
        return value.toString()
    }

    fun deleteMarketCompetitive(){
        Hawk.delete(Constant().competitive_market)
    }

    fun  setMarketNameCompetitor (name : String){
        Hawk.put(Constant().name_competitor, name)
    }

    fun getMarketNameCompetitor () : String{
        var value = Hawk.get<Any>(Constant().name_competitor)
        var data =""
        if (value.equals(null)){
            data
        } else{
            data = value.toString()
        }
        return data
    }

    fun deleteMarketNameCompetitor(){
        Hawk.delete(Constant().name_competitor)
    }

    fun  setMarketWeaknessCompetitor (name : String){
        Hawk.put(Constant().weakness_competitor, name)
    }

    fun getMarketWeaknessCompetitor () : String{
        val value = Hawk.get<Any>(Constant().weakness_competitor)
        return value.toString()
    }

    fun deleteMarketWeaknessCompetitor(){
        Hawk.delete(Constant().weakness_competitor)
    }

    fun  setMarketCustomerManagement (name : String){
        Hawk.put(Constant().customer_managements, name)
    }

    fun getMarketCustomerManagement () : String{
        val value = Hawk.get<Any>(Constant().customer_managements)
        return value.toString()
    }

    fun deleteMarketCustomerManagement(){
        Hawk.delete(Constant().customer_managements)
    }

    fun  setMarketCustomerReachWays (name : String){
        Hawk.put(Constant().customer_reach_ways, name)
    }

    fun getMarketCustomerReachWays () : String{
        val value = Hawk.get<Any>(Constant().customer_reach_ways)
        return value.toString()
    }

    fun deleteMarketCustomerReachWays(){
        Hawk.delete(Constant().customer_reach_ways)
    }
}