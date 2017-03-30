package com.le.skin.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.letv.recorder.bean.CameraParams;
import com.letv.recorder.letvrecorderskin.R;
public class FilterListView {
    private FilterListListener listListener;
    private FrameLayout firstView;
    private FrameLayout currentView;
    private Context mContext;
    private RelativeLayout filterLayout;
    private RelativeLayout viewGroup;
    private LayoutInflater inflater;
    private FrameLayout filterRoot;
    private ImageView imageView;
    private ImageView cancle;
    private ImageView save;
    private final static int[] imagefilters = new int[]{R.drawable.filter_thumb_original,R.drawable.filter_thumb_beautyskin,R.drawable.filter_thumb_romance,R.drawable.filter_thumb_warm,R.drawable.filter_thumb_calm};
    private final static int[] filters = new int[]{CameraParams.FILTER_VIDEO_NONE, CameraParams.FILTER_VIDEO_DEFAULT,CameraParams.FILTER_VIDEO_ROMANCE,CameraParams.FILTER_VIDEO_WARM,CameraParams.FILTER_VIDEO_CALM};
    public FilterListView(RelativeLayout view,Context context,FilterListListener listener){
        this.listListener = listener;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.viewGroup = view;
        filterLayout = (RelativeLayout) inflater.inflate(R.layout.le_recorder_filter_list,null);
        LinearLayout ll = (LinearLayout) filterLayout.findViewById(R.id.ll_filter_list);
        cancle = (ImageView) filterLayout.findViewById(R.id.btn_filter_cancle);
        save = (ImageView) filterLayout.findViewById(R.id.btn_filter_save);
        firstView =  addImage(ll,imagefilters[0]);
        firstView.setTag(filters[0]);
        filterLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideFilter();
//                currentView.findViewById(R.id.filter_thumb_selected).setVisibility(View.GONE);
//                listListener.selectFilter((Integer) firstView.getTag());
//                currentView = firstView;
//                currentView.findViewById(R.id.filter_thumb_selected).setVisibility(View.VISIBLE);
                return true;
            }
        });
        filterLayout.findViewById(R.id.rl_test).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        for (int i =1;i<imagefilters.length;i++) {
            addImage(ll,imagefilters[i]).setTag(filters[i]);
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView.findViewById(R.id.filter_thumb_selected).setVisibility(View.GONE);
                currentView = firstView;
                currentView.findViewById(R.id.filter_thumb_selected).setVisibility(View.VISIBLE);
                hideFilter();
                listListener.selectFilter((Integer) firstView.getTag());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilter();
            }
        });
    }
    public void showFilter(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.viewGroup.addView(filterLayout,params);
    }
    public void hideFilter(){
        this.viewGroup.removeView(filterLayout);
    }

    private FrameLayout addImage(LinearLayout layout, int imgRes){
        filterRoot = (FrameLayout) inflater.inflate(R.layout.le_filter_item_layout,null);
        imageView = (ImageView) filterRoot.findViewById(R.id.filter_thumb_image);
        imageView.setImageResource(imgRes);
        layout.addView(filterRoot);
        filterRoot.setOnClickListener(clickListener);
        if(currentView == null){
            currentView = filterRoot;
        }
        currentView.findViewById(R.id.filter_thumb_selected).setVisibility(View.VISIBLE);
        return filterRoot;
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentView.findViewById(R.id.filter_thumb_selected).setVisibility(View.GONE);
            listListener.selectFilter((Integer) v.getTag());
            v.findViewById(R.id.filter_thumb_selected).setVisibility(View.VISIBLE);
            currentView = (FrameLayout) v;
        }
    };

    public interface  FilterListListener{
        void selectFilter(int current);
    }
}
