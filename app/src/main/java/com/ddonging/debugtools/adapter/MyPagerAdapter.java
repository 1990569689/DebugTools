package com.ddonging.debugtools.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ddonging.debugtools.fragmnet.BleLogFragment;
import com.ddonging.debugtools.fragmnet.BleServiceFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    String[] tabTitles = {"BLE服务", "BLE日志"};
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BleServiceFragment();
            case 1:
                return new BleLogFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2; // 总页数为3
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position]; // 总页数为3
    }
}