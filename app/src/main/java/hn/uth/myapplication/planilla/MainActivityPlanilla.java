package hn.uth.myapplication.planilla;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import hn.uth.myapplication.R;
import hn.uth.myapplication.databinding.ActivityMainPlanillaBinding;

public class MainActivityPlanilla extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainPlanillaBinding binding;

    private PlanillaApp app;

    private String concepto;
    private String fecha;
    private String totals;

    private PlanillaViewModel planillaViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainPlanillaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = PlanillaApp.getInstance();
        planillaViewModel = new ViewModelProvider(this).get(PlanillaViewModel.class);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_planilla);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrearPlanillaActivity.class);


            startActivityForResult(intent, 7, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });


        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 7){
            //ES LA CREACIÓN DE UN VEHICULO
            if(resultCode == RESULT_OK){
                concepto = data.getStringExtra("CONCEPTO");
                fecha = data.getStringExtra("FECHA");
                totals = data.getStringExtra("TOTAL");

                int totalsInt=0;
                if(totals != null && !totals.isEmpty()){
                    try {
                        totalsInt = Integer.parseInt(totals);
                    }catch (NumberFormatException e){

                    }
                }

                planillaViewModel.insert(new Planilla(concepto, fecha, totalsInt));

                String datosPlanilla = concepto + " "+fecha + " ("+totalsInt+")";

                Snackbar.make(binding.clMainLayoutPlanilla, getString(R.string.mensaje_creacion_planilla, datosPlanilla), Snackbar.LENGTH_LONG).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.delete_all_dialog_message)
                    .setTitle(R.string.delete_dialog_title);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    planillaViewModel.deleteAll();
                    Snackbar.make(binding.clMainLayoutPlanilla, getString(R.string.mensaje_eliminar_todo), Snackbar.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
            dialog.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_planilla);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}