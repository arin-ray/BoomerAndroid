package com.boomer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Future;


public class UploadActivity extends Activity implements LocationListener{
    Button imgsel,upload;
    ImageView img;
    String path;
    Double latt,longd;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView loc;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boomer_upload);
        loc = (TextView) findViewById(R.id.loc);

        img = (ImageView)findViewById(R.id.img);
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        imgsel = (Button)findViewById(R.id.selimg);
        upload =(Button)findViewById(R.id.uploadimage);
        upload.setVisibility(View.INVISIBLE);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) this);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(path);

                Future uploading = Ion.with(UploadActivity.this)
                        .load("http://192.168.0.12:8080/uploadboomer")

                        .setMultipartFile("image", f)
                        .setMultipartParameter("lattitude",latt.toString())
                        .setMultipartParameter("longditude",longd.toString())

                        .asString()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                try {
                                    JSONObject jobj = new JSONObject(result.getResult());
                                    Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        });
            }

        });

        imgsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                try {
                    startActivityForResult(intent, 100);
                } catch (ActivityNotFoundException e) {

                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        loc = (TextView) findViewById(R.id.loc);
        loc.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        latt = location.getLatitude();
        longd = location.getLongitude();
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = getPathFromURI(data.getData());
                    img.setImageURI(data.getData());
                    upload.setVisibility(View.VISIBLE);

                }
        }
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
