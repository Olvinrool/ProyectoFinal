package hn.uth.myapplication;

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

import hn.uth.myapplication.databinding.ActivityCrearEmpleadoBinding;

public class CrearEmpleadoActivity  extends AppCompatActivity {

    private ActivityCrearEmpleadoBinding binding;
    private int idEmpleado;

    private EmpleadoViewModel empleadosViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        empleadosViewModel = new ViewModelProvider(this).get(EmpleadoViewModel.class);

        binding = ActivityCrearEmpleadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        obtenerDatosIntent(getIntent());
        setSupportActionBar(binding.toolbar);


        binding.btnGuardar.setOnClickListener(v-> {
            Intent resultado = new Intent();
            resultado.putExtra("ID", idEmpleado);
            resultado.putExtra("NOMBRE",binding.tilNombre.getEditText().getText().toString());
            resultado.putExtra("APELLIDO",binding.tilApellido.getEditText().getText().toString());
            resultado.putExtra("SALARIO",binding.tilSalario.getEditText().getText().toString());

            setResult(Activity.RESULT_OK, resultado);
            finish();
        });


    }

    private void obtenerDatosIntent(Intent intent) {
        String nombre = intent.getStringExtra("NOMBRE_EMPLEADO");

        if(nombre!= null){
            String apellido = intent.getStringExtra("APELLIDO_EMPLEADO");
            int salario = intent.getIntExtra("SALARIO_EMPLEADO", 0);
            idEmpleado = intent.getIntExtra("EMPLEADO_ID", 0);

            binding.tilNombre.getEditText().setText(nombre);
            binding.tilApellido.getEditText().setText(apellido);
            binding.tilSalario.getEditText().setText(String.valueOf(salario));
            binding.btnGuardar.setText(getString(R.string.btn_guardar_vehiculo));

            binding.toolbar.setTitle(R.string.title_update_vehicle);
        }else{
            binding.btnGuardar.setText(getString(R.string.btn_crear_vehiculo));

            binding.toolbar.setTitle(R.string.title_create_vehicle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ME SIRVE PARA INFLAR EL MENÚ (ASOCIAR EL MENÚ CREADO AL ACTIVITY O TOOLBAR DONDE SE VA A MOSTRAR)
        getMenuInflater().inflate(R.menu.eliminar_empleado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //SIRVE PARA INDICAR QUE HACER AL PRESIONAR LA OPCIÓN DEL MENÚ

        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
        }else if(id == R.id.action_delete_vehicle){
            Empleado empleadoEliminar = new Empleado(binding.tilNombre.getEditText().getText().toString(),
                    binding.tilApellido.getEditText().getText().toString(),
                    0);
            empleadoEliminar.setIdEmpleado(idEmpleado);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.delete_dialog_message)
                    .setTitle(R.string.delete_dialog_title);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   empleadosViewModel.delete(empleadoEliminar);

                    Snackbar.make(binding.clCrearEmpleado, getString(R.string.mensaje_eliminacion_vehiculo), Snackbar.LENGTH_LONG).show();

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
