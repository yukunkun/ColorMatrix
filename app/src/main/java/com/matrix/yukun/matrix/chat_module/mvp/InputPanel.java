package com.matrix.yukun.matrix.chat_module.mvp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.adapter.ChatPictureAdapter;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.util.KeyBoardUtil;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * author: kun .
 * date:   On 2019/5/10
 */
public class InputPanel implements View.OnClickListener {
    public String TAG="InputPanel";
    private View mRootView;
    private InputListener mInputListener;
    private Context mContext;
    private EditText mEtMessage;
    private Button mBtSend;
    private Button mBtAdd;
    private ImageView mIvPicture;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRlSelectPicture;
    private LinearLayout mLlMoreFuction;
    private ImageView mIvVoice;
    private ImageView mIvEmoji;
    private LinearLayout mLlBottomView;
    private boolean isBottomShow;
    private int bottomHeight;
    private int MAX_IMAGE=50;
    private List<Photo> imgList = new ArrayList<>();
    private RecyclerView mRvSelectList;
    private TextView mTvEdit;
    private TextView mTvPhoto;
    private Button mBtImageSend;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatPictureAdapter mChatPictureAdapter;
    private List<Photo> imgSendList = new ArrayList<>();


    public InputPanel(Context context,View rootView, InputListener inputListener) {
        mRootView = rootView;
        mContext=context;
        mInputListener = inputListener;
        initView();
        initData();
        initListener();
    }


    private void initView() {
        mEtMessage = mRootView.findViewById(R.id.et_messg);
        mLlBottomView = mRootView.findViewById(R.id.ll_bottom);
        mBtSend = mRootView.findViewById(R.id.send_btn);
        mBtAdd = mRootView.findViewById(R.id.bt_add);
        mIvPicture = mRootView.findViewById(R.id.iv_picture);
        mIvEmoji = mRootView.findViewById(R.id.iv_emoji);
        mIvVoice = mRootView.findViewById(R.id.iv_voice);
        mFrameLayout = mRootView.findViewById(R.id.fl_contain);
        mRlSelectPicture = mRootView.findViewById(R.id.select_pic_view);
        mLlMoreFuction = mRootView.findViewById(R.id.ll_more_fuction);
        //select picture
        mRvSelectList = mRootView.findViewById(R.id.select_img_rv);
        mTvEdit = mRootView.findViewById(R.id.image_editor);
        mTvPhoto = mRootView.findViewById(R.id.image_album);
        mBtImageSend = mRootView.findViewById(R.id.btn_send_photo);

        //获取底部高度
        mLlBottomView.post(new Runnable() {
            @Override
            public void run() {
                bottomHeight=mLlBottomView.getHeight();
            }
        });
    }

