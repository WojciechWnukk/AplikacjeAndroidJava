package com.example.projekt2;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Phone.class}, version = 2, exportSchema = false)
public abstract class PhoneRoomDataBase extends RoomDatabase {

    public abstract PhoneDao phoneDao();
    private static volatile PhoneRoomDataBase INSTANCE;

    static PhoneRoomDataBase getDatabase(final Context context) {
        if(INSTANCE==null){
            synchronized (PhoneRoomDataBase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PhoneRoomDataBase.class, "Phones")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor  = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(()-> {
                PhoneDao dao = INSTANCE.phoneDao();
            });
        }
    };
}
