package com.ddonging.debugtools.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ddonging.debugtools.R;

public class ToastUtil {
    public static void Toast (Context context, String content) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast, null);
        TextView v=view.findViewById(R.id.toast);
        v.setText(content);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}