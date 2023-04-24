package com.example.projekt2;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhoneViewModel extends AndroidViewModel {
    private final PhoneRepository mRepository;
    private final LiveData<List<Phone>> mAllPhones;

    public PhoneViewModel(@NonNull Application application){
        super(application);
        mRepository = new PhoneRepository(application);
        mAllPhones = mRepository.getAllPhones();
    }

    LiveData<List<Phone>> getAllPhones(){
        return mAllPhones;
    }

    public void insert(Phone phone) {
        mRepository.insert(phone);
    }

    public void deletePhone(long id) {
        mRepository.deletePhone(id);
    }
    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void updatePhone(Phone phone) {
        mRepository.updatePhone(phone);

    }


}
