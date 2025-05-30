package com.ddonging.debugtools.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> data;
    private MutableLiveData<Boolean> connection;
    private MutableLiveData<Boolean> connectionState;
    public GalleryViewModel() {
        data = new MutableLiveData<>();
    }
    public void setData(String value) {
        if(data == null){
            data = new MutableLiveData<>();
        }
        this.data.setValue(value); }
    public LiveData<String> getData() {
        if(data == null){
            data = new MutableLiveData<>();
        }
        return data; }
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
}