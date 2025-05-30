package com.ddonging.debugtools.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class BleViewModel extends ViewModel {
    private MutableLiveData<Boolean> clear;
    private MutableLiveData<Integer> receiveTotal;
    private MutableLiveData<Integer> sendTotal;
    private MutableLiveData<String> format;
    private MutableLiveData<String> mtu;
    private MutableLiveData<String> data;
    private MutableLiveData<Boolean> connection;
    private MutableLiveData<Boolean> connectionState;
    private MutableLiveData<HashMap<String,String>> ble;
    private MutableLiveData<String> writeData;
    public MutableLiveData<String> getContent(){
        if(data == null){
            data = new MutableLiveData<>();
        }
        return data;
    }
    public void setData(String datas) {
        if(data == null){
            data = new MutableLiveData<>();
        }
        this.data.setValue(datas); }
    public LiveData<String> getData() { return data; }



    public void setMtu(String data) {
        if(mtu == null){
            mtu = new MutableLiveData<>();
        }
        this.mtu.setValue(data); }
    public LiveData<String> getMtu() {
        if(mtu == null){
            mtu = new MutableLiveData<>();
        }
        return mtu; }
    public void setWriteData(String data) { this.writeData.setValue(data); }
    public LiveData<String> getWriteData() {
        if(writeData == null){
            writeData = new MutableLiveData<>();
        }
        return writeData; }

    public void setBle(HashMap<String,String> ble) { this.ble.setValue(ble); }
    public LiveData<HashMap<String,String>> getBle() {
        if(ble == null){
            ble = new MutableLiveData<>();
        }
        return ble; }
    public void setConnectionState(boolean data) {
        if(connectionState == null){
            connectionState = new MutableLiveData<>();
        }
        this.connectionState.setValue(data); }
    public LiveData<Boolean> getConnectionState() {
        if(connectionState == null){
            connectionState = new MutableLiveData<>();
        }
        return connectionState; }
    public void setConnection(boolean data) {
        if(connection == null){
            connection = new MutableLiveData<>();
        }
        this.connection.setValue(data); }
    public LiveData<Boolean> getConnection() {
        if(connection == null){
            connection = new MutableLiveData<>();
        }
        return connection; }

    public void setClear(boolean data) {
        if(clear == null){
            clear = new MutableLiveData<>();
        }
        this.clear.setValue(data); }
    public LiveData<Boolean> getClear() {
        if(clear == null){
            clear = new MutableLiveData<>();
        }
        return clear; }




    public void setFormat(String data) {
        if(format == null){
            format = new MutableLiveData<>();
        }
        this.format.setValue(data); }
    public LiveData<String> getFormat() {
        if(format == null){
            format = new MutableLiveData<>();
        }
        return format; }

    public void setReceiveTotal(Integer data) {
        if(receiveTotal == null){
            receiveTotal = new MutableLiveData<>();
        }
        this.receiveTotal.setValue(data); }
    public LiveData<Integer> getReceiveTotal() {
        if(receiveTotal == null){
            receiveTotal = new MutableLiveData<>();
        }
        return receiveTotal; }


    public void setSendTotal(Integer data) {
        if(sendTotal == null){
            sendTotal = new MutableLiveData<>();
        }
        this.sendTotal.setValue(data); }
    public LiveData<Integer> getSendTotal() {
        if(sendTotal == null){
            sendTotal = new MutableLiveData<>();
        }
        return sendTotal; }
}
