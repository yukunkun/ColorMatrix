package com.matrix.yukun.matrix.gaia_module.adapter.create_person.rec_viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matrix.yukun.matrix.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-7-28.
 */
   public  class CreateViewHolder0 extends RecyclerView.ViewHolder{
        public CircleImageView imageViewHead;
        public TextView textViewName,textViewCreateNum,textViewAttent,textViewSign,textViewComp,textViewPlace;
        public ImageView imageViewChat,imageViewL1Map1, imageViewL2Map1,imageViewL2Map2, imageViewMapL3Map1,imageViewL3Map2,imageViewL3Map3;
        public LinearLayout layout1,layout2, layout3;
        public CreateViewHolder0(View itemView) {
            super(itemView);
            imageViewHead= (CircleImageView) itemView.findViewById(R.id.create_head);
            textViewName=(TextView) itemView.findViewById(R.id.create_name);
            textViewCreateNum=(TextView) itemView.findViewById(R.id.create_create_num);
            textViewAttent=(TextView) itemView.findViewById(R.id.create_attention);
            textViewSign=(TextView) itemView.findViewById(R.id.create_sign);
            textViewComp=(TextView) itemView.findViewById(R.id.create_company_name);
            textViewPlace=(TextView) itemView.findViewById(R.id.create_place);
            imageViewChat= (ImageView) itemView.findViewById(R.id.create_chat);
        }
    }
