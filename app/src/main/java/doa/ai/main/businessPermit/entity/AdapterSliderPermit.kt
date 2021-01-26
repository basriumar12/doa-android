package doa.ai.main.businessPermit.entity

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import kotlinx.android.synthetic.main.text_slider.view.*

class SliderAdapter(private val context: Context, private val permit : MutableList<String>) : PagerAdapter() {

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return permit.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(context).inflate(R.layout.text_slider,container,false)

        with(permit) {
            view.textSlider.text = permit[position]

            container.addView(view)
        }

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, p1: Any) {
        val viewPager = container as ViewPager
        val view = p1 as View
        viewPager.removeView(view)
    }
}
