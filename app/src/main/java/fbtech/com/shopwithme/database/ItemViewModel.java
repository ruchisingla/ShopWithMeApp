package fbtech.com.shopwithme.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Ruchi on 27/Mar/2020.
 */

public class ItemViewModel extends ViewModel {

    private LiveData<List<ItemEntity>> tasks;
    public ItemViewModel(AppDatabase dbInstance,int taskId) {


        tasks = dbInstance.itemDao().fetchallitemsofTask(taskId);

    }

    public LiveData<List<ItemEntity>> getItems()
    {
        return tasks;
    }
}
