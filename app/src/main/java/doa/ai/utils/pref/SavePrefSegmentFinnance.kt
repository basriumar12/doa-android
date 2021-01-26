package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constant

class SavePrefSegmentFinnance {
    fun  setFinnanceCurrency (name : String){
        Hawk.put(Constant().currency, name)
    }

    fun getFinnanceCurrency () : String{
        val value = Hawk.get<Any>(Constant().currency)
        return value.toString()
    }

    fun deleteFinnanceCurrency (){
        Hawk.delete(Constant().currency)
    }

    fun  setFinnancePlanning (name : String){
        Hawk.put(Constant().planning_year, name)
    }

    fun getFinnancePlanning () : String{
        val value = Hawk.get<Any>(Constant().planning_year)
        return value.toString()
    }

    fun deleteFinnancePlanning (){
        Hawk.delete(Constant().planning_year)
    }

    fun  setFinnanceInvestMoney (name : String){
        Hawk.put(Constant().invest_money, name)
    }

    fun getFinnanceInvestMoney () : String{
        val value = Hawk.get<Any>(Constant().invest_money)
        return value.toString()
    }

    fun deleteFinnanceMoney (){
        Hawk.delete(Constant().invest_money)
    }

    fun  setFinnanceSalesFirstYear (name : String){
        Hawk.put(Constant().sales_first_year, name)
    }

    fun getFinnanceSalesFirstYear () : String{
        val value = Hawk.get<Any>(Constant().sales_first_year)
        return value.toString()
    }

    fun deleteFinnanceSalesFirstYear (){
        Hawk.delete(Constant().sales_first_year)
    }


    fun  setFinnanceSalesGrowth (name : String){
        Hawk.put(Constant().sales_growth, name)
    }

    fun getFinnanceSalesFirstGrowth () : String{
        val value = Hawk.get<Any>(Constant().sales_growth)
        return value.toString()
    }

    fun deleteFinnanceSalesGrowth (){
        Hawk.delete(Constant().sales_growth)
    }


    fun  setFinnanceCashSales (name : String){
        Hawk.put(Constant().cash_sales, name)
    }

    fun getFinnanceCashSales () : String{
        val value = Hawk.get<Any>(Constant().cash_sales)
        return value.toString()
    }

    fun deleteFinnanceInvestCashSales(){
        Hawk.delete(Constant().cash_sales)
    }


    fun  setFinnanceExpansesFirstYear (name : String){
        Hawk.put(Constant().expanses_first_year, name)
    }

    fun getFinnanceExpansesFirstYear () : String{
        val value = Hawk.get<Any>(Constant().expanses_first_year)
        return value.toString()
    }

    fun deleteFinnanceExpansesFirstYear(){
        Hawk.delete(Constant().expanses_first_year)
    }

    fun  setFinnanceExpansesGrowth (name : String){
        Hawk.put(Constant().expanses_growth, name)
    }

    fun getFinnanceExpansesGrowth () : String{
        val value = Hawk.get<Any>(Constant().expanses_growth)
        return value.toString()
    }

    fun deleteFinnanceExpansesGrowth(){
        Hawk.delete(Constant().expanses_growth)
    }

    fun  setFinnanceCreditCustomer(name : String){
        Hawk.put(Constant().credit_customer, name)
    }

    fun getFinnanceCreditCustomer() : String{
        val value = Hawk.get<Any>(Constant().credit_customer)
        return value.toString()
    }

    fun deleteFinnanceCreditCustomer(){
        Hawk.delete(Constant().credit_customer)
    }

    fun  setFinnanceCreditSupplier(name : String){
        Hawk.put(Constant().credit_supplier, name)
    }

    fun getFinnanceCreditSupplier() : String{
        val value = Hawk.get<Any>(Constant().credit_supplier)
        return value.toString()
    }

    fun deleteFinnanceCreditSupplier(){
        Hawk.delete(Constant().credit_supplier)
    }





}