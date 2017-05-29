package com.example.savio.focoaedes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.savio.focoaedes.fragments.Nova_OcorrenciaFragment;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FloatingActionButton fab, fab_nova_correncia, fab_info;
    Animation fab_open, fab_close, fab_rotate, fab_back_rotate;

    boolean aberto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//--------------Botões flutuantes e animações-------------------------------------------------------//


        //Botões flutuantes
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_nova_correncia = (FloatingActionButton) findViewById(R.id.fab_nova_ocorrencia);
        fab_info = (FloatingActionButton) findViewById(R.id.fab_info);

        //animações do botão flutuante
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_close);
        fab_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_rotate);
        fab_back_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_back_rotate);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (aberto) {
                    fab.startAnimation(fab_back_rotate);
                    fab_nova_correncia.startAnimation(fab_close);
                    fab_nova_correncia.setClickable(false);
                    fab_info.startAnimation(fab_close);
                    fab_info.setClickable(false);
                    aberto = false;

                } else {

                    fab.startAnimation(fab_rotate);
                    fab_nova_correncia.startAnimation(fab_open);
                    fab_nova_correncia.setClickable(true);
                    fab_info.startAnimation(fab_open);
                    fab_info.setClickable(true);
                    aberto = true;
                }
            }
        });

        fab_nova_correncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Inicia Fragment da nova ocorrencia
                alterarFragment(new Nova_OcorrenciaFragment(), "NovaOcorrenciaFragment");
            }
        });

        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Informações", Toast.LENGTH_SHORT).show();
            }
        });


        //Inicia Fragment do mapa
        mostrarFragment(new MapsFragment(), "MapsFragment");

    }


//--------------Meus metodos para facilitar minha vida----------------------------------------------//


    private void mostrarFragment(Fragment fragment, String key){

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //confirmar transição
        transaction.add(R.id.frag_container, fragment, key).commit();

    }

    private void alterarFragment(Fragment fragment, String key){

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //confirmar transição
        transaction.replace(R.id.frag_container, fragment, key).addToBackStack(null).commit();

    }

    public void mostraFloatingActionButton(){ fab.show(); }

    public void escondeFloatingActionButton(){

        fab.hide();
        fab.startAnimation(fab_back_rotate);
        fab_nova_correncia.startAnimation(fab_close);
        fab_nova_correncia.setClickable(false);
        fab_info.startAnimation(fab_close);
        fab_info.setClickable(false);
        aberto = false;
    }

//--------------Fim de codigo-----------------------------------------------------------------------//

}
