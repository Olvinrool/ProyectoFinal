package hn.uth.myapplication;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import hn.uth.myapplication.databinding.FragmentFirstBinding;


public class FirstFragment extends Fragment implements OnItemClickListener<Empleado> {

    private FragmentFirstBinding binding;

    private EmpleadosAdapter adaptador;

    private EmpleadosApp app;
    private EmpleadoViewModel empleadosViewModel;

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        app = EmpleadosApp.getInstance();
        empleadosViewModel = new ViewModelProvider(this).get(EmpleadoViewModel.class);

        adaptador = new EmpleadosAdapter(new ArrayList<>(), this);

        //CONSULTA A BASE DE DATOS MEDIANTE BACKGRUOND THREAD
        empleadosViewModel.getEmpleadosDataset().observe(this, empleados -> {
            adaptador.setItems(empleados);
            validarDataset();
        });

        setupReciclerView();

        return binding.getRoot();
    }

    private void validarDataset() {
        if(adaptador.getItemCount() == 0){
            binding.tvWarning.setVisibility(View.VISIBLE);
            binding.ivWarning.setVisibility(View.VISIBLE);
            binding.rvVehiculos.setVisibility(View.INVISIBLE);
        }else{
            binding.tvWarning.setVisibility(View.INVISIBLE);
            binding.ivWarning.setVisibility(View.INVISIBLE);
            binding.rvVehiculos.setVisibility(View.VISIBLE);
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupReciclerView(){
        LinearLayoutManager layoutLineal = new LinearLayoutManager(this.getContext());
        binding.rvVehiculos.setLayoutManager(layoutLineal);
        binding.rvVehiculos.setAdapter(adaptador);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Empleado data, int type) {
        Intent intent = new Intent(this.getContext(), CrearEmpleadoActivity.class);
        intent.putExtra("EMPLEADO_ID", data.getIdEmpleado());
        intent.putExtra("EMPLEADO_NOMBRE", data.getNombre());
        intent.putExtra("EMPLEADO_APELLIDO", data.getApellido());
        intent.putExtra("EMPLEADO_SALARIO", data.getSalario());
        startActivityForResult(intent, 6, ActivityOptions.makeSceneTransitionAnimation(this.getActivity()).toBundle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 6){
            //EDICIÓN DE UN VEHICULO EXISTENTE
            if(resultCode == RESULT_OK){
                int id = data.getIntExtra("ID", 0);//EL ID DEL VEHICULO SELECCIONADO
                String nombre = data.getStringExtra("NOMBRE");//EL MODELO NUEVO
                String apellido = data.getStringExtra("APELLIDO");
                String salario = data.getStringExtra("SALARIO");

                Empleado actualizar = new Empleado(nombre, apellido, Integer.parseInt(salario));
                actualizar.setIdEmpleado(id);
                empleadosViewModel.update(actualizar);

                //adaptador.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}