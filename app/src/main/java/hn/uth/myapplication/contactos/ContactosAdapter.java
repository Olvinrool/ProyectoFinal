package hn.uth.myapplication.contactos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hn.uth.myapplication.R;
import hn.uth.myapplication.databinding.ContentContactBinding;


public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder> {
    private List<Contacto> dataset;
    private OnItemClickListener<Contacto> clickListener;

    public ContactosAdapter(List<Contacto> dataset, OnItemClickListener<Contacto> clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContentContactBinding binding = ContentContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto contactoMostrar = dataset.get(position);
        holder.binding.tvNombre.setText(contactoMostrar.getName());
        holder.binding.tvTelefono.setText(contactoMostrar.getPhone());
        holder.binding.tvEmail.setText(contactoMostrar.getEmail());

        String phone = validatePhoneNumber(contactoMostrar.getPhone());
        if (phone.startsWith("9")) {
            //ES TIGO
            holder.binding.imgOperador.setImageResource(R.drawable.tigo);
        } else if (phone.startsWith("3") || phone.startsWith("8")) {
            //ES CLARO
            holder.binding.imgOperador.setImageResource(R.drawable.claro);
        } else {
            //ES NUMERO FIJO
            holder.binding.imgOperador.setVisibility(View.INVISIBLE);

        }



        holder.setOnClickListener(contactoMostrar, clickListener);





    }

    private String validateemail() {
        return validateemail();
    }

    private String validatePhoneNumber(String phone) {
        String formatNumber = phone.replaceAll("\\)","");
        formatNumber = formatNumber.replaceAll("\\(","");
        formatNumber = formatNumber.replaceAll("-","");
        formatNumber = formatNumber.replaceAll("\\+","");
        formatNumber = formatNumber.replaceAll(" ","");

        formatNumber = formatNumber.startsWith("504")?formatNumber.substring(3,formatNumber.length()-1):formatNumber;

        return formatNumber;
    }

    public void setItems(List<Contacto> dataset){
        this.dataset = dataset;
        Map<String, Contacto> mapa = new HashMap<>();
        for (Contacto contacto:dataset) {
            String key = contacto.getPhone()+contacto.getName();
            mapa.put(key, contacto);
        }
        this.dataset.clear();
        for (Map.Entry<String, Contacto> contacto:mapa.entrySet()) {
            this.dataset.add(contacto.getValue());
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ContentContactBinding binding;
        public ViewHolder(ContentContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setOnClickListener(final Contacto contacto, final OnItemClickListener<Contacto> listener){
            this.binding.cardContact.setOnClickListener(v -> listener.onItemClick(contacto, 1));
        }
    }
}
