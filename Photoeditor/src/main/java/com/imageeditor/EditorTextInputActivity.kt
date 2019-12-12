package com.imageeditor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.TextClock
import android.widget.TextView
import com.miracle.view.imageeditor.R
import com.miracle.view.imageeditor.bean.InputTextData
import com.miracle.view.imageeditor.view.ColorSeekBar
import kotlinx.android.synthetic.main.activity_editor_text_input.*
import java.math.BigInteger

/**
 * ## TextInput activity
 *
 * Created by lxw
 */
class EditorTextInputActivity : AppCompatActivity() {
  private val mResultCode = 301
  private var mTextColor = 0// 需要回传的文字颜色
  private var mBgColor = 0//需要回传的背景色
  private var mTextInputId: String? = null
  private var Deputytext: TextView? = null
  internal var isChange = false//当前编辑模式 默认为true，为编辑字体颜色(此时关闭背景色)，false为编辑背景颜色


  companion object {
    private val EXTRA_CODE = "extraInput"
    fun intent(context: Context, data: InputTextData?): Intent {
      val intent = Intent(context, EditorTextInputActivity::class.java)
      intent.putExtra(EXTRA_CODE, data)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_editor_text_input)
    Deputytext = findViewById(R.id.deputy_text) as TextView
    AdjustResizeInFullScreen.assistActivity(this)
    initData()
    initListener()
  }


  private fun initData() {
    val readyData = intent.getSerializableExtra(EXTRA_CODE) as? InputTextData
    readyData?.let {
      mTextInputId = readyData.id
      etInput.setText(readyData.text ?: "")
      colorBarInput.setOnInitDoneListener(object : ColorSeekBar.OnInitDoneListener {  //用于线条型回调
        override fun done() {
          var position = 8
          readyData.color?.let {
            position = colorBarInput.getColorIndexPosition(it)
          }
          colorBarInput.setColorBarPosition(position)
        }
      })
      cg_colors.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
          var position = 8
          readyData.color?.let {
            position = cg_colors.checkColor
          }
        }
      })
    }
  }

  private fun initListener() {
    tvCancelInput.setOnClickListener {
      finish()
    }
    tvConfirmInput.setOnClickListener {
      val text = etInput.text.trim()
      if (text.isBlank()) {
        finish()
      } else {
        val intent = Intent()
        if (mTextColor != 0) {
          intent.putExtra(mResultCode.toString(),InputTextData(mTextInputId, text.toString(), mTextColor,mBgColor))
        } else {
          intent.putExtra(mResultCode.toString(), InputTextData(mTextInputId, text.toString(),resources.getColor(R.color.image_color_white),mBgColor))
        }
        setResult(mResultCode, intent)
        finish()
      }
    }

    etInput!!.addTextChangedListener(object : TextWatcher {

      private var temp: CharSequence? = null

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        temp = s
      }

      override fun afterTextChanged(s: Editable) {
        val spanString = SpannableString(temp!!.toString())
        val span = BackgroundColorSpan(cg_colors.checkColor)
        spanString.setSpan(span, 0, temp!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        Deputytext!!.text = spanString
      }
    })
    tvbackgroundchange.setOnClickListener {
      if (!isChange) {
        isChange = true
        Deputytext!!.visibility = View.VISIBLE
        Deputytext!!.setTextColor(resources.getColor(R.color.image_color_white))
        val spanString = SpannableString(etInput.editableText!!.toString())
        val span = BackgroundColorSpan(cg_colors.checkColor)
        spanString.setSpan(span, 0, etInput.editableText!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        Deputytext!!.text = spanString
        tvbackgroundchange.setImageDrawable(resources.getDrawable(R.drawable.font_bottom_ic))
        if (mTextColor == 0) {
          etInput.setTextColor(Color.parseColor("#26252a"))
        }
//        else if (cg_colors.checkColor == mTextColor) {
//          etInput.setTextColor(Color.parseColor("#F9F9F9"))
//        }
        else if (cg_colors.checkColor == Color.parseColor("#F9F9F9")) {
          etInput.setTextColor(Color.parseColor("#26252a"))
        } else {
          etInput.setTextColor(Color.parseColor("#F9F9F9"))
        }
      } else {
        isChange = false
        Deputytext!!.visibility = View.GONE
        etInput!!.setTextColor(cg_colors.checkColor)
        Log.e("cg_colors.checkColor==", "isChange==true" + cg_colors.checkColor)
        tvbackgroundchange.setImageDrawable(resources.getDrawable(R.drawable.font_bottom_ic_select))
      }
    }
    cg_colors.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
      override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        if (isChange) {
//          if (cg_colors.checkColor == mTextColor) {
//            etInput.setTextColor(Color.parseColor("#F9F9F9"))
//            mTextColor = Color.parseColor("#F9F9F9")
//          } else if (Color.parseColor("#F9F9F9") == cg_colors.checkColor) {
//            etInput.setTextColor(Color.parseColor("#26252a"))
//            mTextColor = Color.parseColor("#26252a")
//          } else if (Color.parseColor("#26252a") == cg_colors.checkColor) {
//            etInput.setTextColor(Color.parseColor("#F9F9F9"))
//            mTextColor = Color.parseColor("#F9F9F9")
//          }else{
//
//          }
          if (mTextColor == 0) {
            etInput.setTextColor(Color.parseColor("#26252a"))
            mTextColor = Color.parseColor("#F9F9F9")
          }
//        else if (cg_colors.checkColor == mTextColor) {
//          etInput.setTextColor(Color.parseColor("#F9F9F9"))
//        }
          else if (cg_colors.checkColor == Color.parseColor("#F9F9F9")) {
            etInput.setTextColor(Color.parseColor("#26252a"))
            mTextColor = Color.parseColor("#26252a")
          } else {
            etInput.setTextColor(Color.parseColor("#F9F9F9"))
            mTextColor = Color.parseColor("#F9F9F9")
          }

          val spanString = SpannableString(etInput.editableText!!.toString())
          val span = BackgroundColorSpan(cg_colors.checkColor)
          mBgColor = cg_colors.checkColor
          spanString.setSpan(span, 0, etInput.editableText!!.length,
              Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
          Deputytext!!.text = spanString
          mBgColor = cg_colors.checkColor
        } else {
          etInput.setTextColor(cg_colors.checkColor)
          mTextColor = cg_colors.checkColor
        }
        Log.e("cg_colors.checkColor==", "cg_colors" + cg_colors.checkColor)
      }
    })

  }


  override fun finish() {
    super.finish()
    overridePendingTransition(0, R.anim.animation_top_to_bottom)
  }
}