    private void initData() {
        mLinearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        mRvSelectList.setLayoutManager(mLinearLayoutManager);
        mChatPictureAdapter = new ChatPictureAdapter(mContext,imgList);
        mRvSelectList.setAdapter(mChatPictureAdapter);
        mRvSelectList.addItemDecoration(new SpacesItemDecoration(5));
        mChatPictureAdapter.setOnItemClickListener(new ChatPictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if(imgList.get(pos).selected){
                    imgList.get(pos).selected=false;
                    imgSendList.remove(imgList.get(pos));
                }else {
                    if(imgSendList.size()>9){
                        ToastUtils.showToast("最多发送九张图片");
                        return;
                    }else {
                        imgList.get(pos).selected=true;
                        imgSendList.add(imgList.get(pos));
                    }
                }
                mChatPictureAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initListener() {
        mBtAdd.setOnClickListener(this);
        mBtSend.setOnClickListener(this);
        mIvEmoji.setOnClickListener(this);
        mIvVoice.setOnClickListener(this);
        mIvPicture.setOnClickListener(this);
        mBtImageSend.setOnClickListener(this);
        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    mBtAdd.setVisibility(View.VISIBLE);
                    mBtSend.setVisibility(View.GONE);
                }else {
                    mBtAdd.setVisibility(View.GONE);
                    mBtSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //回调监听
                    mInputListener.onSendMessageClick(mEtMessage.getText().toString());
                    /*隐藏软键盘*/
                    mEtMessage.setText("");
                    KeyBoardUtil.closeKeyboard(mContext,mEtMessage);
                    return true;
                } else
                    return true;
            }
        });
        mEtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtil.i("onFocusChange",hasFocus+"");
                if(hasFocus){
                    taggleToSend();
                }
            }
        });
        mEtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("onClick",""+isBottomShow);
                if(isBottomShow){
                    taggleToSend();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add :
                taggleToMore();
                break;
            case R.id.send_btn :
                mInputListener.onSendMessageClick(mEtMessage.getText().toString().trim());
                mEtMessage.setText("");
                KeyBoardUtil.closeKeyboard(mContext,mEtMessage);
                break;
            case R.id.iv_picture:
                taggleToPicture();
                showSelectPicture();
                break;
            case R.id.iv_emoji :
                taggleToEmoji();
                break;
            case R.id.iv_voice :
                taggleToVoice();
                break;
            case R.id.btn_send_photo :
                mInputListener.onPictureClick(imgSendList);
                taggleToSend();
                updateImageList();
                break;
                //相册
            case R.id.image_album :
                break;
                //编辑
            case R.id.image_editor :

                break;
        }
    }

    private void showSelectPicture() {

        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        String[] projections = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projections = new String[] {
                    MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.SIZE
            };
        }
        else {
            projections = new String[] {
                    MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE
            };
        }
        Cursor mCursor = (contentResolver).query(contentUri, projections, null, null, sortOrder);
        if (mCursor == null) {
           return;
        }
        // Take 100 images
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        if (mCursor.moveToFirst()) {
            do {
                long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
                Log.i(TAG, "MediaStore.Images.Media_ID=" + id + "");
                Photo info = new Photo();
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                int width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                int height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                Uri uri = ContentUris.withAppendedId(mediaUri, mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID)));
                info.setHeight(height);
                info.setWidth(width);
                info.setPath(path);
                info.setUri(uri);
                info.setName(mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
                info.setType(mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
                info.setSize(new File(path).length());
                imgList.add(info);
            }
            while (mCursor.moveToNext() && imgList.size() < MAX_IMAGE);
            mCursor.close();
            mChatPictureAdapter.notifyDataSetChanged();
        }
    }

    public void dismissLayout(){
        taggleToSend();
    }

    //清空图片集合
    private void updateImageList() {
        for (int i = 0; i < imgList.size(); i++) {
            imgList.get(i).selected=false;
        }
        imgSendList.clear();
        mChatPictureAdapter.notifyDataSetChanged();
    }

    private void taggleToSend(){
        dismissMore();
        dismissVoice();
        dismissEmoji();
        dismissPicture();
        if(isBottomShow){
            isBottomShow=false;
            mInputListener.onBottomMove(bottomHeight);
        }
    }

    private void taggleToPicture(){
        dismissMore();
        dismissVoice();
        dismissEmoji();
        showPicture();
        if(!isBottomShow){
            isBottomShow=true;
            mInputListener.onBottomMove(-mLlBottomView.getHeight());
        }
    }

    private void taggleToVoice(){
        dismissPicture();
        dismissEmoji();
        dismissMore();
        showVoice();
        if(!isBottomShow){
            mInputListener.onBottomMove(-bottomHeight);
            isBottomShow=true;
        }
    }

    private void taggleToMore(){
        dismissPicture();
        dismissVoice();
        dismissEmoji();
        showMore();
        if(!isBottomShow){
            mInputListener.onBottomMove(-bottomHeight);
            isBottomShow=true;
        }
    }
    private void taggleToEmoji(){
        dismissMore();
        dismissVoice();
        dismissPicture();
        showEmoji();
        if(!isBottomShow){
            mInputListener.onBottomMove(-bottomHeight);
            isBottomShow=true;
        }
    }

    private void dismissPicture(){
        mRlSelectPicture.setVisibility(View.GONE);
    }

    private void dismissVoice(){
        mFrameLayout.setVisibility(View.GONE);
    }

    private void dismissEmoji(){
        mFrameLayout.setVisibility(View.GONE);
    }

    private void dismissMore(){
        mLlMoreFuction.setVisibility(View.GONE);
    }

    private void showPicture(){
        mRlSelectPicture.setVisibility(View.VISIBLE);
    }

    private void showVoice(){
        mFrameLayout.setVisibility(View.VISIBLE);
    }

    private void showEmoji(){
        mFrameLayout.setVisibility(View.VISIBLE);
    }

    private void showMore(){
        mLlMoreFuction.setVisibility(View.VISIBLE);
    }
}
