package com.matrix.yukun.matrix.main_module;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.adapter.RecAdapter;
import com.matrix.yukun.matrix.bean.EventMorePos;
import com.matrix.yukun.matrix.bean.EventPos;
import com.matrix.yukun.matrix.main_module.filters.AutoAdjustFilter;
import com.matrix.yukun.matrix.main_module.filters.BannerFilter;
import com.matrix.yukun.matrix.main_module.filters.BigBrotherFilter;
import com.matrix.yukun.matrix.main_module.filters.BlackWhiteFilter;
import com.matrix.yukun.matrix.main_module.filters.BlindFilter;
import com.matrix.yukun.matrix.main_module.filters.BlockPrintFilter;
import com.matrix.yukun.matrix.main_module.filters.BrickFilter;
import com.matrix.yukun.matrix.main_module.filters.BrightContrastFilter;
import com.matrix.yukun.matrix.main_module.filters.CleanGlassFilter;
import com.matrix.yukun.matrix.main_module.filters.ColorQuantizeFilter;
import com.matrix.yukun.matrix.main_module.filters.ColorToneFilter;
import com.matrix.yukun.matrix.main_module.filters.ComicFilter;
import com.matrix.yukun.matrix.main_module.filters.EdgeFilter;
import com.matrix.yukun.matrix.main_module.filters.FeatherFilter;
import com.matrix.yukun.matrix.main_module.filters.FillPatternFilter;
import com.matrix.yukun.matrix.main_module.filters.FilmFilter;
import com.matrix.yukun.matrix.main_module.filters.FocusFilter;
import com.matrix.yukun.matrix.main_module.filters.GammaFilter;
import com.matrix.yukun.matrix.main_module.filters.GaussianBlurFilter;
import com.matrix.yukun.matrix.main_module.filters.Gradient;
import com.matrix.yukun.matrix.main_module.filters.HslModifyFilter;
import com.matrix.yukun.matrix.main_module.filters.IImageFilter;
import com.matrix.yukun.matrix.main_module.filters.IllusionFilter;
import com.matrix.yukun.matrix.main_module.filters.InvertFilter;
import com.matrix.yukun.matrix.main_module.filters.LensFlareFilter;
import com.matrix.yukun.matrix.main_module.filters.LightFilter;
import com.matrix.yukun.matrix.main_module.filters.LomoFilter;
import com.matrix.yukun.matrix.main_module.filters.MirrorFilter;
import com.matrix.yukun.matrix.main_module.filters.MistFilter;
import com.matrix.yukun.matrix.main_module.filters.MonitorFilter;
import com.matrix.yukun.matrix.main_module.filters.MosaicFilter;
import com.matrix.yukun.matrix.main_module.filters.NeonFilter;
import com.matrix.yukun.matrix.main_module.filters.NightVisionFilter;
import com.matrix.yukun.matrix.main_module.filters.NoiseFilter;
import com.matrix.yukun.matrix.main_module.filters.OilPaintFilter;
import com.matrix.yukun.matrix.main_module.filters.OldPhotoFilter;
import com.matrix.yukun.matrix.main_module.filters.PaintBorderFilter;
import com.matrix.yukun.matrix.main_module.filters.PixelateFilter;
import com.matrix.yukun.matrix.main_module.filters.PosterizeFilter;
import com.matrix.yukun.matrix.main_module.filters.RadialDistortionFilter;
import com.matrix.yukun.matrix.main_module.filters.RainBowFilter;
import com.matrix.yukun.matrix.main_module.filters.RaiseFrameFilter;
import com.matrix.yukun.matrix.main_module.filters.RectMatrixFilter;
import com.matrix.yukun.matrix.main_module.filters.ReflectionFilter;
import com.matrix.yukun.matrix.main_module.filters.ReliefFilter;
import com.matrix.yukun.matrix.main_module.filters.SaturationModifyFilter;
import com.matrix.yukun.matrix.main_module.filters.SceneFilter;
import com.matrix.yukun.matrix.main_module.filters.SepiaFilter;
import com.matrix.yukun.matrix.main_module.filters.SharpFilter;
import com.matrix.yukun.matrix.main_module.filters.ShiftFilter;
import com.matrix.yukun.matrix.main_module.filters.SmashColorFilter;
import com.matrix.yukun.matrix.main_module.filters.SoftGlowFilter;
import com.matrix.yukun.matrix.main_module.filters.SupernovaFilter;
import com.matrix.yukun.matrix.main_module.filters.ThreeDGridFilter;
import com.matrix.yukun.matrix.main_module.filters.ThresholdFilter;
import com.matrix.yukun.matrix.main_module.filters.TileReflectionFilter;
import com.matrix.yukun.matrix.main_module.filters.TintFilter;
import com.matrix.yukun.matrix.main_module.filters.VideoFilter;
import com.matrix.yukun.matrix.main_module.filters.VignetteFilter;
import com.matrix.yukun.matrix.main_module.filters.VintageFilter;
import com.matrix.yukun.matrix.main_module.filters.WaterWaveFilter;
import com.matrix.yukun.matrix.main_module.filters.XRadiationFilter;
import com.matrix.yukun.matrix.main_module.filters.YCBCrLinearFilter;
import com.matrix.yukun.matrix.main_module.filters.ZoomBlurFilter;
import com.matrix.yukun.matrix.main_module.filters.distort.BulgeFilter;
import com.matrix.yukun.matrix.main_module.filters.distort.RippleFilter;
import com.matrix.yukun.matrix.main_module.filters.distort.TwistFilter;
import com.matrix.yukun.matrix.main_module.filters.distort.WaveFilter;
import com.matrix.yukun.matrix.main_module.filters.main.ImageFilterMain;
import com.matrix.yukun.matrix.main_module.filters.textures.CloudsTexture;
import com.matrix.yukun.matrix.main_module.filters.textures.LabyrinthTexture;
import com.matrix.yukun.matrix.main_module.filters.textures.MarbleTexture;
import com.matrix.yukun.matrix.main_module.filters.textures.TextileTexture;
import com.matrix.yukun.matrix.main_module.filters.textures.TexturerFilter;
import com.matrix.yukun.matrix.main_module.filters.textures.WoodTexture;
import com.matrix.yukun.matrix.weather_module.fragment.RecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 17-5-4.
 */
