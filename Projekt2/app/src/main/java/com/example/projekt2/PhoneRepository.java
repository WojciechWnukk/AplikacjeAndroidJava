package com.example.projekt2;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

public class PhoneRepository {
    private PhoneDao mPhoneDao;
    private LiveData<List<Phone>> mAllPhones;

    PhoneRepository(Application application) {
        PhoneRoomDataBase phoneRoomdatabase = PhoneRoomDataBase.getDatabase(application);
        mPhoneDao = phoneRoomdatabase.phoneDao();
        mAllPhones = mPhoneDao.getAlphabetizedElements();
    }

    LiveData<List<Phone>> getAllPhones(){
        return mAllPhones;
    }

    public void insert(Phone phone) {
        PhoneRoomDataBase.databaseWriteExecutor.execute(() -> {
            mPhoneDao.insert(phone);
        });
    }

    void deleteAll() {
        PhoneRoomDataBase.databaseWriteExecutor.execute(() -> {
            mPhoneDao.deleteAll();
        });
    }

    public void deletePhone(long id) {
        PhoneRoomDataBase.databaseWriteExecutor.execute(() -> {
            mPhoneDao.deletePhone(id);
        });
    }

    public void updatePhone(Phone phone) {
        PhoneRoomDataBase.databaseWriteExecutor.execute(() -> {
            mPhoneDao.updatePhone(phone);
        });
    }


}
