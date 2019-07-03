package com.matrix.yukun.matrix.chat_module.mvp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imageeditor.ImageEditorActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.ChatBaseActivity;
import com.matrix.yukun.matrix.chat_module.adapter.ChatPictureAdapter;
import com.matrix.yukun.matrix.chat_module.emoji.CubeEmoticonEditText;
import com.matrix.yukun.matrix.chat_module.entity.Photo;
import com.matrix.yukun.matrix.chat_module.fragment.emoji.EmojiFragment;
import com.matrix.yukun.matrix.chat_module.fragment.emoji.EmojiPreFragment;
import com.matrix.yukun.matrix.chat_module.fragment.more.ChatToolFragment;
import com.matrix.yukun.matrix.chat_module.fragment.voice.RecordFragment;
import com.matrix.yukun.matrix.chat_module.inputListener.InputListener;
import com.matrix.yukun.matrix.AppConstant;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.KeyBoardUtil;
import com.matrix.yukun.matrix.util.SpacesItemDecoration;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;
import com.miracle.view.imageeditor.bean.EditorSetup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * author: kun .
 * date:   On 2019/5/10
 */
public class InputPanel implements View.OnClickListener, EmojiPreFragment.OnEmojiClickListener {
    public String TAG = "InputPanel";
    private View mRootView;
    private InputListener mInputListener;
    private Context mContext;
    private CubeEmoticonEditText mEtMessage;
    private Button mBtSend;
    private Button mBtAdd;
    private ImageView mIvPicture;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRlSelectPicture;
    private ImageView mIvVoice;
    private ImageView mIvEmoji;
    private int MAX_IMAGE = 50;
    private List<Photo> imgList = new ArrayList<>();
    private RecyclerView mRvSelectList;
    private TextView mTvEdit;
    private TextView mTvPhoto;
    private Button mBtImageSend;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatPictureAdapter mChatPictureAdapter;
    private List<Photo> imgSendList = new ArrayList<>();
    public final static int ACTION_REQUEST_CAMERA = 1;
    public final static int ACTION_REQUEST_IMAGE = 2;
    public final static int ACTION_REQUEST_EDITOR = 3;
    public final static int ACTION_REQUEST_VIDEO = 4;
    public final static int ACTION_REQUEST_FILE = 5;
    public static String cameraSavePath;
    private ImageView mIvVideo;
    private CheckBox mCheckBox;

    public InputPanel(Context context, View rootView, InputListener inputListener) {
        mRootView = rootView;
        mContext = context;
        mInputListener = inputListener;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mEtMessage = mRootView.findViewById(R.id.et_messg);
        mBtSend = mRootView.findViewById(R.id.send_btn);
        mBtAdd = mRootView.findViewById(R.id.bt_add);
        mIvPicture = mRootView.findViewById(R.id.iv_picture);
        mIvEmoji = mRootView.findViewById(R.id.iv_emoji);
        mIvVoice = mRootView.findViewById(R.id.iv_voice);
        mIvVideo = mRootView.findViewById(R.id.iv_video);
        mFrameLayout = mRootView.findViewById(R.id.fl_contain);
        mRlSelectPicture = mRootView.findViewById(R.id.select_pic_view);
        //select picture
        mRvSelectList = mRootView.findViewById(R.id.select_img_rv);
        mTvEdit = mRootView.findViewById(R.id.image_editor);
        mTvPhoto = mRootView.findViewById(R.id.image_album);
        mBtImageSend = mRootView.findViewById(R.id.btn_send_photo);
        mCheckBox = mRootView.findViewById(R.id.cb_origin);
    }

