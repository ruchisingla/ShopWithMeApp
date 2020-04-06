package fbtech.com.shopwithme;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fbtech.com.shopwithme.database.AppDatabase;
import fbtech.com.shopwithme.database.ItemEntity;
import fbtech.com.shopwithme.database.MainViewModel;
import fbtech.com.shopwithme.database.TaskEntity;

/**
 * Created by Ruchi on 05/Mar/2020.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private TaskViewHolder view;
    private Context mContext;
    private List<TaskEntity> obj;
    private  TaskclickListner mClickHandler;
    static boolean  isempty =false;
     static ArrayList<ItemEntity> itemlist = null;

    public TaskAdapter(Context context,TaskclickListner listner)
    {

        mContext = context;
        this.mClickHandler=listner;


    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview,parent,false);
        view = new TaskViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        holder.name.setText(obj.get(position).getName());
        holder.date.setText(String.valueOf(obj.get(position).getUpdate_date()));

 //holder.image.setOnClickListener(new View.OnClickListener() {

          /*  PopupMenu popupMenu = new PopupMenu(mContext,holder.image);
            @Override
            public void onClick(View v) {

                popupMenu.inflate(R.menu.item_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final TaskEntity taskEntity = obj.get(position);

                        switch (item.getItemId())
                        {
                            case R.id.delete :

                                Toast.makeText(mContext,"delete is clicked",Toast.LENGTH_LONG).show();
                                AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        //AppDatabase.getsInstance(mContext).taskDao().deleteTask(taskEntity);
                                     itemlist = (ArrayList<ItemEntity>) AppDatabase.getsInstance(mContext).itemDao().fetchitemsofTask(taskEntity.getTaskId());
                              if(itemlist.isEmpty())
                              {
                               TaskAdapter.isempty = true;
                              }

                                    }
                                });

                                if(isempty)
                                       {
                                           AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                               @Override
                                               public void run() {
                                                   AppDatabase.getsInstance(mContext).taskDao().deleteTask(taskEntity);

                                               }
                                           });

                                       }
                                       else {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                                return  true;

                            case R.id.edit :
                                Toast.makeText(mContext,"Edit is clicked",Toast.LENGTH_LONG).show();

                                final CustomDialog dialog = new CustomDialog(mContext);
                                final EditText txtName =(EditText) dialog.findViewById(R.id.edtListName);

                               txtName.setText(taskEntity.getName().toString());

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
                                        if(name.isEmpty())
                                        {
                                            txtName.setError("Enter valid name");
                                        }
                                        else {
                                            taskEntity.setName(txtName.getText().toString());
                                            taskEntity.setUpdate_date(new Date());
                                            AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("TaskAdapter", "i am in run");
                                                    AppDatabase database = AppDatabase.getsInstance(mContext);
                                                    Log.d("TaskAdapter", "taskentity " + taskEntity.getTaskId() + taskEntity.getName());
                                                    database.taskDao().updateTask(taskEntity);
                                                }
                                            });
                                            dialog.dismiss();
                                        }

                                    }
                                });

                                dialog.show();

                                return  true;
                            case R.id.share :

                                Log.d("Taskadapter","share is clicked");


                                AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        //AppDatabase.getsInstance(mContext).taskDao().deleteTask(taskEntity);
                                        itemlist = (ArrayList<ItemEntity>) AppDatabase.getsInstance(mContext).itemDao().fetchitemsofTask(taskEntity.getTaskId());
                                    Log.d("list", itemlist.toString());
                                        Intent sendIntent = new Intent();
                                        sendIntent.setAction(Intent.ACTION_SEND);
                                        // sendIntent.putParcelableArrayListExtra("ItemList",  itemlist);
                                        sendIntent.putExtra(Intent.EXTRA_TEXT,itemlist.toString());
                                        sendIntent.setType("text/plain");
                                        Log.d("list before intent", itemlist.toString());

                                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                                        if (shareIntent.resolveActivity(mContext.getPackageManager()) != null) {
                                            mContext.startActivity(shareIntent);
                                        }

                                    }
                                });

                                return  true;
default:
    return false;

                        }

                    }

                });

                popupMenu.show();
            }

        });*/
    }

    @Override
    public int getItemCount() {

        if(obj == null)
            return 0;
        else
        {
            return obj.size();
        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView name;
        final ImageView image;
        TextView date;


        public TaskViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_view);
            image =(ImageView)itemView.findViewById(R.id.image_View);
            date=(TextView)itemView.findViewById(R.id.txt_date);
            name.setOnClickListener(this);
            date.setOnClickListener(this);
            //itemView.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("Adapter","i am in adapter");
            int adapterPosition = getAdapterPosition();
            TaskEntity task_item = obj.get(adapterPosition);
            if(v.getId() == R.id.txt_view||v.getId()==R.id.txt_date) {
                mClickHandler.onListItemClicked(task_item, v);
            }
            else if(v.getId() == R.id.image_View)
            mClickHandler.onImageItemClicked(task_item,v);

        }
    }
    public void setTaskList(List<TaskEntity> taskList)
    {
     this.obj = taskList;
        notifyDataSetChanged();
    }


    public List<TaskEntity> getTaskList(){ return this.obj;}
    public interface TaskclickListner
    {
        void onImageItemClicked(TaskEntity grid_item,View v);
        //void onListItemClicked(TaskEntity grid_item,TextView txtView);
        void onListItemClicked(TaskEntity grid_item,View v);
    }
}
