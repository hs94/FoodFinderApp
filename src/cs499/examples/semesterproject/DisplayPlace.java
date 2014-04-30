package cs499.examples.semesterproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.view.View;

public class DisplayPlace extends Activity 
{
	String s = "";
	String placeName = "";
	String formattedAddress;
	String sentAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_place);
		
		Bundle extras = getIntent().getExtras();
		String address = extras.getString("Places");
		
		formattedAddress = address.substring(address.indexOf(":")+1, address.length());
		Log.d("formattedAddress: ", formattedAddress);
		sentAddress = formattedAddress.substring(formattedAddress.indexOf(",")+1, formattedAddress.length());
		Log.d("sentAddress: ", sentAddress);
		String[] addressArray = formattedAddress.split(",");
		placeName = addressArray[0].trim();
		Log.d("DisplayPlace placeName: ", placeName);
		
		for (int i = 0; i < addressArray.length; i++)
		{
			s += addressArray[i] + "\n";
		}
		
		TextView displayPlace = (TextView) findViewById (R.id.displayPlace);
		displayPlace.setText(s);
	}
	
	public void displayItems (View view)
	{
		Bundle extras = getIntent().getExtras();
		String sender = extras.getString("sendername");
		Intent i = new Intent (DisplayPlace.this, DisplayPlaceItems.class);
		i.putExtra("placeName", placeName);
		i.putExtra("sender", sender);
		startActivity(i);
	}
	
	public void displayMaps (View view)
	{
		if (formattedAddress != null || formattedAddress.trim().length()==0)
		{
			Intent i = new Intent (DisplayPlace.this, DisplayMap.class);
			i.putExtra("address", sentAddress);
			startActivity(i);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_place, menu);
		return true;
	}

}
