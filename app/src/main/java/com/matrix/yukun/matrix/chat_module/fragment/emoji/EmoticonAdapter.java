package com.shixinyun.cubeware.ui.chat.panel.input.emoticon.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.shixinyun.cubeware.R;
import com.shixinyun.cubeware.ui.chat.panel.input.emoticon.EmoticonView;
import com.shixinyun.cubeware.ui.chat.panel.input.emoticon.manager.EmoticonManager;

/**
 * 表情适配器
 *
 * @author Wangxx
 * @date 2017/1/4
 */
public class EmoticonAdapter extends BaseAdapter {

    private Context context;
    private int     startIndex;

    public EmoticonAdapter(Context mContext, int startIndex) {
        this.context = mContext;
        this.startIndex = startIndex;
    }

    public int getCount() {
        int count = EmoticonManager.getDisplayCount() - startIndex + 1;
        count = Math.min(count, EmoticonView.EMOJI_PER_PAGE + 1);
        return count;
    }

    @Override
    public Object getItem(int position) {
        return EmoticonManager.getAssetPath(startIndex + position);
    }

    @Override
    public long getItemId(int position) {
        return startIndex + position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({ "ViewHolder", "InflateParams" })
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_emoji_layout, null);
        ImageView imgEmoticon = (ImageView) convertView.findViewById(R.id.img_emoticon);
        int count = EmoticonManager.getDisplayCount();
        int index = startIndex + position;
        if (position == EmoticonView.EMOJI_PER_PAGE || index == count) {
            imgEmoticon.setBackgroundResource(R.drawable.ic_emoji_del);
        }
        else if (index < count) {
            imgEmoticon.setBackground(EmoticonManager.getDisplayDrawable(context, index));
        }
        return convertView;
    }
}