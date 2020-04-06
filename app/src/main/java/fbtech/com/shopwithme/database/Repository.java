package fbtech.com.shopwithme.database;

import android.content.Context;

import java.util.concurrent.Executor;

import fbtech.com.shopwithme.AppExecutor;

/**
 * Created by Ruchi on 02/Apr/2020.
 */

public class Repository
{

    private AppDatabase dbInstance;
    private TaskDao taskDao;
    private Context mContext;

    public Repository( Context context) {
        this.mContext=context;
        this.dbInstance = AppDatabase.getsInstance(mContext);
        this.taskDao = dbInstance.taskDao();

        this.itemDao = dbInstance.itemDao();

    }

    private ItemDao itemDao;
    private  Executor executor = AppExecutor.getsInstance().diskIO();

    public void insertTask(final TaskEntity taskEntity)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dbInstance.taskDao().insertTask(taskEntity);
            }
        });
    }

    public void deleteTask(final TaskEntity taskEntity)
    {

    }


}
