package com.matrix.yukun.matrix.main_module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.main_module.utils.SPUtils;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.main_module.utils.component.MutiComponent;
import com.matrix.yukun.matrix.selfview.guideview.Guide;
import com.matrix.yukun.matrix.selfview.guideview.GuideBuilder;
import com.matrix.yukun.matrix.tool_module.barrage.BarrageActivity;
import com.matrix.yukun.matrix.tool_module.calarder.TripartiteActivity;
import com.matrix.yukun.matrix.tool_module.cropmap.ImageCropActivity;
import com.matrix.yukun.matrix.tool_module.deskwallpaper.DeskActivity;
import com.matrix.yukun.matrix.tool_module.gif.activity.GifProActivity;
import com.matrix.yukun.matrix.tool_module.internet.NetActivity;
import com.matrix.yukun.matrix.tool_module.notebook.activity.NoteActivity;
import com.matrix.yukun.matrix.tool_module.qrcode.QRCodeActivity;
import com.matrix.yukun.matrix.tool_module.videorecord.ProductVideoActivity;
import com.matrix.yukun.matrix.tool_module.weather.activity.HeWeatherActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yukun on 17-11-20.
 */

public class ToolsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Integer> imageList;
    List<String> mListName;
    int showTimes=0;
    Map<String,Class> mClassMap=new HashMap<>();
    public ToolsItemAdapter(Context context, List<Integer> imageList, List<String> mListName) {
        this.context = context;
        this.imageList = imageList;
        this.mListName= mListName;
        mClassMap.put("图片处理", ImageCropActivity.class);
        mClassMap.put("摄像", ProductVideoActivity.class);
        mClassMap.put("手机弹幕", BarrageActivity.class);
        mClassMap.put("二维码生成", QRCodeActivity.class);
        mClassMap.put("今日天气", HeWeatherActivity.class);
        mClassMap.put("备忘录", TripartiteActivity.class);
        mClassMap.put("GIF制作", GifProActivity.class);
        mClassMap.put("日记本", NoteActivity.class);
        mClassMap.put("网络检测", NetActivity.class);
        mClassMap.put("抖音桌面", DeskActivity.class);

    }

    public void updateData(List<String>  listName){
        this.mListName=listName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tool_item_layout, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MHolder) {
            Glide.with(context).load(imageList.get(position)).into(((MHolder) holder).mTvIcon);
            ((MHolder) holder).mTvName.setText(mListName.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = (String) ((MHolder) holder).mTvName.getText();
                    Class aClass = mClassMap.get(text);
                    if (aClass != null) {
                        Intent intent = new Intent(context, aClass);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        ToastUtils.showToast("developing...");
                    }
                }
            });

            if (position == 1 && showTimes == 0) {
                final View finalView = holder.itemView;
                holder.itemView.post(new Runnable() {
                    @Override
                    public void run() {
                        long guide_time = SPUtils.getInstance().getLong("guide_time");
                        long currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis - guide_time > 2 * 24 * 60 * 60 * 1000) {
                            showGuideView(finalView);
                        }
                    }
                });
            }
        }
    }

    public void showGuideView(View targetView) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
                .setAlpha(180)
                .setHighTargetCorner(5)
                .setHighTargetPadding(5)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override public void onShown() {
                long currentTimeMillis = System.currentTimeMillis();
                SPUtils.getInstance().saveLong("guide_time",currentTimeMillis);
            }

            @Override public void onDismiss() {
                long currentTimeMillis = System.currentTimeMillis();
                SPUtils.getInstance().saveLong("guide_time",currentTimeMillis);
            }
        });
        builder.setOutsideTouchable(false);
        builder.addComponent(new MutiComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }

    @Override
    public int getItemCount() {
        return mListName.size();
    }

    class MHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tool_name)
        TextView mTvName;
        @BindView(R.id.iv_image)
        ImageView mTvIcon;
        @BindView(R.id.ll_contain)
        LinearLayout ll_contain;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ViewGroup.LayoutParams params=ll_contain.getLayoutParams();
            params.height= ScreenUtils.instance().getWidth(context)/4-10;
            ll_contain.setLayoutParams(params);
        }
    }

}
