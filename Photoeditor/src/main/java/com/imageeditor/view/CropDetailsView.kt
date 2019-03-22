package com.miracle.view.imageeditor.view

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.imageeditor.PreDrawSizeListener
import com.miracle.view.imageeditor.R
import com.imageeditor.Utils

/**
 * ## Ui details element for crop view
 *
 * Created by lxw
 */
class CropDetailsView(val view: View) : ViewTreeObserver.OnPreDrawListener {
    var cropListener: OnCropOperationListener? = null
    private val mRestoreView: TextView
    var onPreDrawListener: PreDrawSizeListener? = null

    init {
        view.viewTreeObserver.addOnPreDrawListener(this)
        view.findViewById<TextView>(R.id.ivCropRotate).setOnClickListener {
            cropListener?.onCropRotation(90f)
        }
        view.findViewById<ImageView>(R.id.ivCropCancel).setOnClickListener {
            cropListener?.onCropCancel()
        }
        mRestoreView = view.findViewById<TextView>(R.id.tvCropRestore) as TextView
        mRestoreView.setOnClickListener {
            cropListener?.onCropRestore()
        }
        view.findViewById<ImageView>(R.id.ivCropConfirm).setOnClickListener {
            cropListener?.onCropConfirm()
        }
    }

    override fun onPreDraw(): Boolean {
        onPreDrawListener?.invoke(view.width, view.height)
        view.viewTreeObserver.removeOnPreDrawListener(this)
        return false;
    }

    @SuppressLint("ResourceAsColor")
    fun setRestoreTextStatus(restore: Boolean) {
        var color = if (restore) R.color.C7 else R.color.C10
        color = Utils.getResourceColor(view.context, color)
        mRestoreView.setTextColor(color)
    }

    fun showOrHide(show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    interface OnCropOperationListener {

        fun onCropRotation(degree: Float)

        fun onCropCancel()

        fun onCropConfirm()

        fun onCropRestore()
    }
}