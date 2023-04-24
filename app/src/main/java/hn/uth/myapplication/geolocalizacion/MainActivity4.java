package hn.uth.myapplication.geolocalizacion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import hn.uth.myapplication.databinding.ActivityMain4Binding;

public class MainActivity4 extends AppCompatActivity implements LocationListener {

    private @NonNull ActivityMain4Binding binding;
    private static final int REQUEST_CODE_GPS = 78;
    private LocationManager locationManager;
    private GPSLocation ubicacionActual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnShare.setVisibility(View.INVISIBLE);
        binding.btnOpenMap.setVisibility(View.INVISIBLE);

        binding.ibLocation.setOnClickListener(l -> {
            solicitarPermisoGPS(this);
        });
        binding.btnShare.setOnClickListener(l -> {
            compartirUbicacion();
        });
        binding.btnOpenMap.setOnClickListener(l -> {
            abrirGoogleMaps();
        });
    }

    private void abrirGoogleMaps() {
        Uri location = Uri.parse("geo:"+ubicacionActual.getLatitude()+","+ubicacionActual.getLongitude()+"?z=14");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    private void compartirUbicacion() {
        //CREAMOS UN INTENT PARA COMPARTIR LA UBICACIÓN EN FORMA DE TEXTO
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mi ubicación Actual");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ubicacionActual.toText());

        startActivity(Intent.createChooser(shareIntent, "Compartir Texto"));
    }

    private void solicitarPermisoGPS(Context context) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //TENGO EL PERMISO DE USAR EL GPS
            useFineLocation();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GPS){
            //LA SOLICITUD RECIBIDA ES PARA EL PERMISO DE GPS
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //PERMISO PRECISO OTORGADO
                    useFineLocation();
                }else if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    //PERMISO APROXIMADO OTORGADO
                    useCoarseLocation();
                }
            }else{
                binding.tvLocation.setText("Permiso NO otorgado");
            }
            return;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    private void useCoarseLocation() {
        //OBTIENE EL SERVICIO DE UBICACIÓN DEL DISPOSITIVO
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //SOLICITO ACTUALIZAR LA POSICIÓN GPS DE LA RED
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @SuppressLint("MissingPermission")
    private void useFineLocation() {
        //OBTIENE EL SERVICIO DE UBICACIÓN DEL DISPOSITIVO
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //SOLICITO ACTUALIZAR LA POSICIÓN GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        ubicacionActual = new GPSLocation(latitude, longitude);

        binding.tvLocation.setText("Latitide: "+latitude + ", longitude: "+longitude);

        binding.btnShare.setVisibility(View.VISIBLE);
        binding.btnOpenMap.setVisibility(View.VISIBLE);

        //DETENIENDO LA LECTURA DE GPS
        locationManager.removeUpdates(this);
    }

}