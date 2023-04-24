package hn.uth.myapplication.planilla;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlanillaDao {
    @Insert
    void insert(Planilla nuevo);

    @Update
    void update(Planilla carro);

    @Query("DELETE FROM planilla_table")
    void deleteAll();
    @Delete
    void delete(Planilla eliminar);

    @Query("select * from planilla_table order by fecha asc")
    LiveData<List<Planilla>> getPlanillas();
}
