package fbtech.com.shopwithme.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

/**
 * Created by Ruchi on 07/Mar/2020.
 */

@Database(entities = {TaskEntity.class,ItemEntity.class},version = 4,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG =AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "shopping_list";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context)
    {
   if(sInstance==null) {
       synchronized (LOCK)
       {
           Log.d(LOG_TAG,"Creating a new Database");
           sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME)
                  .addMigrations(MIGRATION_3_4).build();
       }
   }
   Log.d(LOG_TAG,"Getting database instance");
   return sInstance;
    }
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `items` ADD COLUMN units TEXT");
        }
    };

    static final Migration MIGRATION_3_4= new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `task` ADD COLUMN status TEXT");
        }
    };
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `items` (`itemId` INTEGER NOT NULL , "
                    + "`itemName` TEXT ,'taskId' INTEGER NOT NULL,'itemQuantity' REAL NOT NULL, PRIMARY KEY(`itemId`))");
        }
    };
    public abstract TaskDao taskDao();

    public abstract ItemDao itemDao();
}
