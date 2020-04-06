package fbtech.com.shopwithme.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Ruchi on 05/Mar/2020.
 */
@Entity(tableName="task")
public class TaskEntity {
    private String name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public TaskEntity(String name, Date update_date, int taskId,String status) {
        this.name = name;
        this.update_date = update_date;
        this.taskId = taskId;
        this.status= status;
    }

    @Ignore
    public TaskEntity(String name, Date update_date ,String status) {
        this.name = name;
        this.update_date = update_date;
        this.status = status;

    }

    private Date update_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @PrimaryKey(autoGenerate = true)
    private int taskId;


}
