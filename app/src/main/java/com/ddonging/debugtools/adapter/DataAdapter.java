package com.ddonging.debugtools.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ddonging.debugtools.R;

/**
 * 数据适配器类，用于在RecyclerView中显示字符串数据
 * 继承自BaseQuickAdapter，简化了RecyclerView.Adapter的实现
 */
public class DataAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    /**
     * 数据适配器类，用于在RecyclerView中显示字符串数据
     * 继承自BaseQuickAdapter，简化了RecyclerView.Adapter的实现
     */
    public DataAdapter() {
        super(R.layout.item_data);
    }
    /**
     * 数据绑定方法
     * @param helper ViewHolder帮助类，用于操作item视图
     * @param item 当前item对应的数据对象
     */
    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_data, item);
    }
}
