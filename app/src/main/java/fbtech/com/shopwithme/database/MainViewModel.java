package fbtech.com.shopwithme.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Ruchi on 20/Mar/2020.pu
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<TaskEntity>>  tasks;
    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        tasks = database.taskDao().fetchAllTask();

    }
    public LiveData<List <TaskEntity>> getTasks()
    {
        return tasks;
    }


    public void updateTask(Context context ,TaskEntity taskEntity)
    {
        AppDatabase database =AppDatabase.getsInstance(context);
        database.taskDao().updateTask(taskEntity);

    }

}
