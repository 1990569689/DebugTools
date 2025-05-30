package com.ddonging.debugtools.ui.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ddonging.debugtools.Database;
import com.ddonging.debugtools.R;

import com.ddonging.debugtools.databinding.ActivityMqttNewBinding;
import com.ddonging.debugtools.utils.ToastUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MqttConnectActivity extends AppCompatActivity {
    private ActivityMqttNewBinding binding;
    private MqttClient client;
    private MqttConnectOptions options;
    private Handler handler;

    SQLiteOpenHelper dbHelper = new Database(MqttConnectActivity.this, "database.db", null, 1);
    private ScheduledExecutorService scheduler;
    private String productKey = "k1qgz60pTZR";
    private String deviceName = "Android";
    private String password="",username="";
    private String host = "tcp://iot-06z00jjc9mv2znr.mqtt.iothub.aliyuncs.com:1883";
    private String clientId = "k1qgz60pTZR.Android|securemode=2,signmethod=hmacsha256,timestamp=1746531473577|";
    private final String pub_topic = "/sys/k1qgz60pTZR/Android/thing/event/property/post";
    private final String sub_topic = "/sys/k1qgz60pTZR/Android/thing/service/property/set";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMqttNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        // setSupportActionBar(binding.getRoot().findViewById(R.id.toolbar));
        setSupportActionBar(binding.getRoot().findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mqtt");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.instanceName.getText().toString().equals("")&&!binding.username.getText().toString().equals("") &&!binding.password.getText().toString().equals("")
                        &&!binding.ipAddress.getText().toString().equals("")
                        &&!binding.clientId.getText().toString().equals("")
                        &&!binding.topic.getText().toString().equals("")
                        &&!binding.portNumber.getText().toString().equals(""))
                {
                    if(binding.ipAddress.getText().toString().startsWith("tcp://")||binding.ipAddress.getText().toString().startsWith("ssl://")||binding.ipAddress.getText().toString().startsWith("ws://")||binding.ipAddress.getText().toString().startsWith("wss://")||binding.ipAddress.getText().toString().startsWith("mqtt://")||binding.ipAddress.getText().toString().startsWith("mqtts://"))

                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date=new Date();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        if (db.isOpen()) {
                            Cursor cursorr = db.rawQuery("SELECT * FROM MqttLog WHERE name='"+binding.instanceName.getText().toString()+"'", null);
                            if (cursorr.getCount() > 0) {
                                ToastUtil.Toast(MqttConnectActivity.this,"实例已经存在");
                            }else {
                                String sql = "insert into MqttLog(name,username,password,address,sid,topic,port,date) values(\"" +binding.instanceName.getText().toString()+ "\",\""+binding.username.getText().toString()+ "\",\""+binding.password.getText().toString()+ "\",\""+binding.ipAddress.getText().toString()+ "\",\""+binding.clientId.getText().toString()+ "\",\""+binding.topic.getText().toString()+ "\",\""+binding.portNumber.getText().toString()+  "\",\"" +dateFormat.format(date) + "\")";
                                db.execSQL(sql);
                                ToastUtil.Toast(MqttConnectActivity.this,"新建成功");
                                finish();
                            }
                        }
                    }else {
                        ToastUtil.Toast(MqttConnectActivity.this,"连接地址有误");
                    }

                   galleryViewModel.setData("有一个新建连接");
                }else
                {
                    ToastUtil.Toast(MqttConnectActivity.this,"请输入完整信息");

                }
              //  mqtt_init(binding.ipAddress.getText().toString()+":"+binding.portNumber.getText().toString(),binding.clientId.getText().toString(),binding.username.getText().toString(),binding.password.getText().toString());
            }
        });


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
        menu.findItem(R.id.action_connect).setVisible(false);
        menu.findItem(R.id.action_clear).setVisible(false);
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
