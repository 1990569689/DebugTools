package com.ddonging.debugtools.adapter;

import android.bluetooth.BluetoothProfile;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.ddonging.debugtools.R;
import com.ddonging.debugtools.utils.PreferenceUtil;

import java.util.Objects;

public class QuickAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {
    public QuickAdapter() {
        super(R.layout.item_scan_device);
    }

    @Override
    protected void convert(BaseViewHolder helper, BleDevice item) {
        if (BleManager.getInstance().getConnectState(item)== BluetoothProfile.STATE_CONNECTED) {
            helper.setText(R.id.tv_device_state, "已连接");
        } else if(BleManager.getInstance().getConnectState(item)== BluetoothProfile.STATE_CONNECTING) {
            helper.setText(R.id.tv_device_state, "正在连接");
        } else if(BleManager.getInstance().getConnectState(item)== BluetoothProfile.STATE_DISCONNECTING) {
            helper.setText(R.id.tv_device_state, "正在断开");
        }else if(BleManager.getInstance().getConnectState(item)== BluetoothProfile.STATE_DISCONNECTED) {
            helper.setText(R.id.tv_device_state, "未连接");
        }else {
            helper.setText(R.id.tv_device_state, "未连接");
        }
        if(Objects.equals(item.getName(),"")||item.getName()==null)
        {
            helper.setText(R.id.deviceName, "不知名设备").setText(R.id.deviceMac, item.getMac()).setText(R.id.deviceRssi, String.valueOf(item.getRssi()+"dbm"));

        }else
        {
            helper.setText(R.id.deviceName, item.getName()).setText(R.id.deviceMac, item.getMac()).setText(R.id.deviceRssi, String.valueOf(item.getRssi()+"dbm"));

        }
              if(!PreferenceUtil.getInstance("data",getContext()).getIsMac(true)){
            helper.setGone(R.id.deviceMac,true);
        }else {
            helper.setGone(R.id.deviceMac,false);
        }
        if(!PreferenceUtil.getInstance("data",getContext()).getIsRssi(true)){
            helper.setGone(R.id.deviceRssi,true);
        }else {
            helper.setGone(R.id.deviceRssi,false);
        }
    }

}
