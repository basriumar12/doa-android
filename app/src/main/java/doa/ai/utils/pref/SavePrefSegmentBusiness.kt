package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constant

class SavePrefSegmentBusiness {
    fun  setBusinessProvince (name : String){
        Hawk.put(Constant().provinsi, name)
    }

    fun getBusinessProvince () : String{
        val value = Hawk.get<Any>(Constant().provinsi)
        return value.toString()
    }

    fun deleteBusinessProvince(){
        Hawk.delete(Constant().provinsi)
    }

    fun  setBusinessProvinceId (name : String){
        Hawk.put(Constant().provinsi_id, name)
    }

    fun getBusinessProvinceId () : String{
        val value = Hawk.get<Any>(Constant().provinsi_id)
        return value.toString()
    }

    fun deleteBusinessProvinceId(){
        Hawk.delete(Constant().provinsi_id)
    }

    fun  setBusinessCity (name : String){
        Hawk.put(Constant().city, name)
    }

    fun getBusinesscity () : String{
        val value = Hawk.get<Any>(Constant().city)
        return value.toString()
    }

    fun deleteBusinessCity(){
        Hawk.delete(Constant().city)
    }


    fun  setBusinessSubDistric (name : String){
        Hawk.put(Constant().district, name)
    }

    fun getBusinessSubDistric () : String{
        val value = Hawk.get<Any>(Constant().district)
        return value.toString()
    }

    fun deleteBusinessSubDistric(){
        Hawk.delete(Constant().district)
    }
    fun  setBusinessStreet (name : String){
        Hawk.put(Constant().street, name)
    }

    fun getBusinessStreet () : String{
        val value = Hawk.get<Any>(Constant().street)
        return value.toString()
    }

    fun deleteBusinessStreet(){
        Hawk.delete(Constant().street)
    }

    fun  setBusinessRT (name : String){
        Hawk.put(Constant().rt, name)
    }

    fun getBusinessRT () : String{
        val value = Hawk.get<Any>(Constant().rt)
        return value.toString()
    }

    fun deleteBusinessRT(){
        Hawk.delete(Constant().rt)
    }

    fun  setBusinessRW (name : String){
        Hawk.put(Constant().rw, name)
    }

    fun getBusinessRW () : String{
        val value = Hawk.get<Any>(Constant().rw)
        return value.toString()
    }

    fun deleteBusinessRW (){
        Hawk.delete(Constant().rw)
    }

    fun  setBusinessPostalCode (name : String){
        Hawk.put(Constant().postalCode, name)
    }

    fun getBusinessPostalCode () : String{
        val value = Hawk.get<Any>(Constant().postalCode)
        return value.toString()
    }

    fun deleteBusinessPostalCode (){
        Hawk.delete(Constant().postalCode)
    }

    fun  setBusinessNameFounder (name : String){
        Hawk.put(Constant().name_founder, name)
    }

    fun getBusinessNameFounder () : String{
        val value = Hawk.get<Any>(Constant().name_founder)
        return value.toString()
    }

    fun deleteBusinessNameFounder (){
        Hawk.delete(Constant().name_founder)
    }

    fun  setBusinessPositionFounder (name : String){
        Hawk.put(Constant().position_founder, name)
    }

    fun getBusinessPositionFounder () : String{
        val value = Hawk.get<Any>(Constant().position_founder)
        return value.toString()
    }

    fun deleteBusinessPositionFounder (){
        Hawk.delete(Constant().position_founder)
    }

    fun  setBusinessExpertisFounder (name : String){
        Hawk.put(Constant().expertis_founder, name)
    }

    fun getBusinessExpertisFounder () : String{
        val value = Hawk.get<Any>(Constant().expertis_founder)
        return value.toString()
    }

    fun deleteBusinessExpertisFounder (){
        Hawk.delete(Constant().expertis_founder)
    }

    fun  setBusinessKeySuccess (name : String){
        Hawk.put(Constant().key_success, name)
    }

    fun getBusinesskeySuccess () : String{
        val value = Hawk.get<Any>(Constant().key_success)
        return value.toString()
    }

    fun deleteBusinessKeySuccess (){
        Hawk.delete(Constant().key_success)
    }


    fun  setBusinessBusinessActivities (name : String){
        Hawk.put(Constant().business_activities, name)
    }

    fun getBusinesskBusinessActivities () : String{
        val value = Hawk.get<Any>(Constant().business_activities)
        return value.toString()
    }

    fun deleteBusinessBusinessActivities (){
        Hawk.delete(Constant().business_activities)
    }


    fun  setBusinessBusinessPartnerts (name : String){
        Hawk.put(Constant().business_partnerts, name)
    }

    fun getBusinesskBusinessPartnerts() : String{
        val value = Hawk.get<Any>(Constant().business_partnerts)
        return value.toString()
    }

    fun deleteBusinessBusinessPartnerts (){
        Hawk.delete(Constant().business_partnerts)
    }

    fun  setBusinessOperatingExpaness (name : String){
        Hawk.put(Constant().operating_expanses, name)
    }

    fun getBusinessOperatingExpaness() : String{
        val value = Hawk.get<Any>(Constant().operating_expanses)
        return value.toString()
    }

    fun deleteBusinessOperatingExpaness (){
        Hawk.delete(Constant().operating_expanses)
    }


}