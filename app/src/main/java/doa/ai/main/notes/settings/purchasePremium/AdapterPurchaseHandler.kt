package doa.ai.main.notes.settings.purchasePremium

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import kotlinx.android.synthetic.main.view_purchase_premium.view.*

class AdapterPurchaseHandler (private val context: Context, private val purchse: List<PurchasePremium>): PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.view_purchase_premium,container,false)
        with(purchse){
            view.textTitlePurchase.text = purchse[position].title

            container.addView(view)
        }
        return view
    }

    override fun getCount(): Int {
        return purchse.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

}