package cs499.examples.semesterproject;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Menu;

public class DisplayMap extends MapActivity 
{
	private double lat;
	private double lon;
	private MapView myMap;
	private Geocoder gc;
	private String address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_map);
		
		Bundle extras = getIntent().getExtras();
		address = extras.getString("address");
		
		myMap = (MapView) findViewById(R.id.simpleGM_map);
		gc = new Geocoder(this);
		
		try 
		{

			List<Address> foundAdresses = gc.getFromLocationName(
					address, 5); // Search addresses

			if (foundAdresses.size() == 0) { // if no address found,
				// display an error
				Dialog locationError = new AlertDialog.Builder(DisplayMap.this).setIcon(0).setTitle(
						"Error").setPositiveButton("ok", null)
						.setMessage(
								"Sorry, your address doesn't exist.")
						.create();
				locationError.show();
			} 
			else 
			{ // else display address on map
				for (int i = 0; i < foundAdresses.size(); ++i) 
				{
					// Save results as Longitude and Latitude
					// @todo: if more than one result, then show a
					// select-list
					Address x = foundAdresses.get(i);
					lat = x.getLatitude();
					lon = x.getLongitude();
				}
				navigateToLocation((lat * 1000000), (lon * 1000000),
						myMap); // display the found address
			}
		} catch (Exception e) {
			// @todo: Show error message
		}

	}
	
	public static void navigateToLocation(double latitude, double longitude,
			MapView mv) {
		GeoPoint p = new GeoPoint((int) latitude, (int) longitude); // new
		// GeoPoint
		mv.displayZoomControls(true); // display Zoom (seems that it doesn't
		// work yet)
		MapController mc = mv.getController();
		mc.animateTo(p); // move map to the given point
		int zoomlevel = mv.getMaxZoomLevel(); // detect maximum zoom level
		mc.setZoom(zoomlevel - 1); // zoom
		mv.setSatellite(false); // display only "normal" mapview

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
