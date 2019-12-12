package com.imageeditor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.imageeditor.view.bottomPopupDialog.BottomPopupDialog
import com.miracle.view.imageeditor.R
import com.miracle.view.imageeditor.bean.CropSaveState
import com.miracle.view.imageeditor.bean.EditorCacheData
import com.miracle.view.imageeditor.bean.EditorResult
import com.miracle.view.imageeditor.bean.EditorSetup
import com.miracle.view.imageeditor.layer.BaseLayerView
import com.miracle.view.imageeditor.layer.LayerCacheNode
import com.miracle.view.imageeditor.layer.LayerComposite
import com.miracle.view.imageeditor.layer.RootEditorDelegate
import com.miracle.view.imageeditor.view.*
import kotlinx.android.synthetic.main.action_bar_editor.*
import kotlinx.android.synthetic.main.activity_image_editor.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.Executors


/**
 * ## Entrance of image editor,delegate for all ui elements and also imageCompose func
 * Simple code to start:
 *```
 *  val setup = EditorSetup(source, mOriginalPath, getEditorSavePath())
 *  val intent = ImageEditorActivity.intent(this, setup)
 *  startActivityForResult(intent, ACTION_REQUEST_EDITOR)
 *```
 * Created by lxw
 */
class ImageEditorActivity : AppCompatActivity(), LayerViewProvider {
    private lateinit var mRootEditorDelegate: RootEditorDelegate
    private lateinit var mFuncAndActionBarAnimHelper: FuncAndActionBarAnimHelper
    private lateinit var mFuncHelper: FuncHelper
    private lateinit var mCropHelper: CropHelper
    private lateinit var mEditorSetup: EditorSetup
    private lateinit var mEditorId: String
    private lateinit var mEditorPath: String
    private var mEditorWidth: Int = 0
    private var mEditorHeight: Int = 0
    internal var isSave = false//当前编辑模式 默认为true，为编辑字体颜色(此时关闭背景色)，false为编辑背景颜色

    companion object {
        private val intentKey = "editorSetup"
        private val RESULT_OK_CODE = Activity.RESULT_OK
        private val RESULT_CANCEL_CODE = Activity.RESULT_CANCELED

        fun intent(context: Context, editorSetup: EditorSetup): Intent {
            val intent = Intent(context, ImageEditorActivity::class.java)
            intent.putExtra(intentKey, editorSetup)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initRootView()
        initData()
        initView()
        initActionBarListener()
    }

    private fun initRootView() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.window.statusBarColor = Utils.getResourceColor(this, R.color.bg_black);
        }
        //transparent necessary
        window.setBackgroundDrawableResource(R.color.transparent)
        //flag necessary
        if (Build.VERSION.SDK_INT >= 19) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        val mBackgroundColor = ColorDrawable(Color.BLACK)

