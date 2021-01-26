package doa.ai.extentions

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class SpacesItemDecoration(val spaces: Int): RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = spaces
        outRect.right = spaces
        outRect.bottom = spaces
        outRect.top = spaces
    }
}