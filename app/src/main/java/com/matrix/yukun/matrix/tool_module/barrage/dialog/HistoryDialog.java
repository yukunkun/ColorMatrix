package com.matrix.yukun.matrix.tool_module.barrage.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.tool_module.barrage.BarrageActivity;
import com.matrix.yukun.matrix.tool_module.barrage.BarrageHistory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * author: kun .
 * date:   On 2018/10/8
 */
public class HistoryDialog extends BaseBottomDialog implements View.OnClickListener {

    private ListView mListView;
    private ImageView mImageView;
    List<BarrageHistory> mBarrageHistories=new ArrayList();
    private LVAdapter mLvAdapter;

    public static HistoryDialog getInstance(){
        HistoryDialog settingDialog = new HistoryDialog();
        return settingDialog;
    }

    @Override
    public int setLayout() {
        return R.layout.history_dialog;
    }

    @Override
    protected void initView(View inflate, Bundle savedInstanceState) {
        mListView = inflate.findViewById(R.id.lv_list);
        mImageView = inflate.findViewById(R.id.iv_clear);
    }

    @Override
    protected void initData() {
        List<BarrageHistory> all = DataSupport.findAll(BarrageHistory.class);
        if(all!=null&&all.size()>0){
            mBarrageHistories.addAll(all);
        }
        mLvAdapter = new LVAdapter();
        mListView.setAdapter(mLvAdapter);
    }

    @Override
    protected void initListener() {
        mImageView.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BarrageActivity)getActivity()).getHistoryContent(mBarrageHistories.get(position).getContent());
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id== R.id.iv_clear){
            DataSupport.deleteAll(BarrageHistory.class);
            mBarrageHistories.clear();
            mLvAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((BarrageActivity)getActivity()).dialogDismiss();
    }

    class LVAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mBarrageHistories.size();
        }

        @Override
        public Object getItem(int position) {
            return mBarrageHistories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.history_item,null);
            TextView textView=convertView.findViewById(R.id.tv_content);
            textView.setText(mBarrageHistories.get(position).getContent());
            return convertView;
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            int height = ScreenUtils.instance().getHeight(getContext());
            params.dimAmount =0f;
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.height = height*2/5;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }
}
