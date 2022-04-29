package com.templo.androidcoursefinalproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.templo.androidcoursefinalproject.databinding.ActivityMapsSelectLocBinding;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsSelectLocActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap = null;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private ActivityMapsSelectLocBinding binding;

    private Location currentLocation;

    private static final int MAP_MARKER_ZOOM = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsSelectLocBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Set-up location manager for retrieving current location in GPS.
        //(Location Manager instantiated must be before requestPermission(). Otherwise, there'll be null object reference function call error.)
        setupLocationManager();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Set-up auto complete searchbar for location in Google Map.
        setupPlacesAutoCompleteSupportFragment();

        binding.confirmLocFAB.setOnClickListener(v -> {
            if (mMap != null && currentLocation != null) { //If map is ready and current location is set.
                try {
                    List<Address> addresses = getAddressesInItsLocaleLanguage();
                    Address address = addresses.get(0);
                    Intent intent = getIntent();
                    intent.putExtra("Address", address.getAddressLine(0));
                    intent.putExtra("FeatureName", address.getFeatureName());
                    intent.putExtra("Locality", address.getLocality());
                    String state = address.getAdminArea();
                    String subAdmin = address.getSubAdminArea(); //This is equivalent to "City" or "County" in Taiwan.
                    if (state != null) intent.putExtra("State", state);
                    if (subAdmin != null) intent.putExtra("SubAdmin", subAdmin);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (IOException e) {
                    Log.d("AddressReceived", "FAILED");
                    e.printStackTrace();
                }
            }
        });
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (locationManager != null)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener); //Must stop or it will keep going after activity finishes.
        locationManager = null;
    }

    private void setupLocationManager() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) { //When user has changed location.
                Log.d("LOCATION", location.toString());
                currentLocation = location;
                if (mMap != null) {
                    updateMapMarkerToCurrentLoc();
                }
            }
        };

        //This permission check is required before requestLocationUpdates(). If permission check failed, will request permission explicitly.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //Request permission explicitly if permission check fails. This will execute onRequestPermissionsResult() callback.
            //This launches a popup window asking for permission for the LocationManager to use GPS.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            //Register for location updates from the given provider with the given arguments, and a callback on the Looper of the calling thread.
            //2nd arg: minTimeMs – minimum time interval between location updates in milliseconds;
            //3rd arg: minDistanceM – minimum distance between location updates in meters.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void setupPlacesAutoCompleteSupportFragment() {
        //Places must be initialized so that AutocompleteSupportFragment can be used.
        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));

        //Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }

    private void updateMapMarkerToCurrentLoc() {
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Marker marker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, MAP_MARKER_ZOOM));
    }

    //Link to tutorial: https://stackoverflow.com/questions/31077014/how-to-get-geocoder-s-results-on-the-latlng-s-country-language
    private List<Address> getAddressesInItsLocaleLanguage() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
        Address address = addresses.get(0);
        String countryCode = address.getCountryCode();
        String langCode = null;

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale localeIn : locales) {
            if (countryCode.equalsIgnoreCase(localeIn.getCountry())) {
                langCode = localeIn.getLanguage();
                break;
            }
        }
        Locale locale = new Locale(langCode, countryCode);
        geocoder = new Geocoder(this, locale);
        try {
            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
//            Log.d("Address", addresses.get(0).getAddressLine(0));
        }
        catch (IOException | IndexOutOfBoundsException | NullPointerException ex) {
            addresses = null;
        }
        return addresses;
    }
}