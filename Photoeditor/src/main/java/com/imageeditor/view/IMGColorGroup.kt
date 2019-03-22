package com.imageeditor.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RadioGroup

/**
 * Created by wujinjian on 2018/11/22 15:10
 * E-Mail Address：373149993@qq.com
 */

class IMGColorGroup : RadioGroup {

    var checkColor: Int
        get() {
            val checkedId = checkedRadioButtonId
            val radio = findViewById<IMGColorRadio>(checkedId)
            return radio?.color ?: Color.WHITE
        }
        set(color) {
            val count = childCount
            for (i in 0 until count) {
                val radio = getChildAt(i) as IMGColorRadio
                if (radio.color == color) {
                    radio.isChecked = true
                    break
                }
            }
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
}
