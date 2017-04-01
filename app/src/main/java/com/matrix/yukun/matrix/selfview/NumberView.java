package com.matrix.yukun.matrix.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.yukun.matrix.R;

/**
 * Created by yukun on 17-3-31.
 */
public class NumberView extends LinearLayout implements View.OnClickListener {

    private ImageView imageViewClose;
    private TextView textViewContent;
    private ImageView imageViewAdd;
    private int num=1;
    private Context mContext;

    public NumberView(Context context) {
        super(context);
        initView(context,null,0);
    }

    public NumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs,0);
    }

    public NumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.numview,null);
        imageViewClose = (ImageView) inflate.findViewById(R.id.close);
        textViewContent = (TextView) inflate.findViewById(R.id.text);
        imageViewAdd = (ImageView) inflate.findViewById(R.id.adds);
        textViewContent.setText(num+"");
        setListener();
        mContext=context;
        addView(inflate);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void setListener() {
        imageViewAdd.setOnClickListener(this);
        imageViewClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adds:
                if(num<20){
                    num++;
                    textViewContent.setText(num+"");
                }else {
                    Toast.makeText(mContext, "最多20个", Toast.LENGTH_SHORT).show();
                }
            break;
            case R.id.close:
                if(num>1){
                    num--;
                    textViewContent.setText(num+"");
                }
                break;
        }
    }

    public int getNumber(){
        return num;
    }
    public void setNumber(int num){
        this.num=num;
        textViewContent.setText(num+"");
    }
}
