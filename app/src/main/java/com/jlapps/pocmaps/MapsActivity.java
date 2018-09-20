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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;

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

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    }

    public void setMyLocation( Location location ){
        if(location != null) {
            // Recupera latitude e longitude da
            // ultima localização do usuário
            LatLng ultimaLocalizacao = new LatLng(location.getLatitude(), location.getLongitude());
            // Configuração da câmera
            final CameraPosition position = new CameraPosition.Builder()
                    .target(ultimaLocalizacao)     //  Localização
                    .bearing(45)        //  Rotação da câmera
                    .tilt(90)            //   ngulo em graus
                    .zoom(17)           //  Zoom
                    .build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.animateCamera(update);
            // Criando um objeto do tipo MarkerOptions
            final MarkerOptions markerOptions = new MarkerOptions();
            // Configurando as propriedades do marker
            markerOptions.position( ultimaLocalizacao )    // Localização
                    .title("Minha Localização")       // Título
                    .snippet("Latitude: , Longitude:"); // Descrição
            mMap.addMarker( markerOptions );
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates(); // Inicia o GPS
        // Log.i("LOG", "Conectado ao Google Play Services!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();//para o GPS
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



    // Método responsável por ativar o monitoramento do GPS
    protected void startLocationUpdates(){
        //LocationServices.getFusedLocationProviderClient( this );
        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(this);
    }

    // Método responsável por desativar o monitoramento do GPS
    protected void stopLocationUpdates(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        setMyLocation( location ); // Atualiza localização do usuário no mapa
    }
}
