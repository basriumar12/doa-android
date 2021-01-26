package doa.ai.main.notes.samples

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import doa.ai.R

class SampleFragment: Fragment() {

    companion object {
        fun newInstance(): SampleFragment {
            return SampleFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bplan_sample,container,false)
    }

}