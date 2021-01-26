package doa.ai.main.notes.themes.theme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import android.widget.ArrayAdapter
import doa.ai.R
import doa.ai.main.notes.themes.shopping.ShoppingCartActivity
import kotlinx.android.synthetic.main.fragment_themes.*

class ThemesFragment : Fragment(){

    companion object {
        fun newInstance(): ThemesFragment {
            return ThemesFragment()
        }
    }

    private lateinit var adapterThemes: AdapterThemes
    private lateinit var listthemes: ArrayList<Themes>
    private var menu : Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentThemes()
        spinnerCategory()
        spinnerSort()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_themes,container,false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_themes,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setContentThemes(){
        listthemes = ArrayList()
        listthemes.add(Themes("https://s3.envato.com/files/259816212/Preview%20Image%20Set/Slide1.JPG", "Technology Model", "by in the mean time studio", "Rp 200.000", "technology"))
        listthemes.add(Themes("https://s3.envato.com/files/259812291/Preview%20Image%20Set/Slide1%20(2).JPG", "Trending Business Plan", "by in the mean time studio", "Rp 150.000", "Creative"))
        listthemes.add(Themes("https://s3.envato.com/files/259812122/Big%20Img/Slide1.JPG", "Trending Business Plan", "by in the mean time studio", "Rp 300.000", "Creative"))
        listthemes.add(Themes("https://s3.envato.com/files/259921905/preview-image-set/01_preview1.JPG", "Trending Business Plan", "by in the mean time studio", "Rp 100.000", "Creative"))
        listthemes.add(Themes("https://s3.envato.com/files/259796068/preview-image-set/01_preview1.JPG", "Trending Business Plan", "by in the mean time studio", "$12", "Creative"))
        listthemes.add(Themes("https://s3.envato.com/files/259698287/Preview%20Image%20Set/Slide1.JPG", "Trending Business Plan", "by in the mean time studio", "$12", "Creative"))
        adapterThemes = AdapterThemes(listthemes, context as Context)
        recyleContentThemes.layoutManager = LinearLayoutManager(context)
        recyleContentThemes.adapter = adapterThemes
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.actions_cart -> {
                context?.startActivity(Intent(context, ShoppingCartActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun spinnerCategory(){
        val parent = ArrayList<String>()
        parent.add("Category")
        parent.add("Business Plan")
        parent.add("Pitch Deck")
        parent.add("Branding")

        val dataAdapter = ArrayAdapter<String>(context as Context,android.R.layout.simple_spinner_item,parent)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoryThemes.prompt = "Select"
        spinnerCategoryThemes.adapter = dataAdapter
    }

    private fun spinnerSort(){
        val parent = ArrayList<String>()
        parent.add("Sort")
        parent.add("Best Sellers")
        parent.add("Newest Items")
        parent.add("Trending Items")
        parent.add("Lowest Price")
        parent.add("Highest Price")

        val dataAdapter = ArrayAdapter<String>(context as Context,android.R.layout.simple_spinner_item,parent)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSortThemes.prompt = "Select"
        spinnerSortThemes.adapter = dataAdapter
    }
}