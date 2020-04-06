package fbtech.com.shopwithme;

import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Ruchi on 14/Mar/2020.
 */

public class AppExecutor {

    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final Executor diskIO;
    private final  Executor mainThread;
    private final Executor networkIO;

    public AppExecutor(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static AppExecutor getsInstance()
    {

        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO()
    {
        return diskIO;
    }

    public Executor mainThread()
    {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor
    {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {mainThreadHandler.post(command);
        }
    }
}
