package com.jlapps.pocmaps;

import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Localização sede da Google
        final LatLng google = new LatLng(40.740637, -74.002039);
        // Configuração da câmera
        final CameraPosition position = new CameraPosition.Builder()
                .target(google)     //  Localização
                .bearing(45)        //  Rotação da câmera
                .tilt(90)             //  Ângulo em graus
                .zoom(17)           //  Zoom
                .build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
        mMap.animateCamera(update);

        // Criando um objeto do tipo MarkerOptions

        final MarkerOptions markerOptions = new MarkerOptions();

        // Configurando as propriedades do marker

        markerOptions.position(google)    // Localização

                .title("Google Inc.")    // Título

                .snippet("Sede da Google"); // Descrição
        // Adicionando marcador ao mapa

        mMap.addMarker(markerOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Criar marker no marker
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                mMap.addMarker(options);

                // Configurando as propriedades da Linha
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(google);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.BLUE);
                // Adiciona a linha no mapa
                mMap.addPolyline(polylineOptions);
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("LOG", "Conectado ao Google Play Services!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "Conexão Interrompida");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOG", "Erro ao conectar: " + connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