public class FilterAdapter extends RecyclerView.Adapter<MyHolder> {
    private Context mContext;
    private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();
    public int selectPosition;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    private class FilterInfo {
        public int filterID;

        public IImageFilter filter;
        public FilterInfo(int filterID, IImageFilter filter) {
            this.filterID = filterID;
            this.filter = filter;
        }

    }
    public Object getItem(int position) {
        return position < filterArray.size() ? filterArray.get(position).filter
                : null;
    }
    public FilterAdapter(Context c) {

        mContext = c;
        //v0.4
        filterArray.add(new FilterInfo(R.drawable.video_filter1, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
        filterArray.add(new FilterInfo(R.drawable.video_filter2, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
        filterArray.add(new FilterInfo(R.drawable.video_filter3, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
        filterArray.add(new FilterInfo(R.drawable.video_filter4, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
        filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1, new TileReflectionFilter(20, 8, 45, (byte)1)));
        filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2, new TileReflectionFilter(20, 8, 45, (byte)2)));
        filterArray.add(new FilterInfo(R.drawable.fillpattern_filter, new FillPatternFilter((Activity) mContext, R.drawable.texture1)));
        filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1, new FillPatternFilter((Activity)mContext, R.drawable.texture2)));
        filterArray.add(new FilterInfo(R.drawable.mirror_filter1, new MirrorFilter(true)));
        filterArray.add(new FilterInfo(R.drawable.mirror_filter2, new MirrorFilter(false)));
        filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f, 0.3f))));
        filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter2, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.276f, 0.163f), new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
        filterArray.add(new FilterInfo(R.drawable.texturer_filter, new TexturerFilter(new CloudsTexture(), 0.8f, 0.8f)));
        filterArray.add(new FilterInfo(R.drawable.texturer_filter1, new TexturerFilter(new LabyrinthTexture(), 0.8f, 0.8f)));
        filterArray.add(new FilterInfo(R.drawable.texturer_filter2, new TexturerFilter(new MarbleTexture(), 1.8f, 0.8f)));
        filterArray.add(new FilterInfo(R.drawable.texturer_filter3, new TexturerFilter(new WoodTexture(), 0.8f, 0.8f)));
        filterArray.add(new FilterInfo(R.drawable.texturer_filter4, new TexturerFilter(new TextileTexture(), 0.8f, 0.8f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter, new HslModifyFilter(20f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0, new HslModifyFilter(40f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1, new HslModifyFilter(60f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2, new HslModifyFilter(80f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3, new HslModifyFilter(100f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4, new HslModifyFilter(150f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5, new HslModifyFilter(200f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6, new HslModifyFilter(250f)));
        filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7, new HslModifyFilter(300f)));

        //v0.3
        filterArray.add(new FilterInfo(R.drawable.zoomblur_filter, new ZoomBlurFilter(30)));
        filterArray.add(new FilterInfo(R.drawable.threedgrid_filter, new ThreeDGridFilter(16, 100)));
        filterArray.add(new FilterInfo(R.drawable.colortone_filter, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
        filterArray.add(new FilterInfo(R.drawable.colortone_filter2, new ColorToneFilter(0x00FF00, 192)));//green
        filterArray.add(new FilterInfo(R.drawable.colortone_filter3, new ColorToneFilter(0xFF0000, 192)));//blue
        filterArray.add(new FilterInfo(R.drawable.colortone_filter4, new ColorToneFilter(0x00FFFF, 192)));//yellow
        filterArray.add(new FilterInfo(R.drawable.softglow_filter, new SoftGlowFilter(10, 0.1f, 0.1f)));
        filterArray.add(new FilterInfo(R.drawable.tilereflection_filter, new TileReflectionFilter(20, 8)));
        filterArray.add(new FilterInfo(R.drawable.blind_filter1, new BlindFilter(true, 96, 100, 0xffffff)));
        filterArray.add(new FilterInfo(R.drawable.blind_filter2, new BlindFilter(false, 96, 100, 0x000000)));
        filterArray.add(new FilterInfo(R.drawable.raiseframe_filter, new RaiseFrameFilter(20)));
        filterArray.add(new FilterInfo(R.drawable.shift_filter, new ShiftFilter(10)));
        filterArray.add(new FilterInfo(R.drawable.wave_filter, new WaveFilter(25, 10)));
        filterArray.add(new FilterInfo(R.drawable.bulge_filter, new BulgeFilter(-97)));
        filterArray.add(new FilterInfo(R.drawable.twist_filter, new TwistFilter(27, 106)));
        filterArray.add(new FilterInfo(R.drawable.ripple_filter, new RippleFilter(38, 15, true)));
        filterArray.add(new FilterInfo(R.drawable.illusion_filter, new IllusionFilter(3)));
        filterArray.add(new FilterInfo(R.drawable.supernova_filter, new SupernovaFilter(0x00FFFF,20,100)));
        filterArray.add(new FilterInfo(R.drawable.lensflare_filter, new LensFlareFilter()));
        filterArray.add(new FilterInfo(R.drawable.posterize_filter, new PosterizeFilter(2)));
        filterArray.add(new FilterInfo(R.drawable.gamma_filter, new GammaFilter(50)));
        filterArray.add(new FilterInfo(R.drawable.sharp_filter, new SharpFilter()));

        //v0.2
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new ComicFilter()));
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene())));//green
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene1())));//purple
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene2())));//blue
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene3())));
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new FilmFilter(80f)));
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new FocusFilter()));
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new LomoFilter()));
//
        //v0.1
        filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
        filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
        filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
        filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
        filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
        filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
        filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
        filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
        filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter,new BrightContrastFilter()));
        filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,	new SaturationModifyFilter()));
        filterArray.add(new FilterInfo(R.drawable.threshold_filter,	new ThresholdFilter()));
        filterArray.add(new FilterInfo(R.drawable.noisefilter,	new NoiseFilter()));
        filterArray.add(new FilterInfo(R.drawable.banner_filter1, new BannerFilter(10, true)));
        filterArray.add(new FilterInfo(R.drawable.banner_filter2, new BannerFilter(10, false)));
        filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter, new RectMatrixFilter()));
        filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
        filterArray.add(new FilterInfo(R.drawable.brick_filter,	new BrickFilter()));
        filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter,	new GaussianBlurFilter()));
        filterArray.add(new FilterInfo(R.drawable.light_filter,	new LightFilter()));
        filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MistFilter()));
        filterArray.add(new FilterInfo(R.drawable.mosaic_filter,new MosaicFilter()));
        filterArray.add(new FilterInfo(R.drawable.oilpaint_filter,	new OilPaintFilter()));
        filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter,new RadialDistortionFilter()));
        filterArray.add(new FilterInfo(R.drawable.reflection1_filter,new ReflectionFilter(true)));
        filterArray.add(new FilterInfo(R.drawable.reflection2_filter,new ReflectionFilter(false)));
        filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter,	new SaturationModifyFilter()));
        filterArray.add(new FilterInfo(R.drawable.smashcolor_filter,new SmashColorFilter()));
        filterArray.add(new FilterInfo(R.drawable.tint_filter,	new TintFilter()));
        filterArray.add(new FilterInfo(R.drawable.vignette_filter,	new VignetteFilter()));
        filterArray.add(new FilterInfo(R.drawable.autoadjust_filter,new AutoAdjustFilter()));
        filterArray.add(new FilterInfo(R.drawable.colorquantize_filter,	new ColorQuantizeFilter()));
        filterArray.add(new FilterInfo(R.drawable.waterwave_filter,	new WaterWaveFilter()));
        filterArray.add(new FilterInfo(R.drawable.vintage_filter,new VintageFilter()));
        filterArray.add(new FilterInfo(R.drawable.oldphoto_filter,new OldPhotoFilter()));
        filterArray.add(new FilterInfo(R.drawable.sepia_filter,	new SepiaFilter()));
        filterArray.add(new FilterInfo(R.drawable.rainbow_filter,new RainBowFilter()));
        filterArray.add(new FilterInfo(R.drawable.feather_filter,new FeatherFilter()));
        filterArray.add(new FilterInfo(R.drawable.xradiation_filter,new XRadiationFilter()));
        filterArray.add(new FilterInfo(R.drawable.nightvision_filter,new NightVisionFilter()));
//        filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter,null/* �˴�������ԭͼЧ�� */));
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.rec_itrem,null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if(holder instanceof MyHolder){
            ((MyHolder) holder).textView.setText("");
            Glide.with(mContext).load(filterArray.get(position).filterID).into(((MyHolder) holder).imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(selectPosition!=position){
                        selectPosition=position;
                        EventBus.getDefault().post(new EventMorePos(position));
                    }
                }
            });
        }

        if(selectPosition==position){
            ((MyHolder) holder).imageViewCheck.setVisibility(View.VISIBLE);
        }
        else {
            ((MyHolder) holder).imageViewCheck.setVisibility(View.GONE);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filterArray.size();
    }



}
