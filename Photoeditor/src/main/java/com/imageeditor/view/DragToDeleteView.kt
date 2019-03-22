package com.miracle.view.imageeditor.view

import android.graphics.RectF
import android.view.View
import android.widget.TextView
import com.imageeditor.OnLayoutRectChange
import com.miracle.view.imageeditor.R
import com.imageeditor.Utils
import com.imageeditor.setInt

/**
 * ## Ui element of pasting View to drag and delete .
 *
 * Created by lxw
 */
class DragToDeleteView(private val view: View) {
    var onLayoutRectChange: OnLayoutRectChange? = null
    private val mTextView: TextView

    init {
        view.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val rect = RectF()
            rect.setInt(left, top, right, bottom)
            onLayoutRectChange?.invoke(view, rect)
        }
        mTextView = view.findViewById<TextView>(R.id.tvDragDelete)
    }

    fun showOrHide(show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setDrag2DeleteText(focus: Boolean) {
        val text = if (focus){
            Utils.getResourceString(view.context, R.string.editor_drag_to_delete)
        }  else {
            Utils.getResourceString(view.context, R.string.editor_release_to_delete)
        }
        val img =if (focus){
           view.context.resources.getDrawable(R.drawable.ic_drag_to)
        }else{
            view.context.resources.getDrawable(R.drawable.ic_drag_to_delete)
        }
        img.setBounds(0, 0, img.minimumWidth, img.minimumHeight)
        mTextView.text = text
        mTextView.setCompoundDrawables(img, null, null, null) //设置左图标
    }

}