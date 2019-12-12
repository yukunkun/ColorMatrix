package com.imageeditor.view.bottomPopupDialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.miracle.view.imageeditor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhenxin  on 2017/8/4.
 *
 * 在原有基础上修改为BottomSheetDialog，可以进行视图弹出下拉
 *
 * 解决6.0以上状态栏为黑色的问题  二次弹出不显示问题
 */

public class BottomPopupDialog extends BottomSheetDialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Context mContext;
    private ListView mListView; // 弹出listView
    private Button mCancelBtn;   // 取消按钮

    private View mHeaderView;   // ListView头部视图
    private TextView mTitleTextView;    // 标题视图

    private BottomPopupListAdapter mAdapter;
    private List<String> mItemData; // 条目数据

    private boolean hasHeaderView = false;    // 是否有头部视图

    private List<Integer> mRedPositions;//需要渲染成红色的position的集合
    private List<Integer> mPutTheAshPositions;//需要置灰不可点击的position的集合

    private OnItemClickListener listener;
    private OnDismissListener   dismissListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDismissListener(OnDismissListener dismissListener){
        this.dismissListener = dismissListener;
    }

    /**
     * 构造方法
     *
     * @param mContext
     * @param itemData
     */
    public BottomPopupDialog(Context mContext, List<String> itemData) {
        super(mContext, R.style.BottomPopupDialogStyle);
        setContentView(R.layout.bottom_popup_dialog);
        this.mItemData = itemData;
        this.mContext = mContext;
        initDialog();
        initView();
    }
    /**
     * 重写onCreate方法  进行屏幕高度  -  导航栏高度 =  dialogHeight
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBehaviorCallback();
        int screenHeight = getScreenHeight((Activity) mContext);
        int statusBarHeight = getStatusBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     *
     * @return
     */
    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    //获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 解决dialog打开→关闭后，不能再打开的问题
     */
    private void setBehaviorCallback() {
        View view = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    /**
     * 设置点击外部是否可取消
     * @param cancelable
     */
    public void setDialogCancelable(boolean cancelable){
        setCancelable(cancelable);
    }

    /**
     * 设置某位置文字为红色
     */
    public void setRedPosition(int position) {
        if (mRedPositions == null) {
            mRedPositions = new ArrayList<>();
        }
        this.mRedPositions.add(position);
    }

    /**
     * 清除文字颜色
     */
    public void ClearRedPosition() {
        if (mRedPositions != null) {
            this.mRedPositions.clear();
        }
    }

    /**
     * 设置某位置文字为灰色
     */
    public void setPutTheAshPosition(int position) {
        if (mPutTheAshPositions == null) {
            mPutTheAshPositions = new ArrayList<>();
        }
        this.mPutTheAshPositions.add(position);
    }

    /**
     * 清除文字颜色
     */
    public void ClearPutTheAshPosition() {
        if (mPutTheAshPositions != null) {
            this.mPutTheAshPositions.clear();
        }
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        getWindow().getAttributes().width = mContext.getResources().getDisplayMetrics().widthPixels;
        getWindow().getAttributes().gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mCancelBtn = (Button) findViewById(R.id.cancel_btn);

        mAdapter = new BottomPopupListAdapter(mContext, mItemData);

        mListView.setOnItemClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mCancelBtn.setVisibility(View.GONE);
    }

    /**
     * 是否呈现取消按钮
     *
     * @param isShow
     */
    public void showCancelBtn(boolean isShow) {
        mCancelBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    public void setData(List<String> mData) {
        //        mListView.getLayoutParams().height = mData.size() > 3 ? DpUtils.dpToPx(mContext, 180) : LinearLayout.LayoutParams.WRAP_CONTENT;
        mAdapter.setData(mData);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.cancel_btn) {
            BottomPopupDialog.this.dismiss();
            if(null != dismissListener){
                dismissListener.onDismiss();
            }
        }
    }

    /**
     * 弹出框条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null) {
            if(mPutTheAshPositions != null && mPutTheAshPositions.contains(i)){
                return;
            }
            if (this.hasHeaderView) {
                listener.onItemClick(view, i - 1);
            }
            else {
                listener.onItemClick(view, i);
            }
            BottomPopupDialog.this.dismiss();
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * 弹出框条目点击事件监听器
     */
    public interface OnItemClickListener {
        void onItemClick(View v, final int position);
    }

    /**
     * 设置标题
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        if (null != titleText && !"".equals(titleText)) {
            this.addListHeaderView();
            this.mTitleTextView.setText(titleText);
        }
    }

    /**
     * 设置标题颜色
     *
     * @param titleColor
     */
    public void setTitleColor(int titleColor) {
        if (null != this.mTitleTextView) {
            this.mTitleTextView.setTextColor(mContext.getResources().getColor(titleColor));
        }
    }

    /**
     * 设置标题大小
     *
     * @param titleSize
     */
    public void setTitleSize(float titleSize) {
        if (null != this.mTitleTextView) {
            this.mTitleTextView.setTextSize(titleSize);
        }
    }

    /**
     * 添加头部视图
     */
    private void addListHeaderView() {
        if (mHeaderView == null) {
            this.mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.bottom_popup_dialog_title, null);
            this.mListView.addHeaderView(this.mHeaderView, null, false);
        }
        this.mTitleTextView = (TextView) this.mHeaderView.findViewById(R.id.title_tv);

        // 标记为有头部视图
        this.hasHeaderView = true;
    }

    @Override
    public void show() {
        mAdapter.setRedPositions(mRedPositions);
        mAdapter.setPutTheAshPositions(mPutTheAshPositions);
        mListView.setAdapter(mAdapter);
        super.show();
    }
}
