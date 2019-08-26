package com.matrix.yukun.matrix.download_module;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.download_module.bean.FileInfo;
import com.matrix.yukun.matrix.download_module.fragment.DownLoadFragment;
import com.matrix.yukun.matrix.download_module.fragment.DownLoadedFragment;
import com.matrix.yukun.matrix.download_module.service.DownLoadEngine;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DownLoadActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rg)
    RadioGroup mRadioGroup;
    @BindView(R.id.fl_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.iv_back)
    ImageView mImageView;
    @BindView(R.id.iv_more)
    ImageView mImageMore;
    int lastPos;
    List<Fragment> mFrameLayouts=new ArrayList<>();
    private DownLoadFragment mDownLoadFragment;
    private DownLoadedFragment mDownLoadedFragment;

    @Override
    public int getLayout() {
        return R.layout.activity_down_load;
    }

    @Override
    public void initView() {
        mDownLoadFragment = DownLoadFragment.getInstance();
        mDownLoadedFragment = DownLoadedFragment.getInstance();
        mFrameLayouts.add(mDownLoadFragment);
        mFrameLayouts.add(mDownLoadedFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, mDownLoadFragment).commit();
        ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(true);
    }

    @Override
    public void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_download) {
//                    ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(true);
//                    show(0);
//                    if(mDownLoadFragment.isError()){
//                        //异常了
//                        mImageMore.setVisibility(View.VISIBLE);
//                    }
                }else if(checkedId == R.id.rb_downloaded){
                    MyDownloadActivity.start(DownLoadActivity.this);
                    ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(true);
                }
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImageMore.setOnClickListener(this);
    }

    /**
     * fragment 的show和hide
     * @param pos
     */
    public void show(int pos) {
        Fragment fragment = mFrameLayouts.get(pos);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mFrameLayouts.get(lastPos));
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fl_layout, fragment);
        }
        fragmentTransaction.commit();
        lastPos = pos;
    }

    @Override
    public void initDate() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.iv_more){
            ShoeDialog();
        }
    }

    private void ShoeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ReDownload")
                .setMessage("是否重新下载全部文件")
                .setNegativeButton("否",null)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownLoadEngine.getInstance().getDownManager().removeAllDownload();
                        List<FileInfo> all = DataSupport.findAll(FileInfo.class);
                        for (int i = 0; i < all.size(); i++) {
                            DownLoadEngine.getInstance().getDownManager().addDownload(all.get(i).url,all.get(i).imageUrl);
                        }
                        DataSupport.deleteAll(FileInfo.class);
                    }
                })
                .show();
    }
}
