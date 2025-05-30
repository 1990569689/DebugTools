package com.ddonging.debugtools.fragmnet;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.ddonging.debugtools.adapter.ExpandableListAdapter;
import com.ddonging.debugtools.databinding.FragmentBleServiceBinding;
import com.ddonging.debugtools.entity.BleLog;
import com.ddonging.debugtools.ui.home.BleDetailActivity;
import com.ddonging.debugtools.ui.home.BleViewModel;
import com.ddonging.debugtools.utils.TextUtils;
import com.ddonging.debugtools.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BleServiceFragment extends Fragment {
    private FragmentBleServiceBinding binding;
    HashMap<String,String> listDataHeader;

    private String mac;
    private String NAME;
    ArrayList<HashMap<String, Object>> list  =new ArrayList<>();;

    UUID uuid_service,uuid_characteristic_notify;
    BleDevice bleDevice;
    private ProgressDialog progressDialog;
    String TAG = "Ble";
    ExpandableListAdapter expandableListAdapter;
    String writeData,write_uuid_service,write_uuid_characteristic;
    String format="UTF-8";
    int total=0,send=0;
    HashMap<String,  ArrayList<HashMap<String,String>>> listDataChild;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBleServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        BleViewModel bleViewModel =new ViewModelProvider(getActivity()).get(BleViewModel.class);
        mac = getActivity().getIntent().getStringExtra("MAC");
        NAME =getActivity(). getIntent().getStringExtra("NAME");
        bleDevice = getActivity().getIntent().getParcelableExtra("BLE");
        listDataHeader = new HashMap<>();
        listDataChild = new HashMap<>();
        initBle();
        bleViewModel.getFormat().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                format=s;
            }
        });
        bleViewModel.getConnection().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                {
                    initBle();
                }else
                {
                    binding.bleStatus.setVisibility(View.VISIBLE);
//                    BleManager.getInstance().disconnectAllDevice();
                    BleManager.getInstance().disconnect(bleDevice);
//                    BleManager.getInstance().destroy();
                }
            }
        });
        bleViewModel.getMtu().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                BleManager.getInstance().setMtu(bleDevice, Integer.parseInt(s), new BleMtuChangedCallback() {
                    @Override
                    public void onSetMTUFailure(BleException exception) {
                        // 设置MTU失败
                    }
                    @Override
                    public void onMtuChanged(int mtu) {
                        // 设置MTU成功，并获得当前设备传输支持的MTU值
                        bleViewModel.setData("设置MTU为"+mtu);
                    }
                });
            }
        });
        bleViewModel.getWriteData().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                writeData=s;
                BleManager.getInstance().write(
                        bleDevice,
                        write_uuid_service,
                        write_uuid_characteristic,
                        writeData.getBytes(StandardCharsets.UTF_8),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                                // 发送数据到设备成功
                                send+=justWrite.length;
                                bleViewModel.setSendTotal(send);
                                bleViewModel.setData("发送数据到设备："+new String(justWrite));
                            }
                            @Override
                            public void onWriteFailure(BleException exception) {
                                // 发送数据到设备失败
//                                bleViewModel.setData("发送数据到设备失败："+exception);
                                try {
                                    JSONObject jsonObject = new JSONObject(String.valueOf(String.valueOf(exception).substring(String.valueOf(exception).indexOf("{"))));
                                    String description=jsonObject.getString("description");
                                    bleViewModel.setData("发送数据到设备失败："+description);
                                } catch (JSONException e) {

                                }


                            }
                        });
            }
        });
        expandableListAdapter=new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        binding.expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.setOnItemClickListener(new ExpandableListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String uuid_service, String uuid_characteristic,String type) {
             if(type.equals("read"))
             {
                 BleManager.getInstance().read(
                         bleDevice,
                         uuid_service,
                         uuid_characteristic,
                         new BleReadCallback() {
                             @Override
                             public void onReadSuccess(byte[] data) {
                                 // 读特征值数据成功
                                 BleViewModel bleViewModel =new ViewModelProvider(getActivity()).get(BleViewModel.class);
                                 if(new String(data)!=null||new String(data)!="")
                                 {
                                     total+=data.length;
                                     bleViewModel.setReceiveTotal(total);
                                     if(Objects.equals(format, "UTF-8"))
                                     {
                                         bleViewModel.setData(new String(data));
                                     }else if(Objects.equals(format, "HEX"))
                                     {
                                         bleViewModel.setData(TextUtils.byteToHexString(data));
                                     }else if(Objects.equals(format, "GBK"))
                                     {
                                         try {
                                             bleViewModel.setData(new String(data,"GBK"));
                                         } catch (UnsupportedEncodingException e) {
                                             bleViewModel.setData(new String(data));
                                             throw new RuntimeException(e);

                                         }
                                     }else if(Objects.equals(format, "GB2312"))
                                     {
                                         try {
                                             bleViewModel.setData(new String(data,"GB2312"));
                                         } catch (UnsupportedEncodingException e) {
                                             bleViewModel.setData(new String(data));
                                             throw new RuntimeException(e);
                                         }
                                     }else if(Objects.equals(format, "GB18030"))
                                     {
                                         try {
                                             bleViewModel.setData(new String(data,"GB18030"));
                                         } catch (UnsupportedEncodingException e) {
                                             bleViewModel.setData(new String(data));
                                             throw new RuntimeException(e);
                                         }
                                     }

                                 }
                             }
                             @Override
                             public void onReadFailure(BleException exception) {
                                 // 读特征值数据失败
                                 bleViewModel.setData("读特征值数据失败");
                             }
                         });
             }else if(type.equals("write"))
             {
                 ToastUtil.Toast(getContext(),"选择成功");
                 write_uuid_service=uuid_service;
                 write_uuid_characteristic=uuid_characteristic;
                 bleViewModel.setData("\n选择发送的ServiceUuid:"+write_uuid_service+"\n选择发送的CharacteristicUuid:"+write_uuid_characteristic+"\n"+"请输入要发送的数据");
             }else if(type.equals("notify"))
             {
                 BleManager.getInstance().notify(
                         bleDevice,
                         uuid_service,
                         uuid_characteristic,
                         new BleNotifyCallback() {
                             @Override
                             public void onNotifySuccess() {
                                 bleViewModel.setData("通知打开成功");
                                 // 打开通知操作成功
                                 Log.w(TAG, uuid_service+"打开成功"+  uuid_characteristic);
                                 ToastUtil.Toast(getContext(),"通知打开成功");
                             }
                             @Override
                             public void onNotifyFailure(BleException exception) {
                                 // 打开通知操作失败
                                 try {
                                     JSONObject jsonObject = new JSONObject(String.valueOf(String.valueOf(exception).substring(String.valueOf(exception).indexOf("{"))));
                                     String description=jsonObject.getString("description");
                                     bleViewModel.setData("通知打开失败："+description);
                                     ToastUtil.Toast(getContext(),"通知打开失败");
                                 } catch (JSONException e) {

                                 }
                                 Log.w(TAG, uuid_service+"打开失败"+exception+  uuid_characteristic);
                             }
                             @Override
                             public void onCharacteristicChanged(byte[] data) {
                                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                                 Date date = new Date(System.currentTimeMillis());
                                 String formattedDate = formatter.format(date);
                                 HashMap<String,Object> map=new HashMap<>();
                                 map.put("date",formattedDate);
                                 total+=data.length;
                                 bleViewModel.setReceiveTotal(total);
                                // bleViewModel.setData("收到数据："+new String(data)+"\n"+"数据长度："+data.length+"\n"+"数据总长度："+total);
                                 if(Objects.equals(format, "UTF-8"))
                                 {
                                     bleViewModel.setData(new String(data));
                                     map.put("data",new String(data));
                                 }else if(Objects.equals(format, "HEX"))
                                 {
                                     bleViewModel.setData(TextUtils.byteToHexString(data));
                                     map.put("data",TextUtils.byteToHexString(data));
                                 }else if(Objects.equals(format, "GBK"))
                                 {
                                     try {
                                         bleViewModel.setData(new String(data,"GBK"));
                                         map.put("data",new String(data,"GBK"));
                                     } catch (UnsupportedEncodingException e) {
                                         bleViewModel.setData(new String(data));
                                         throw new RuntimeException(e);

                                     }
                                 }else if(Objects.equals(format, "GB2312"))
                                 {
                                     try {
                                         bleViewModel.setData(new String(data,"GB2312"));
                                         map.put("data",new String(data,"GB2312"));
                                     } catch (UnsupportedEncodingException e) {
                                         bleViewModel.setData(new String(data));
                                         throw new RuntimeException(e);
                                     }
                                 }else if(Objects.equals(format, "GB18030"))
                                 {
                                     try {
                                         bleViewModel.setData(new String(data,"GB18030"));
                                         map.put("data",new String(data,"GB18030"));
                                     } catch (UnsupportedEncodingException e) {
                                         bleViewModel.setData(new String(data));
                                         throw new RuntimeException(e);
                                     }
                                 }
                                 BleLog.setLog(map);
                             }
                         });
             }else if(type.equals("cancel_notify"))
             {
                 if(BleManager.getInstance().stopNotify(bleDevice, uuid_service, uuid_characteristic))
                 {
                     bleViewModel.setData("取消通知成功");
                 }
             }else if(type.equals("cancel_write"))
             {

             }else if(type.equals("cancel_read"))
             {

             }
            }
        });
        return root;
    }

    private void initBle() {
        BleViewModel bleViewModel =new ViewModelProvider(getActivity()).get(BleViewModel.class);
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                binding.bleStatus.setVisibility(View.GONE);
                show("Loading");
                bleViewModel.setData("开始连接");
            }
            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(String.valueOf(exception).substring(String.valueOf(exception).indexOf("{"))));
                    String description=jsonObject.getString("description");
                    bleViewModel.setData("连接失败："+description);
                } catch (JSONException e) {

                }
                ToastUtil.Toast(getContext(),"连接失败");
                binding.bleStatus.setVisibility(View.VISIBLE);
            }
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                dismiss();
                bleViewModel.setData("连接成功");
                bleViewModel.setConnectionState(true);
                ToastUtil.Toast(getContext(),"连接成功");
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    List<BluetoothGattService> serviceList = gatt.getServices();
                    int i=0;
                    for (BluetoothGattService service : serviceList) {
                        uuid_service = service.getUuid();
                        listDataHeader.put("uuid"+i,service.getUuid().toString());
                        i+=1;
                        Log.w(TAG+"123", service.getUuid().toString());
                        ArrayList<HashMap<String,String>> childList =new ArrayList<>();
                        for( BluetoothGattCharacteristic characteristicList :service.getCharacteristics())
                        {
                            HashMap<String,String> child =new HashMap<>();
                            BluetoothGattDescriptor descriptor = characteristicList.getDescriptor(UUID.fromString(characteristicList.getUuid().toString()));// 获取服务名称的描述符UUID，通常是0x2904或类似的UUID用于服务名称的描述符。
                            if (descriptor != null) {
                                byte[] value = descriptor.getValue(); // 获取描述符的值，这通常是UTF-8编码的服务名称。
                                if (value != null && value.length > 0) {
                                    String serviceName = new String(value, StandardCharsets.UTF_8); // 将字节转换为字符串获取服务名称。
                                    child.put("name","123"+serviceName);
                                }else
                                {
                                    child.put("name","Unknown Service");
                                }
                            }
                            child.put("characteristic",characteristicList.getUuid().toString());
                            child.put("properties",String.valueOf(characteristicList.getProperties()));
                            child.put("permission",String.valueOf(characteristicList.getPermissions()));
                           // child.put("descriptors",String.valueOf(characteristicList.getDescriptor(UUID.fromString(service.getUuid().toString()))));
                            child.put("value",String.valueOf(characteristicList.getValue()));
                            childList.add(child);
                            uuid_characteristic_notify= characteristicList.getUuid();
                            if((characteristicList.getProperties()&BluetoothGattCharacteristic.PROPERTY_NOTIFY)!=0)
                            {

                            }

                        }
                        listDataChild.put(service.getUuid().toString(),childList);
                        expandableListAdapter.notifyDataSetChanged();

                    }
                }, 1000); // 延迟 2 秒
            }
            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                if(!isActiveDisConnected)
                {

                    bleViewModel.setConnectionState(false);
                    ToastUtil.Toast(getContext(),"设备断开连接");
                    bleViewModel.setData("设备断开连接");
                    binding.bleStatus.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void show(final String title) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage(title);
                    //设置点击屏幕不消失
                    progressDialog.setCanceledOnTouchOutside(false);
                    //设置点击返回键不消失
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                } else {
                    progressDialog.setMessage(title);
                }
            }
        });

    }

    public void dismiss() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        BleManager.getInstance().disconnect(bleDevice);
    }

}

