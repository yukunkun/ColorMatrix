package com.miracle.view.imageeditor.layer

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import androidx.collection.ArrayMap
import android.util.AttributeSet
import android.view.MotionEvent
import com.imageeditor.*
import com.miracle.view.imageeditor.bean.PastingSaveStateMarker

/**
 *  ## Base  pasting layerView  for [StickerView] and [TextPastingView]
 *  It's hold drag info and callback of show or hide pasting removable
 *
 * Created by lxw
 */
abstract class BasePastingLayerView<T : PastingSaveStateMarker> : BaseLayerView<T> {
    var showOrHideDragCallback: ShowOrHideDragCallback? = null
    var setOrNotDragCallback: SetOrNotDragCallback? = null
    var onLayerViewDoubleClick: OnLayerViewDoubleClick? = null
    var dragViewRect: RectF = RectF()
    /*pasting info...*/
    protected val pastingMap = androidx.collection.ArrayMap<String, RectF>()
    protected var pastingOutOfBound = false
    protected var pastingDoubleClick = false
    protected var currentPastingState: T? = null
    protected val hidePastingOutOfBoundsRunnable = HidePastingOutOfBoundsRunnable()
    //pasting always in editMode
    override var isLayerInEditMode = true
    protected lateinit var focusRectPaint: Paint
    protected lateinit var focusRectCornerPaint: Paint
    protected var focusRectCornerWidth = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun initSupportView(context: Context) {
        super.initSupportView(context)
        //focus rect paint
        focusRectPaint = Paint()
        focusRectPaint.style = Paint.Style.STROKE
        focusRectPaint.isAntiAlias = true
        focusRectPaint.strokeWidth = 2f
        focusRectPaint.color = Color.WHITE
        //focusCornerRect.
        focusRectCornerWidth = Utils.dp2px(context, 2f).toFloat()
        focusRectCornerPaint = Utils.copyPaint(focusRectPaint)
        focusRectCornerPaint.style = Paint.Style.FILL
    }

    override fun drawMask(canvas: Canvas) {
        if (pastingOutOfBound) {
            currentPastingState?.let {
                drawPastingState(it, canvas)
            }
        }
        drawFocusDecorate(canvas)
    }

    protected fun drawFocusDecorate(canvas: Canvas) {
        currentPastingState?.let {
            val display = getStateDisplayRect(it, false)
            canvas.drawRect(display, focusRectPaint)
            //rect corner rect
            drawFocusRectCornerRect(canvas, display.left, display.top)
            drawFocusRectCornerRect(canvas, display.right, display.top)
            drawFocusRectCornerRect(canvas, display.right, display.bottom)
            drawFocusRectCornerRect(canvas, display.left, display.bottom)
        }
    }

    protected fun drawFocusRectCornerRect(canvas: Canvas, centerX: Float, centerY: Float) {
        val rect = RectF()
        rect.schedule(centerX, centerY, focusRectCornerWidth, focusRectCornerWidth)
        canvas.drawRect(rect, focusRectCornerPaint)
    }

    override fun checkInterceptedOnTouchEvent(event: MotionEvent): Boolean {
        super.checkInterceptedOnTouchEvent(event)
        val action = event.action and MotionEvent.ACTION_MASK
        if (action == MotionEvent.ACTION_DOWN) {
            val downState = getFingerDownState(event.x, event.y)
            if (downState != null && downState == currentPastingState) {
                pastingDoubleClick = true
            }
            currentPastingState = downState
            currentPastingState?.let {
                checkDisplayRegion(it)
                saveStateMap.remove(it.id)
                saveStateMap.put(it.id, it)
                it.initEventDisplayMatrix.set(it.displayMatrix)
                it.initEventDisplayMatrix.postConcat(drawMatrix)
                redrawAllCache()
            }
            currentPastingState ?: return false
        }
        return true
    }

    protected fun getFingerDownState(downX: Float, downY: Float): T? {
        for (index in saveStateMap.size - 1 downTo 0) {
            val state = saveStateMap.valueAt(index)
            val displayRect = getStateDisplayRect(state, true)
            if (displayRect.contains(downX, downY)) {
                return state
            }
        }
        return null
    }

    private fun checkDisplayRegion(state: PastingSaveStateMarker) {
        val rect = getStateDisplayRect(state, true)
        pastingOutOfBound = !validateRect.contains(rect)
    }

