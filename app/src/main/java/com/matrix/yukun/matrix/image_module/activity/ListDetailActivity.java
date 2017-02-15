package com.matrix.yukun.matrix.image_module.activity;

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
import com.matrix.yukun.matrix.image_module.adapter.GlideViewAdapter;
import com.matrix.yukun.matrix.image_module.bean.EventDetail;
import com.matrix.yukun.matrix.image_module.bean.EventList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ListDetailActivity extends BaseActivity {

    private ArrayList<String> lists;
    private GridView gridView;
    private TextView textView;
    private RelativeLayout layout;

    private void init() {
        gridView = (GridView) findViewById(R.id.grideview);
        textView = (TextView) findViewById(R.id.textviewshi);
        layout = (RelativeLayout) findViewById(R.id.deal);
        if(lists.size()==0){
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        lists = getIntent().getStringArrayListExtra("photo");
        init();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        GlideViewAdapter glideViewAdapter=new GlideViewAdapter(getApplicationContext(),lists);
        gridView.setAdapter(glideViewAdapter);
    }
    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                layout.setVisibility(View.VISIBLE);
                if(lists.size()!=0){
                    final String detail = lists.get(position);
                    int nameCount = detail.lastIndexOf("/");
                    final String name=detail.substring(nameCount+1,detail.length());
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

                }
            }
        });
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
