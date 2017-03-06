package com.flycode.transporttrackeryerevan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.flycode.transporttrackeryerevan.api.APIBuilder;
import com.flycode.transporttrackeryerevan.api.APIService;
import com.flycode.transporttrackeryerevan.model.ItemBus;
import com.flycode.transporttrackeryerevan.response.BusesListResponse;
import com.flycode.transporttrackeryerevan.util.DeviceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private com.google.android.gms.maps.MapView myMapView;
    private GoogleApiClient mGoogleApiClient;

    private LatLng latLng;
    private GoogleMap mGoogleMap;
    private LatLng busLatLng;
    private double busRotationAzimuth;
    private String busNumberValue;
    private int clearParam = 0;

    private Toast infoToast;

    private static final String MAP_TYPES[] = {"Normal", "Hybrid", "Satellite", "Terrain"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getJasonParams();

        myMapView = (com.google.android.gms.maps.MapView) findViewById(R.id.myMap);
        myMapView.onCreate(savedInstanceState);
        myMapView.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Intent titleIntent = getIntent();
        String busNumbers[] = titleIntent.getStringArrayExtra("BUS_NUMBERS_LIST");
        int clickPosition = titleIntent.getIntExtra("CLICK_POSITION", 0);
        setTitle(busNumbers[clickPosition]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myMapView.onResume();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        myMapView.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        myMapView.onStop();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mGoogleMap = googleMap;

        //_________permission_check________
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        processSpinner();
    }

    @Override
    protected void onPause() {
        myMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        myMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getMyLastLocation();
        try {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        } catch (Exception e) {
//            infoToast = Toast.makeText(getBaseContext(), R.string.error_my_location, Toast.LENGTH_LONG);
//            infoToast.show();
            Log.i("TAGG", getResources().getString(R.string.error_my_location));
        };

        updateMarkerPosition();

        try {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busLatLng, 16));
        } catch (Exception e) {
//            infoToast = Toast.makeText(getBaseContext(), R.string.error_bus_location, Toast.LENGTH_LONG);
//            infoToast.show();
            Log.i("TAGG", getResources().getString(R.string.error_bus_location));
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private static final LatLngBounds ARMENIA = new LatLngBounds(
            new LatLng(38.968, 43.588), new LatLng(41.041, 46.485));

    private void processSpinner() {
        //_________spinner____________________
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MAP_TYPES);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner changeMapType = (Spinner) findViewById(R.id.map_type_spinner);
        changeMapType.setAdapter(modeAdapter);
        changeMapType.setSelection(0);
        changeMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 2:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getMyLastLocation() {

        //__permission check_________
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            mGoogleMap.setLatLngBoundsForCameraTarget(ARMENIA);
            mGoogleMap.setMinZoomPreference(8);
            mGoogleMap.setMaxZoomPreference(18);
        }
    }

    private void getJasonParams() {

        APIService apiService =
                APIBuilder
                        .getClient()
                        .create(APIService.class);

        Call<BusesListResponse> call = apiService.getBuses();
        call.enqueue(new Callback<BusesListResponse>() {
            @Override
            public void onResponse(Call<BusesListResponse> call, Response<BusesListResponse> response) {

                if (response.isSuccessful()) {
                    List<ItemBus> itemBusList = response.body().getObjs();
                    Log.i("TAG", "onResponse");

                    busLatLng = null;
                    busLatLng = new LatLng(itemBusList.get(0).getGeo()[0], itemBusList.get(0).getGeo()[1]);
                    busRotationAzimuth = Double.parseDouble(itemBusList.get(0).getAzimuth());
                    busNumberValue = String.valueOf(itemBusList.get(0).getBusNumber());
                }
            }

            @Override
            public void onFailure(Call<BusesListResponse> call, Throwable t) {
                Log.i("TAG", "onFailure");
                if (!isOnline()) {
//                    infoToast = Toast.makeText(getBaseContext(), R.string.error_network, Toast.LENGTH_LONG);
//                    infoToast.show();
                    Log.i("TAGG", getResources().getString(R.string.error_network));
                    if (clearParam == 1) {
                        mGoogleMap.clear();
                    }

                } else {
                    if (clearParam ==1) {
//                        infoToast = Toast.makeText(getBaseContext(), R.string.error_bus_location, Toast.LENGTH_LONG);
//                        infoToast.show();
                        Log.i("TAGG", getResources().getString(R.string.error_bus_location));
                        mGoogleMap.clear();
                    }
                }
            }
        });
    }

    /*private void tuneBusMarker() {
        markerOptions = new MarkerOptions();
        markerOptions.position(busLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
        markerOptions.rotation(Math.round(busRotationAzimuth));
    }*/

    private void addBusMarker(){
        int contentRightPadding = (int) DeviceUtil.getPxForDp(this, 20);

        IconGenerator iconFactory = new IconGenerator(getBaseContext());
        iconFactory.setBackground(getResources().getDrawable(R.drawable.bus));
        if (busRotationAzimuth == 0) {
            busRotationAzimuth = 90;
            iconFactory.setContentPadding(0 , 0 , contentRightPadding, 0);
        }
        iconFactory.setContentRotation(-(int) Math.round(busRotationAzimuth));
        iconFactory.setRotation((int) Math.round(busRotationAzimuth));
        addIcon(iconFactory, busNumberValue, busLatLng);
    }

    private void updateMarkerPosition() {
        CountDownTimer t = new CountDownTimer(Long.MAX_VALUE, 10000) {

            // This is called every interval. (Every 10 seconds)
            public void onTick(long millisUntilFinished) {
                Log.i("TAG", "Timer tick");

                getJasonParams();

                //tuneBusMarker();

                mGoogleMap.clear();
                //mGoogleMap.addMarker(markerOptions);
                try {

                    if (busNumberValue != null && busLatLng != null) {
                        addBusMarker();
                        clearParam = 1;

                    } else {

                        ConnectivityManager cm =
                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
//                            infoToast = Toast.makeText(getBaseContext(), R.string.error_bus_location, Toast.LENGTH_LONG);
//                            infoToast.show();
                            Log.i("TAGG", getResources().getString(R.string.error_bus_location));

                            mGoogleMap.clear();
                        }
                    }

                } catch (Exception e){

//                    infoToast = Toast.makeText(getBaseContext(), "Something get wrong...", Toast.LENGTH_LONG);
//                    infoToast.show();
                    Log.i("TAGG", "Something get wrong...");
                };
            }

            public void onFinish() {
                Log.i("TAG", "Timer last tick");
                updateMarkerPosition();
                start();
            }
        }.start();
    }

//_________google_Icon_generator_function___________________
    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        mGoogleMap.addMarker(markerOptions);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