    private fun checkDisplayRegion(display: RectF) {
        pastingOutOfBound = !validateRect.contains(display)
    }

    protected fun getStateDisplayRect(state: PastingSaveStateMarker, realDisplay: Boolean): RectF {
        val finalMatrix = Matrix()
        finalMatrix.set(state.displayMatrix)
        if (realDisplay) {
            finalMatrix.postConcat(drawMatrix)
        }
        val displayRect = RectF()
        finalMatrix.mapRect(displayRect, state.initDisplayRect)
        return displayRect
    }

    //gesture region...

    /*gesture state detect with viewValidate*/
    override fun onFingerDown(downX: Float, downY: Float) {
        removeCallbacks(hidePastingOutOfBoundsRunnable)
    }

    override fun onDrag(dx: Float, dy: Float, x: Float, y: Float, rootLayer: Boolean) {
        if (!rootLayer) {
            pastingDoubleClick = false
            currentPastingState?.let {
                if (x != -1f || y != -1f) {
                    showOrHideDragCallback?.invoke(true)
                }
                //calc
                val invert = Utils.mapInvertMatrixTranslate(drawMatrix, dx, dy)
                it.displayMatrix.postTranslate(invert[0], invert[1])
                val displayRect = getStateDisplayRect(it, true)
                checkDisplayRegion(displayRect)
                //setStates
                setOrNotDragCallback?.invoke(!dragViewRect.contains(displayRect.centerX(), displayRect.centerY()))
                redrawAllCache()
            }
        }
    }

    override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float, rootLayer: Boolean) {
        if (!rootLayer) {
            currentPastingState?.let {
                val invert = Utils.mapInvertMatrixScale(drawMatrix, scaleFactor, scaleFactor)
                checkDisplayRegion(it)
                it.displayMatrix.postScale(invert[0], invert[1], focusX, focusY)
                redrawAllCache()
            }
        }
    }

    override fun onFingerUp(upX: Float, upY: Float) {
        showOrHideDragCallback?.invoke(false)
        //edit mode
        if (pastingDoubleClick && currentPastingState != null) {
            onPastingDoubleClick(currentPastingState!!)
        }
        pastingDoubleClick = false
        //remove mode,rebound mode
        currentPastingState?.let {
            val displayRect = getStateDisplayRect(it, true)
            val delete = dragViewRect.contains(displayRect.centerX(), displayRect.centerY())
            //remove
            if (delete) {
                saveStateMap.remove(it.id)
                currentPastingState = null
                redrawAllCache()
            } else {
                //rebound
                if (!validateRect.contains(displayRect.centerX(), displayRect.centerY())) {
                    val initEventMatrix = it.initEventDisplayMatrix
                    val currentMatrix = Matrix()
                    currentMatrix.set(it.displayMatrix)
                    currentMatrix.postConcat(drawMatrix)
                    val dx = Utils.getMatrixTransX(currentMatrix) - Utils.getMatrixTransX(initEventMatrix)
                    val dy = Utils.getMatrixTransY(currentMatrix) - Utils.getMatrixTransY(initEventMatrix)
                    rebound(dx, dy)
                }
            }
            it.initEventDisplayMatrix.reset()
        }
        //hide extra validate rect (over mValidateRect should be masked)
        hideExtraValidateRect()
    }

    protected fun hideExtraValidateRect() {
        postDelayed(hidePastingOutOfBoundsRunnable, 1500)

    }

    private fun rebound(dx: Float, dy: Float) {
        OverBoundRunnable(dx, dy).run()
    }

    protected inner class HidePastingOutOfBoundsRunnable : Runnable {
        override fun run() {
            recover2ValidateRect()
        }
    }

    private fun recover2ValidateRect() {
        currentPastingState = null
        pastingOutOfBound = false
        redrawAllCache()
    }


    override fun drawAllCachedState(canvas: Canvas) {
        for (state in saveStateMap.values) {
            drawPastingState(state, canvas)
            //update pasting available rect
            pastingMap.put(state.id, getStateDisplayRect(state, true))
        }
    }

    override fun onStartCompose() {
        super.onStartCompose()
        //recover2ValidateRect()
    }

    override fun redrawOnPhotoRectUpdate() {
        redrawAllCache()
    }

    open fun onPastingDoubleClick(state: T) {

    }

    open fun drawPastingState(state: T, canvas: Canvas) {

    }


}

