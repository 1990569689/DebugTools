package com.ddonging.debugtools.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddonging.debugtools.R;
import com.ddonging.debugtools.utils.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 可展开列表适配器，用于显示蓝牙GATT服务和特征值
 * 继承自BaseExpandableListAdapter实现可展开列表功能
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    Boolean isRead=false,isWrite=false,isNotify=false;
    private HashMap<String,String> listDataHeader; // 组数据
    private HashMap<String,  ArrayList<HashMap<String, String>>> listDataChild; // 子项数据
    OnItemClickListener mOnItemClickListener;

    /**
     * 构造函数
     * @param context 上下文
     * @param listDataHeader 组数据Map
     * @param listChildData 子项数据Map
     */
    public ExpandableListAdapter(Context context, HashMap<String,String> listDataHeader, HashMap<String, ArrayList<HashMap<String, String>>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    /**
     * 获取子项数据
     * @param groupPosition 组位置
     * @param childPosition 子项位置
     * @return 特征值UUID字符串
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get("uuid"+groupPosition)).get(childPosition).get("characteristic");
    }
    /**
     * 获取特征值名称
     * @param uuid 特征值UUID
     * @return 特征值名称
     */
    public String CharacteristicName(String uuid) {
        return TextUtils.getCharacteristicName(uuid);
    }
    /**
     * 获取特征值属性
     * @param groupPosition 组位置
     * @param childPosition 子项位置
     * @return 特征值属性值
     */
    public int getProperties(int groupPosition, int childPosition) {
        return Integer.parseInt(this.listDataChild.get(this.listDataHeader.get("uuid"+groupPosition)).get(childPosition).get("properties"));
    }
    public String getDescriptors(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get("uuid"+groupPosition)).get(childPosition).get("descriptors");
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);


         final String childText = (String) getChild(groupPosition, childPosition);
         int properties = (int) getProperties(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_expandable, null);
        }
        ImageView send = convertView.findViewById(R.id.send);
        ImageView notify = convertView.findViewById(R.id.notify);
        ImageView read = convertView.findViewById(R.id.read);
        TextView pro = convertView.findViewById(R.id.tv_properties);
        pro.setText("Properties：");
        notify.setVisibility(View.GONE);
        send.setVisibility(View.GONE);
        read.setVisibility(View.GONE);
        read.setColorFilter(Color.BLACK);
//        notify.setColorFilter(Color.BLACK);

        if ((properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
            pro.setText(pro.getText().toString()+" INDICATE");
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
            read.setVisibility(View.VISIBLE);

            pro.setText(pro.getText().toString()+" READ");
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            notify.setVisibility(View.VISIBLE);
            pro.setText(pro.getText().toString()+" NOTIFY");
            notify.setColorFilter(Color.BLACK);
        }
        if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
            send.setVisibility(View.VISIBLE);
            pro.setText(pro.getText().toString()+" WRITE");
        }

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNotify){
                    notify.setColorFilter(Color.RED);
                    mOnItemClickListener.onItemClick(v,headerTitle,childText,"notify");
                    isNotify=true;
                }else {
                    notify.setColorFilter(Color.BLACK);
                    mOnItemClickListener.onItemClick(v,headerTitle,childText,"cancel_notify");
                    isNotify=false;
                }

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,headerTitle,childText,"write");
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRead){
//                    read.setColorFilter(Color.RED);
                    mOnItemClickListener.onItemClick(v,headerTitle,childText,"read");
                    isRead=true;
                }else {
//                    read.setColorFilter(Color.BLACK);
                    mOnItemClickListener.onItemClick(v,headerTitle,childText,"cancel_read");
                    isRead=false;
                }
            }
        });
        String name = (String) CharacteristicName(childText);
        TextView txtListChild = convertView.findViewById(R.id.tv_data);
        txtListChild.setText(name);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) txtListChild.getLayoutParams();
//        layoutParams.setMargins(0, 0, 10, 0); // 设置左边距为16dp，顶部、右边和底部的外边距为0
        layoutParams.setMargins(180,0,0,0);
        txtListChild.setLayoutParams(layoutParams);
//

        TextView uuid = convertView.findViewById(R.id.tv_uuid);
        uuid.setText(childText);
        uuid.setLayoutParams(layoutParams);
        pro.setLayoutParams(layoutParams);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get("uuid"+groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get("uuid"+groupPosition);
    }
    public String serviceName(String uuid) {
        return TextUtils.getServiceName(uuid);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_expandable, null);
        }
        String name = (String) serviceName(headerTitle);
        TextView txtListChild = convertView.findViewById(R.id.tv_data);
        txtListChild.setText(name);
        txtListChild.setTypeface(null, Typeface.BOLD);
        TextView lblListHeader = convertView.findViewById(R.id.tv_uuid);
        ImageView send = convertView.findViewById(R.id.send);
        ImageView notify = convertView.findViewById(R.id.notify);
        send.setVisibility(View.GONE);
        notify.setVisibility(View.GONE);
        TextView pro = convertView.findViewById(R.id.tv_properties);
        pro.setVisibility(View.GONE);

        lblListHeader.setText(headerTitle);


//        int properties = (int) getProperties(groupPosition, childPosition);
//        ImageView read = convertView.findViewById(R.id.read);
//        pro.setText("Properties：");
//        if ((properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
//            pro.setText(pro.getText().toString()+" INDICATE");
//        }
//        if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) != 0) {
//            read.setVisibility(View.VISIBLE);
//            pro.setText(pro.getText().toString()+" READ");
//        }
//        if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
//            notify.setVisibility(View.VISIBLE);
//            pro.setText(pro.getText().toString()+" NOTIFY");
//            notify.setColorFilter(Color.BLACK);
//        }
//        if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0) {
//            send.setVisibility(View.VISIBLE);
//            pro.setText(pro.getText().toString()+" WRITE");
//        }
//
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public interface OnItemClickListener{
        /**
         * 点击事件回调
         * @param view 被点击的视图
         * @param uuid_service 服务UUID
         * @param uuid_characteristic 特征值UUID
         * @param type 操作类型(read/write/notify等)
         */
        void onItemClick(View view, String uuid_service,String uuid_characteristic,String type);
//        void onReadClick(View view, String uuid_service,String uuid_characteristic);
//        void onWriteClick(View view, String uuid_service,String uuid_characteristic);
    }
    /**
     * 设置点击监听器
     * @param listener 监听器实例
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
}