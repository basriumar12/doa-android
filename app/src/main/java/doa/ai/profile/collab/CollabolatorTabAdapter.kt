package doa.ai.profile.collab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import doa.ai.main.notes.bplan.plan.BPlanFragment
import doa.ai.main.notes.ideation.IdeationFragment
import doa.ai.profile.collab.connected.CollaboratorConnectedFragment
import doa.ai.profile.collab.find_out.CollabolatorSugestionFragment
import doa.ai.profile.collab.request.CollabolatorRequestFragment
import doa.ai.profile.collab.sent.CollabolatorSentFragment

class CollabolatorTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val pages = listOf(
            CollaboratorConnectedFragment(),
            CollabolatorSugestionFragment(),
            CollabolatorRequestFragment(),
            CollabolatorSentFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Terhubung"
            1 -> "Temukan"
            2 -> "Permintaan"
            3 -> "Terkirim"

            else -> null
        }

    }
}