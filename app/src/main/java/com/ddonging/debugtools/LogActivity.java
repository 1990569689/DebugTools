package com.ddonging.debugtools;


import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogActivity extends AppCompatActivity {
//
//    SimpleAdapter mSimpleAdapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.data);
//        getSupportActionBar().hide();
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  //设置StatusBar透明
//        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        ListView listView=(ListView)findViewById(R.id.list);
//         mSimpleAdapter = new SimpleAdapter(LogActivity.this, get(LogActivity.this), R.layout.item, new String[]{"acce_x","acce_y","acce_z","gyro_x","gyro_y","gyro_z","temperature","date"}, new int[]{R.id.ax,R.id.ay,R.id.az,R.id.gx,R.id.gy,R.id.gz,R.id.temp,R.id.date});
//        listView.setAdapter(mSimpleAdapter);
//        findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog();
//            }
//        });
//    }
//    public void Dialog() {
//        LayoutInflater inflater = LayoutInflater.from(LogActivity.this);
//        View view = inflater.inflate(R.layout.dialog, null);
//        // 对话框
//        Dialog dialog=initDialog(view);
//        EditText file_edit = view.findViewById(R.id.file_edit);
//        TextView file_sure = view.findViewById(R.id.file_sure);
//        file_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (file_edit.getText().toString().equals("")) {
//                    Toast.makeText(LogActivity.this, "不能为空",Toast.LENGTH_SHORT).show();
//
//                } else {
//               export(file_edit.getText().toString());
//                }
//            }
//        });
//        TextView file_cancel = view.findViewById(R.id.file_cancel);
//        file_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.cancel();
//            }
//        });
//        // 只呈现1s
//    }
//    public Dialog initDialog(View view) {
//        final Dialog dialog = new Dialog(LogActivity.this);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.show();
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = display.getWidth() - 150; // 设置宽度
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setContentView(view);
//        return dialog;
//    }
//        private void export(String s) {
//
////        List<String> list = mSimpleAdapter.getData();
////        if(list.size()==0){
////            ToastUtil.showShort(self,"没有数据需要导出");
////            return;
////        }
//        List<Map<String, String>> objects = new ArrayList<>();
//        String[] title;
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        final String excelFileName = "/" + s +"_"+ format.format(new Date()) + ".xls";
////        List<String> all = list;
//        title = new String[]{"acce_x", "acce_y", "acce_z", "gyro_x", "gyro_y", "gyro_z","temperature","date"};
//        ArrayList<HashMap<String, Object>> data=get(LogActivity.this);
//        for (HashMap<String, Object> de : data) {
//           // String[] split = de.split(",");
//            Map<String, String> map = new LinkedHashMap<String, String>();
//            map.put("1",de.get("acce_x").toString());
//            map.put("2",de.get("acce_y").toString());
//            map.put("3", de.get("acce_z").toString());
//            map.put("4",de.get("gyro_x").toString());
//            map.put("5", de.get("gyro_y").toString());
//            map.put("6", de.get("gyro_z").toString());
//            map.put("7", de.get("temperature").toString());
//            map.put("8", de.get("date").toString());
//            objects.add(map);
//        }
//        final String filePath = "/storage/emulated/0/Download";
//        File file = new File(filePath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        ExcelUtil.Export(filePath + excelFileName, title, objects, this);
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(LogActivity.this, "文件位置：" + filePath + excelFileName,Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    protected void setDarkStatusIcon(boolean isDark) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View decorView = getWindow().getDecorView();
//            if (decorView != null) {
//                int vis = decorView.getSystemUiVisibility();
//                if (isDark) {
//                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                } else {
//                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                }
//                decorView.setSystemUiVisibility(vis);
//            }
//        }
//    }

}
