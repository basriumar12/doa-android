package doa.ai.extentions

import doa.ai.R

fun getOnBoardingImage(id: Int?): Int{
    return when (id) {
        0 -> R.drawable.onboarding_screen_1
        1 -> R.drawable.onboarding_screen_2
        else -> R.drawable.onboarding_screen_3
    }
}