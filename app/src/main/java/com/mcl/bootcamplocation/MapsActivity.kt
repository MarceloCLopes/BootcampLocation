package com.mcl.bootcamplocation

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Obtenha o SupportMapFragment e seja notificado quando o mapa estiver pronto para ser usado.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available. -- (Manipula o mapa quando disponível.)
     * This callback is triggered when the map is ready to be used. -- (Este retorno de chamada é acionado quando o mapa está pronto para ser usado.)     *
     * This is where we can add markers or lines, add listeners or move the camera. In this case, -- (É aqui que podemos adicionar marcadores ou linhas, adicionar ouvintes ou mover a câmera. Nesse caso,)
     * we just add a marker near Sydney, Australia. -- (acabamos de adicionar um marcador perto de Sydney, Austrália.)
     * If Google Play services is not installed on the device, the user will be prompted to install -- (Se o Google Play Services não estiver instalado no dispositivo, o usuário será solicitado a instalar)
     * it inside the SupportMapFragment. This method will only be triggered once the user has -- (dentro do SupportMapFragment. Este método só será acionado quando o usuário tiver)
     * installed Google Play services and returned to the app. -- (instalou o Google Play Services e voltou ao aplicativo.)
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val myPlace = LatLng(40.73, -73.99)
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false
}