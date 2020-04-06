package fbtech.com.shopwithme;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fbtech.com.shopwithme.database.AppDatabase;
import fbtech.com.shopwithme.database.ItemEntity;
import fbtech.com.shopwithme.database.TaskEntity;

/**
 * Created by Ruchi on 08/Mar/2020.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>
{
    private ItemViewHolder view;
    private Context mContext;
    private onItemClickListner mClickHandler;

    public ItemAdapter(Context mContext,onItemClickListner mClickHandler) {

        this.mContext = mContext;
        this.mClickHandler=mClickHandler;

    }

    private List<ItemEntity> obj;


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,parent,false);
        view = new ItemViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.name.setText(obj.get(position).getItemName());
        holder.quantity.setText(String.valueOf(obj.get(position).getItemQuantity()));
        holder.units.setText(String.valueOf(obj.get(position).getUnits()));
        holder.taskId = obj.get(position).getTaskId();
      //  holder.image.setOnClickListener(new View.OnClickListener() {
         //   PopupMenu popupMenu = new PopupMenu(mContext,holder.image);
         //   @Override
           // public void onClick(View v) {

              //  popupMenu.inflate(R.menu.item_menu);
               /* popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {

                        final ItemEntity itemEntity = obj.get(position);
                        switch (item.getItemId())
                        {
                            case R.id.delete :
                                Toast.makeText(mContext,"delete is clicked",Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("Do you want to delete this item?");
                                builder.setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                AppExecutor.getsInstance().diskIO().execute(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AppDatabase.getsInstance(mContext).itemDao().deleteItem(itemEntity);
                                                    }
                                                });



                                            }
                                        });

                                builder  .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                        Toast.makeText(mContext,"you choose no action for alertbox",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually

                                alert.show();


                                return true;
                            case R.id.edit :
                                Toast.makeText(mContext,"Edit is clicked",Toast.LENGTH_LONG).show();
                                return true;
                                default:
                                    return false;
                        }
                    }

                });
                popupMenu.show();*/
            //}

       // });


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
    public void setItemList(List<ItemEntity> itemList)
    {
        this.obj = itemList;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView name,quantity,units;
        final ImageView image;
        int taskId;

        public ItemViewHolder(View itemView) {
            super(itemView);

             name= (TextView) itemView.findViewById(R.id.txt_name);
            quantity =(TextView)itemView.findViewById(R.id.txt_quantity);
            image =(ImageView)itemView.findViewById(R.id.imageview_settings);
            units =(TextView)itemView.findViewById(R.id.txt_units);
            image.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            Log.d("ItemClick","I am on onclick of item");
            int adapterPosition = getAdapterPosition();
           ItemEntity item_entity = obj.get(adapterPosition);
            mClickHandler.onImageClick(item_entity,v);
        }
    }

    public interface onItemClickListner
    {
        void onImageClick(ItemEntity itemEntity,View v);
    }
}
