package doa.ai.profile.profiles

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import doa.ai.R
import doa.ai.base.BaseFragment
import doa.ai.login.logins.LoginActivity
import doa.ai.main.notes.settings.purchasePremium.PurchasePremiumrActivity
import doa.ai.payment.PaymentActivity
import doa.ai.profile.collab.CollaborationActivity
import doa.ai.profile.edit.EditProfileActivity
import doa.ai.register.AdapterRegister
import kotlinx.android.synthetic.main.content_for_fundings.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import okhttp3.MultipartBody

class ProfileFragment : BaseFragment(),ProfileView {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    var tokenLogin: String? = ""
    private lateinit var presenter: ProfilePresenter
    private var prefs: SharedPreferences? = null
    private var menu : Menu? = null
    private lateinit var adapter: AdapterProfileInterest
    private lateinit var recylerviewInterest: RecyclerView
    private var interestsItem: MutableList<InterestsItem> = mutableListOf()
    private lateinit var buttonGetPremiumFree : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view : View = inflater.inflate(R.layout.fragment_profile,container,false)
        recylerviewInterest = view.rv_interest
        recylerviewInterest.layoutManager = GridLayoutManager(context,1)
        buttonGetPremiumFree = view.btnGetPremiumFree
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ProfilePresenter()
        presenter.profileContract()
        prefs = context?.applicationContext?.getSharedPreferences("MyPref",0)
        tokenLogin = prefs?.getString("loginToken","")
        presenter.getProfile("token $tokenLogin")
        adapter = AdapterProfileInterest(interestsItem)

        layout_connection.setOnClickListener {
            context?.startActivity(Intent(activity,CollaborationActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("flags",""))
        }

        koleksiLayout.setOnClickListener { showInfoMessage("Segera Hadir") }
        kotakMasukLayout.setOnClickListener { showInfoMessage("Segera Hadir") }

    }

    override fun onResume() {
        super.onResume()
        presenter.getProfile("token $tokenLogin")

    }


    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    @SuppressLint("DefaultLocale")
    override fun onSuccessProfile(profile: ProfileResult) {
        interestsItem.clear()
        profile.interests?.forEach {
            it?.let { it1 -> interestsItem.add(it1) }

        }
        recylerviewInterest.adapter = adapter
        adapter.notifyDataSetChanged()
        Picasso.get().load(profile.photo_url).error(R.drawable.ic_menu_profiles).fit().into(imgProfile)

        val level = profile.subscription_level.capitalize()
        textMyprofileName.text = profile.fullname.capitalize()
        btnGetPremium.text = "Member $level"
        btnGetPremiumFree.text = "Update level $level ke premium"
        textProfesion.text = profile.profession.capitalize()
        textAbout.text = profile.about.capitalize()
        if (level.equals("free")){
            btnGetPremium.visibility = View.GONE
            textMyprofileName.visibility = View.GONE
        }
        else {
            btnGetPremiumFree.visibility = View.GONE
            btnGetPremium.visibility = View.VISIBLE
        }
        buttonGetPremiumFree.setOnClickListener {
          showErrorMessage("test etetetetete")
            Log.e("Tag","interest tadgatdftadafdafdha dahdjahdja")
            context?.startActivity(Intent(context, PurchasePremiumrActivity::class.java))
        }



    }

    override fun onFailedProfile(error: String) {
       // showErrorMessage(error)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_profile,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun showLoad() {
      //  progress_profile.visibility = View.VISIBLE
    }

    override fun hideLoad() {
//        progress_profile.visibility = View.GONE
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.action_edit_profile -> {
                context?.startActivity(Intent(context, EditProfileActivity::class.java))
                true
            }
            R.id.action_invite_friend -> {
                context?.startActivity(Intent(context, EditProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                Toast.makeText(context,"Logout", Toast.LENGTH_LONG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}