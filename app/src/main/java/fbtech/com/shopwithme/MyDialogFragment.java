package fbtech.com.shopwithme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fbtech.com.shopwithme.database.AppDatabase;
import fbtech.com.shopwithme.database.ItemEntity;

/**
 * Created by Ruchi on 01/Apr/2020.
 */

public class MyDialogFragment  {

    String listName,units;
    float quantity;

    public void display_dialog(Context mContext , String flag , final AppDatabase dbInstance,final int taskId) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_ist_items);
        final Spinner ut =dialog.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("kg");
        list.add("litres");
        list.add("pieces");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ut.setAdapter(dataAdapter);
        Button dialogCancel = (Button) dialog.findViewById(R.id.btnCancel);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOk = (Button) dialog.findViewById(R.id.btnOk);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            public static final String LOG_TAG = "MyDialogFragment";

            @Override
            public void onClick(View v) {
                EditText editText = dialog.findViewById(R.id.edtListName);
                EditText editText1 = dialog.findViewById(R.id.edtItemQuant);
                final String unit = ut.getSelectedItem().toString();
                listName = editText.getText().toString();

                quantity = Float.parseFloat(editText1.getText().toString());

                Log.d(LOG_TAG, "String entered is=" + listName);

                if (listName.isEmpty()) {
                    editText.setError("Enter valid Name");
                    Log.d(LOG_TAG, "List is empty");


                } else {
                    Log.d(LOG_TAG, "List is not empty");
                    AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            ItemEntity itemEntity = new ItemEntity(listName, quantity, taskId,unit);
                            dbInstance.itemDao().insertItemInList(itemEntity);

                        }
                    });

                    //enter in room
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }
}
