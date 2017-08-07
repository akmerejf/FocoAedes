package com.example.savio.focoaedes.fragments;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.savio.focoaedes.R;
import com.example.savio.focoaedes.model.Ocorrencia;

public class Detalhes_OcorrenciaFragment extends Fragment {

    View view;
    private ImageView image;
    private TextView title;
    private Ocorrencia ocorrencia;

    public Detalhes_OcorrenciaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalhes_ocorrencia, container, false);




        title = (TextView) view.findViewById(R.id.detalhes_title);
        image = (ImageView) view.findViewById(R.id.detalhes_image);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();

        if (bundle != null)
            ocorrencia = bundle.getParcelable("ocorrencia");


        if (ocorrencia != null) {
            title.setText(ocorrencia.getTitulo());

            if (ocorrencia.getFoto() != null){
                byte[] decodedString = Base64.decode(ocorrencia.getFoto(), Base64.DEFAULT);

                image.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

            }
        }
    }
}
