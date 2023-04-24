package hn.uth.myapplication.contactos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import hn.uth.myapplication.databinding.ActivityContactoBinding;

public class ContactoActivity extends AppCompatActivity implements OnItemClickListener<Contacto> {
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 100;

    private ActivityContactoBinding binding;
    private ContactosAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adaptador = new ContactosAdapter(new ArrayList<>(), this);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        binding.contentLayout.pbContacts.setVisibility(View.INVISIBLE);
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
            int cantidad = 0;
            binding.contentLayout.pbContacts.setVisibility(View.VISIBLE);
            List<Contacto> misContactos = solicitarPermisoContactos(this);
            misContactos = misContactos.subList(0, 500);


            adaptador.setItems(misContactos);
            validateDataset();
            binding.contentLayout.pbContacts.setVisibility(View.INVISIBLE);
        });
        setupRecycle();
    }

    private void validateDataset() {
        if(this.adaptador.getItemCount() > 0){
            binding.contentLayout.imgNoContacts.setVisibility(View.INVISIBLE);
            binding.contentLayout.txtNoContacts.setVisibility(View.INVISIBLE);
            binding.contentLayout.rvContactos.setVisibility(View.VISIBLE);
        }else{
            binding.contentLayout.imgNoContacts.setVisibility(View.VISIBLE);
            binding.contentLayout.txtNoContacts.setVisibility(View.VISIBLE);
            binding.contentLayout.rvContactos.setVisibility(View.INVISIBLE);
        }
    }

    public void setupRecycle(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        binding.contentLayout.rvContactos.setLayoutManager(linearLayoutManager);
        binding.contentLayout.rvContactos.setAdapter(adaptador);
    }

    public List<Contacto> solicitarPermisoContactos(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            //SI NO ME HAN DADO EL PERMISO, ENTRA Y DEBO DE SOLICITARLO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
            return new ArrayList<>();
        } else{
            //SI YA TENGO EL PERMISO DEL USUARIO
            return getContacts(context);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_READ_CONTACTS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //CONCEDIO EL PERMISO EN TIEMPO DE EJECUCIÓN
                int cantidad = 0;

                List<Contacto> misContactos = getContacts(this);
                misContactos = misContactos.subList(0, 500);
                adaptador.setItems(misContactos);
                validateDataset();
                binding.contentLayout.pbContacts.setVisibility(View.INVISIBLE);
            }else{
                Snackbar.make(binding.appBar, "No se pueden mostrar los contactos, permiso rechazado", Snackbar.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("Range")
    public static List<Contacto> getContacts(Context context){
        List<Contacto> contactos = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " DESC");
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()) {
                int idColumIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts._ID), 0);
                int nameColumIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME), 0);
                int phoneColumIndex = Math.max(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER), 0);


                String id = cursor.getString(idColumIndex);
                String name = cursor.getString(nameColumIndex);


                String email = null;
                Cursor ce = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                if (ce != null && ce.moveToFirst()) {
                    email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    ce.close();
                }


                if (Integer.parseInt(cursor.getString(phoneColumIndex)) > 0) {
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{id}, null);


                    while (pCur.moveToNext()) {
                        int phoneCommonColumIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phone = pCur.getString(phoneCommonColumIndex);



                        Contacto nuevo = new Contacto();
                        nuevo.setName(name);
                        nuevo.setPhone(phone);
                        nuevo.setEmail(email);
                        contactos.add(nuevo);
                    }
                    pCur.close();

                }

            }
        }




        cursor.close();

        return contactos;
    }

    @Override
    public void onItemClick(Contacto data, int type) {
        try{
            String phoneNumber = validatePhoneNumber(data.getPhone());
            Uri urlWhatsapp = Uri.parse("https://api.whatsapp.com/send?phone="+phoneNumber+"&text="+generateEncodedText("Hola "+data.getName()));
            Intent intent = new Intent(Intent.ACTION_VIEW, urlWhatsapp);
            intent.setPackage("com.whatsapp.w4b");
            startActivity(intent);
        }catch(Exception error){
            error.printStackTrace();
            Snackbar.make(binding.appBar, "Hay un error al abrir Whatsapp", Snackbar.LENGTH_LONG).show();
        }
    }

    private String validatePhoneNumber(String phone) {
        String formatNumber = phone.replaceAll("\\)","");
        formatNumber = formatNumber.replaceAll("\\(","");
        formatNumber = formatNumber.replaceAll("-","");
        formatNumber = formatNumber.replaceAll("\\+","");
        formatNumber = formatNumber.replaceAll(" ","");

        formatNumber = formatNumber.startsWith("504")?formatNumber:"504"+formatNumber;

        return formatNumber;
    }

    private String generateEncodedText(String mensaje) throws UnsupportedEncodingException {
        return URLEncoder.encode(mensaje, StandardCharsets.UTF_8.toString());
    }
}