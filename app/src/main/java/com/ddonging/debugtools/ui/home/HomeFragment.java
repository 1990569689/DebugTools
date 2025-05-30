package com.ddonging.debugtools.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.ddonging.debugtools.R;
import com.ddonging.debugtools.adapter.QuickAdapter;
import com.ddonging.debugtools.databinding.FragmentHomeBinding;
import com.ddonging.debugtools.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private QuickAdapter adapter;
    String TAG = "HomeFragment";
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        BleManager.getInstance().init(getActivity().getApplication());
        BleManager.getInstance().enableLog(true).setReConnectCount(1, 2000).setOperateTimeout(3000);
        binding.ble.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setNewData(new ArrayList<BleDevice>());
                ble();
            }
        });
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        //创建适配器
        adapter = new QuickAdapter();
        //给RecyclerView设置适配器
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BleDevice o = (BleDevice) adapter.getData().get(position);
                Intent intent2 = new Intent(getContext(), BleDetailActivity.class);
                intent2.putExtra("NAME", o.getName());
                intent2.putExtra("MAC", o.getMac());
                intent2.putExtra("BLE",o);
                startActivityForResult(intent2, 0);
            }
        });
        ble();
        return root;
    }
    private void ble() {
        boolean bluetoothOpened = BleManager.getInstance().isBlueEnable();
        if (!bluetoothOpened) {
            BleManager.getInstance().enableBluetooth();
        } else {
            boolean b = requestPermission();
            if (b) {
                initBle();
            }
        }
    }
    int REQUEST_PERMISSION_ACCESS_LOCATION = 1;
    private boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkAccessFinePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN)!= PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT)!= PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSION_ACCESS_LOCATION);
                return false;
            }
        }
        //做下面该做的事
        return true;
    }
    private void initBle() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
//                binding.ble.setRefreshing(true);
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                List<BleDevice> data = adapter.getData();
                if(PreferenceUtil.getInstance("data",getContext()).getIsName(false))
                {
                    if (!data.contains(bleDevice)) {
                        adapter.addData(bleDevice);
                    } else {
                    }
                }else {
                    if(!Objects.equals(bleDevice.getName(), "")&&bleDevice.getName()!=null)
                    {
                        if (!data.contains(bleDevice)) {
                            adapter.addData(bleDevice);
                        } else {
                        }
                    }

                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                //pb.setVisibility(View.GONE);
                //binding.ble.setRefreshing(false);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED&&grantResults[2] == PackageManager.PERMISSION_GRANTED&&grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    initBle();
                } else {
                    Toast.makeText(getContext(), "获取权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
        binding = null;
    }

}