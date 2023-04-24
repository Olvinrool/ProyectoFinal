package hn.uth.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EmpleadoViewModel extends AndroidViewModel {

    private EmpleadoRepository repository;

    private final LiveData<List<Empleado>> empleadosDataset;

    public EmpleadoViewModel(@NonNull Application application) {
        super(application);
        repository = new EmpleadoRepository(application);
        this.empleadosDataset = repository.getEmpleadosDataset();
    }

    public LiveData<List<Empleado>> getEmpleadosDataset() {
        return empleadosDataset;
    }

    public void insert(Empleado nuevo){
        repository.insert(nuevo);
    }

    public void update(Empleado actualizar){
        repository.update(actualizar);
    }

    public void delete(Empleado eliminar){
        repository.delete(eliminar);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
