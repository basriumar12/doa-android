package doa.ai.utils.pref

import com.orhanobut.hawk.Hawk
import doa.ai.utils.Constans
import doa.ai.utils.Constant

class SavePref {

       fun  setName (name : String){
            Hawk.put(Constant().saveName, name)
       }

        fun getName () : String{
            val value = Hawk.get<Any>(Constant().saveName)
            return value.toString()
        }

        fun deleteName (){
            Hawk.delete(Constant().saveName)
        }
        //email
        fun  setEmail (name : String){
            Hawk.put(Constant().saveEmail, name)
       }

        fun getEmail () : String{
            val value = Hawk.get<Any>(Constant().saveEmail)
            return value.toString()
        }

        fun deleteEmail (){
            Hawk.delete(Constant().saveEmail)
        }

        //phone
        fun  setPhone (name : String){
            Hawk.put(Constant().savePhone, name)
       }

        fun getPhone () : String{
            val value = Hawk.get<Any>(Constant().savePhone)
            return value.toString()
        }

        fun deletePhone (){
            Hawk.delete(Constant().savePhone)
        }
}
