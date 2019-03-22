package com.imageeditor.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RadioButton

import com.miracle.view.imageeditor.R


/**
 * Created by wujinjian on 2018/11/22 15:43
 * E-Mail Address：373149993@qq.com
 */

@SuppressLint("AppCompatCustomView")
class IMGColorRadio @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RadioButton(context, attrs), ValueAnimator.AnimatorUpdateListener {

    private var mColor = Color.WHITE

    private var mStrokeColor = Color.WHITE

    private var mRadiusRatio = 0f

    private var mAnimator: ValueAnimator? = null

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        initialize(context, attrs, 0)
    }

    private val animator: ValueAnimator
        get() {
            if (mAnimator == null) {
                mAnimator = ValueAnimator.ofFloat(0f, 1f)
                mAnimator!!.addUpdateListener(this)
                mAnimator!!.duration = 200
                mAnimator!!.interpolator = AccelerateDecelerateInterpolator()
            }
            return mAnimator as ValueAnimator
        }

    var color: Int
        get() = mColor
        set(color) {
            mColor = color
            mPaint.color = mColor
        }


    private fun initialize(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.IMGColorRadio)

        mColor = a.getColor(R.styleable.IMGColorRadio_image_color, Color.WHITE)
        mStrokeColor = a.getColor(R.styleable.IMGColorRadio_image_stroke_color, Color.WHITE)

        a.recycle()

        buttonDrawable = null

        mPaint.color = mColor
        mPaint.strokeWidth = 5f
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val hw = width / 2f
        val hh = height / 2f
        val radius = Math.min(hw, hh)

        canvas.save()
        mPaint.color = mColor
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(hw, hh, getBallRadius(radius), mPaint)

        mPaint.color = mStrokeColor
        mPaint.style = Paint.Style.STROKE
        canvas.drawCircle(hw, hh, getRingRadius(radius), mPaint)
        canvas.restore()
    }

    private fun getBallRadius(radius: Float): Float {
        return radius * ((RADIUS_BALL - RADIUS_BASE) * mRadiusRatio + RADIUS_BASE)
    }

    private fun getRingRadius(radius: Float): Float {
        return radius * ((RADIUS_RING - RADIUS_BASE) * mRadiusRatio + RADIUS_BASE)
    }

    override fun setChecked(checked: Boolean) {
        val isChanged = checked != isChecked

        super.setChecked(checked)

        if (isChanged) {
            val animator = animator

            if (checked) {
                animator.start()
            } else {
                animator.reverse()
            }
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mRadiusRatio = animation.animatedValue as Float
        invalidate()
    }

    companion object {

        private val TAG = "IMGColorRadio"

        private val RADIUS_BASE = 0.6f

        private val RADIUS_RING = 0.9f

        private val RADIUS_BALL = 0.72f
    }
}
