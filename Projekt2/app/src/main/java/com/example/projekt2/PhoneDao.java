package com.example.projekt2;

        import androidx.lifecycle.LiveData;
        import androidx.room.Dao;
        import androidx.room.Insert;
        import androidx.room.OnConflictStrategy;
        import androidx.room.Query;
        import androidx.room.Update;

        import java.util.List;

@Dao
public interface PhoneDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Phone phone);

    @Query("SELECT * FROM Phones")
    List<Phone> getAllPhones();

    @Query("DELETE FROM Phones")
    void deleteAll();

    @Query("SELECT * FROM Phones ORDER BY Phone_Name ASC")
    LiveData<List<Phone>> getAlphabetizedElements();

    @Query("DELETE FROM Phones WHERE id = :id")
    void deletePhone(long id);

    @Update
    void updatePhone(Phone phone);

}
