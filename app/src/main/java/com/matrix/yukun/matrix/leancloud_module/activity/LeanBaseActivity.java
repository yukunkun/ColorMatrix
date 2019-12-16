package com.matrix.yukun.matrix.leancloud_module.activity;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.leancloud_module.entity.ContactInfo;

public class LeanBaseActivity extends BaseActivity {

    private ContactInfo mData;

    @Override
    public int getLayout() {
        return R.layout.activity_lean_base;
    }

    @Override
    public void initView() {
        mData = (ContactInfo) getIntent().getSerializableExtra("data");
    }
}
