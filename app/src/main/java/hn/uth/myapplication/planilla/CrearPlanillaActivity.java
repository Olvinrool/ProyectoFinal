package hn.uth.myapplication.planilla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import hn.uth.myapplication.R;
import hn.uth.myapplication.databinding.ActivityCrearPlanillaBinding;

public class CrearPlanillaActivity extends AppCompatActivity {

    private ActivityCrearPlanillaBinding binding;
    private int idPlanilla;

    private PlanillaViewModel planillaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        planillaViewModel = new ViewModelProvider(this).get(PlanillaViewModel.class);

        binding = ActivityCrearPlanillaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        obtenerDatosIntent(getIntent());
        setSupportActionBar(binding.toolbar);


        binding.btnGuardar.setOnClickListener(v-> {
            Intent resultado = new Intent();
            resultado.putExtra("ID", idPlanilla);
            resultado.putExtra("CONCEPTO",binding.tilConcepto.getEditText().getText().toString());
            resultado.putExtra("FECHA",binding.tilFecha.getEditText().getText().toString());
            resultado.putExtra("TOTAL SALARIO",binding.tilTotals.getEditText().getText().toString());

            setResult(Activity.RESULT_OK, resultado);
            finish();
        });


    }

    private void obtenerDatosIntent(Intent intent) {
        String concepto = intent.getStringExtra("PLANILLA_CONCEPTO");

        if(concepto != null){
            String fecha = intent.getStringExtra("PLANILLA_FECHA");
            int totals = intent.getIntExtra("PLANILLA_TOTAL", 0);
            idPlanilla = intent.getIntExtra("PLANILLA_ID", 0);

            binding.tilConcepto.getEditText().setText(concepto);
            binding.tilFecha.getEditText().setText(fecha);
            binding.tilTotals.getEditText().setText(String.valueOf(totals));
            binding.btnGuardar.setText(getString(R.string.btn_guardar_planilla));

            binding.toolbar.setTitle(R.string.title_update_planilla);
        }else{
            binding.btnGuardar.setText(getString(R.string.btn_crear_planilla));

            binding.toolbar.setTitle(R.string.title_create_planilla);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ME SIRVE PARA INFLAR EL MENÚ (ASOCIAR EL MENÚ CREADO AL ACTIVITY O TOOLBAR DONDE SE VA A MOSTRAR)
        getMenuInflater().inflate(R.menu.eliminar_planilla, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //SIRVE PARA INDICAR QUE HACER AL PRESIONAR LA OPCIÓN DEL MENÚ

        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
        }else if(id == R.id.action_delete_vehicle){
            Planilla planillaEliminar = new Planilla(binding.tilConcepto.getEditText().getText().toString(),
                    binding.tilFecha.getEditText().getText().toString(),
                    0);
            planillaEliminar.setIdPlanilla(idPlanilla);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.delete_dialog_message)
                    .setTitle(R.string.delete_dialog_title);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    planillaViewModel.delete(planillaEliminar);

                    Snackbar.make(binding.clCrearPlanilla, getString(R.string.mensaje_eliminacion_planilla), Snackbar.LENGTH_LONG).show();

                    finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}