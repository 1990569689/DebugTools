package com.ddonging.debugtools.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import com.ddonging.debugtools.R;
import com.ddonging.debugtools.adapter.MyPagerAdapter;
import com.ddonging.debugtools.databinding.ActivityBleBinding;
import com.ddonging.debugtools.entity.BleLog;
import com.ddonging.debugtools.utils.ExcelUtils;
import com.ddonging.debugtools.utils.ToastUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BleDetailActivity extends AppCompatActivity  {
        private ActivityBleBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityBleBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setSupportActionBar(binding.getRoot().findViewById(R.id.toolbar));
            getSupportActionBar().setTitle("BLE");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
            binding.viewPager.setAdapter(adapter);
            binding.tabLayout.setupWithViewPager(binding.viewPager);
            binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

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
    private void export() {

        List<Map<String, String>> objects = new ArrayList<>();
        String[] title;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String excelFileName = "/" + format.format(new Date()) + ".xls";
        title = new String[]{"time", "data"};
        ArrayList<HashMap<String, Object>> data= BleLog.getLog();
        for (HashMap<String, Object> de : data) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("1",de.get("date").toString());
            map.put("2",de.get("data").toString());
            objects.add(map);
        }
        final String filePath = "/storage/emulated/0/Download";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        ExcelUtils.Export(filePath + excelFileName, title, objects, this);

    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);

            BleViewModel bleViewModel =new ViewModelProvider(BleDetailActivity.this).get(BleViewModel.class);
            bleViewModel.getConnectionState().observe(BleDetailActivity.this, new Observer<Boolean>() {
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
            // Inflate the menu; this adds items to the action bar if it is present.
            menu.findItem(R.id.action_settings).setVisible(false);
            menu.findItem(R.id.action_about).setVisible(false);
            menu.findItem(R.id.action_exit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    bleViewModel.setConnection(false);
                    return false;
                }
            });
            menu.findItem(R.id.action_clear).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    BleViewModel bleViewModel =new ViewModelProvider(BleDetailActivity.this).get(BleViewModel.class);
                    bleViewModel.setClear(true);

                    return false;
                }
            });
            menu.findItem(R.id.action_export).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BleDetailActivity.this);
                    builder.setTitle("导出");
                    builder.setMessage("确定导出成Excel文件吗？");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            export();
                        }
                    });
                    builder.show();
                    return false;
                }
            });
            menu.findItem(R.id.action_mtu).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BleDetailActivity.this);
                    View dialogView = LayoutInflater.from(BleDetailActivity.this).inflate(R.layout.dialog_mtu, null);
                    builder.setView(dialogView);
                    TextView textView = dialogView.findViewById(R.id.mtu);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 处理点击事件
                            BleViewModel bleViewModel =new ViewModelProvider(BleDetailActivity.this).get(BleViewModel.class);
                            bleViewModel.setMtu(textView.getText().toString());
                        }
                    });
                    builder.show();
                    return false;
                }
            });
            menu.findItem(R.id.action_connect).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem item) {
                    if( menu.findItem(R.id.action_connect).getTitle().equals("断开连接"))
                    {
                        menu.findItem(R.id.action_connect).setTitle("连接");
                        bleViewModel.setConnection(false);
                    }else {
                        //menu.findItem(R.id.action_connect).setTitle("连接");
                        bleViewModel.setConnection(true);
                    }
                    return false;
                }
            });
            return false;
        }
    @Override
    public void onDestroy() {
        super.onDestroy();
        BleViewModel bleViewModel =new ViewModelProvider(BleDetailActivity.this).get(BleViewModel.class);
        bleViewModel.setConnection(false);
    }
}
