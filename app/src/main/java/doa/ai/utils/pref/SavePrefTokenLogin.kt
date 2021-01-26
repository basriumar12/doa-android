package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constans
import doa.ai.utils.Constant

class SavePrefTokenLogin {

       fun  setTokenLogin (name : String){
            Hawk.put(Constant().tokenLogin, name)
       }

        fun getTokenLogin () : String{
            val value = Hawk.get<Any>(Constant().tokenLogin)
            return value.toString()
        }

        fun deleteTokenLogin (){
            Hawk.delete(Constant().tokenLogin)
        }

    fun  setTagSegmentSpinner (name : String){
            Hawk.put(Constant().tagSegment, name)
       }

        fun getTagSegmentSpinner () : String{
            val value = Hawk.get<Any>(Constant().tagSegment)
            return value.toString()
        }

        fun deleteTagSegmentSpinner (){
            Hawk.delete(Constant().tagSegment)
        }

}
