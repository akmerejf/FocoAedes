package com.example.savio.focoaedes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.savio.focoaedes.fragments.Lista_OcorrenciasFragment;
import com.example.savio.focoaedes.fragments.Nova_OcorrenciaFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    LinearLayout fab_change;

    FragmentManager fragmentManager;
    FloatingActionButton fab, fab_nova_correncia, fab_info;
    CircleImageView fab_lista, fab_mapa;
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
        fab_lista = (CircleImageView) findViewById(R.id.fab_lista);
        fab_mapa = (CircleImageView) findViewById(R.id.fab_mapa);
        fab_change = (LinearLayout) findViewById(R.id.fab_change);

        //animações do botão flutuante
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_close);
        fab_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_rotate);
        fab_back_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_menu_back_rotate);


//--------------Listeners dos Botões----------------------------------------------------------------//


        fab_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cor do icone
                fab_lista.setColorFilter(getColor(R.color.branquinho));
                fab_mapa.setColorFilter(getColor(R.color.pretinho));

                //cor do fundo
                fab_lista.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pretinho)));
                fab_mapa.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.branquinho)));

                //Inicia Fragment da lista
                mostrarFragmentSemPilha(new Lista_OcorrenciasFragment(), "ListaFragment");

                fab_lista.setClickable(false);
                fab_mapa.setClickable(true);
            }
        });





        fab_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Permissão para acessar sua localização
                if (ActivityCompat.checkSelfPermission(v.getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }
                else{

                    //cor do icone
                    fab_mapa.setColorFilter(getColor(R.color.branquinho));
                    fab_lista.setColorFilter(getColor(R.color.pretinho));

                    //cor do fundo
                    fab_mapa.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pretinho)));
                    fab_lista.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.branquinho)));

                    //Inicia Fragment do mapa
                    mostrarFragmentSemPilha(new MapsFragment(), "MapsFragment");

                    fab_lista.setClickable(true);
                    fab_mapa.setClickable(false);
                }


            }
        });

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
                mostrarFragment(new Nova_OcorrenciaFragment(), "NovaOcorrenciaFragment");
            }
        });

        fab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Informações", Toast.LENGTH_SHORT).show();
            }
        });


        //Inicia Fragment do mapa
        alterarFragment(new Lista_OcorrenciasFragment(), "ListaOcorrencias");
        //cor do icone
        fab_lista.setColorFilter(getColor(R.color.branquinho));
        fab_mapa.setColorFilter(getColor(R.color.pretinho));

        //cor do fundo
        fab_lista.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pretinho)));
        fab_mapa.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.branquinho)));

        //Inicia Fragment da lista
        mostrarFragmentSemPilha(new Lista_OcorrenciasFragment(), "ListaFragment");

        fab_lista.setClickable(false);
        fab_mapa.setClickable(true);

    }


//--------------Meus metodos para facilitar minha vida----------------------------------------------//


    private void alterarFragment(Fragment fragment, String key){

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //confirmar transição
        transaction.add(R.id.frag_container, fragment, key).commit();

    }

    private void mostrarFragment(Fragment fragment, String key){

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //confirmar transição
        transaction.replace(R.id.frag_container, fragment, key).addToBackStack(null).commit();

    }

    private void mostrarFragmentSemPilha(Fragment fragment, String key){

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //confirmar transição
        transaction.replace(R.id.frag_container, fragment, key).commit();

    }

    public void mostraFloatingActionButton(){
        fab.show();
        fab_change.animate().alpha(1.0f);
        fab_lista.setClickable(true);
        fab_mapa.setClickable(true);
    }

    public void escondeFloatingActionButton(){

        fab.hide();
        fab.startAnimation(fab_back_rotate);
        fab_nova_correncia.startAnimation(fab_close);
        fab_nova_correncia.setClickable(false);
        fab_info.startAnimation(fab_close);
        fab_info.setClickable(false);
        fab_change.animate().alpha(0.0f);
        fab_lista.setClickable(false);
        fab_mapa.setClickable(false);
        aberto = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //cor do icone
            fab_mapa.setColorFilter(getColor(R.color.branquinho));
            fab_lista.setColorFilter(getColor(R.color.pretinho));

            //cor do fundo
            fab_mapa.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.pretinho)));
            fab_lista.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.branquinho)));

            //Inicia Fragment do mapa
            mostrarFragmentSemPilha(new MapsFragment(), "MapsFragment");

            fab_lista.setClickable(true);
            fab_mapa.setClickable(false);

        } else{

            Toast.makeText(MainActivity.this, "Permita acessar Maps para visualizar", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }

//--------------Fim de codigo-----------------------------------------------------------------------//

}
