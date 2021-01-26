package doa.ai.main.notes.themes.shopping

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import doa.ai.R
import kotlinx.android.synthetic.main.activity_shopping_cart.*

class ShoppingCartActivity : AppCompatActivity (){

    private lateinit var adapter : AdapterCart
    private lateinit var itemcart : ArrayList<ShoppingCart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        setTitle("Shopping Cart")
        setSupportActionBar(toolbarShoppingCart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        setItemCart()
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

    private fun setItemCart(){
        itemcart = ArrayList()
        itemcart.add(ShoppingCart("https://s3.envato.com/files/260574396/preview-image-set/01_preview1.JPG", "Sinarum Pitching Deck",
                "by in the mean time s" +
                        "tudio", "$15"))
        itemcart.add(ShoppingCart("https://cdn-images-1.medium.com/max/1600/1*Ab4LuCp5ob1VyiceJ7rmKA.jpeg", "Trending Business Plan",
                "by in the mean time studio", "$15"))
        itemcart.add(ShoppingCart("https://s3.envato.com/files/259928313/Preview%20Image%20Set/Slide1.JPG", "Trending Business Plan",
                "by in the mean time studio", "$15"))
        itemcart.add(ShoppingCart("https://s3.envato.com/files/259917904/Slide01.jpg", "Trending Business Plan",
                "by in the mean time studio", "$15"))
        itemcart.add(ShoppingCart("https://s3.envato.com/files/259919329/Preview%20Image%20Set/Slide1%20(2).JPG", "Trending Business Plan",
                "by in the mean time studio", "$15"))
        itemcart.add(ShoppingCart("https://s3.envato.com/files/259934170/preview/Funnel-Infographics-PowerPoint-Template-PPT-Slides-01.PNG", "Trending Business Plan",
                "by in the mean time studio", "$15"))
        adapter = AdapterCart(itemcart, this)
        recyleItemCart.layoutManager = LinearLayoutManager(this)
        recyleItemCart.adapter = adapter
    }
}