    private void initData() {
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRvSelectList.setLayoutManager(mLinearLayoutManager);
        mChatPictureAdapter = new ChatPictureAdapter(mContext, imgList);
        mRvSelectList.setAdapter(mChatPictureAdapter);
        mRvSelectList.addItemDecoration(new SpacesItemDecoration(5));
        mChatPictureAdapter.setOnItemClickListener(new ChatPictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if (imgList.get(pos).selected) {
                    imgList.get(pos).selected = false;
                    imgSendList.remove(imgList.get(pos));
                } else {
                    if (imgSendList.size() >= 9) {
                        ToastUtils.showToast("最多发送九张图片");
                        return;
                    } else {
                        imgList.get(pos).selected = true;
                        imgSendList.add(imgList.get(pos));
                    }
                }
                updateCheck();
                if (imgSendList.size() == 1) {
                    mTvEdit.setEnabled(true);
                    mTvEdit.setTextColor(mContext.getResources().getColor(R.color.C2));
                } else {
                    mTvEdit.setEnabled(false);
                    mTvEdit.setTextColor(mContext.getResources().getColor(R.color.C3));
                }
                mChatPictureAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onEmojClickListener(String key){
        this.mEtMessage.requestFocus();
        Editable editable = this.mEtMessage.getText();
        if (key.equals("/DEL")) {
            this.mEtMessage.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        }
        else {
            int start = this.mEtMessage.getSelectionStart();
            int end = this.mEtMessage.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (end < 0 ? 0 : end);
            editable.replace(start, end, key);
        }
        this.mEtMessage.requestFocus();
    };

    private void initListener() {
        mBtAdd.setOnClickListener(this);
        mBtSend.setOnClickListener(this);
        mIvEmoji.setOnClickListener(this);
        mIvVoice.setOnClickListener(this);
        mIvPicture.setOnClickListener(this);
        mBtImageSend.setOnClickListener(this);
        mTvEdit.setOnClickListener(this);
        mTvPhoto.setOnClickListener(this);
        mIvVideo.setOnClickListener(this);
        EmojiPreFragment.setOnEmojiClickListener(this);
        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    mBtAdd.setVisibility(View.VISIBLE);
                    mBtSend.setVisibility(View.GONE);
                } else {
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
                    KeyBoardUtil.closeKeyboard(mContext, mEtMessage);
                    return true;
                } else
                    return true;
            }
        });
        mEtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taggleToSend();
            }
        });
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    updateCheck();
                }else {
                    mCheckBox.setText("原图");
                }
            }
        });
    }

    private void updateCheck() {
        if(mCheckBox.isChecked()){
            long size=0;
            for (int i = 0; i < imgSendList.size(); i++) {
                File file=new File(imgSendList.get(i).path);
                size+=size+file.length();
            }
            mCheckBox.setText("原图 "+ FileUtil.formatFileSize(size));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                taggleToMore();
                break;
            case R.id.send_btn:
                mInputListener.onSendMessageClick(mEtMessage.getText().toString().trim());
                mEtMessage.setText("");
//                KeyBoardUtil.closeKeyboard(mContext, mEtMessage);
//                dismissLayout();
                break;
            case R.id.iv_picture:
                taggleToPicture();
                showSelectPicture();
                break;
            case R.id.iv_video:
                openVideo();
                break;
            case R.id.iv_emoji:
                taggleToEmoji();
                break;
            case R.id.iv_voice:
                taggleToVoice();
                break;
            case R.id.btn_send_photo:
                mInputListener.onPictureClick(imgSendList);
                taggleToSend();
                updateImageList();
                break;
            //相册
            case R.id.image_album:
                openPhoto();
                break;
            //编辑
            case R.id.image_editor:
                File file = new File(imgSendList.get(0).path);
                EditorSetup setup = new EditorSetup(file.getPath(), file.getPath(), AppConstant.IMAGEPATH + "/" + file.getName(), true);
                Intent intent = ImageEditorActivity.Companion.intent(mContext, setup);
                ((Activity) mContext).startActivityForResult(intent, ACTION_REQUEST_EDITOR);
                break;
        }
    }

    private void openVideo() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("video/*");
        ((Activity) mContext).startActivityForResult(intent, InputPanel.ACTION_REQUEST_VIDEO);
    }

    private void openPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity) mContext).startActivityForResult(intent, InputPanel.ACTION_REQUEST_IMAGE);
    }

    private void showSelectPicture() {
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        String[] projections = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projections = new String[]{
                    MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.SIZE
            };
        } else {
            projections = new String[]{
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

    public void dismissLayout() {
        taggleToSend();
    }

    //清空图片集合
    private void updateImageList() {
        for (int i = 0; i < imgList.size(); i++) {
            imgList.get(i).selected = false;
        }
        imgSendList.clear();
        mChatPictureAdapter.notifyDataSetChanged();
    }

    private void taggleToSend() {
        dismissMore();
        dismissVoice();
        dismissEmoji();
        dismissPicture();
    }

    private void taggleToPicture() {
        dismissMore();
        dismissVoice();
        dismissEmoji();
        showPicture();
    }

    private void taggleToVoice() {
        dismissPicture();
        dismissEmoji();
        dismissMore();
        showVoice();

    }

    private void taggleToMore() {
        dismissPicture();
        dismissVoice();
        dismissEmoji();
        showMore();
    }

    private void taggleToEmoji() {
        dismissMore();
        dismissVoice();
        dismissPicture();
        showEmoji();
    }

    private void dismissPicture() {
        mRlSelectPicture.setVisibility(View.GONE);
    }

    private void dismissVoice() {
        mFrameLayout.setVisibility(View.GONE);
    }

    private void dismissEmoji() {
        mFrameLayout.setVisibility(View.GONE);
    }

    private void dismissMore() {
        mFrameLayout.setVisibility(View.GONE);
    }

    private void showPicture() {
        mRlSelectPicture.setVisibility(View.VISIBLE);
    }

    private void showVoice() {
        mFrameLayout.setVisibility(View.VISIBLE);
        (((ChatBaseActivity) mContext).getSupportFragmentManager()).popBackStack();
        (((ChatBaseActivity) mContext).getSupportFragmentManager()).beginTransaction().replace(R.id.fl_contain, new RecordFragment(mRootView, (Activity) mContext)).commit();
    }

    private void showEmoji() {
        mFrameLayout.setVisibility(View.VISIBLE);
        (((ChatBaseActivity) mContext).getSupportFragmentManager()).popBackStack();
        (((ChatBaseActivity) mContext).getSupportFragmentManager()).beginTransaction().replace(R.id.fl_contain, new EmojiFragment(mRootView, (Activity) mContext)).commit();
    }

    private void showMore() {
        mFrameLayout.setVisibility(View.VISIBLE);
        cameraSavePath = AppConstant.IMAGEPATH + "/yk_" + System.currentTimeMillis() + ".png";
        (((ChatBaseActivity) mContext).getSupportFragmentManager()).popBackStack();
        (((ChatBaseActivity) mContext).getSupportFragmentManager()).beginTransaction().replace(R.id.fl_contain, ChatToolFragment.getInstance(mContext, cameraSavePath)).commit();
    }
}
