package fbtech.com.shopwithme.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcelable;

/**
 * Created by Ruchi on 08/Mar/2020.
 */
@Entity(tableName="items")
public class ItemEntity {
    private String itemName;

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    private  String units;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    private float itemQuantity;
    private int taskId;
    @PrimaryKey(autoGenerate = true)
    private int itemId;

    public ItemEntity(String itemName, float itemQuantity, int itemId ,int taskId,String units) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemId = itemId;
        this.taskId=taskId;
        this.units=units;
    }
    @Ignore
    public ItemEntity(String itemName, float itemQuantity,int taskId ,String units) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.taskId=taskId;
        this.units=units;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(float itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }



    @Override
    public String toString() {
        return (getItemName()+
        " : "+ getItemQuantity() +"  "
               + getUnits()+"\n"
                );
    }

}