//BLE的基本操作流程
//通常的流程是：
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
//BLE操作中需要注意什么
//如果遇到wirte或者notify失败的情况，建议在两次操作之间间隔一段时间。比如连接成功之后，间隔100ms再进行notify或wirte，具体时间数据可以在不同手机上尝试选择一个最短的有效时间。
//
//BLE在不同的Android系统的区别
//只有在Android 4.3版本以上才可以使用BLE功能；在Android 6.0 以上使用扫描方法必须获取位置权限；某些型号手机还可能需要打开定位功能。
//
//设备主动断开连接（如关机）后，隔了很久才收到onDisconnect回调
//这个与BLE设备有关，针对不同的设备收到的回调时间不一样，建议在硬件端优化。
//
//什么情况下要分包
//一般来说，每次write限制20个字节，如果超过了20个字节，需要进行分包发送。FastBle默认会对超过20字节的数据进行分包，发送的进度可以关注每一包数据发送的回调结果onWriteSuccess(int current, int total, byte[] justWrite)。如果不需要分包，也可以选择关闭。
//
//连接成功后马上断开连接
//这个与BLE设备有关，建议在硬件端进行优化，尝试调整设备的连接参数（ConnectionInterval、SlaveLatency、 SupervisionTimeout），优化设置BLE连接过程中的传输速度和功耗。尤其是 SupervisionTimeout这个超时时间，如果BLE在这个时间内没有发生通信的话，就会自动断开。这些参数APP端无法修改。
