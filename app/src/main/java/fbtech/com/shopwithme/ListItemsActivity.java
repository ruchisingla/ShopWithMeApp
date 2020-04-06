package fbtech.com.shopwithme;

import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fbtech.com.shopwithme.database.AppDatabase;
import fbtech.com.shopwithme.database.ItemEntity;
import fbtech.com.shopwithme.database.ItemViewModel;
import fbtech.com.shopwithme.database.ItemViewModelFactory;

public class ListItemsActivity extends AppCompatActivity implements ItemAdapter.onItemClickListner {
    private static int taskId = 0;

    public AppDatabase dbInstance = null;
    private static final String LOG_TAG = ListItemsActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private final int REQ_CODE = 100;
    private String listName,units;
    private float quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        taskId = getIntent().getIntExtra("taskId", 0);
        Log.d(LOG_TAG, "the taskId value is" + taskId);
        dbInstance = AppDatabase.getsInstance(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListItemsActivity.this));
        recyclerView.setHasFixedSize(true);
        itemAdapter = new ItemAdapter(ListItemsActivity.this,this);
        recyclerView.setAdapter(itemAdapter);
        fetchitemList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act2, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void fetchitemList()
    {
        Log.d(LOG_TAG,"I am in on resume of mainactivity");

        ItemViewModelFactory factory = new ItemViewModelFactory(dbInstance,taskId);
        final ItemViewModel viewModel= ViewModelProviders.of(this,factory).get(ItemViewModel.class);

        viewModel.getItems().observe(this, new Observer<List<ItemEntity>>() {
            @Override
            public void onChanged(@Nullable List<ItemEntity> itemEntities) {
                //viewModel.getItems().removeObserver(this);
                itemAdapter.setItemList(itemEntities);
            }
        });


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus == true) {

            fetchitemList();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                display_dialog();

                break;
            case R.id.action_speak:
                speech_to_text();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void display_dialog() {
        final Dialog dialog = new Dialog(ListItemsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_ist_items);
        final Spinner ut =dialog.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("kg");
        list.add("litres");
        list.add("pieces");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ListItemsActivity.this,android.R.layout.simple_spinner_item, list);
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

    public void edit_dialog(final ItemEntity itemEntity) {
        final Dialog dialog = new Dialog(ListItemsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_ist_items);
        final Spinner ut =dialog.findViewById(R.id.spinner);
       final EditText editText = dialog.findViewById(R.id.edtListName);
        final EditText editText1 = dialog.findViewById(R.id.edtItemQuant);
        editText.setText(itemEntity.getItemName().toString().trim());

        editText1.setText(String.valueOf(itemEntity.getItemQuantity()));
        String itemUnit=itemEntity.getUnits();
        if(itemUnit.equalsIgnoreCase("kg"))
        {
            ut.setSelection(0);
        }else if(itemUnit.equalsIgnoreCase("litres"))
        {ut.setSelection(1);}
        else if(itemUnit.equalsIgnoreCase("pieces"))
        {ut.setSelection(2);}

        List<String> list = new ArrayList<String>();
        list.add("kg");
        list.add("litres");
        list.add("pieces");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ListItemsActivity.this,android.R.layout.simple_spinner_item, list);
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
                           ItemEntity itemEntity1 = new ItemEntity(listName, quantity, itemEntity.getItemId(),taskId,unit);
                            dbInstance.itemDao().updateItem(itemEntity1);

                        }
                    });

                    //enter in room
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }
    public void speech_to_text() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "ItemName,Quantity,units'");
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK &&  data != null){
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(LOG_TAG , "strng array is"+result);
                    Log.d(LOG_TAG,"item is "+result.get(0));
                    String parse = result.get(0).toString();

                    final List<String> items = Arrays.asList(parse.split("\\s*,\\s*"));
                    for(int i=0;i<items.size();i++)
                    {
                      Log.d(LOG_TAG ,"item is"+items.get(i));
                      /*if(items.get(0))
                      {
                          listName = items.get(0);
                      }*/
                    }
                    if(items.get(0)!=null && Integer.valueOf(items.get(1))!=null && items.get(2)!=null) {
                        AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                ItemEntity itemEntity = new ItemEntity(items.get(0), Integer.valueOf(items.get(1)), taskId, items.get(2));
                                dbInstance.itemDao().insertItemInList(itemEntity);

                            }
                        });
                    }

                }
                break;
            }

        }
    }

    @Override
    public void onImageClick(final ItemEntity itemEntity, View v) {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.inflate(R.menu.item_menu2);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.delete :
                        Toast.makeText(ListItemsActivity.this,"delete is clicked",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                        builder.setMessage("Do you want to delete this item?");
                        builder.setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                AppDatabase.getsInstance(ListItemsActivity.this).itemDao().deleteItem(itemEntity);
                                            }
                                        });



                                    }
                                });

                        builder  .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(ListItemsActivity.this,"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually

                        alert.show();

                        return true;
                    case R.id.edit :
                        Toast.makeText(ListItemsActivity.this,"Edit is clicked",Toast.LENGTH_LONG).show();
                         edit_dialog(itemEntity);

                        return true;
                    default:
                        return false;
                }
            }

        });
    }
}
