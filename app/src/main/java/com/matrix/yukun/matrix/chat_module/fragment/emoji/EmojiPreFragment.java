package com.matrix.yukun.matrix.chat_module.fragment.emoji;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.chat_module.emoji.EmoticonManager;
import com.matrix.yukun.matrix.util.log.LogUtil;
import com.matrix.yukun.matrix.video_module.BaseFragment;
import com.matrix.yukun.matrix.video_module.utils.ToastUtils;

/**
 * author: kun .
 * date:   On 2019/6/24
 */
public class EmojiPreFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private String mPos;
    private GridView mGridView;
    private EmoticonAdapter mEmoticonAdapter;

    public static EmojiPreFragment Instance(String s){
        EmojiPreFragment emojiPreFragment=new EmojiPreFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("pos",s);
        emojiPreFragment.setArguments(bundle);
        return emojiPreFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.emoji_pre_fragment;
    }

    public String getPos() {
        return mPos;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mPos = getArguments().getString("pos");
        mGridView = inflate.findViewById(R.id.grideview);
        mEmoticonAdapter = new EmoticonAdapter(getContext(),Integer.valueOf(mPos)*EmoticonAdapter.EMOJI_PER_PAGE);
        mGridView.setAdapter(mEmoticonAdapter);
        mGridView.setHorizontalSpacing(5);
        mGridView.setVerticalSpacing(5);
        mGridView.setGravity(Gravity.CENTER);
        mGridView.setSelection(R.drawable.selector_emoji_bg);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String displayText="";
        if(position==EmoticonAdapter.EMOJI_PER_PAGE||(Integer.valueOf(mPos)==2&&position==10)){
            displayText ="/DEL";
        }else {
            displayText = EmoticonManager.getDisplayText(position+EmoticonAdapter.EMOJI_PER_PAGE*Integer.valueOf(mPos));
        }
        if(mOnEmojiClickListener!=null){
            mOnEmojiClickListener.onEmojClickListener(displayText);
        }
    }

    public static OnEmojiClickListener mOnEmojiClickListener;

    public static void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        mOnEmojiClickListener = onEmojiClickListener;
    }

    public interface OnEmojiClickListener{
        void onEmojClickListener(String s);
    }
}
