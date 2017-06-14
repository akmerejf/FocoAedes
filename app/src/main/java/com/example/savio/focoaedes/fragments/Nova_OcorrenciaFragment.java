package com.example.savio.focoaedes.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savio.focoaedes.MainActivity;
import com.example.savio.focoaedes.R;
import com.example.savio.focoaedes.base.BaseLocal;
import com.example.savio.focoaedes.base.Service;
import com.example.savio.focoaedes.mascaras.MaskTelefone;
import com.example.savio.focoaedes.model.Ocorrencia;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nova_OcorrenciaFragment extends Fragment {

    MaskTelefone maskTelefone;
    BaseLocal baseLocal;

    View view;
    EditText titulo, bairro, rua, telefone, email, descricao;
    TextView salvar;
    ImageView cancelar, foto;
    Toolbar toolbar;
    private final int CAMERA_REQUEST = 1888;
    final int   CROP_PIC = 2;
    private Uri pic_uri;

    SimpleDateFormat formato;
    String data = "";

    public Nova_OcorrenciaFragment() { /*Required empty public constructor*/ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nova_ocorrencia, container, false);

        baseLocal = new BaseLocal(getContext());
        formato = new SimpleDateFormat("dd/MM/yyyy");

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        cancelar = (ImageView) view.findViewById(R.id.nova_cancelar);
        salvar = (TextView) view.findViewById(R.id.nova_salvar);

        foto = (ImageView) view.findViewById(R.id.nova_foto);
        titulo = (EditText) view.findViewById(R.id.nova_titulo);
        data = formato.format(new Date());
        bairro = (EditText) view.findViewById(R.id.nova_bairro);
        rua = (EditText) view.findViewById(R.id.nova_endereco);
        telefone = (EditText) view.findViewById(R.id.nova_telefone);
        email = (EditText) view.findViewById(R.id.nova_email);
        descricao = (EditText) view.findViewById(R.id.nova_descricao);

        maskTelefone = new MaskTelefone(new WeakReference<>(telefone));
        telefone.addTextChangedListener(maskTelefone);


//--------------Focus no formulario-----------------------------------------------------------------//


        //chama o metodo para ativar o focus
        focusFormulario(titulo);focusFormulario(bairro);focusFormulario(rua);
        focusFormulario(telefone);focusFormulario(email);focusFormulario(descricao);


//--------------Listeners do toolbar e outros-------------------------------------------------------//


        //clique no icone com a camerazinha ativa a camera do celular
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }

            }
        });

        //clique na seta para voltar ao mapa
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //escoder teclado
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                getFragmentManager().popBackStack();
            }
        });

        //salva a informações registradas no formulario
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            novaOcorrencia();

            //escoder teclado
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            getFragmentManager().popBackStack();
            }
        });


        return view;
    }


//--------------Ciclo de vida-----------------------------------------------------------------------//


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //adiciona o action bar criado
        ((MainActivity) getActivity()).setSupportActionBar(toolbar); //ativar toolbar

        //esconde o menu flutuante
        ((MainActivity) getActivity()).escondeFloatingActionButton(); //esconder o botao flutuante
    }


//--------------Meus metodos para facilitar minha vida----------------------------------------------//


    //ativa o o focus na borda de cada item do formulario escolhido
    void focusFormulario(EditText editText){

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.textfield_bg_focus);
                }else{
                    v.setBackgroundResource(R.drawable.textfield_bg);
                }
        }
        });
    }

    //recebe a foto tirada
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //se a camera for ativado
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            //captura a imagem tirada da camera
            pic_uri = data.getData();

            cortarFoto();
        }
        //se o cortador de foto for ativado
        else if (requestCode == CROP_PIC) {
            Bundle extras = data.getExtras();
            Bitmap photo = extras.getParcelable("data");

            foto.setImageBitmap(photo);
        }
    }

    //metodo para abrir a edição de foto (cortar)
    private void cortarFoto() {

        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(pic_uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 250);
            cropIntent.putExtra("outputY", 250);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_PIC);
        }
        catch (ActivityNotFoundException e) {

            Log.i("NovaOcorrencia", e.toString());
        }
    }

    void novaOcorrencia(){

        //implementa a interface Service e faz a requisição dos dados
        Service service = Service.retrofit.create(Service.class);

        //Call<Catalogos> requisicao = service.listaCatalogos();
        Call<Ocorrencia> post = service.setOcorrencias(new Ocorrencia(
                foto.getDrawable().toString(),
                titulo.getText().toString(),
                data,
                bairro.getText().toString(),
                rua.getText().toString(),
                telefone.getText().toString(),
                email.getText().toString(),
                descricao.getText().toString()));

        post.enqueue(new Callback<Ocorrencia>() {
            @Override
            public void onResponse(Call<Ocorrencia> call, Response<Ocorrencia> response) {

                if(!response.isSuccessful()){

                    Log.i("NOVA OCORRENCIA", "Codigo de Erro: " + response.code());

                }else{

                    Toast.makeText(getActivity(), "Criado Com sucesso", Toast.LENGTH_SHORT).show();

                    //codigo acima deveria entrar aqui
                    Log.i("NOVA OCORRENCIA", "Beleza ");
                }

            }

            @Override
            public void onFailure(Call<Ocorrencia> call, Throwable t) {

                Log.i("NOVA OCORRENCIA", "Falha: " + t.getMessage());

            }
        });

    }


//--------------Fim de codigo-----------------------------------------------------------------------//


}
