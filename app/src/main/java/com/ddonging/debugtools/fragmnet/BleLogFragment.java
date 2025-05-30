package com.ddonging.debugtools.fragmnet;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.ddonging.debugtools.R;
import com.ddonging.debugtools.adapter.ExpandableListAdapter;
import com.ddonging.debugtools.databinding.FragmentBleLogBinding;
import com.ddonging.debugtools.databinding.FragmentBleServiceBinding;
import com.ddonging.debugtools.ui.home.BleDetailActivity;
import com.ddonging.debugtools.ui.home.BleViewModel;
import com.ddonging.debugtools.utils.PreferenceUtil;
import com.ddonging.debugtools.utils.ToastUtil;
import com.ddonging.debugtools.utils.lineChartHandle;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BleLogFragment extends Fragment {
    private FragmentBleLogBinding binding;
    String formats="UTF-8";
    Timer timer;
    String TAG = "Ble";
    lineChartHandle lineChart;
    HashMap<String,  ArrayList<HashMap<String,String>>> listDataChild;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBleLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lineChart = new lineChartHandle();
        lineChart.initLineChart(binding.chart);
        binding.time.setText(PreferenceUtil.getInstance("data",getContext()).getTime(1000).toString()+"ms");

        binding.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mtu, null);
                builder.setView(dialogView);
                TextView textView = dialogView.findViewById(R.id.mtu);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("设置连续发送间隔");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(textView.getText().toString()==null || textView.getText().toString().equals(""))
                        {
                            ToastUtil.Toast(getContext(),"不能为空");
                        }else {
                            // 处理点击事件
                            PreferenceUtil.getInstance("data", getContext()).setTime(Integer.parseInt(textView.getText().toString()));
                            binding.time.setText(PreferenceUtil.getInstance("data", getContext()).getTime(1000).toString() + "ms");
                        }
                    }
                });
                builder.show();
            }
        });
        binding.charts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    binding.chart.setVisibility(View.VISIBLE);

                }else {
                    binding.chart.setVisibility(View.GONE);

                }
            }
        });
        BleViewModel  bleViewModel= new ViewModelProvider(getActivity()).get(BleViewModel.class);
        bleViewModel.getClear().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.bleLog.setText("");
            }
        });
        bleViewModel.getReceiveTotal().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.receive.setText("成功接收："+integer+"字节");
            }
        });
        bleViewModel.getSendTotal().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.sends.setText("成功发送："+integer+"字节");
            }
        });
        binding.format.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_format, null);
                builder.setView(dialogView);
                RadioGroup f = dialogView.findViewById(R.id.format);
                if(formats.equals("HEX")){
                    f.check(R.id.hex);
                }else if(formats.equals("GBK")){
                    f.check(R.id.gbk);
                }else if(formats.equals("UTF-8")){
                    f.check(R.id.utf8);
                }else if(formats.equals("GB2312")){
                    f.check(R.id.gb2312);
                }else if(formats.equals("GB18030")){
                    f.check(R.id.gb18030);
                }
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理点击事件
                        if(f.getCheckedRadioButtonId()==R.id.hex){
                            binding.formatText.setText("HEX");
                            bleViewModel.setFormat("HEX");
                            formats="HEX";
                        }else if(f.getCheckedRadioButtonId()==R.id.gbk){
                            binding.formatText.setText("GBK");
                            formats="GBK";
                            bleViewModel.setFormat("GBK");
                        }else if(f.getCheckedRadioButtonId()==R.id.utf8){
                            binding.formatText.setText("UTF-8");
                            formats="UTF-8";
                            bleViewModel.setFormat("UTF-8");
                        }else if(f.getCheckedRadioButtonId()==R.id.gb2312){
                            binding.formatText.setText("GB2312");
                            formats="GB2312";
                            bleViewModel.setFormat("GB2312");
                        }else if(f.getCheckedRadioButtonId()==R.id.gb18030){
                            binding.formatText.setText("GB18030");
                            formats="GB18030";
                            bleViewModel.setFormat("GB18030");
                        }

                    }
                });
                builder.show();
            }
        });

        //监听了content属性变化,只要触发了setValue/postValue方法就会走
        bleViewModel.getContent().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                try {
                    Float a=Float.valueOf(s);
                    lineChart.addLine1Data(Float.valueOf(a));
                }catch (NumberFormatException e)
                {

                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                Date date = new Date(System.currentTimeMillis());
                String formattedDate = formatter.format(date);
                if(PreferenceUtil.getInstance("data",getContext()).getIsTime(true))
                {
                    binding.bleLog.setText(binding.bleLog.getText().toString()+"\n"+formattedDate+"-->"+s);
                }else{
                    binding.bleLog.setText(binding.bleLog.getText().toString()+"\n"+"-->"+s);
                }
                if(PreferenceUtil.getInstance("data",getContext()).getIsAutoScroll(true)){
                    binding.scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }else {

                }

            }
        });
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton) {
                binding.send.setText("单次发送");
            } else if (checkedId == R.id.radioButton2) {
                binding.send.setText("连续发送");
            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.send.getText().toString().equals("连续发送")){
                    binding.send.setText("暂停发送");
                    timer= new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 在这里编写需要执行的代码
                            Log.d(TAG, "run: ");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bleViewModel.setWriteData(binding.data.getText().toString());
                                }
                            });

                        }
                    }, 0,    PreferenceUtil.getInstance("data",getContext()).getTime(1000)); // 1000 表示每1秒执行一次
                }else if(binding.send.getText().toString().equals("单次发送")){
                    if(timer != null){
                        timer.cancel();
                        timer = null;
                    }
                    bleViewModel.setWriteData(binding.data.getText().toString());
                }else if (binding.send.getText().toString().equals("暂停发送")){
                    binding.send.setText("连续发送");
                    if(timer != null){
                        timer.cancel();
                        timer = null;
                    }
                }

            }
        });
        return root;
    }



}
//Q&A
//1. 这个库可以用在哪些开发场景
//在支持BLE的Android手机上开发一个APP，通过这个APP去连接外围蓝牙设备，如小米手环，蓝牙耳机，蓝牙血压计，蓝牙门锁等。这个库可以方便地帮你实现一些基本BLE操作，而不需要去考虑蓝牙连接过程中系统发出的各种状态通知。
//
//2. BLE的基本操作流程
//通常的流程是：
//
//第一步，扫描周围已打开蓝牙的BLE设备。
//第二步，扫描结束后在扫描的结果中选取一个符合条件的BLE设备，在APP扫描（BLE设备广播）的过程中，BLE设备会有以下几个属性用于辨别身份：蓝牙名、MAC、广播数据。
//第三步，对选取的BLE设备进行连接。
//第四步，连接成功后可以列出这个设备所包含的所有服务和特征，服务和特征是APP与设备进行交互的通道。
//第五步，对指定的特征进行通知、读、写等操作。常用的操作是notify和wirte，前者是APP接收BLE发过来的数据，后者是APP向BLE设备发送数据。
//第六步，APP与BLE设备断开连接。
//其他流程中的一些技巧：
//
//并不是每次都需要扫描设备后再进行连接，假如之前已经扫描到过这个设备，或者从其他方式已知你需要的设备的MAC，则可以将该设备对象或者其MAC保存起来，下次需要对其进行操作的时候，直接连接该设备，省去扫描的步骤，加快流程。
//连接成功之后，如果已知你需要的服务和特征的UUID，可以直接对其进行操作，并不一定需要将设备的所有服务和特征列出来。
//APP与设备的连接与系统挂钩，不与APP的生命周期挂钩，假如你不主动调用disconnect或者destory方法，当APP退出后，设备与APP依然会是连接状态。假如在APP或页面切换过程中，你丢失了原先的连接回调，可以再次对已连接的设备MAC进行connect，快速建立连接通知的桥梁。
//3. BLE操作中需要注意什么
//如果遇到wirte或者notify失败的情况，建议在两次操作之间间隔一段时间。比如连接成功之后，间隔100ms再进行notify或wirte，具体时间数据可以在不同手机上尝试选择一个最短的有效时间。
//
//4. BLE在不同的Android系统的区别
//只有在Android 4.3版本以上才可以使用BLE功能；在Android 6.0 以上使用扫描方法必须获取位置权限；某些型号手机还可能需要打开定位功能。
//
//5. 设备主动断开连接（如关机）后，隔了很久才收到onDisconnect回调
//这个与BLE设备有关，针对不同的设备收到的回调时间不一样，建议在硬件端优化。
//
//6. 什么情况下要分包
//一般来说，每次write限制20个字节，如果超过了20个字节，需要进行分包发送。FastBle默认会对超过20字节的数据进行分包，发送的进度可以关注每一包数据发送的回调结果onWriteSuccess(int current, int total, byte[] justWrite)。如果不需要分包，也可以选择关闭。
//
//7. 连接成功后马上断开连接
//这个与BLE设备有关，建议在硬件端进行优化，尝试调整设备的连接参数（ConnectionInterval、SlaveLatency、 SupervisionTimeout），优化设置BLE连接过程中的传输速度和功耗。尤其是 SupervisionTimeout这个超时时间，如果BLE在这个时间内没有发生通信的话，就会自动断开。这些参数APP端无法修改。
