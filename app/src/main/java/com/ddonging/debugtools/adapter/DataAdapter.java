package com.ddonging.debugtools.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ddonging.debugtools.R;

public class DataAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DataAdapter() {
        super(R.layout.item_data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_data, item);
    }
}