        val view = View.inflate(this, R.layout.activity_image_editor, null)
        if (Build.VERSION.SDK_INT >= 16) {
            view.background = mBackgroundColor
        } else {
            view.setBackgroundDrawable(mBackgroundColor)
        }
        setContentView(view)
    }

    private fun initData() {
        val intent = intent
        intent ?: let {
            finish()
            return
        }
        val editorSetup = intent.getSerializableExtra(intentKey) as? EditorSetup
        editorSetup ?: let {
            logD1("editorSetup=null")
            finish()
            return
        }
        mEditorSetup = editorSetup
        val op = mEditorSetup.originalPath
        val ep = mEditorSetup.editorPath
        if (op == null && ep == null) {
            logD1("originalPath,editorPath are both null")
            finish()
            return
        }
    }

    /*init for different kinds of EditorMode usage in java code...<xml code viewStub...>*/
    private fun initView() {
        // EditorMode.StickerMode,贴纸表请，无需求暂时屏蔽
        val functionalModeList = listOf(EditorMode.ScrawlMode, EditorMode.TextPastingMode, EditorMode.MosaicMode, EditorMode.CropMode)
        val toolFragment = FuncModeToolFragment.StaticDelegate.newInstance(functionalModeList)
        supportFragmentManager.beginTransaction().add(R.id.flFunc, toolFragment).commit()
        mRootEditorDelegate = RootEditorDelegate(layerImageView, layerEditorParent)
        mFuncAndActionBarAnimHelper = FuncAndActionBarAnimHelper(layerActionView, editorBar, flFunc, this)
        mCropHelper = CropHelper(layerCropView, CropDetailsView(layoutCropDetails), this)
        mFuncHelper = FuncHelper(this, DragToDeleteView(layoutDragDelete))
        toolFragment.addFuncModeListener(mFuncHelper)
        toolFragment.addFuncModeDetailsListener(mFuncHelper)
        toolFragment.addOnRevokeListener(mFuncHelper)
        //restore
        restoreData()
    }

    /**
     * restore data for redraw all cache ,and undo or other func
     * it's important to get right editor path and editorId...
     */
    private fun restoreData() {
        val op = mEditorSetup.originalPath
        val ep = mEditorSetup.editorPath
        var cacheData: MutableMap<String, EditorCacheData>? = null
        if (op != null) {
            mEditorId = op + (ep ?: "")
            cacheData = LayerCache.getCacheDataById(mEditorId)
        }
        if ((cacheData == null || cacheData.isEmpty()) && ep != null) {
            mEditorId = (op ?: "") + ep
            cacheData = LayerCache.getCacheDataById(mEditorId)
            //set up layer cache with ep...
            mEditorPath = ep
        } else {
            //op has extra data or not
            mEditorPath = op!!
        }
        logD1("edtiorId=${mEditorId},editorPath=${mEditorPath},init cached data=$cacheData")
        if (!File(mEditorPath).exists()) {
            toastShort("文件不存在！")
            finish()
            return
        }
        val imageBitmap = EditorCompressUtils.getImageBitmap(mEditorPath)
        mCropHelper.restoreLayerData(cacheData!!)
        val cropBitmap = mCropHelper.restoreCropData(imageBitmap)
        layerImageView.setImageBitmap(cropBitmap)
        val cropState = mCropHelper.getSavedCropState()
        layerImageView.addOnLayoutChangeListener(LayerImageOnLayoutChangeListener(cropState))
//        Executors.newSingleThreadExecutor().execute {
        val data = cacheData[layerMosaicView.getLayerTag()]
        data?.let {
            layerMosaicView.setupForMosaicView(imageBitmap)
        }
        Utils.callChildren(LayerCacheNode::class.java, layerComposite) {
            it.restoreLayerData(cacheData!!)
        }
        data ?: Executors.newSingleThreadExecutor().execute {
            layerMosaicView.setupForMosaicView(imageBitmap)
        }
        //    }
        mEditorWidth = imageBitmap.width;
        mEditorHeight = imageBitmap.height;
    }

    /*for initialize mosaic view's matrix*/
    inner class LayerImageOnLayoutChangeListener(val state: CropSaveState?) : android.view.View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            val matrix = state?.originalMatrix ?: layerImageView.getBaseLayoutMatrix()
            state?.let {
                mCropHelper.resetEditorSupportMatrix(it)
            }
            layerMosaicView.initializeMatrix = matrix
            layerImageView.removeOnLayoutChangeListener(this)
        }

    }

    private fun initActionBarListener() {
        ivBack.setOnClickListener {
            onImageComposeCancel()
        }
        tvComplete.setOnClickListener {
            if(mEditorSetup.isP2P){
                saveBmp2Gallery()
//                showBottomSheet()
            }else{
                imageCompose()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFuncHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun findLayerByEditorMode(editorMode: EditorMode): View? {
        when (editorMode) {
            EditorMode.ScrawlMode -> return layerScrawlView
            EditorMode.StickerMode -> return layerStickerView
            EditorMode.TextPastingMode -> return layerTextPastingView
            EditorMode.MosaicMode -> return layerMosaicView
            EditorMode.CropMode -> return layerCropView
            else -> return null
        }
    }

    override fun getActivityContext(): Context {
        return this
    }

    override fun onBackPressed() {
        if (!mFuncHelper.onBackPress()) {
            super.onBackPressed()
        }
    }

    override fun getEditorSizeInfo(): Pair<Int, Int> {
        return Pair(mEditorWidth, mEditorHeight)
    }

    override fun getScreenSizeInfo(): Pair<Int, Int> {
        return Pair(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun getFuncAndActionBarAnimHelper(): FuncAndActionBarAnimHelper = mFuncAndActionBarAnimHelper

    override fun getCropHelper(): CropHelper = mCropHelper

    override fun getRootEditorDelegate(): RootEditorDelegate = mRootEditorDelegate

    override fun getLayerCompositeView(): LayerComposite = layerComposite

    override fun getSetupEditorId() = mEditorId

    override fun getResultEditorId() = mEditorPath + mEditorSetup.editor2SavedPath

    private var imageComposeTask: ImageComposeTask? = null

    private fun imageCompose() {
        val path = mEditorSetup.editor2SavedPath
        val parentFile = File(path).parentFile
        parentFile?.mkdirs()
        imageComposeTask?.cancel(true)
        imageComposeTask = ImageComposeTask(this)
        imageComposeTask?.execute(path)
    }

    /**
     * image compose cancel
     */
    private fun onImageComposeCancel() {
        supportRecycle()
        val intent = Intent()
        setResult(RESULT_CANCEL_CODE, intent)
        finish()
    }

    /**
     * image compose result success or fail
     */
    private fun onImageComposeResult(editStatus: Boolean) {
        supportRecycle()
        val intent = Intent()
        val resultData = EditorResult(mEditorPath, mEditorSetup.editorPath, mEditorSetup.editor2SavedPath, editStatus,isSave)
        intent.putExtra(RESULT_OK_CODE.toString(), resultData)
        setResult(RESULT_OK_CODE, intent)
        finish()
    }

    private fun supportRecycle() {
        recycleBitmap(mRootEditorDelegate.getDisplayBitmap())
        mCropHelper.getSavedCropState()?.reset()
    }

    /**
     * AsyncTask for image Compose
     */
    inner class ImageComposeTask(private val mProvider: LayerViewProvider) : AsyncTask<String, Void, Boolean>() {
        private var mDialog = ProgressDialog(mProvider.getActivityContext())
        private var mPath: String? = null
        private val layerComposite = mProvider.getLayerCompositeView()
        private val mEditorId = mProvider.getResultEditorId()

        init {
            mDialog.isIndeterminate = true
            mDialog.setCancelable(false)
            mDialog.setCanceledOnTouchOutside(false)
            mDialog.setMessage(Utils.getResourceString(mProvider.getActivityContext(), R.string.editor_handle))

        }

        override fun doInBackground(vararg params: String): Boolean {
            mPath = params[0]
            val cropState = mProvider.getCropHelper().getSavedCropState()
            val delegate = mProvider.getRootEditorDelegate()
            //draw image data layer by layer
            val rootBit = cropState?.cropBitmap ?: delegate.getDisplayBitmap()
            val compose = Bitmap.createBitmap(layerComposite.width, layerComposite.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(compose)
            canvas.drawBitmap(rootBit, delegate.getBaseLayoutMatrix(), null)
            Utils.callChildren(BaseLayerView::class.java, layerComposite) {
                val (supportMatrix, bitmap) = it.getEditorResult()
                bitmap?.let {
                    val matrix = Matrix()
                    matrix.set(supportMatrix)
                    canvas.drawBitmap(bitmap, matrix, null)
                }
            }
            val rect = delegate.getOriginalRect()!!
            val result = Bitmap.createBitmap(compose, rect.left.toInt(), rect.top.toInt(), rect.width().toInt(), rect.height().toInt())
            result.compress(Bitmap.CompressFormat.JPEG, 85, File(mPath).outputStream())
            recycleBitmap(compose)
            recycleBitmap(result)
            recycleBitmap(rootBit)
            //Save cached data.
            val cacheData = LayerCache.getCacheDataById(mEditorId)
            Utils.callChildren(BaseLayerView::class.java, layerComposite) {
                it.saveLayerData(cacheData)
            }
            mProvider.getCropHelper().saveLayerData(cacheData)
            return true
        }

        override fun onPreExecute() {
            super.onPreExecute()
//            Utils.callChildren(BaseLayerView::class.java, layerComposite) {
//                it.onStartCompose()
//            }
            mDialog.show()
        }

        override fun onCancelled() {
            super.onCancelled()
            mDialog.dismiss()
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            logD1("ImageComposeTask:result=$mPath")
            //this@ImageEditorActivity.toastShort( "合成图片完成")
            mDialog.dismiss()
            onImageComposeResult(result)
        }

    }

    /**
     * 显示底部弹出框
     */
    private fun showBottomSheet() {
        val bottomDialogContents = ArrayList<String>()
        bottomDialogContents.add("保存到相册")
        val bottomPopupDialog = BottomPopupDialog(this, bottomDialogContents)
        bottomPopupDialog.showCancelBtn(true)
        bottomPopupDialog.show()
        bottomPopupDialog.setCancelable(true)
        bottomPopupDialog.setOnItemClickListener { v, position ->
            bottomPopupDialog.dismiss()
            if (position == 0) {    //保存到相册
                isSave =true
                saveBmp2Gallery()
            } else if (position == 1) {//发送给好友
                isSave =false
                imageCompose()
            }
        }
        bottomPopupDialog.setOnDismissListener({ dialog -> dialog.dismiss() })
    }


    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    fun saveBmp2Gallery() {
        val path = mEditorSetup.editor2SavedPath
        val parentFile = File(path).parentFile
        parentFile?.mkdirs()
        imageComposeTask?.cancel(true)
        imageComposeTask = ImageComposeTask(this)
        imageComposeTask?.execute(path)
    }
}
