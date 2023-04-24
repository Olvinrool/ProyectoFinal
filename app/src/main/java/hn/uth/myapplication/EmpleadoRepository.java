package hn.uth.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import hn.uth.myapplication.Empleado;
import hn.uth.myapplication.EmpleadoDao;
import hn.uth.myapplication.EmpleadoDatabase;

public class EmpleadoRepository {

    private EmpleadoDao empleadoDao;
    private LiveData<List<Empleado>> EmpleadosDataset;

    public EmpleadoRepository(Application app){
        EmpleadoDatabase db = EmpleadoDatabase.getDatabase(app);
        empleadoDao = db.empleadoDao();
        EmpleadosDataset = empleadoDao.getEmpleados();
    }

    public LiveData<List<Empleado>> getEmpleadosDataset() {
        return EmpleadosDataset;
    }

    public void insert(Empleado nuevo){
        EmpleadoDatabase.databaseWriteExecutor.execute(() ->{
            empleadoDao.insert(nuevo);
        });
    }

    public void update(Empleado actualizar){
       EmpleadoDatabase.databaseWriteExecutor.execute(() ->{
            empleadoDao.update(actualizar);
        });
    }

    public void delete(Empleado eliminar){
        EmpleadoDatabase.databaseWriteExecutor.execute(() ->{
            empleadoDao.delete(eliminar);
        });
    }

    public void deleteAll(){
        EmpleadoDatabase.databaseWriteExecutor.execute(() ->{
            empleadoDao.deleteAll();
        });
    }



}
