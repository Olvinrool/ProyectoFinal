package hn.uth.myapplication.planilla;

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

import hn.uth.myapplication.databinding.FragmentFirstPlanillaBinding;

public class FirstFragmentPlanilla extends Fragment implements OnItemClickListener<Planilla> {

    private FragmentFirstPlanillaBinding binding;

    private PlanillaAdapter adaptadorplanilla;

    private PlanillaApp app;
    private PlanillaViewModel planillaViewModel;

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstPlanillaBinding.inflate(inflater, container, false);
        app = PlanillaApp.getInstance();
        planillaViewModel = new ViewModelProvider(this).get(PlanillaViewModel.class);

        adaptadorplanilla = new PlanillaAdapter(new ArrayList<>(), this);

        //CONSULTA A BASE DE DATOS MEDIANTE BACKGRUOND THREAD
        planillaViewModel.getPlanillaDataset().observe(this, planillas -> {
            adaptadorplanilla.setItems(planillas);
            validarDataset();
        });

        setupReciclerView();

        return binding.getRoot();
    }

    private void validarDataset() {
        if(adaptadorplanilla.getItemCount() == 0){
            binding.tvWarning.setVisibility(View.VISIBLE);
            binding.ivWarning.setVisibility(View.VISIBLE);
            binding.rvPlanillas.setVisibility(View.INVISIBLE);
        }else{
            binding.tvWarning.setVisibility(View.INVISIBLE);
            binding.ivWarning.setVisibility(View.INVISIBLE);
            binding.rvPlanillas.setVisibility(View.VISIBLE);
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
        binding.rvPlanillas.setLayoutManager(layoutLineal);
        binding.rvPlanillas.setAdapter(adaptadorplanilla);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Planilla data, int type) {
        Intent intent = new Intent(this.getContext(), CrearPlanillaActivity.class);
        intent.putExtra("PLANILLA_ID", data.getIdPlanilla());
        intent.putExtra("PLANILLA_CONCEPTO", data.getConcepto());
        intent.putExtra("PLANILLA_FECHA", data.getFecha());
        intent.putExtra("PLANILLA_TOTAL", data.getTotals());
        startActivityForResult(intent, 6, ActivityOptions.makeSceneTransitionAnimation(this.getActivity()).toBundle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 6){
            //EDICIÓN DE UNA PLANILLA EXISTENTE
            if(resultCode == RESULT_OK){
                int id = data.getIntExtra("ID", 0);//EL ID DEL PLANILLA SELECCIONADO
                String concepto = data.getStringExtra("CONCEPTO");//EL CONCEPTO NUEVO
                String fecha = data.getStringExtra("FECHA");
                String totals = data.getStringExtra("TOTAL");

                int totalsInt=0;
                if(totals != null && !totals.isEmpty()){
                    try {
                        totalsInt = Integer.parseInt(totals);
                    }catch (NumberFormatException e){

                    }
                }

                Planilla actualizar = new Planilla(concepto, fecha, totalsInt);
                actualizar.setIdPlanilla(id);
                planillaViewModel.update(actualizar);

                //adaptador.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}