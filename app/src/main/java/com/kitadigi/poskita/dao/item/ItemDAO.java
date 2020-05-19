package com.kitadigi.poskita.dao.item;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.Query;
import java.util.List;



@Dao
public interface ItemDAO {
    @Insert
    public void insert(ItemModel... itemModels);

    @Update
    public void update(ItemModel... itemModels);

    @Delete
    public void delete(ItemModel itemModel);

    @Query("SELECT * FROM items")
    public List<ItemModel> getItems();

    @Query("SELECT * FROM items WHERE id = :id")
    public ItemModel getItemById(Long id);


}
