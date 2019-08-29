package com.matrix.yukun.matrix.gaia_module.adapter.create_person;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.rec_viewholder.CreateViewHolder;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.rec_viewholder.CreateViewHolder0;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.rec_viewholder.CreateViewHolder2;
import com.matrix.yukun.matrix.gaia_module.adapter.create_person.rec_viewholder.CreateViewHolder3;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.main_module.activity.ImageDetailActivity;
import com.matrix.yukun.matrix.main_module.utils.ScreenUtils;
import com.matrix.yukun.matrix.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-7-27.
 */
public class CreateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CreatePersonInfo> createPersonList;
    private List<WorksInfo> worksList;
    private final int COVER_SIZE_0=0;//没有图片的布局
    private final int COVER_SIZE_1=1;//一张图片的布局
    private final int COVER_SIZE_2=2;//两张图片的布局
    private final int COVER_SIZE_3=3;//三张图片的布局


    public CreateAdapter(Context context, List<CreatePersonInfo> createPersonList, List<WorksInfo> worksList){
        this.context=context;
        this.createPersonList=createPersonList;
        this.worksList=worksList;
    }


    int sreenWidth;
    private void getScreeWidth() {
        sreenWidth = ScreenUtils.instance().getWidth(context);
    }

    private void setItemHeight(View itemView, int i) {
        ImageView itemImageView = (ImageView) itemView.findViewById(i);
        ViewGroup.LayoutParams layoutParams = itemImageView.getLayoutParams();
        getScreeWidth();
        layoutParams.height = (int) ((sreenWidth*0.48));//16:9
        itemImageView.setLayoutParams(layoutParams);
    }
    private void setItemHeight2(View itemView, int i) {
        ImageView itemImageView = (ImageView) itemView.findViewById(i);
        ViewGroup.LayoutParams layoutParams = itemImageView.getLayoutParams();
        getScreeWidth();
        layoutParams.height = (int) ((sreenWidth*0.42));//5:4
        itemImageView.setLayoutParams(layoutParams);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View inflate = null;
        if (viewType==COVER_SIZE_0){
            View inflate= LayoutInflater.from(context).inflate(R.layout.create_person_item_zero, null);
            CreateViewHolder0 holder=new CreateViewHolder0(inflate);
            return holder;
        }if(viewType==COVER_SIZE_1){
            View inflate= LayoutInflater.from(context).inflate(R.layout.create_person_item, null);
            setItemHeight(inflate, R.id.create_lin1_map);
            CreateViewHolder holder=new CreateViewHolder(inflate);
            return holder;
        }if(viewType==COVER_SIZE_2){
            View inflate= LayoutInflater.from(context).inflate(R.layout.create_person_item_two, null);
            setItemHeight2(inflate, R.id.create_lin2_map1);
            setItemHeight2(inflate, R.id.create_lin2_map2);
            CreateViewHolder2 holder=new CreateViewHolder2(inflate);
            return holder;
        }if(viewType==COVER_SIZE_3){
            View inflate= LayoutInflater.from(context).inflate(R.layout.create_person_item_three, null);
            setItemHeight(inflate, R.id.create_lin3_map1);
            setItemHeight2(inflate, R.id.create_lin3_map2);
            setItemHeight2(inflate, R.id.create_lin3_map3);
            CreateViewHolder3 holder=new CreateViewHolder3(inflate);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final CreatePersonInfo personInfo = createPersonList.get(position);
        final WorksInfo worksInfo = worksList.get(position);
        if(holder instanceof CreateViewHolder0){
            //共有的
            ((CreateViewHolder0)holder).textViewName.setText(personInfo.getNickName());
            ((CreateViewHolder0)holder).textViewCreateNum.setText(personInfo.getCreateCount()+"创作·"+personInfo.getLikeCount()+"粉丝·"+personInfo.getBrowseCount()+"访问");
            //签名为null
            if(personInfo.getSignature().equals("null")){
                ((CreateViewHolder0)holder).textViewSign.setText("没有签名");
            }else {
                ((CreateViewHolder0) holder).textViewSign.setText(personInfo.getSignature());
            }
            //地址
            if(personInfo.getAddress().equals("")){
                ((CreateViewHolder0)holder).textViewPlace.setText("其他");
            }else {
                ((CreateViewHolder0)holder).textViewPlace.setText(personInfo.getAddress());
            }
            //头像
            GlideUtil.loadImage(Api.COVER_PREFIX+personInfo.getAvatar(),((CreateViewHolder0)holder).imageViewHead);
            ((CreateViewHolder0)holder).imageViewHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(context,Api.COVER_PREFIX+personInfo.getAvatar(),false);
                }
            });
            //聊天
            ((CreateViewHolder0)holder).imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            //关注
