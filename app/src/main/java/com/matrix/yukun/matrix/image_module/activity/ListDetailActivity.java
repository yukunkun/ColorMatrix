package com.matrix.yukun.matrix.image_module.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.BaseActivity;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.OnEventImage;
import com.matrix.yukun.matrix.image_module.adapter.GlideViewAdapter;
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.image_module.bean.EventList;
import com.matrix.yukun.matrix.setting_module.PhotoActivity;
import com.matrix.yukun.matrix.util.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ListDetailActivity extends AppCompatActivity {

    private ArrayList<String> lists;
    private GridView gridView;
    private TextView textView;
    private RelativeLayout layout;
    private GlideViewAdapter glideViewAdapter;
    private int mTag;
    private int fromWhitch;


    private void init() {
        gridView = (GridView) findViewById(R.id.grideview);
        textView = (TextView) findViewById(R.id.textviewshi);
        layout = (RelativeLayout) findViewById(R.id.deal);
        if(lists.size()==0){
            textView.setVisibility(View.VISIBLE);
        }
        Toast.makeText(ListDetailActivity.this, "长按可删除图片", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        lists = getIntent().getStringArrayListExtra("photo");
        mTag = getIntent().getIntExtra("tag",0);
        fromWhitch=getIntent().getIntExtra("whitch",0);
        init();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        glideViewAdapter = new GlideViewAdapter(getApplicationContext(),lists);
        gridView.setAdapter(glideViewAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(gridView);
    }

    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                View childAt = adapterView.getChildAt(position);
                if(lists.size()!=0){

                    final String detail = lists.get(position);
                    int nameCount = detail.lastIndexOf("/");
                    final String name=detail.substring(nameCount+1,detail.length());
                    if(fromWhitch==1){
                        //跳转到头像设置页
                        if(mTag==0){
                            layout.setVisibility(View.VISIBLE);
                            EventBus.getDefault().post(new OnEventImage(detail));
                            finish();
                        }

                    }else {
                        //直接返回到主页面
                        if(mTag==0){
                            layout.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        EventBus.getDefault().post(new EventDetail(detail,name));
                                        finish();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else if(mTag==1){
                            //设置页传递过来的,照片查看
                            Intent intent=new Intent(ListDetailActivity.this,PhotoActivity.class);
                            intent.putExtra("photoname",name);
                            intent.putExtra("photopath",detail);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&childAt!=null) {
                                ActivityOptions transitionActivityOptions = ActivityOptions
                                        .makeSceneTransitionAnimation(ListDetailActivity.this,childAt, "share");
                                startActivity(intent, transitionActivityOptions.toBundle());
                            }else {
                                startActivity(intent);
                            }
                        }
                    }
                    }

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setAlert(position);
                return true;
            }
        });
    }

    private void setAlert(final int position) {
        new AlertDialog.Builder(this).setMessage("确认删除吗?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean b = FileUtil.deleteFile(lists.get(position));
                        lists.remove(position);
                        glideViewAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void ListBack(View view) {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }
}
