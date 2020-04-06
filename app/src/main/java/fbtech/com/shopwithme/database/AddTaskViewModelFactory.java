package fbtech.com.shopwithme.database;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

/**
 * Created by Ruchi on 25/Mar/2020.
 */

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase dbInstance;
    private final int mTaskId;

    public AddTaskViewModelFactory(AppDatabase dbInstance, int mTaskId) {
        this.dbInstance = dbInstance;
        this.mTaskId = mTaskId;
    }
}
