package com.example.projekt2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Phones")
public class Phone {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "Phone_Name")
    private String PhoneName;

    @NonNull
    @ColumnInfo(name = "producer")
    private String PhoneProducer;

    @NonNull
    @ColumnInfo(name = "model")
    private String PhoneModel;

    @NonNull
    @ColumnInfo(name = "AndroidVersion")
    private String AndroidVersion;

    @NonNull
    @ColumnInfo(name = "Page")
    private String PhonePage;

    public Phone() {
    }

    public Phone(@NonNull String phoneName, @NonNull String phoneProducer, @NonNull String phoneModel, @NonNull String androidVersion, @NonNull String phonePage) {
        PhoneName = phoneName;
        PhoneProducer = phoneProducer;
        PhoneModel = phoneModel;
        AndroidVersion = androidVersion;
        PhonePage = phonePage;
    }

    public Phone(long id, @NonNull String phoneName, @NonNull String phoneProducer, @NonNull String phoneModel, @NonNull String androidVersion, @NonNull String phonePage) {
        this.id = id;
        PhoneName = phoneName;
        PhoneProducer = phoneProducer;
        PhoneModel = phoneModel;
        AndroidVersion = androidVersion;
        PhonePage = phonePage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getPhoneName() {
        return PhoneName;
    }

    public void setPhoneName(@NonNull String phoneName) {
        PhoneName = phoneName;
    }

    @NonNull
    public String getPhoneProducer() {
        return PhoneProducer;
    }

    public void setPhoneProducer(@NonNull String phoneProducer) {
        PhoneProducer = phoneProducer;
    }

    @NonNull
    public String getPhoneModel() {
        return PhoneModel;
    }

    public void setPhoneModel(@NonNull String phoneModel) {
        PhoneModel = phoneModel;
    }

    @NonNull
    public String getAndroidVersion() {
        return AndroidVersion;
    }

    public void setAndroidVersion(@NonNull String androidVersion) {
        AndroidVersion = androidVersion;
    }

    @NonNull
    public String getPhonePage() {
        return PhonePage;
    }

    public void setPhonePage(@NonNull String phonePage) {
        PhonePage = phonePage;
    }
}
