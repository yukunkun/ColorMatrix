package com.matrix.yukun.matrix.gaia_module.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matrix.yukun.matrix.R;
import com.matrix.yukun.matrix.gaia_module.bean.ItemBean;

import java.util.List;

/**
 * author: kun .
 * date:   On 2019/8/14
 */
public class RVRestAdapter extends BaseMultiItemQuickAdapter<ItemBean,BaseViewHolder> {

    public RVRestAdapter(List<ItemBean> data) {
        super(data);
        addItemType(1, R.layout.item_smart_1);
        addItemType(2, R.layout.item_smart_2);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemBean item) {
        int adapterPosition = helper.getAdapterPosition();
        if(helper.getItemViewType()==1){
            helper.setText(R.id.tv,item.getDescription()+" "+adapterPosition);
        }else if(helper.getItemViewType()==2){
            helper.setText(R.id.tv,item.getDescription());
        }
    }
}