//            if (personInfo.getFocus()==1){
//                ((CreateViewHolder0)holder).textViewAttent.setBackground(context.getResources().getDrawable(R.drawable.create_person_attented));
//                ((CreateViewHolder0)holder).textViewAttent.setText("已关注");
//                ((CreateViewHolder0)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
//
//            }else {
//                ((CreateViewHolder0)holder).textViewAttent.setBackground(context.getResources().getDrawable(R.drawable.create_person_attent));
//                ((CreateViewHolder0)holder).textViewAttent.setText("关注");
//                ((CreateViewHolder0)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_ff5773));
//
//            }

        }if(holder instanceof CreateViewHolder){
            //共有的
            ((CreateViewHolder)holder).textViewName.setText(personInfo.getNickName());
            ((CreateViewHolder)holder).textViewCreateNum.setText(personInfo.getCreateCount()+"创作·"+personInfo.getLikeCount()+"粉丝·"+personInfo.getBrowseCount()+"访问");
            //签名为null
            if(personInfo.getSignature().equals("null")){
                ((CreateViewHolder)holder).textViewSign.setText("没有签名");
            }else {
                ((CreateViewHolder) holder).textViewSign.setText(personInfo.getSignature());
            }
            //地址
            if(personInfo.getAddress().equals("")){
                ((CreateViewHolder)holder).textViewPlace.setText("其他");
            }else {
                ((CreateViewHolder)holder).textViewPlace.setText(personInfo.getAddress());
            }
            //头像
            GlideUtil.loadImage(Api.COVER_PREFIX+personInfo.getAvatar(),((CreateViewHolder)holder).imageViewHead);
            ((CreateViewHolder)holder).imageViewHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(context,Api.COVER_PREFIX+personInfo.getAvatar(),false);
                }
            });
            //聊天
            ((CreateViewHolder)holder).imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            //关注
//            if (personInfo.getFocus()==1){
//                ((CreateViewHolder)holder).textViewAttent.setBackground(context.getResources().getDrawable(R.drawable.create_person_attented));
//                ((CreateViewHolder)holder).textViewAttent.setText("已关注");
//                ((CreateViewHolder)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
//            }else {
//                ((CreateViewHolder)holder).textViewAttent.setBackground(context.getResources().getDrawable(R.drawable.create_person_attent));
//                ((CreateViewHolder)holder).textViewAttent.setText("关注");
//                ((CreateViewHolder)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_ff5773));
//            }

            //图片
            if(!worksInfo.getCoverImgInfos().get(0).getCover().isEmpty()&&worksInfo.getCoverImgInfos().get(0).getCover().length()!=0&&!"null".equals(worksInfo.getCoverImgInfos().get(0).getCover())){
                GlideUtil.loadImage(Api.COVER_PREFIX+worksInfo.getCoverImgInfos().get(0).getCover(),((CreateViewHolder)holder).imageViewL1Map1);
            }else if (!worksInfo.getCoverImgInfos().get(0).getScreenshot().isEmpty()&&worksInfo.getCoverImgInfos().get(0).getScreenshot().length()!=0){
                String screenshot = worksInfo.getCoverImgInfos().get(0).getScreenshot();
                int flag = worksInfo.getCoverImgInfos().get(0).getFlag();
                if(flag==1){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot+"_18.png",((CreateViewHolder)holder).imageViewL1Map1);
                }else if(flag==0){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot.replace(".","_18."),((CreateViewHolder)holder).imageViewL1Map1);
                }
            }

            //视频播放页面
            ((CreateViewHolder)holder).imageViewL1Map1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }if(holder instanceof CreateViewHolder2){
            //共有的
            ((CreateViewHolder2)holder).textViewName.setText(personInfo.getNickName());
            ((CreateViewHolder2)holder).textViewCreateNum.setText(personInfo.getCreateCount()+"创作·"+personInfo.getLikeCount()+"粉丝·"+personInfo.getBrowseCount()+"访问");
            //签名为null
            if(personInfo.getSignature().equals("null")){
                ((CreateViewHolder2)holder).textViewSign.setText("签名为空");
            }else {
                ((CreateViewHolder2) holder).textViewSign.setText(personInfo.getSignature());
            }
            //地址
            if(personInfo.getAddress().equals("")){
                ((CreateViewHolder2)holder).textViewPlace.setText("其他");
            }else {
                ((CreateViewHolder2)holder).textViewPlace.setText(personInfo.getAddress());
            }
            //头像
            GlideUtil.loadImage(Api.COVER_PREFIX+personInfo.getAvatar(),((CreateViewHolder2)holder).imageViewHead);
            ((CreateViewHolder2)holder).imageViewHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(context,Api.COVER_PREFIX+personInfo.getAvatar(),false);
                }
            });
            //聊天
            ((CreateViewHolder2)holder).imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            //关注
