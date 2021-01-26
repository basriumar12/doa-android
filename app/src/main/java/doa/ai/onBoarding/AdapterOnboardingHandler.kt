package doa.ai.onBoarding

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import doa.ai.R
import doa.ai.extentions.getOnBoardingImage
import kotlinx.android.synthetic.main.view_onboarding.view.*

class AdapterOnboardingHandler(private val context: Context, private val onBoarding: List<OnBoarding>): PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.view_onboarding,container,false)
        with(onBoarding){
            view.imageOnboarding.setImageResource(getOnBoardingImage(onBoarding[position].image))
            view.textFirstOnBoarding.text = onBoarding[position].title
            view.textSecondOnBoarding.text = onBoarding[position].desc

            container.addView(view)
        }
        return view
    }

    override fun getCount(): Int {
        return onBoarding.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

}