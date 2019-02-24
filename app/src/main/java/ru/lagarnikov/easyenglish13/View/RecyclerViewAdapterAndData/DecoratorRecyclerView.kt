package ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DecoratorRecyclerView(val padding:Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if ((view?.let { parent?.getChildAdapterPosition(it) }?.and(1)) == 0) {
            outRect?.bottom=padding
        }else{
            outRect?.left=padding
            outRect?.bottom=padding
        }
    }


}