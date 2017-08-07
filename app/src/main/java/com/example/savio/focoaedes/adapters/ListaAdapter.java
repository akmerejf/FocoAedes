package com.example.savio.focoaedes.adapters;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savio.focoaedes.MainActivity;
import com.example.savio.focoaedes.R;
import com.example.savio.focoaedes.fragments.Detalhes_OcorrenciaFragment;
import com.example.savio.focoaedes.fragments.Lista_OcorrenciasFragment;
import com.example.savio.focoaedes.model.Ocorrencia;

import java.util.Collections;
import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.Visao>{

    private LayoutInflater inflater;
    Lista_OcorrenciasFragment listaOcorrencias;
    Context context;

    List<Ocorrencia> ocorrencias = Collections.emptyList();


    public ListaAdapter(Context context, List<Ocorrencia> ocorrencias){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.ocorrencias = ocorrencias;
    }

    @Override
    public Visao onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lista_layout, parent, false);
        Visao visao = new Visao(view);

        return visao;
    }

    @Override
    public void onBindViewHolder(Visao visao, int position) {

        final Ocorrencia atual = ocorrencias.get(position);

        if (atual.getFoto() != null){
            byte[] decodedString = Base64.decode(atual.getFoto(), Base64.DEFAULT);

            visao.foto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            visao.foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailsFragment(atual);
                }
            });
        }

        visao.id.setText(atual.getId());
        visao.titulo.setText(atual.getTitulo());
        visao.data.setText(atual.getData());
        visao.endereco.setText(atual.getRua() +" - "+ atual.getBairro());

    }

    @Override
    public int getItemCount() {

        return ocorrencias.size();
    }

    class Visao extends RecyclerView.ViewHolder{

        TextView id;
        ImageView foto;
        TextView titulo;
        TextView data;
        TextView endereco;

        public Visao(final View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.lista_id);
            foto = (ImageView) itemView.findViewById(R.id.lista_foto);
            titulo = (TextView) itemView.findViewById(R.id.lista_titulo);
            data = (TextView) itemView.findViewById(R.id.lista_data);
            endereco = (TextView) itemView.findViewById(R.id.lista_endereco);




            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    listaOcorrencias = new Lista_OcorrenciasFragment();
                    listaOcorrencias.deletarOcorrencia(id.getText().toString(), context);

                    ocorrencias.remove(getAdapterPosition());
                    notifyDataSetChanged();

                    return false;
                }
            });

        }
    }
    private void detailsFragment(Ocorrencia mItemSelected) {
        Detalhes_OcorrenciaFragment mFragment = new Detalhes_OcorrenciaFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("ocorrencia", mItemSelected);
        mFragment.setArguments(mBundle);
        switchContent(R.id.frag_container, mFragment);
    }

    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }
}
