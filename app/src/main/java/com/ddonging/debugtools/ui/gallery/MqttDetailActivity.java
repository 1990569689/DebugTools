package com.ddonging.debugtools.ui.gallery;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ddonging.debugtools.Database;
import com.ddonging.debugtools.R;
import com.ddonging.debugtools.databinding.ActivityMqttLogBinding;
import com.ddonging.debugtools.databinding.ActivityMqttNewBinding;
import com.ddonging.debugtools.ui.home.BleDetailActivity;
import com.ddonging.debugtools.ui.home.BleViewModel;
import com.ddonging.debugtools.utils.PreferenceUtil;
import com.ddonging.debugtools.utils.ToastUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MqttDetailActivity extends AppCompatActivity {
    private ActivityMqttLogBinding binding;
    private MqttClient client;
    private MqttConnectOptions options;
    private Handler handler;
    int send=0,get=0;
    private ScheduledExecutorService scheduler;
    private String productKey = "k1qgz60pTZR";
    private String deviceName = "Android";
    private String password="6b768dc7128178c70c9aa7fb2cc4d0839f7d44869c8444ff6b0e0e0ecdee5c37",username=deviceName+"&"+productKey;
    private String host = "tcp://iot-06z00jjc9mv2znr.mqtt.iothub.aliyuncs.com:1883";
    private String clientId = "k1qgz60pTZR.Android|securemode=2,signmethod=hmacsha256,timestamp=1746531473577|";
    private final String pub_topic = "/sys/k1qgz60pTZR/Android/thing/event/property/post";
    private final String sub_topic = "/sys/k1qgz60pTZR/Android/thing/service/property/set";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMqttLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.getRoot().findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mqtt");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        Intent intent = getIntent();
        mqtt_init(intent.getStringExtra("address").toString()+":"+intent.getStringExtra("port").toString(),intent.getStringExtra("sid").toString(),intent.getStringExtra("username").toString(),intent.getStringExtra("password").toString());
        binding.isConnected.setText("连接中");
        Log.w("MQas",intent.getStringExtra("address").toString()+":"+intent.getStringExtra("port").toString()+"\n"+intent.getStringExtra("sid").toString()+"\n"+intent.getStringExtra("username").toString()+"\n"+intent.getStringExtra("password").toString());
        mqtt_connect();
        List<String> dataset = new LinkedList<>(Arrays.asList("菜单一", "菜单二", "菜单三", "菜单四", "菜单五"));
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish_message(binding.data.getText().toString());
            }
        });
        handler = new Handler(msg -> {
            switch (msg.what) {
                case 3:
                    String message = msg.obj.toString();
                    break;
                case 30:
                    GalleryViewModel ViewModel =new ViewModelProvider(MqttDetailActivity.this).get(GalleryViewModel.class);
                    ViewModel.setConnectionState(false);
                    binding.isConnected.setText("连接失败");
                    ToastUtil.Toast(MqttDetailActivity.this,"连接失败");
                    break;
                case 31:
                      binding.isConnected.setText("已连接");
                       GalleryViewModel ViewMode =new ViewModelProvider(MqttDetailActivity.this).get(GalleryViewModel.class);
                       ViewMode.setConnectionState(true);
                      ToastUtil.Toast(MqttDetailActivity.this,"连接成功");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                    Date date = new Date(System.currentTimeMillis());
                    String formattedDate = formatter.format(date);
                    binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：MQTT连接成功");
                    try {
                            client.subscribe(intent.getStringExtra("topic").toString(), 1);
                            binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：订阅Topic成功"+"\n"+"message：成功订阅Topic:"+intent.getStringExtra("topic").toString());
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：订阅Topic失败"+"\n"+"message："+e);
                            }
                        });
                    }
                    break;
            }
            return true;
        });

    }
    private void publish_message(String message) {
        if (client == null || !client.isConnected()) {
           ToastUtil.Toast(MqttDetailActivity.this,"未连接");
            return;
        }
        if(Objects.equals(message, ""))
        {
            ToastUtil.Toast(MqttDetailActivity.this,"发送消息不能为空");
            return;
        }
        MqttMessage mqtt_message = new MqttMessage();
        mqtt_message.setPayload(message.getBytes());
        try {
            if(binding.topic.getText().toString().contains("#")||binding.topic.getText().toString().contains("+"))
            {
                ToastUtil.Toast(MqttDetailActivity.this,"主题不能包含通配符");
            }else {
                binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.Qos0) {
                            try {
                                client.publish(binding.topic.getText().toString(), message.getBytes(),0,false);
                            } catch (MqttException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                                        Date date = new Date(System.currentTimeMillis());
                                        String formattedDate = formatter.format(date);
                                        binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：发送失败"+"\n"+"message："+e);

                                    }
                                });
                            }
                        } else if (checkedId == R.id.Qos1) {
                            try {
                                client.publish(binding.topic.getText().toString(), message.getBytes(), 1, false);
                            } catch (MqttException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                                        Date date = new Date(System.currentTimeMillis());
                                        String formattedDate = formatter.format(date);
                                        binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：发送失败"+"\n"+"message："+e);

                                    }
                                });
                            }
                        }
                        else if (checkedId == R.id.Qos1) {
                            try {
                                client.publish(binding.topic.getText().toString(), message.getBytes(),2,false);
                            } catch (MqttException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                                        Date date = new Date(System.currentTimeMillis());
                                        String formattedDate = formatter.format(date);
                                        binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：发送失败"+"\n"+"message："+e);

                                    }
                                });
                            }
                        }
                    }
                });

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                Date date = new Date(System.currentTimeMillis());
                String formattedDate = formatter.format(date);
                binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"发布topic："+binding.topic.getText().toString()+"\n"+"message："+message);
                send+=message.getBytes().length;
                binding.sends.setText("成功发送："+send+"字节");
                binding.scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_mtu).setVisible(false);
        menu.findItem(R.id.action_export).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        menu.findItem(R.id.action_exit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                finish();
                return false;
            }
        });
        menu.findItem(R.id.action_clear).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                binding.mqttLog.setText("");
                ToastUtil.Toast(MqttDetailActivity.this,"日志已清空");
                return false;
            }
        });

        GalleryViewModel ViewModel =new ViewModelProvider(MqttDetailActivity.this).get(GalleryViewModel.class);
        ViewModel.getConnectionState().observe(MqttDetailActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                {
                    menu.findItem(R.id.action_connect).setTitle("断开连接");
                }else {
                    menu.findItem(R.id.action_connect).setTitle("连接");
                }
            }
        });
        menu.findItem(R.id.action_connect).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if( menu.findItem(R.id.action_connect).getTitle().equals("断开连接"))
                {
                    menu.findItem(R.id.action_connect).setTitle("连接");
                    if(client!=null)
                    {
                        if(client.isConnected())
                        {
                            try {
                                client.disconnect();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                                Date date = new Date(System.currentTimeMillis());
                                String formattedDate = formatter.format(date);
                                binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：MQTT断开连接");
                                binding.isConnected.setText("断开连接");
                            } catch (MqttException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    ViewModel.setConnection(false);
                }else {
                    //menu.findItem(R.id.action_connect).setTitle("断开连接");
                    binding.isConnected.setText("正在连接");
                    mqtt_connect();
                    ViewModel.setConnection(true);
                }
                return false;
            }
        });
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(client!=null)
        {
            if(client.isConnected())
            {
                try {
                    client.disconnect();
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private void mqtt_init(String host,String clientId,String username,String password) {
        try {
            client = new MqttClient(host, clientId, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // 超时时间，单位为秒
            options.setConnectionTimeout(PreferenceUtil.getInstance("setting",MqttDetailActivity.this).getTimeout(10));
            // 心跳包发送间隔，单位为秒
            options.setKeepAliveInterval(PreferenceUtil.getInstance("setting",MqttDetailActivity.this).getKeepAlive(60));
            // 自动重连
            options.setAutomaticReconnect(PreferenceUtil.getInstance("setting",MqttDetailActivity.this).getIsAutoReconnect(false));
            // 清除会话
            options.setCleanSession(PreferenceUtil.getInstance("setting",MqttDetailActivity.this).getIsCleanSession(false));
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                    Date date = new Date(System.currentTimeMillis());
                    String formattedDate = formatter.format(date);
                    binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：MQTT断开连接");
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            get+=message.toString().getBytes().length;
                            binding.get.setText("成功接收："+get+"字节");
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                            Date date = new Date(System.currentTimeMillis());
                            String formattedDate = formatter.format(date);
                            binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"订阅topic："+topicName+"\n"+"message："+message);
                            binding.scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.scrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                    });

                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = message.toString();
                    handler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                    Date date = new Date(System.currentTimeMillis());
                    String formattedDate = formatter.format(date);
                    binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：MQTT初始化失败"+"\n"+"message："+e);
                }
            });

        }
    }

    private void mqtt_connect() {
        new Thread(() -> {
            try {
                if (!client.isConnected()) {
                    client.connect(options);
                    handler.sendEmptyMessage(31);
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms");
                        Date date = new Date(System.currentTimeMillis());
                        String formattedDate = formatter.format(date);
                        binding.mqttLog.setText(binding.mqttLog.getText().toString()+"\n\n"+formattedDate+"\n"+"system：MQTT连接失败"+"\n"+"message："+e);

                    }
                });

                e.printStackTrace();
                handler.sendEmptyMessage(30);
            }
        }).start();
    }

}