//            if (personInfo.getFocus()==1){
//                ((CreateViewHolder2)holder).textViewAttent.setBackground(context.getResources().getDrawable(R.drawable.create_person_attented));
//                ((CreateViewHolder2)holder).textViewAttent.setText("已关注");
//                ((CreateViewHolder2)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
//            }else {
//                ((CreateViewHolder2)holder).textViewAttent.setBackground(context.getResources().getDrawable(R.drawable.create_person_attent));
//                ((CreateViewHolder2)holder).textViewAttent.setText("关注");
//                ((CreateViewHolder2)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_ff5773));
//            }

            //图片
            if(!worksInfo.getCoverImgInfos().get(0).getCover().isEmpty()&&worksInfo.getCoverImgInfos().get(0).getCover().length()!=0&&!"null".equals(worksInfo.getCoverImgInfos().get(0).getCover())){
                GlideUtil.loadImage(Api.COVER_PREFIX+worksInfo.getCoverImgInfos().get(0).getCover(),((CreateViewHolder2)holder).imageViewL2Map1);
            }else if (!worksInfo.getCoverImgInfos().get(0).getScreenshot().isEmpty()&&worksInfo.getCoverImgInfos().get(0).getScreenshot().length()!=0){
                String screenshot = worksInfo.getCoverImgInfos().get(0).getScreenshot();
                int flag = worksInfo.getCoverImgInfos().get(0).getFlag();
                if(flag==1){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot+"_18.png",((CreateViewHolder2)holder).imageViewL2Map1);
                }else if(flag==0){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot.replace(".","_18."),((CreateViewHolder2)holder).imageViewL2Map1);
                }
//                Glide.with(context).load(Configs.COVER_PREFIX+worksInfo.getCoverImgInfos().get(0).getScreenshot()).placeholder(R.mipmap.bg_general).into(((CreateViewHolder2)holder).imageViewL2Map1);
            }

            if(!worksInfo.getCoverImgInfos().get(1).getCover().isEmpty()&&worksInfo.getCoverImgInfos().get(1).getCover().length()!=0&&!"null".equals(worksInfo.getCoverImgInfos().get(1).getCover())){
                GlideUtil.loadImage(Api.COVER_PREFIX+worksInfo.getCoverImgInfos().get(1).getCover(),((CreateViewHolder2)holder).imageViewL2Map2);
            }else if (!worksInfo.getCoverImgInfos().get(1).getScreenshot().isEmpty()&&worksInfo.getCoverImgInfos().get(1).getScreenshot().length()!=0){
                String screenshot = worksInfo.getCoverImgInfos().get(1).getScreenshot();
                int flag = worksInfo.getCoverImgInfos().get(1).getFlag();
                if(flag==1){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot+"_18.png",((CreateViewHolder2)holder).imageViewL2Map2);
                }else if(flag==0){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot.replace(".","_18."),((CreateViewHolder2)holder).imageViewL2Map2);
                }
//                Glide.with(context).load(Configs.COVER_PREFIX+worksInfo.getCoverImgInfos().get(1).getScreenshot()).placeholder(R.mipmap.bg_general).into(((CreateViewHolder2)holder).imageViewL2Map2);
            }

            //视频播放页面
            ((CreateViewHolder2)holder).imageViewL2Map1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ((CreateViewHolder2)holder).imageViewL2Map2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });


        }if(holder instanceof CreateViewHolder3){
            //共有的
            ((CreateViewHolder3)holder).textViewName.setText(personInfo.getNickName());
            ((CreateViewHolder3)holder).textViewCreateNum.setText(personInfo.getCreateCount()+"创作·"+personInfo.getLikeCount()+"粉丝·"+personInfo.getBrowseCount()+"访问");
            //签名为null
            if(personInfo.getSignature().equals("null")){
                ((CreateViewHolder3)holder).textViewSign.setText("没有签名");
            }else {
                ((CreateViewHolder3) holder).textViewSign.setText(personInfo.getSignature());
            }
            //地址
            if(personInfo.getAddress().equals("")){
                ((CreateViewHolder3)holder).textViewPlace.setText("其他");
            }else {
                ((CreateViewHolder3)holder).textViewPlace.setText(personInfo.getAddress());
            }
            //头像
            GlideUtil.loadImage(Api.COVER_PREFIX+personInfo.getAvatar(),((CreateViewHolder3)holder).imageViewHead);            //头像
            ((CreateViewHolder3)holder).imageViewHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(context,Api.COVER_PREFIX+personInfo.getAvatar(),false);
                }
            });
            //聊天
            ((CreateViewHolder3)holder).imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            //关注
