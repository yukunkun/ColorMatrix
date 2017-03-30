package com.le.skin;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.letv.recorder.controller.Publisher;

public class PublisherSkinView extends BaseSkinView {
    private String url;
    public PublisherSkinView(Context context) {
        super(context);
    }

    public PublisherSkinView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PublisherSkinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean startPublisher() {
        super.startPublisher();
        if (TextUtils.isEmpty(url)) {
            Log.d(TAG,"startPublisher，推流地址不可以为空");
            Toast.makeText(getContext(), "推流地址不可以为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        publisher.setUrl(url);
        publisher.publish();
        return true;
    }

    @Override
    protected void publisherMessage(Message msg) {

    }

    public void initPublish(String url) {
        Log.d(TAG,"initPublish，初始化");
        this.url = url;
        publisher = Publisher.getInstance();
        super.initPublish();
        if(TextUtils.isEmpty(skinParams.getTitle())){
            nameView.setText(url);
        }
    }
}
