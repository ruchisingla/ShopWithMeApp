package fbtech.com.shopwithme.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

/**
 * Created by Ruchi on 25/Mar/2020.
 */

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntity> mTask;


    public LiveData<TaskEntity> getmTasks()
    {
        return mTask;
    }

    public AddTaskViewModel(AppDatabase database,int taskId)
    {
        mTask = database.taskDao().getTaskById(taskId);
    }

}
