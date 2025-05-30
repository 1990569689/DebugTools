package com.ddonging.debugtools.ui.slideshow;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ddonging.debugtools.R;
import com.ddonging.debugtools.databinding.FragmentSlideshowBinding;
import com.ddonging.debugtools.ui.home.BleDetailActivity;
import com.ddonging.debugtools.ui.home.BleViewModel;
import com.ddonging.debugtools.utils.PreferenceUtil;
import com.ddonging.debugtools.utils.ToastUtil;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //PreferenceUtil.getInstance("data",getContext()).getIsMac(true);
        binding.isMac.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsMac(true));
        binding.isRssi.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsRssi(true));
        binding.isAutoScroll.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsAutoScroll(true));
        binding.isTime.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsTime(true));

        binding.isName.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsName(false));
        binding.isCleanSession.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsCleanSession(false));
        binding.time.setText(PreferenceUtil.getInstance("data",getContext()).getTime(1000).toString()+"ms");
        binding.interval.setText(PreferenceUtil.getInstance("data",getContext()).getInterval(2000).toString()+"ms");
        binding.operate.setText(PreferenceUtil.getInstance("data",getContext()).getOperate(3000).toString()+"ms");
        binding.isCleanSession.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsCleanSession(isChecked);
            }
        });
        binding.isAutoReconnect.setChecked(PreferenceUtil.getInstance("data",getContext()).getIsAutoReconnect(false));
        binding.timeout.setText(PreferenceUtil.getInstance("data",getContext()).getTimeout(10).toString()+"s");
        binding.keepalive.setText(PreferenceUtil.getInstance("data",getContext()).getKeepAlive(60).toString()+"s");
        binding.isAutoReconnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsAutoReconnect(isChecked);
            }
        });
        binding.setKeepalive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mtu, null);
                builder.setView(dialogView);
                TextView textView = dialogView.findViewById(R.id.mtu);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("设置心跳间隔");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(textView.getText().toString()==null || textView.getText().toString().equals(""))
                        {
                            ToastUtil.Toast(getContext(),"不能为空");
                        }else {
                            PreferenceUtil.getInstance("data",getContext()).setKeepalive(Integer.parseInt(textView.getText().toString()));
                            binding.keepalive.setText(PreferenceUtil.getInstance("data",getContext()).getKeepAlive(60).toString()+"s");
                        }
                        // 处理点击事件

                    }
                });
                builder.show();
            }
        });
        binding.setTimeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mtu, null);
                builder.setView(dialogView);
                TextView textView = dialogView.findViewById(R.id.mtu);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("设置连接超时时间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(textView.getText().toString()==null || textView.getText().toString().equals(""))
                        {
                            ToastUtil.Toast(getContext(),"不能为空");
                        }else {
                            PreferenceUtil.getInstance("data",getContext()).setTimeout(Integer.parseInt(textView.getText().toString()));
                            binding.timeout.setText(PreferenceUtil.getInstance("data",getContext()).getTimeout(30).toString()+"s");
                        }
                        // 处理点击事件

                    }
                });
                builder.show();
            }
        });







        binding.isName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsName(isChecked);
            }
        });
        binding.setInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mtu, null);
                builder.setView(dialogView);
                TextView textView = dialogView.findViewById(R.id.mtu);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("设置Interval时间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(textView.getText().toString()==null || textView.getText().toString().equals(""))
                        {
                            ToastUtil.Toast(getContext(),"不能为空");
                        }else {
                            PreferenceUtil.getInstance("data",getContext()).setInterval(Integer.parseInt(textView.getText().toString()));
                            binding.time.setText(PreferenceUtil.getInstance("data",getContext()).getInterval(2000).toString()+"ms");
                        }
                        // 处理点击事件

                    }
                });
                builder.show();
            }
        });
        binding.setOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mtu, null);
                builder.setView(dialogView);
                TextView textView = dialogView.findViewById(R.id.mtu);
                TextView title = dialogView.findViewById(R.id.title);
                title.setText("设置Operate超时时间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 处理点击事件
                        if(textView.getText().toString()==null || textView.getText().toString().equals(""))
                        {
                            ToastUtil.Toast(getContext(),"不能为空");
                        }else {
                            PreferenceUtil.getInstance("data", getContext()).setOperate(Integer.parseInt(textView.getText().toString()));
                            binding.time.setText(PreferenceUtil.getInstance("data", getContext()).getOperate(3000).toString() + "ms");
                        }
                    }
                });
                builder.show();
            }
        });
        binding.isAutoScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsAutoScroll(isChecked);
            }
        });
        binding.isTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsTime(isChecked);
            }
        });
        binding.isMac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsMac(isChecked);
            }
        });
        binding.isRssi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.getInstance("data",getContext()).setIsRssi(isChecked);
            }
        });
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}