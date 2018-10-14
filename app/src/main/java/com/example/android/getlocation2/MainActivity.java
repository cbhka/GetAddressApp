package com.example.android.getlocation2;

import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.getlocation2.LocationUtil.LocationHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, OnRequestPermissionsResultCallback{

    Location lastLocation;
    LocationHelper locationHelper;
    double longitude;
    double latitude;
    String mAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationHelper = new LocationHelper(this);
        locationHelper.checkpermission();
        if(locationHelper.checkPlayServices()){
            locationHelper.buildGoogleApiClient();
        }

        //Create onClickListener for the button and when it clicked call the getLocation method
        setOnClickListenerForMyButton();
    }

    public void getLocation(){
        //Get the last known (Or Current) location of the user.
        lastLocation = locationHelper.getLocation();
        //if location is not null(empty) get longitude and latitude
        if(lastLocation != null){
            //get the longitude
            longitude = lastLocation.getLongitude();
            //get the latitude
            latitude = lastLocation.getLatitude();
            //get the address
            getAddress();
        }
        else{
            //if location is empty show toast message
            Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device\n Or wait few seconds then try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnClickListenerForMyButton(){
        Button button = findViewById(R.id.get_location_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }


    private void showLocation(String txt){
        TextView textView = findViewById(R.id.text_view);
        textView.setText(txt);
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        lastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }


    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    public void getAddress()
    {
        Address locationAddress;

        locationAddress = locationHelper.getAddress(latitude, longitude);

        if(locationAddress!=null)
        {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+="\n"+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;

                //Here we can use the currentLocation variable which contains the address
                mAddress = currentLocation;
                //show the address to the screen
                showLocation(mAddress);
            }

        }
        else
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

}
