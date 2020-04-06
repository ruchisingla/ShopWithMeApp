package fbtech.com.shopwithme.database;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ruchi on 07/Mar/2020.
 */

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {


      return timestamp == null ?null : new Date(timestamp);


    }

    @TypeConverter
    public static Long toTimestamp(Date date)
    {
        return date == null ? null :date.getTime();
    }
}
