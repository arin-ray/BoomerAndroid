package com.boomer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_screen);
		
		
		Bundle extra  = getIntent().getExtras();
		String city = (String) extra.get("CITY");
		if (city !=null ){
			
			int image = CityImageEnum.getImageFromCity(city);
			//retrieve city name 
			ImageView img = (ImageView) findViewById(R.id.imgView);
			img.setBackgroundResource(image);
			img.setImageResource(image);
			
			
			TextView testo = (TextView) findViewById(R.id.txtCity);
			testo.setText(city);
			
			/*Thread timerLogo = new Thread(){
				@Override
				public void run() {
					try {
						sleep(2000);
					}catch (Exception e) {
	                    e.printStackTrace();
	                }finally{
	                	startActivity(new Intent(getBaseContext(),MapActivity2.class));
	                  	finish();
	                }
				}
			};
			timerLogo.start();		*/
		}

	}
}
