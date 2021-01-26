package doa.ai.main.home

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
import com.valdesekamdem.library.mdtoast.MDToast
import doa.ai.R
import doa.ai.extentions.RoundedTransformation
import doa.ai.main.event.EventActivity
import doa.ai.main.notes.bplan.setup.SetupBPlanActivity
import kotlinx.android.synthetic.main.content_for_fundings.view.*
import kotlinx.android.synthetic.main.content_menu.view.*
import kotlinx.android.synthetic.main.view_home_idea.view.*
import kotlinx.android.synthetic.main.view_pinned_idea.view.*
import kotlinx.android.synthetic.main.view_sample_business.view.*

class AdapterMenu(val context: Context, val menu: List<HomeMenu>?): RecyclerView.Adapter<AdapterMenu.ViewHolder>(){

    private lateinit var adapterIdea: AdapterIdea
    private lateinit var adapterPinned: AdapterPinned
    private lateinit var adapterSample: AdapterSample

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View

        when(viewType){
            TYPE_1 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.content_menu, parent,false)
                return VHHeader(view)
            }
            TYPE_2 ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_home_idea, parent, false)
                return  VHIdea(view)
            }
            TYPE_3 ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_pinned_idea, parent, false)
                return  VHPinned(view)
            }
            TYPE_4 ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_sample_business,parent,false)
                return VHBusinessSample(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.content_for_fundings,parent,false)
                return VHFundings(view)
            }
        }
    }

    override fun getItemCount(): Int {
        if (menu == null)
            return 0
        return menu.size
    }

    override fun getItemViewType(position: Int): Int {
        if (menu != null){
            val listMenu: HomeMenu? = menu.get(position)
            if (listMenu!= null){
                return listMenu.type
            }
        }

        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val type: HomeMenu? = menu?.get(position)
        if (type != null){
            when(type.type){
                TYPE_1 ->{
                    val header = holder as VHHeader
                    menu?.get(holder.layoutPosition)?.let { header.bindView(it) }
                }
                TYPE_2 ->{
                    val idea = holder as VHIdea
                    menu?.get(holder.layoutPosition)?.let { idea.bindView(it) }
                }
                TYPE_3 ->{
                    val pinned = holder as VHPinned
                    menu?.get(holder.layoutPosition)?.let { pinned.bindView(it) }
                }
                TYPE_4 ->{
                    val sample = holder as VHBusinessSample
                    menu?.get(holder.layoutPosition)?.let { sample.bindView(it) }
                }
                TYPE_5 ->{
                    val business = holder as VHFundings
                    menu?.get(holder.layoutPosition)?.let { business.bindView(it) }
                }
            }
        }

    }

    open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    inner class VHHeader(itemView: View): ViewHolder(itemView){
        fun bindView(listMenu: HomeMenu){
            with(listMenu){
                itemView.textMenu.text = text
            }
        }
    }

    inner class VHIdea(itemView: View): ViewHolder(itemView){
        fun bindView(listMenu: HomeMenu){
            with(listMenu){
                adapterIdea = AdapterIdea(homeIdea, context)
                itemView.listHomeIdea.setHasFixedSize(true)
                itemView.listHomeIdea.layoutManager = GridLayoutManager(context, 4)
                itemView.listHomeIdea.adapter = adapterIdea
            }
        }
    }

    inner class VHPinned(itemView: View): ViewHolder(itemView){

        fun bindView(listMenu: HomeMenu){
            with(listMenu){
                if (pinnedIdea?.size != 0){
                    itemView.view_pinned.visibility = View.VISIBLE
                    itemView.tv_view_all_pinned.setOnClickListener {
                        MDToast.makeText(context,"Belum tersedia", MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show()
                    }
                }
                adapterPinned = AdapterPinned(pinnedIdea)
                itemView.listPinnedIdea.setHasFixedSize(true)
                itemView.listPinnedIdea.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                itemView.listPinnedIdea.adapter = adapterPinned
            }

        }
    }

    inner class VHBusinessSample(itemView: View): ViewHolder(itemView) {

        fun bindView(listMenu: HomeMenu) {
            with(listMenu) {
                adapterSample = AdapterSample(sampleBusiness)
                itemView.listBusinessSample.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                itemView.listBusinessSample.adapter = adapterSample
            }
        }
    }

    inner class VHFundings(itemView: View): ViewHolder(itemView){

        fun bindView(listMenu: HomeMenu){
            with(listMenu){
                val textDesc1: String = text.replace("<p>","")
                val textDesc2: String = textDesc1.replace("</p>","")
                val textDesc3: String = textDesc2.replace("<i>","")
                val textDesc4: String = textDesc3.replace("</i>","")
                val textDesc5: String = textDesc4.replace("<strong>","")
                val textDesc6: String = textDesc5.replace("</strong>","")
                itemView.textOfficial.text = title
                itemView.textFunding.text =  textDesc6
                Picasso.get().load(url).error(R.drawable.ic_logo_doa).fit().into(itemView.imageFundings)
                val radius = 0
                val margin = 0
                val transformation = RoundedTransformation(radius,margin)
                Picasso.get().load(urlProfile).fit().transform(transformation).into(itemView.imageProfile)
                itemView.imageBookmarkEvent.setOnClickListener {
                    MDToast.makeText(context,"Bookmark event", MDToast.LENGTH_LONG,MDToast.TYPE_INFO).show()
                }
                val link = context.getString(R.string.link_base_event)
                itemView.imageShareEvent.setOnClickListener {
                    var urlShare = "Hi ikuti event $title dengan cara klik $link$id, jika belum punya aplikasinya download di sini https://play.google.com/store/apps/details?id=doain.ai "
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, urlShare)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Share Ke Sosial Media Kamu"))
                }

                itemView.imageFundings.setOnClickListener {

                    context.startActivity(Intent(context, EventActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("id",id)
                            .putExtra("image", url)
                            .putExtra("title",title)
                            .putExtra("desc",desc)
                            .putExtra("title", title)
                            .putExtra("startDate",starDate)
                            .putExtra("endDate",endDate)
                            .putExtra("place",place)
                            .putExtra("checkRegister",checkRegisterUser)
                            .putExtra("bplan",bPlanEvent)
                            .putExtra("document",documentEvent)
                            .putExtra("flag","info")
                    )
                }

                itemView.buttonEvent.setOnClickListener {

                    context.startActivity(Intent(context, EventActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("id",id)
                            .putExtra("image", url)
                            .putExtra("title",title)
                            .putExtra("desc",desc)
                            .putExtra("title", title)
                            .putExtra("startDate",starDate)
                            .putExtra("endDate",endDate)
                            .putExtra("place",place)
                            .putExtra("checkRegister",checkRegisterUser)
                            .putExtra("bplan",bPlanEvent)
                            .putExtra("document",documentEvent)
                            .putExtra("flag","register")
                    )
                }
            }
        }
    }


    companion object {
        const val TYPE_1 = 1
        const val TYPE_2 = 2
        const val TYPE_3 = 3
        const val TYPE_4 = 4
        const val TYPE_5 = 5
    }
}