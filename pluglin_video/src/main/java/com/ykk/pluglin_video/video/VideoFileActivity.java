package com.ykk.pluglin_video.video;


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

import com.ykk.pluglin_video.BaseActivity;
import com.ykk.pluglin_video.R;
import com.ykk.pluglin_video.R2;
import com.ykk.pluglin_video.utils.FileUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoFileActivity extends BaseActivity {

    List<String> fileName;
    @BindView(R2.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R2.id.listview)
    ListView mListview;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_delete)
    TextView mTvDelete;
    @BindView(R2.id.tv_remind)
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
                    intent.putExtra("imagepath", fileName.get(position)+"#本地文件");
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

    @OnClick({R2.id.iv_back,R2.id.tv_delete, R2.id.tv_remind})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_delete) {
            Toast.makeText(this, "长按可删除文件", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.tv_remind) {
        } else if (i == R.id.iv_back) {
            finish();

        }
    }
}
