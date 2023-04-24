package hn.uth.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EmpleadoDao{

    @Insert
    void insert(Empleado nuevo);

    @Update
    void update(Empleado actualizar);

    @Query("DELETE FROM empleados_table")
    void deleteAll();
    @Delete
    void delete(Empleado eliminar);

    @Query("select * from Empleados_Table order by nombre asc")
    LiveData<List<Empleado>> getEmpleados();






}