//            if (personInfo.getFocus()==1){
//                ((CreateViewHolder3)holder).textViewAttent.setBackgroundResource(R.drawable.create_person_attented);
//                ((CreateViewHolder3)holder).textViewAttent.setText("已关注");
//                ((CreateViewHolder3)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));
//            }else {
//                ((CreateViewHolder3)holder).textViewAttent.setBackgroundResource(R.drawable.create_person_attent);
//                ((CreateViewHolder3)holder).textViewAttent.setText("关注");
//                ((CreateViewHolder3)holder).textViewAttent.setTextColor(context.getResources().getColor(R.color.color_ff5773));
//            }
            //图片
            if(!worksInfo.getCoverImgInfos().get(0).getCover().isEmpty()&&worksInfo.getCoverImgInfos().get(0).getCover().length()!=0&&!"null".equals(worksInfo.getCoverImgInfos().get(0).getCover())){
                GlideUtil.loadImage(Api.COVER_PREFIX+worksInfo.getCoverImgInfos().get(0).getCover(),((CreateViewHolder3)holder).imageViewL3Map1);
            }else if (!worksInfo.getCoverImgInfos().get(0).getScreenshot().isEmpty()&&worksInfo.getCoverImgInfos().get(0).getScreenshot().length()!=0){
                String screenshot = worksInfo.getCoverImgInfos().get(0).getScreenshot();
                int flag = worksInfo.getCoverImgInfos().get(0).getFlag();
                if(flag==1){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot+"_18.png",((CreateViewHolder3)holder).imageViewL3Map1);
                }else if(flag==0){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot.replace(".","_18."),((CreateViewHolder3)holder).imageViewL3Map1);
                }
//                Glide.with(context).load(Configs.COVER_PREFIX+worksInfo.getCoverImgInfos().get(0).getScreenshot()).placeholder(R.mipmap.bg_general).into(((CreateViewHolder3)holder).imageViewL3Map1);
            }

            if(!worksInfo.getCoverImgInfos().get(1).getCover().isEmpty()&&worksInfo.getCoverImgInfos().get(1).getCover().length()!=0&&!"null".equals(worksInfo.getCoverImgInfos().get(1).getCover())){
                GlideUtil.loadImage(Api.COVER_PREFIX+worksInfo.getCoverImgInfos().get(1).getCover(),((CreateViewHolder3)holder).imageViewL3Map2);
            }else if (!worksInfo.getCoverImgInfos().get(1).getScreenshot().isEmpty()&&worksInfo.getCoverImgInfos().get(1).getScreenshot().length()!=0){
                String screenshot = worksInfo.getCoverImgInfos().get(1).getScreenshot();
                int flag = worksInfo.getCoverImgInfos().get(1).getFlag();
                if(flag==1){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot+"_18.png",((CreateViewHolder3)holder).imageViewL3Map2);
                }else if(flag==0){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot.replace(".","_18."),((CreateViewHolder3)holder).imageViewL3Map2);
                }
            }

            if(!worksInfo.getCoverImgInfos().get(2).getCover().isEmpty()&&worksInfo.getCoverImgInfos().get(2).getCover().length()!=0&&!"null".equals(worksInfo.getCoverImgInfos().get(2).getCover())){
                GlideUtil.loadImage(Api.COVER_PREFIX+worksInfo.getCoverImgInfos().get(2).getCover(),((CreateViewHolder3)holder).imageViewL3Map3);
            }else if (!worksInfo.getCoverImgInfos().get(2).getScreenshot().isEmpty()&&worksInfo.getCoverImgInfos().get(2).getScreenshot().length()!=0){
                String screenshot = worksInfo.getCoverImgInfos().get(2).getScreenshot();
                int flag = worksInfo.getCoverImgInfos().get(2).getFlag();
                if(flag==1){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot+"_18.png",((CreateViewHolder3)holder).imageViewL3Map3);
                }else if(flag==0){
                    GlideUtil.loadImage(Api.COVER_PREFIX+screenshot.replace(".","_18."),((CreateViewHolder3)holder).imageViewL3Map3);
                }
            }
            //视频播放页面
            ((CreateViewHolder3)holder).imageViewL3Map1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ((CreateViewHolder3)holder).imageViewL3Map2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ((CreateViewHolder3)holder).imageViewL3Map3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        ArrayList<CoverImgInfo> coverImgInfos = worksList.get(position).getCoverImgInfos();
        int size = coverImgInfos.size();
        boolean empty = coverImgInfos.isEmpty();
        if(size==0||empty==true){
            return COVER_SIZE_0;
        }else if(size==1){
            return COVER_SIZE_1;
        }else if(size==2){
            return COVER_SIZE_2;
        }else if(size==3){
            return COVER_SIZE_3;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return createPersonList.size();
    }

}
