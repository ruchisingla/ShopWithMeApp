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
 * Created by Ruchi on 07/Mar/2020.
 */
@Dao
public interface TaskDao {

    @Query("select * from task where status='pending'")
    LiveData<List<TaskEntity>> fetchAllTask();

    @Insert
    void insertTask(TaskEntity taskEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity taskEntity);

    @Delete
    void deleteTask(TaskEntity taskEntity);

    @Query("select * from task where taskId = :taskId")
    LiveData<TaskEntity> getTaskById(int taskId);


}
