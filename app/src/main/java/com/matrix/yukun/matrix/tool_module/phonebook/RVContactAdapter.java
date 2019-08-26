package com.matrix.yukun.matrix.tool_module.phonebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: kun .
 * date:   On 2018/11/21
 */
public class RVContactAdapter extends BaseAdapter {
    Context mContext;
    List<PhoneBean> mPhoneNumberBeans;
    MoreCallBack mMoreCallBack;
    List<Integer> mListColors=new ArrayList<>();
    Random mRandom=new Random();
    public void setMoreCallBack(MoreCallBack moreCallBack) {
        mMoreCallBack = moreCallBack;
    }

    public RVContactAdapter(Context context, List<PhoneBean> phoneNumberBeans) {
        mContext = context;
        mPhoneNumberBeans = phoneNumberBeans;
        mListColors.add(R.color.color_line);
        mListColors.add(R.color.color_ff2323);
        mListColors.add(R.color.color_ff4081);
        mListColors.add(R.color.color_30f209);
        mListColors.add(R.color.color_ff01bb);
        mListColors.add(R.color.color_1296db);
        mListColors.add(R.color.color_fff82b);
        mListColors.add(R.color.color_2e4eef);
        mListColors.add(R.color.color_b450fc);
        mListColors.add(R.color.color_44fc2c);
    }

    @Override
    public int getCount() {
        return mPhoneNumberBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhoneNumberBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        PhoneNumberBean phoneNumberBean = mPhoneNumberBeans.get(position).getPhoneNumberBean();
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.phone_item, null);
            myHolder.mTvName = convertView.findViewById(R.id.tv_name);
            myHolder.mCircleImageView = convertView.findViewById(R.id.cv_header);
            myHolder.mTvContactName = convertView.findViewById(R.id.tv_contact_name);
            myHolder.mTvNumber = convertView.findViewById(R.id.tv_number);
            myHolder.mIvCall = convertView.findViewById(R.id.iv_call);
            myHolder.mIvMsg = convertView.findViewById(R.id.iv_msg);
            myHolder.mIvMore = convertView.findViewById(R.id.iv_more);
            myHolder.tvLetter = convertView.findViewById(R.id.tv_title);
            myHolder.mLinearLayout=convertView.findViewById(R.id.ll_con);
            convertView.setTag(myHolder);
        }
        myHolder = (MyHolder) convertView.getTag();
        myHolder.mTvName.setText(phoneNumberBean.getContactName().substring(0, 1));
        myHolder.mTvContactName.setText(phoneNumberBean.getContactName());
        myHolder.mTvNumber.setText(phoneNumberBean.getPhoneNum());
        int nextInt = mRandom.nextInt(mListColors.size());
        myHolder.mCircleImageView.setBorderColor(mContext.getResources().getColor(mListColors.get(nextInt)));
        int nextIntText = mRandom.nextInt(mListColors.size());
        myHolder.mTvName.setTextColor(mContext.getResources().getColor(mListColors.get(nextIntText)));
        //位置
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            myHolder.tvLetter.setVisibility(View.VISIBLE);
            myHolder.tvLetter.setText(mPhoneNumberBeans.get(position).getSortLetters());
        } else {
            myHolder.tvLetter.setVisibility(View.GONE);
        }
        myHolder.mIvCall.setOnClickListener(new Listener(position, phoneNumberBean));
        myHolder.mIvMore.setOnClickListener(new Listener(position,phoneNumberBean));
        myHolder.mIvMsg.setOnClickListener(new Listener(position,phoneNumberBean));
        myHolder.mLinearLayout.setOnClickListener(new Listener(position,phoneNumberBean));
        return convertView;
    }

    /**
     * 选中的位置
     */
    public int getSectionForPosition(int position) {
        return mPhoneNumberBeans.get(position).getSortLetters().charAt(0);
    }

    /**
     * 位置是否有,基本就能实现了，
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mPhoneNumberBeans.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


    class MyHolder {
        TextView mTvName;
        CircleImageView mCircleImageView;
        TextView mTvContactName;
        TextView mTvNumber;
        ImageView mIvCall;
        ImageView mIvMsg;
        ImageView mIvMore;
        TextView tvLetter;
        LinearLayout mLinearLayout;
    }

    class Listener implements View.OnClickListener {
        int pos;
        PhoneNumberBean phoneNumberBean;

        public Listener(int pos, PhoneNumberBean phoneNumberBean) {
            this.pos = pos;
            this.phoneNumberBean = phoneNumberBean;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_con:
                case R.id.iv_call:
                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumberBean.getPhoneNum()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    break;
                case R.id.iv_msg:
                    Intent intentMsg = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumberBean.getPhoneNum()));
                    intentMsg.putExtra("sms_body", "");
                    mContext.startActivity(intentMsg);
                    break;
                case R.id.iv_more:
                    mMoreCallBack.onMoreCallBack(pos,phoneNumberBean);
                    break;
            }
        }
    }

    interface MoreCallBack{
        void onMoreCallBack(int pos, PhoneNumberBean phoneNumberBean);
    }
}
