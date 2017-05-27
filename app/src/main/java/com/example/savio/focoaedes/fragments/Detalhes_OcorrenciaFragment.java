package com.example.savio.focoaedes.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savio.focoaedes.R;

public class Detalhes_OcorrenciaFragment extends Fragment {

    View view;

    public Detalhes_OcorrenciaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detalhes_ocorrencia, container, false);

        return view;
    }

}
