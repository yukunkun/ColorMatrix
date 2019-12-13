package com.matrix.yukun.matrix.tool_module.videorecord;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.FileUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoFileActivity extends BaseActivity {

    List<String> fileName;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.tv_remind)
    TextView mTvRemind;
    private LVAdapter mLvAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_video_file;
    }

    @Override
    public void initView() {
        fileName = FileUtils.getFileName();
        Collections.reverse(fileName);
        if (fileName.size() > 0) {
            findViewById(R.id.tv_remind).setVisibility(View.GONE);
        }
        mLvAdapter = new LVAdapter(this, fileName);
        mListview.setAdapter(mLvAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (fileName.get(position).endsWith(".mp4")) {
                    Intent intent = new Intent(VideoFileActivity.this, VideoPlayActivity.class);
                    intent.putExtra("video_path", fileName.get(position));
                    intent.putExtra("video_cover", fileName.get(position));
                    intent.putExtra("video_title", "本地文件");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(VideoFileActivity.this, PhotoViewActivity.class);
                    intent.putExtra("imagepath", fileName.get(position));
                    startActivity(intent);
                }

            }
        });
        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(VideoFileActivity.this)
                        .setTitle("删除")
                        .setMessage("确定删除该文件吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                (new File(fileName.get(position))).delete();
                                fileName.remove(position);
                                mLvAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_delete, R.id.tv_remind})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_delete) {
            Toast.makeText(this, "长按可删除文件", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.iv_back) {
            finish();
        }
    }
}
