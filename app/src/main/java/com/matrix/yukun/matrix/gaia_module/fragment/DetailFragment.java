package com.matrix.yukun.matrix.gaia_module.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.activity.GaiaPlayActivity;
import com.matrix.yukun.matrix.gaia_module.adapter.RVRecommendAdapter;
import com.matrix.yukun.matrix.gaia_module.bean.GaiaIndexBean;
import com.matrix.yukun.matrix.gaia_module.bean.MaterialDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoDetailInfo;
import com.matrix.yukun.matrix.gaia_module.bean.VideoType;
import com.matrix.yukun.matrix.gaia_module.net.Api;
import com.matrix.yukun.matrix.gaia_module.net.VideoUtils;
import com.matrix.yukun.matrix.gaia_module.util.FileDelete;
import com.matrix.yukun.matrix.util.DataUtils;
import com.matrix.yukun.matrix.util.FileUtil;
import com.matrix.yukun.matrix.util.ImageUtils;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.dialog.ShareDialog;
import com.matrix.yukun.matrix.video_module.play.ImageDetailActivity;
import com.qq.e.comm.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: kun .
 * date:   On 2019/7/5
 */
public class DetailFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private long mId;
    private VideoDetailInfo mVideoDetailInfo;
    private MaterialDetailInfo mMaterialDetailInfo;
    private String[] stringArray;
    private List<GaiaIndexBean> mGaiaBeans=new ArrayList<>();
    private RVRecommendAdapter mRvRecommendAdapter;
    private View mHeaderView;
    private TextView mName;
    private TextView mPlaycountPlaytime;
    private CircleImageView mAvatar;
    private TextView mMaster;
    private TextView mCreateNum;
    private TextView mStaff;
    private TextView mTag1;
    private TextView mTag2;
    private TextView mTag3;
    private TextView mTag;
    private TextView mBackStory;
    private ImageView mIvShare;

    public static DetailFragment instance(long id, VideoDetailInfo videoDetailInfo) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putSerializable("work", videoDetailInfo);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    public static DetailFragment instance(long id, MaterialDetailInfo materialDetailInfo) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putSerializable("material", materialDetailInfo);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.detail_gaia_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mVideoDetailInfo = (VideoDetailInfo)getArguments().getSerializable("work");
        mMaterialDetailInfo = (MaterialDetailInfo)getArguments().getSerializable("material");
        mId = getArguments().getLong("id", 0);
        stringArray = getResources().getStringArray(R.array.work_pool);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(linearLayoutManager);
        if(mVideoDetailInfo!=null){
            mRvRecommendAdapter = new RVRecommendAdapter(R.layout.item_frag_player_rec,mGaiaBeans, VideoType.WORK.getType());
        }else {
            mRvRecommendAdapter = new RVRecommendAdapter(R.layout.item_frag_player_rec,mGaiaBeans, VideoType.MATERIAL.getType());
        }
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.gaia_detail_header, null);
        mRvRecommendAdapter.addHeaderView(mHeaderView);
        recyclerview.setAdapter(mRvRecommendAdapter);
        initData();
        initListener();
    }

    private void initData() {
        mName = mHeaderView.findViewById(R.id.name);
        mPlaycountPlaytime = mHeaderView.findViewById(R.id.playcount_playtime);
        mAvatar = mHeaderView.findViewById(R.id.avatar);
        mMaster = mHeaderView.findViewById(R.id.master);
        mCreateNum = mHeaderView.findViewById(R.id.create_num);
        mIvShare = mHeaderView.findViewById(R.id.iv_share);
        mStaff = mHeaderView.findViewById(R.id.staff);
        mTag1 = mHeaderView.findViewById(R.id.tag_1);
        mTag2 = mHeaderView.findViewById(R.id.tag_2);
        mTag3 = mHeaderView.findViewById(R.id.tag_3);
        mTag = mHeaderView.findViewById(R.id.tag);
        mBackStory = mHeaderView.findViewById(R.id.back_story);
        //分享
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo();
            }
        });
        if(mVideoDetailInfo!=null){
            VideoDetailInfo.WorksBean works = mVideoDetailInfo.getWorks();
            VideoDetailInfo.VideoInfoBean videoInfo = mVideoDetailInfo.getVideoInfo();
            //作品名称
            String worksName = works.getName();
            if (worksName.contains(".mp4")) {
                mName.setText(worksName.substring(0, worksName.length() - 4));
            }else {
                mName.setText(worksName);
            }
            int creationCount = mVideoDetailInfo.getCreationCount();
            int likeCount = mVideoDetailInfo.getLikeCount();

            mCreateNum.setText(likeCount+"关注"+" "+creationCount+"创作");
            //播放次数，和上传的时间
            if(mVideoDetailInfo.getWorks().getCreateTime()!=null&&!mVideoDetailInfo.getWorks().getCreateTime().equals("null")){
                mPlaycountPlaytime.setText((mVideoDetailInfo.getWorksProperties().getPlayCount())+"  发布于"+mVideoDetailInfo.getWorks().getCreateTime().substring(0,10));
            }
            /*分类的显示*/
            String type = works.getType();
            List<String> arrayList=getTags(type);
            if(arrayList!=null&&arrayList.size()!=0){
                if(arrayList.size()==1){
                    mTag1.setVisibility(View.VISIBLE);
                    mTag1.setText(arrayList.get(0));
                    mTag1.setBackgroundResource(R.drawable.shape_detail_play);
                }else if(arrayList.size()==2){
                    mTag1.setVisibility(View.VISIBLE);
                    mTag1.setText(arrayList.get(0));
                    mTag2.setVisibility(View.VISIBLE);
                    mTag2.setText(arrayList.get(1));
                    mTag1.setBackgroundResource(R.drawable.shape_detail_play);
                    mTag2.setBackgroundResource(R.drawable.shape_detail_play);
                }else if(arrayList.size()==3){
                    mTag1.setVisibility(View.VISIBLE);
                    mTag1.setText(arrayList.get(0));
                    mTag2.setVisibility(View.VISIBLE);
                    mTag2.setText(arrayList.get(1));
                    mTag3.setVisibility(View.VISIBLE);
                    mTag3.setText(arrayList.get(2));
                    mTag1.setBackgroundResource(R.drawable.shape_detail_play);
                    mTag2.setBackgroundResource(R.drawable.shape_detail_play);
                    mTag3.setBackgroundResource(R.drawable.shape_detail_play);
                }
            }
            //staff
            StringBuilder staffStr = new StringBuilder();
            staffStr.append(getString(R.string.director) + ":" + works.getDirector() + "|");
            staffStr.append(getString(R.string.photographyer) + ":" + works.getPhotographer() + "|");
            staffStr.append(getString(R.string.cutter) + ":" + works.getCutter() + "|");
            staffStr.append(getString(R.string.colorist) + ":" + works.getColorist() + "|");
            staffStr.append(getString(R.string.models) + ":" + works.getMachine() + "|");
            staffStr.append(getString(R.string.lens) + ":" + works.getLens() + "|");
            staffStr.append(getString(R.string.locations) + ":" + works.getAddress() + "|");
            staffStr.append(getString(R.string.scene) + ":" + works.getScene() + "|");
            staffStr.append(getString(R.string.resolution) + ":" + videoInfo.getWidth() + "x" + videoInfo.getHeight() + "|");
            staffStr.append(getString(R.string.format) + ":" + videoInfo.getFormat() + "|");
            staffStr.append(getString(R.string.coding) + ":" + videoInfo.getCodec() + "|");
            staffStr.append(getString(R.string.bitrate) + ":" + videoInfo.getBitRate() + "|");
            String[] strings = videoInfo.getFps().split("/");
            if (!strings[0].isEmpty() && !strings[1].isEmpty()) {
                double v = Double.parseDouble(strings[0]) / Double.parseDouble(strings[1]);
                Double aDouble = Double.valueOf(v);
                int i = aDouble.intValue();
                if (v == i) {
                    staffStr.append(getString(R.string.framerate) + ":" + i + "|");
                } else {
                    mStaff.append(getString(R.string.framerate) + ":" + String.format("%.2f", v));
                }
            }
            staffStr.append(getString(R.string.videosize) + ":" + FileUtil.formatFileSize(videoInfo.getSize()) + "|");
            mStaff.setText(staffStr);
            //作品标签
            String s = works.getKeywords().isEmpty() ? "暂无" : works.getKeywords();
            if(!s.equals("暂无")){
                mTag.setVisibility(View.VISIBLE);
                SpannableStringBuilder tagStr = new SpannableStringBuilder("标签：" + s);
                mTag.setText(tagStr);
            }else {
                mTag.setVisibility(View.GONE);
            }
            //作品背景
            String description = works.getDescription();
            if(description!=null&&description.length()!=0){
                mBackStory.setVisibility(View.VISIBLE);
                Spanned spanned = Html.fromHtml(description);
                SpannableStringBuilder workBg = new SpannableStringBuilder("简介:" + spanned);
                mBackStory.setText(workBg);
            }else {
                mBackStory.setVisibility(View.GONE);
            }

            //头像
            VideoDetailInfo.UserBean user = mVideoDetailInfo.getUser();
            if (user.getAvatar() != null) {
                Glide.with(getContext()).load(Api.COVER_PREFIX+user.getAvatar()).placeholder(R.drawable.head_2).into(mAvatar);
            }
            mAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(getContext(),Api.COVER_PREFIX+user.getAvatar(),false);
                }
            });
            //作者名称
            mMaster.setText(user.getNickName());

            VideoUtils.getVideoRecomend(mVideoDetailInfo.getWorks().getType(), mVideoDetailInfo.getWorks().getKeywords(), new VideoUtils.GetVideoListener() {
                @Override
                public void getVideo(String data) {
                    if(data!=null){
                        Gson gson=new Gson();
                        List<GaiaIndexBean> gaiaIndexBeans = gson.fromJson(data,new TypeToken<List<GaiaIndexBean>>(){}.getType());
                        mGaiaBeans.addAll(gaiaIndexBeans);
                        mRvRecommendAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void getError(String error) {

                }
            });


        }else if(mMaterialDetailInfo!=null){
            final MaterialDetailInfo.MaterialBean works = mMaterialDetailInfo.getMaterial();
            //作品头像
            Glide.with(getContext()).load(Api.COVER_PREFIX + works.getAvatar()).placeholder(R.drawable.head_2).into(mAvatar);
            mMaster.setText(works.getNickName());
            String description = works.getDescription();
            if(!"null".equals(description)&&description.length()!=0){
                mBackStory.setText("缘起:"+Html.fromHtml(description));
            }else {
                mBackStory.setVisibility(View.VISIBLE);
            }

            String worksName = works.getName();
            if (worksName.contains(".mp4")) {
                mName.setText(worksName.substring(0, worksName.length() - 4));
            } else {
                mName.setText(worksName);
            }
            //创作和粉丝
            int creationCount = works.getCreateCount();
            int likeCount = works.getFansCount();
            mCreateNum.setText("创作"+creationCount+" 粉丝"+likeCount);
            //播放次数，和上传的时间
            if (mMaterialDetailInfo.getMaterial().getCreateTime() != null && !mMaterialDetailInfo.getMaterial().getCreateTime().equals("null")) {
                Date d = new Date(works.getCreateTime().getTime());
                SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
                String time = sf.format(d);
                mPlaycountPlaytime.setText(mMaterialDetailInfo.getMaterial().getDownloadCount()+ "下载,"+works.getWorks_collectCount()+"收藏,"+"发布于"+ time/*.substring(0,10)*/);
            }

            //头像的点击事件
            mAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetailActivity.start(getContext(),Api.COVER_PREFIX + works.getAvatar(),false);
                }
            });

            /*分类的显示*/
            String type = works.getType();
            List<String> arrayList = getTags(type);
            if (arrayList != null && arrayList.size() != 0) {
                if (arrayList.size() == 1) {
                    mTag1.setVisibility(View.VISIBLE);
                    mTag1.setText(arrayList.get(0));
                    mTag1.setBackgroundResource(R.drawable.shape_detail_play);
                } else if (arrayList.size() == 2) {
                    mTag1.setVisibility(View.VISIBLE);
                    mTag1.setText(arrayList.get(0));
                    mTag2.setVisibility(View.VISIBLE);
                    mTag2.setText(arrayList.get(1));
                    mTag1.setBackgroundResource(R.drawable.shape_detail_play);
                    mTag2.setBackgroundResource(R.drawable.shape_detail_play);
                } else if (arrayList.size() == 3) {
                    mTag1.setVisibility(View.VISIBLE);
                    mTag1.setText(arrayList.get(0));
                    mTag2.setVisibility(View.VISIBLE);
                    mTag2.setText(arrayList.get(1));
                    mTag3.setVisibility(View.VISIBLE);
                    mTag3.setText(arrayList.get(2));
                    mTag1.setBackgroundResource(R.drawable.shape_detail_play);
                    mTag2.setBackgroundResource(R.drawable.shape_detail_play);
                    mTag3.setBackgroundResource(R.drawable.shape_detail_play);
                }
            }
            //staff
            StringBuilder staffStr = new StringBuilder();
            staffStr.append(getString(R.string.models) + ":" + works.getMachine() + "|");
            staffStr.append("分辨率" + ":" + mMaterialDetailInfo.getVideo().getWidth()+"X"+mMaterialDetailInfo.getVideo().getHeight());
            staffStr.append("帧率" +":"+ works.getFps()+" fps" + "|");
            staffStr.append(getString(R.string.locations) + ":" + works.getAddress() + "|");
            staffStr.append("格式" + ":" + works.getFileFormat() + "|");
            staffStr.append("大小"+":"+FileUtil.formatFileSize(works.getSize())+ "|");
            staffStr.append("拍摄" + ":" + works.getPhotographer() + "|");
            staffStr.append(getString(R.string.lens) + ":" + works.getLens() + "|");
            staffStr.append("编码" + ":" + mMaterialDetailInfo.getAudio().getCodec_long_name() + "");
            mStaff.setText(staffStr.toString());

            VideoUtils.getMaterialRecomend((int) mId, new VideoUtils.GetVideoListener() {
                @Override
                public void getVideo(String data) {
                    if(data!=null){
                        Gson gson=new Gson();
                        List<GaiaIndexBean> gaiaMaterialBeans = gson.fromJson(data,new TypeToken<List<GaiaIndexBean>>(){}.getType());
                        mGaiaBeans.addAll(gaiaMaterialBeans);
                        mRvRecommendAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void getError(String error) {

                }
            });
        }
    }

    /**
     * 分享
     */
    private void shareTo() {
        if(mVideoDetailInfo!=null){
            String screenshot=mVideoDetailInfo.getVideoInfo().getScreenshot();
            if(screenshot.endsWith(".png")){
                screenshot=screenshot.replace(".png","_18.png");
            }else {
                screenshot=screenshot+".png";
            }
            ShareDialog instance = ShareDialog.getInstance(mVideoDetailInfo.getWorks().getName(), "https://www.gaiamount.com/video/" + mVideoDetailInfo.getWorksProperties().getId(), Api.COVER_PREFIX + screenshot);
            instance.show(getChildFragmentManager(),"");
        }else {
            String screenshot=mMaterialDetailInfo.getMaterial().getScreenshot();
            if(screenshot.endsWith(".png")){
                screenshot=screenshot.replace(".png","_18.png");
            }else {
                screenshot=screenshot+".png";
            }
            ShareDialog instance = ShareDialog.getInstance(mMaterialDetailInfo.getMaterial().getName(), "https://gaiamount.com/footage/detail/" + mMaterialDetailInfo.getMaterial().getMid(), Api.COVER_PREFIX + screenshot);
            instance.show(getChildFragmentManager(),"");
        }
    }

    private ArrayList getTags(String type) {
        ArrayList<String> stringarray=new ArrayList<>();
        String[] split = type.split(",");
        for (int i = 0; i < split.length; i++) {
            String strings=split[i];
            String tagName=changeString(strings);
            if(!tagName.equals(""))
                stringarray.add(tagName);
        }
        return stringarray;
    }
    private String changeString(String strings) {
        if(!strings.equals("")){
            if(strings.equals("0")){
                return stringArray[0];
            }else if(strings.equals("1")){
                return stringArray[1];
            }else if(strings.equals("2")){
                return stringArray[2];
            }else if(strings.equals("3")){
                return stringArray[3];
            }else if(strings.equals("4")){
                return stringArray[4];
            }else if(strings.equals("5")){
                return stringArray[5];
            }else if(strings.equals("6")){
                return stringArray[6];
            }else if(strings.equals("7")){
                return stringArray[7];
            }else if(strings.equals("8")){
                return stringArray[8];
            }else if(strings.equals("9")){
                return stringArray[9];
            }else if(strings.equals("10")){
                return stringArray[10];
            }else if(strings.equals("11")){
                return stringArray[11];
            }else if(strings.equals("12")){
                return stringArray[12];
            }
        }
        return "";
    }

    private void initListener() {
        mRvRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FileDelete.deleteAll();
                if(mVideoDetailInfo!=null){
                    GaiaPlayActivity.start(getContext(),mGaiaBeans.get(position).getId(),VideoType.WORK.getType());
                }else {
                    GaiaPlayActivity.start(getContext(),mGaiaBeans.get(position).getMid(),VideoType.MATERIAL.getType());
                }
                getActivity().finish();
            }
        });
    }
}
