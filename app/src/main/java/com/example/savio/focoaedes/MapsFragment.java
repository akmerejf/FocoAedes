package com.example.savio.focoaedes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.savio.focoaedes.model.Endereco;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {


//---------------Variaveis globais------------------------------------------------------------------//

    Endereco endereco;
    GoogleMap mMap;
    LocationManager locationManager;
    float zoom;


//---------------Ciclo de vida----------------------------------------------------------------------//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).mostraFloatingActionButton();

        try {

            //ativar o GPS
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            //atualiza o gps a cada seg ou metros
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);

        } catch (SecurityException e) {

            Log.i("MapsFragment", e.toString());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        //desativar o GPS
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }


//---------------Quando o mapa estiver carregado----------------------------------------------------//


    @Override
    public void onMapReady(GoogleMap googleMap) {

        endereco = new Endereco();

        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        //noinspection MissingPermission
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14.f));

        //configurações do botões nativos da google
        mMap.getUiSettings().setMapToolbarEnabled(false);

        try { geoLocaliza(); } catch (IOException e) { e.printStackTrace(); }

    }


//---------------Espera o clique no mapa------------------------------------------------------------//


    @Override
    public void onMapClick(LatLng latLng) {

        Log.i("coord", latLng.toString());
    }


//---------------Metodos do Location Listener-------------------------------------------------------//


    @Override
    public void onLocationChanged(Location location) {

        //aproxima da sua localização (segue sua localização a cada atualização)
        LatLng meulocal = new LatLng(location.getLatitude(), location.getLongitude());

        zoom = mMap.getCameraPosition().zoom;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meulocal,zoom));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}


    public void geoLocaliza() throws IOException {

        Geocoder geocoder = new Geocoder(getActivity());

        List<Address> list = geocoder.getFromLocationName("Avenida José Malcher, 1963", 1);

        Address add = list.get(0);

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        LatLng marca = new LatLng(lat,lng);

        mMap.addMarker(new MarkerOptions().position(marca).title("Muito lixo no quintal"));

    }


//---------------Fim de codigo----------------------------------------------------------------------//

}
