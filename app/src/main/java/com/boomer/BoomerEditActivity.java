package com.boomer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

//import com.BoomerBackend.RegistrationEndpoint;


public class BoomerEditActivity extends Activity {
    EditText boomerText;
    ImageButton sendBooomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.boomer_edit_activity);

       // Intent intent = new Intent(this, RegistrationEndpoint.class);
       // startActivity(intent);

        boomerText = ((EditText) findViewById(R.id.boomerText));
        boomerText.setGravity(Gravity.CENTER_HORIZONTAL);
        boomerText.setTextColor(Color.WHITE);

        //FrameLayout drop = (FrameLayout)findViewById(R.id.boomerEditVIew);
        sendBooomer = (ImageButton)findViewById(R.id.boomer_right_arrow);

        sendBooomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.boomer_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
