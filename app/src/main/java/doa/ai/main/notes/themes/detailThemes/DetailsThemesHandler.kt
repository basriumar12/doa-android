package doa.ai.main.notes.themes.detailThemes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import doa.ai.R
import kotlinx.android.synthetic.main.activity_detail_themes.*

class DetailsThemesActivity : AppCompatActivity(){

    private lateinit var adapter: AdapterDetailThemes
    private lateinit var list: ArrayList<ThemesDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_themes)
        setSupportActionBar(toolbarDetailThemes)
        setTitle("Detail Product")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        setOderThemes()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        return when(id){
            android.R.id.home ->{
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setOderThemes(){
        Picasso.get()
                .load(intent.getStringExtra("image"))
                .fit()
                .into(imageThemesDetail)
        list = ArrayList()
        list.add(ThemesDetail("https://s3.envato.com/files/259940563/Preview%20Image%20Set/Slide1.jpeg"))
        list.add(ThemesDetail("https://s3.envato.com/files/259940563/Preview%20Image%20Set/Slide101.jpeg"))
        list.add(ThemesDetail("https://s3.envato.com/files/259939316/Preview%20Image%20Set/Slide1.JPG"))
        list.add(ThemesDetail("https://s3.envato.com/files/259938892/Preview%20Image%20Set/Slide1.JPG"))
        list.add(ThemesDetail("https://s3.envato.com/files/259907908/All%20Slide%20Images/Slide1.JPG"))
        list.add(ThemesDetail("https://s3.envato.com/files/259897577/01_preview1.JPG"))
        list.add(ThemesDetail("https://s3.envato.com/files/259880550/01_preview.jpg"))
        list.add(ThemesDetail("https://s3.envato.com/files/259897645/Preview%20Image%20Set/Slide1%20(2).JPG"))
        list.add(ThemesDetail("https://s3.envato.com/files/259883021/Preview%20Image%20Set/Slide1.JPG"))

        adapter = AdapterDetailThemes(list, this)
        recyleDetailThemes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyleDetailThemes.adapter = adapter
    }
}