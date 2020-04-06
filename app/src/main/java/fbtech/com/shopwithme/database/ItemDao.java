package fbtech.com.shopwithme.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Ruchi on 08/Mar/2020.
 */
@Dao
public interface ItemDao {

    @Query("select * from items where taskId = :taskId")
    LiveData<List<ItemEntity>> fetchallitemsofTask(int taskId);

    @Insert
    public void insertItemInList(ItemEntity itemEntity);

    @Query("delete from items where taskId = :taskId and itemId =:itemId")
    public void deleteItemInList(int taskId ,int itemId);

    @Delete
    public void deleteItem(ItemEntity itemEntity);

    @Query("select * from items where taskId = :taskId")
    List<ItemEntity> fetchitemsofTask(int taskId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(ItemEntity taskEntity);
}
