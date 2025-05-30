package com.ddonging.debugtools.ui.gallery;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ddonging.debugtools.Database;
import com.ddonging.debugtools.LogActivity;
import com.ddonging.debugtools.R;
import com.ddonging.debugtools.databinding.FragmentGalleryBinding;
import com.ddonging.debugtools.ui.home.BleDetailActivity;
import com.ddonging.debugtools.ui.home.BleViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GalleryFragment extends Fragment {
    SimpleAdapter mSimpleAdapter;
    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        galleryViewModel.getData().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mSimpleAdapter = new SimpleAdapter(getActivity(), get(getContext()), R.layout.item_mqtt, new String[]{"name","username","password","address","port","sid","date"}, new int[]{R.id.name,R.id.username,R.id.password,R.id.host,R.id.port,R.id.sid,R.id.date});
                binding.listView.setAdapter(mSimpleAdapter);
                binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
            }
        });
        mSimpleAdapter = new SimpleAdapter(getActivity(), get(getContext()), R.layout.item_mqtt, new String[]{"name","username","password","address","port","sid","date"}, new int[]{R.id.name,R.id.username,R.id.password,R.id.host,R.id.port,R.id.sid,R.id.date});
        binding.listView.setAdapter(mSimpleAdapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MqttDetailActivity.class);
                intent.putExtra("name",get(getContext()).get(position).get("name").toString());
                intent.putExtra("username",get(getContext()).get(position).get("username").toString());
                intent.putExtra("password",get(getContext()).get(position).get("password").toString());
                intent.putExtra("address",get(getContext()).get(position).get("address").toString());
                intent.putExtra("sid",get(getContext()).get(position).get("sid").toString());
                intent.putExtra("topic",get(getContext()).get(position).get("topic").toString());
                intent.putExtra("port",get(getContext()).get(position).get("port").toString());
                startActivity(intent);
            }
        });
        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mqtt, null);
                builder.setView(dialogView);
                EditText username = dialogView.findViewById(R.id.username);
                EditText password = dialogView.findViewById(R.id.password);
                EditText name = dialogView.findViewById(R.id.name);
                EditText host = dialogView.findViewById(R.id.host);
                EditText sid = dialogView.findViewById(R.id.sid);
                EditText topic = dialogView.findViewById(R.id.topic);
                username.setText(get(getContext()).get(position).get("username").toString());
                password.setText(get(getContext()).get(position).get("password").toString());
                name.setText(get(getContext()).get(position).get("name").toString());
                host.setText(get(getContext()).get(position).get("address").toString()+":"+get(getContext()).get(position).get("port").toString());
                sid.setText(get(getContext()).get(position).get("sid").toString());
                topic.setText(get(getContext()).get(position).get("topic").toString());
                TextView delete = dialogView.findViewById(R.id.delete);
                TextView update = dialogView.findViewById(R.id.update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteOpenHelper dbHelper = new Database(getActivity(), "database.db", null, 1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        if (db.isOpen()) {
                            Cursor cursorr = db.rawQuery("SELECT * FROM MqttLog", null);
                            if (cursorr.getCount() > 0) {
                                while (cursorr.moveToNext()) {
                                    if(cursorr.getString(cursorr.getColumnIndex("name")).equals(get(getContext()).get(position).get("name").toString())){
                                      ContentValues values = new ContentValues();
                                      values.put("name",name.getText().toString());
                                      values.put("username",username.getText().toString());
                                      values.put("password",password.getText().toString());

                                      values.put("address",host.getText().toString().split(":")[host.getText().toString().split(":").length-3]+":"+host.getText().toString().split(":")[host.getText().toString().split(":").length-2]);
                                      values.put("port",host.getText().toString().split(":")[host.getText().toString().split(":").length-1]);

                                      values.put("sid",sid.getText().toString());
                                      values.put("topic",topic.getText().toString());
                                      values.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                      db.update("MqttLog",values,"name=?",new String[]{get(getContext()).get(position).get("name").toString()});

                                        mSimpleAdapter = new SimpleAdapter(getActivity(), get(getContext()), R.layout.item_mqtt, new String[]{"name","username","password","address","port","sid","date"}, new int[]{R.id.name,R.id.username,R.id.password,R.id.host,R.id.port,R.id.sid,R.id.date});
                                        binding.listView.setAdapter(mSimpleAdapter);
                                        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent = new Intent(getActivity(), MqttDetailActivity.class);
                                                intent.putExtra("name",get(getContext()).get(position).get("name").toString());
                                                intent.putExtra("username",get(getContext()).get(position).get("username").toString());
                                                intent.putExtra("password",get(getContext()).get(position).get("password").toString());
                                                intent.putExtra("address",get(getContext()).get(position).get("address").toString());
                                                intent.putExtra("sid",get(getContext()).get(position).get("sid").toString());
                                                intent.putExtra("topic",get(getContext()).get(position).get("topic").toString());
                                                intent.putExtra("port",get(getContext()).get(position).get("port").toString());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                }
                            }
                        }
                      builder.dismiss();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteOpenHelper dbHelper = new Database(getActivity(), "database.db", null, 1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        TextView name=view.findViewById(R.id.name);
                        if (db.isOpen()) {
                            Cursor cursorr = db.rawQuery("SELECT * FROM MqttLog", null);
                            if (cursorr.getCount() > 0) {
                                while (cursorr.moveToNext()) {
                                    if(cursorr.getString(cursorr.getColumnIndex("name")).equals(name.getText().toString())){
                                        db.delete("MqttLog","name=?",new String[]{name.getText().toString()});

                                        mSimpleAdapter = new SimpleAdapter(getActivity(), get(getContext()), R.layout.item_mqtt, new String[]{"name","username","password","address","port","sid","date"}, new int[]{R.id.name,R.id.username,R.id.password,R.id.host,R.id.port,R.id.sid,R.id.date});
                                        binding.listView.setAdapter(mSimpleAdapter);
                                        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent = new Intent(getActivity(), MqttDetailActivity.class);
                                                intent.putExtra("name",get(getContext()).get(position).get("name").toString());
                                                intent.putExtra("username",get(getContext()).get(position).get("username").toString());
                                                intent.putExtra("password",get(getContext()).get(position).get("password").toString());
                                                intent.putExtra("address",get(getContext()).get(position).get("address").toString());
                                                intent.putExtra("sid",get(getContext()).get(position).get("sid").toString());
                                                intent.putExtra("topic",get(getContext()).get(position).get("topic").toString());
                                                intent.putExtra("port",get(getContext()).get(position).get("port").toString());
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                        builder.dismiss();
                    }
                });
                builder.show();

                return true;
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), MqttConnectActivity.class),1);
            }
        });
        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mSimpleAdapter = new SimpleAdapter(getActivity(), get(getContext()), R.layout.item_mqtt, new String[]{"name","username","password","address","port","sid","date"}, new int[]{R.id.name,R.id.username,R.id.password,R.id.host,R.id.port,R.id.sid,R.id.date});
            binding.listView.setAdapter(mSimpleAdapter);
            binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), MqttDetailActivity.class);
                    intent.putExtra("name",get(getContext()).get(position).get("name").toString());
                    intent.putExtra("username",get(getContext()).get(position).get("username").toString());
                    intent.putExtra("password",get(getContext()).get(position).get("password").toString());
                    intent.putExtra("address",get(getContext()).get(position).get("address").toString());
                    intent.putExtra("sid",get(getContext()).get(position).get("sid").toString());
                    intent.putExtra("topic",get(getContext()).get(position).get("topic").toString());
                    intent.putExtra("port",get(getContext()).get(position).get("port").toString());
                    startActivity(intent);
                }
            });
        }
    }

    public static ArrayList<HashMap<String, Object>> get(Context context) {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String,Object>>();
        SQLiteOpenHelper dbHelper = new Database(context, "database.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursorr = db.rawQuery("SELECT * FROM MqttLog", null);
            if (cursorr.getCount() > 0) {
                while (cursorr.moveToNext()) {
                    HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                    tempHashMap.put("name",cursorr.getString(cursorr.getColumnIndex("name")));
                    tempHashMap.put("username",cursorr.getString(cursorr.getColumnIndex("username")));
                    tempHashMap.put("password",cursorr.getString(cursorr.getColumnIndex("password")));
                    tempHashMap.put("address",cursorr.getString(cursorr.getColumnIndex("address")));
                    tempHashMap.put("sid",cursorr.getString(cursorr.getColumnIndex("sid")));
                    tempHashMap.put("topic",cursorr.getString(cursorr.getColumnIndex("topic")));
                    tempHashMap.put("port",cursorr.getString(cursorr.getColumnIndex("port")));
                    tempHashMap.put("date",cursorr.getString(cursorr.getColumnIndex("date")));
                    arrayList.add(tempHashMap);
                }
            }
        }
        return arrayList;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}