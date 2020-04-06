package fbtech.com.shopwithme;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fbtech.com.shopwithme.database.AppDatabase;
import fbtech.com.shopwithme.database.ItemEntity;
import fbtech.com.shopwithme.database.MainViewModel;
import fbtech.com.shopwithme.database.Repository;
import fbtech.com.shopwithme.database.TaskEntity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TaskAdapter.TaskclickListner {

    private static final String LOG_TAG =MainActivity.class.getSimpleName();
    public  AppDatabase dbInstance = null;
    private RecyclerView mRecyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskEntity> listNames;
    private TaskAdapter.TaskclickListner mListener;
    private String listName;
     ArrayList<ItemEntity> itemlist ;
     boolean  isempty =false;
    private Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(LOG_TAG,"I am in on create of mainactivity");
        dbInstance = AppDatabase.getsInstance(getApplicationContext());
        repository = new Repository(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //display dialog box
               final CustomDialog dialog = new CustomDialog(MainActivity.this);
               // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.setCancelable(false);
                //dialog.setContentView(R.layout.dialog_add_item);

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
                         listName = editText.getText().toString().trim();
                        Log.d(LOG_TAG,"String entered is="+listName);
                        if(listName.isEmpty())
                        {
                            editText.setError("Enter valid Name");
                            Log.d(LOG_TAG,"List is empty");


                        }
                        else
                        {
                            Log.d(LOG_TAG,"List is not empty");

                             Date date = new Date();
                            final TaskEntity taskEntity = new TaskEntity(listName,date,"pending");

                           repository.insertTask(taskEntity);

                            //enter in room
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
       LinearLayoutManager layoutManager
                = new LinearLayoutManager(MainActivity.this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        taskAdapter = new TaskAdapter(MainActivity.this,this) ;

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(taskAdapter);
        setUpViewModel();
       new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( 0,ItemTouchHelper.LEFT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        int position = viewHolder.getAdapterPosition();
                        List<TaskEntity> tasks =taskAdapter.getTaskList();
                        dbInstance.taskDao().deleteTask(tasks.get(position));
                        setUpViewModel();
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void setUpViewModel() {
       // final LiveData<List<TaskEntity>> taskList = dbInstance.taskDao().fetchAllTask();
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getTasks().observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntity> taskEntities) {
                Log.d(LOG_TAG, "Reteriving live data enteries");
                taskAdapter.setTaskList(taskEntities);
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus == true) {
           setUpViewModel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_archived) {

        } else if (id == R.id.nav_share) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
           // intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.putExtra(Intent.EXTRA_SUBJECT, "ShopWithMe Android App");
            intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Share Link"));

        } else if (id == R.id.nav_send) {



            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:ruchi.lotus2409@gmail.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "ruchi.ltus2409@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for ShopWithMe Android App");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onImageItemClicked( final TaskEntity grid_item, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.item_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.delete:
                        Log.d(LOG_TAG,"I am on click of delete");
                        Toast.makeText(MainActivity.this, "delete is clicked", Toast.LENGTH_LONG).show();
                        AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                //AppDatabase.getsInstance(mContext).taskDao().deleteTask(taskEntity);
                                itemlist = (ArrayList<ItemEntity>) AppDatabase.getsInstance(MainActivity.this).itemDao().fetchitemsofTask(grid_item.getTaskId());
                                Log.d(LOG_TAG,"list size is"+itemlist.size());
                                Log.d(LOG_TAG,"list is empty?"+itemlist.isEmpty());

                                if (itemlist.isEmpty()) {
                                    AppDatabase.getsInstance(MainActivity.this).taskDao().deleteTask(grid_item);
                                }
                                else
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("Please delete the items in list");
                                            builder.setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {


                                                        }

                                                    });

                                            //Creating dialog box
                                            AlertDialog alert = builder.create();
                                            //Setting the title manually

                                            alert.show();

                                        }
                                    });


                                }

                            }
                        });




                        return true;

                    case R.id.edit:
                        Toast.makeText(MainActivity.this, "Edit is clicked", Toast.LENGTH_LONG).show();

                        final CustomDialog dialog = new CustomDialog(MainActivity.this);
                        final EditText txtName = (EditText) dialog.findViewById(R.id.edtListName);

                        txtName.setText(grid_item.getName().toString());

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
                                String name = txtName.getText().toString();
                                if (name.isEmpty()) {
                                    txtName.setError("Enter valid name");
                                } else {
                                    grid_item.setName(txtName.getText().toString());
                                    grid_item.setUpdate_date(new Date());
                                    AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("TaskAdapter", "i am in run");
                                            AppDatabase database = AppDatabase.getsInstance(MainActivity.this);
                                            Log.d("TaskAdapter", "taskentity " + grid_item.getTaskId() + grid_item.getName());
                                            database.taskDao().updateTask(grid_item);
                                        }
                                    });
                                    dialog.dismiss();
                                }

                            }
                        });

                        dialog.show();

                        return true;
                    case R.id.share:

                        Log.d("Taskadapter", "share is clicked");


                        AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                //AppDatabase.getsInstance(mContext).taskDao().deleteTask(taskEntity);
                                itemlist = (ArrayList<ItemEntity>) AppDatabase.getsInstance(MainActivity.this).itemDao().fetchitemsofTask(grid_item.getTaskId());
                                Log.d("list", itemlist.toString());
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                // sendIntent.putParcelableArrayListExtra("ItemList",  itemlist);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, itemlist.toString());
                                sendIntent.setType("text/plain");
                                Log.d("list before intent", itemlist.toString());

                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                if (shareIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                                    MainActivity.this.startActivity(shareIntent);
                                }

                            }
                        });

                        return true;
                    case R.id.done:

                        AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                TaskEntity taskEntity = new TaskEntity(grid_item.getName(),new Date(),grid_item.getTaskId(),"done");
                                dbInstance.taskDao().updateTask(taskEntity);
                            }
                        });


                        return  true;

                    case R.id.reminder :
                        Calendar cal = Calendar.getInstance();
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("beginTime", cal.getTimeInMillis());
                        intent.putExtra("allDay", false);
                        intent.putExtra("rrule", "FREQ=DAILY");
                        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                        intent.putExtra("title", "Bring  " +grid_item.getName());
                        startActivity(intent);

                        return true;
                    default:
                        return false;

                }
            }
        });
    }



    @Override
    public void onListItemClicked(TaskEntity grid_item, View view) {
        Log.d(LOG_TAG,"item clicked is="+grid_item.getName()+grid_item.getUpdate_date());
        Intent intent = new Intent(MainActivity.this,ListItemsActivity.class);
        Log.d(LOG_TAG,"Task Id is ="+grid_item.getTaskId());
        intent.putExtra("taskId",grid_item.getTaskId());
        startActivity(intent);

    }
}
