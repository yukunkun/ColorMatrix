package com.matrix.yukun.matrix.main_module.activity;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.fragment.ToolFragment;

/**
 * 简版
 */
public class BriefVersionActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_brief_version;
    }

    @Override
    public void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fl, ToolFragment.getInstance()).commit();
    }
}
