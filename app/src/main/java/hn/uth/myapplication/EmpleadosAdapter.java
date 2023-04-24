package hn.uth.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hn.uth.myapplication.databinding.EmpleadoItemBinding;

public class EmpleadosAdapter extends RecyclerView.Adapter<EmpleadosAdapter.ViewHolder> {

    private List<Empleado> dataset;

    private OnItemClickListener<Empleado> manejadorEventoClick;

    public EmpleadosAdapter(List<Empleado> dataset, OnItemClickListener<Empleado> manejadorEventoClick) {
        copiarDataset(dataset);
        this.manejadorEventoClick = manejadorEventoClick;
    }

    @NonNull
    @Override
    public EmpleadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmpleadoItemBinding binding = EmpleadoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Empleado empleadoMostrar = dataset.get(position);
        holder.binding.tvNombre.setText(empleadoMostrar.getNombre());
        holder.binding.tvApellido.setText(empleadoMostrar.getApellido());
        holder.binding.tvSalario.setText(String.valueOf(empleadoMostrar.getSalario()));
        holder.setOnClickListener(empleadoMostrar, manejadorEventoClick);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Empleado> datasetNuevo){
        copiarDataset(datasetNuevo);
        //this.dataset.addAll(datasetNuevo);
        notifyDataSetChanged();
    }

    public void copiarDataset(List<Empleado> datasetNuevo){
        if(this.dataset == null){
            this.dataset = new ArrayList<>();
        }
        this.dataset.clear();
        datasetNuevo.forEach( d -> {
            this.dataset.add(d);
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        EmpleadoItemBinding binding;

        public ViewHolder(EmpleadoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setOnClickListener(final Empleado vehiculoSeleccionado, final OnItemClickListener<Empleado> listner){
            this.binding.cardVehiculo.setOnClickListener(v-> listner.onItemClick(vehiculoSeleccionado, 1));
        }
    }
}

