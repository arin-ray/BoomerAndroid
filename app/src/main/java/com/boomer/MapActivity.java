package com.boomer;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener,OnMarkerDragListener {

		GoogleApiClient mGoogleApiClient ;
		Location mLastLocation;
		private GoogleMap mMap;
		private List<LatLng> coordinates = new ArrayList<LatLng>();
		
		// These settings are the same as the settings for the map. They will in fact give you updates
		// at the maximal rates currently possible.
//		private static final LocationRequest REQUEST = LocationRequest.create()
//				.setInterval(5000)         // 5 seconds
//				.setFastestInterval(16)    // 16ms = 60fps
//				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.maps_layout);
			buildGoogleApiClient();
			mGoogleApiClient.connect();
			
			
			//Setting hardcoded cords
			coordinates.add(new LatLng(40.739245,-74.005773));
			coordinates.add(new LatLng(40.728579, -73.981311));
			coordinates.add(new LatLng(40.755632,-73.987276));
		}
		
		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			
		}

	  @Override
	    public void onConnected(Bundle connectionHint) {
	        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
	        /*if (mLastLocation != null) {
	            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
	            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
	        }*/
	        
			
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates.get(0), 12);
			mMap.animateCamera(cameraUpdate);

			//Retrive city name 
			Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
			try {
				for (LatLng coor : coordinates){
					List<Address> addresses = geoCoder.getFromLocation(40.739245, -74.005773, 1);
					
					if(addresses.size()>0){
						mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.boomer_marker)).position(coor).title(addresses.get(0).getLocality()).draggable(true));
					}	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

		@Override
		public void onConnectionSuspended(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
		protected synchronized void buildGoogleApiClient() {
		    mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(LocationServices.API)
		        .build();
		}
		
		@Override
		protected void onResume() {
			super.onResume();
			setUpMapIfNeeded();
			mGoogleApiClient.connect();
		}
		
		private void setUpMapIfNeeded() {
			// Do a null check to confirm that we have not already instantiated the map.
			if (mMap == null) {
				// Try to obtain the map from the SupportMapFragment.
				mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
				// Check if we were successful in obtaining the map.
				if (mMap != null) {
					//mMap.setMyLocationEnabled(true);
					mMap.setOnMarkerDragListener(this);
				}
			}
		}



		@Override
		public void onMarkerDragEnd(Marker arg0) {
			startActivity(new Intent(getBaseContext(),MapActivity.class));
          	finish();
			
		}

		@Override
		public void onMarkerDragStart(Marker arg0) {
			Intent imageIntent = new Intent(getApplicationContext(),ImageActivity.class);
			imageIntent.putExtra("CITY", arg0.getTitle());
			imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(imageIntent);
		}

		@Override
		public void onMarkerDrag(Marker arg0) {
			// TODO Auto-generated method stub
			
		}



}
