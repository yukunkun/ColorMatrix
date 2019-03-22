package com.miracle.view.imageeditor.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.ViewSwitcher
import com.imageeditor.view.IMGColorGroup
import com.miracle.view.imageeditor.R

/**
 * ## UI elements of scrawl view
 *
 * Created by lxw
 */
class ScrawlDetailsView(ctx: Context) : FrameLayout(ctx), RadioGroup.OnCheckedChangeListener {


    var onRevokeListener: OnRevokeListener? = null
    var onColorChangeListener: ColorSeekBar.OnColorChangeListener? = null
    private val mColorGroup :IMGColorGroup

    init {
        LayoutInflater.from(ctx).inflate(R.layout.scralw_func_details, this, true)
        findViewById<ImageView>(R.id.ivRevoke).setOnClickListener {
            onRevokeListener?.revoke(EditorMode.ScrawlMode)
        }
        val ckb = findViewById<ColorSeekBar>(R.id.colorBarScrawl)
        ckb.setOnColorChangeListener(object : ColorSeekBar.OnColorChangeListener {
            override fun onColorChangeListener(colorBarPosition: Int, alphaBarPosition: Int, color: Int) {
                onColorChangeListener?.onColorChangeListener(colorBarPosition, alphaBarPosition, color)
            }
        })
        val opsub = findViewById<ViewSwitcher>(R.id.vs_op_sub)
        mColorGroup =findViewById(R.id.cg_colors)
        mColorGroup.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        onColorChangeListener?.onColorChangeListener(mColorGroup.checkColor, mColorGroup.checkColor,mColorGroup.checkColor)
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}