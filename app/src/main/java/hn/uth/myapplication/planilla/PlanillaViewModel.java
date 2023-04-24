package hn.uth.myapplication.planilla;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlanillaViewModel extends AndroidViewModel {
    private PlanillaReposity repository;
    private final LiveData<List<Planilla>> planillaDataset;

    public PlanillaViewModel(@NonNull Application application) {
        super(application);
        repository = new PlanillaReposity(application);
        this.planillaDataset = repository.getPlanillaDataset();
    }

    public LiveData<List<Planilla>> getPlanillaDataset() {
        return planillaDataset;
    }

    public void insert(Planilla nuevo){
        repository.insert(nuevo);
    }

    public void update(Planilla actualizar){
        repository.update(actualizar);
    }

    public void delete(Planilla eliminar){
        repository.delete(eliminar);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
