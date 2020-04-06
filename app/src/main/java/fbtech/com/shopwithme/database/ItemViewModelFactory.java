package fbtech.com.shopwithme.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

/**
 * Created by Ruchi on 27/Mar/2020.
 */

public class ItemViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase dbInstance;
    private final int mTaskId;

    public ItemViewModelFactory(AppDatabase dbInstance, int mTaskId) {
        this.dbInstance = dbInstance;
        this.mTaskId = mTaskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        return (T) new ItemViewModel(dbInstance,mTaskId);
    }
}
