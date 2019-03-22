package com.matrix.yukun.matrix.video_module.play;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.video_module.BaseActivity;
import com.matrix.yukun.matrix.video_module.fragment.ToolFragment;

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
