package com.matrix.yukun.matrix.tool_module.btmovie;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.tool_module.btmovie.activity.DownLoadActivity;
import com.matrix.yukun.matrix.tool_module.btmovie.fragment.SecondFragment;

import java.util.ArrayList;
import java.util.List;

public class SpecialActivity extends BaseActivity {

    private ImageView mIvBack;
    List<Fragment> mFragments = new ArrayList<>();
    private RadioGroup mRadioGroup;
    private int lastPos=0;
    private ImageView mIvMain;

    @Override
    public int getLayout() {
        return R.layout.activity_special;
    }

    @Override
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvMain = (ImageView) findViewById(R.id.iv_main);
        mRadioGroup = (RadioGroup)findViewById(R.id.rg_layout);
    }

    @Override
    public void initDate() {

        SecondFragment secondFragment= SecondFragment.newInstance();
        mFragments.add(secondFragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_layout, secondFragment);
        fragmentTransaction.commit();
        ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(true);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SpecialActivity.this, DownLoadActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initListener() {
//        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.rb_left) {
//                    ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(true);
//                    show(0);
//
//                } else if (checkedId == R.id.rb_center) {
//                    ((RadioButton) (mRadioGroup.getChildAt(1))).setChecked(true);
//                    show(1);
//
//                } else if (checkedId == R.id.rb_right) {
//                    ((RadioButton) (mRadioGroup.getChildAt(2))).setChecked(true);
//                    show(2);
//                }
//                else if (checkedId == R.id.rb_four) {
//                    ((RadioButton) (mRadioGroup.getChildAt(3))).setChecked(true);
//                    show(3);
//                }
//            }
//        });
    }

    /**
     * fragment 的show和hide
     * @param pos
     */
    public void show(int pos) {
        Fragment fragment = mFragments.get(pos);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mFragments.get(lastPos));

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fl_layout, fragment);
        }
        fragmentTransaction.commit();
        lastPos = pos;
    }
}
