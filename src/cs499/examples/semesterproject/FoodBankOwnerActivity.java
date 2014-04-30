package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FoodBankOwnerActivity extends Activity 
{
	List<NameValuePair> foodBankPairs;
	int pid=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_bank_owner);
	}
	
	public void sendData (View view)
	{
		foodBankPairs =  new ArrayList<NameValuePair>();
		EditText foodPlaceOwnerName = (EditText) findViewById (R.id.foodPlaceOwnerName);
		EditText foodPlaceOwnerPassword = (EditText) findViewById (R.id.foodPlaceOwnerPassword);
		EditText foodPlaceName = (EditText) findViewById (R.id.foodPlaceName);
		EditText foodPlaceStreetAddress = (EditText) findViewById (R.id.foodPlaceStreetAddress);
		EditText foodPlaceCity = (EditText) findViewById (R.id.foodPlaceCity);
		EditText foodPlaceState = (EditText) findViewById (R.id.foodPlaceState);
		EditText foodPlaceZip = (EditText) findViewById (R.id.foodPlaceZip);
		EditText foodPlacePhone = (EditText) findViewById (R.id.foodPlacePhone);
		
		if (!this.checkEmpty())
		{
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceOwnerName", foodPlaceOwnerName.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceOwnerPassword", foodPlaceOwnerPassword.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceName", foodPlaceName.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceStreetAddress", foodPlaceStreetAddress.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceCity", foodPlaceCity.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceState", foodPlaceState.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceZip", foodPlaceZip.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("foodPlacePhone", foodPlacePhone.getText().toString()));
			foodBankPairs.add(new BasicNameValuePair ("action", "registerfoodplace"));
			
			/*SecureRandom random = new SecureRandom();
			String username = new BigInteger(130, random).toString(32);
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceOwnerName", "D" + username ));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceOwnerPassword", "E"));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceName", "Shiny"));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceStreetAddress", "100 Riverturn Pike"));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceCity", "Annandale"));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceState", "VA"));
			foodBankPairs.add(new BasicNameValuePair ("foodPlaceZip", "22181"));*/
			
			Log.d("", "FoodBankActivity NameValuePairs");
			for (NameValuePair p: foodBankPairs)
			{
				
				Log.d(p.getName(), p.getValue());
			}
			new AsyncAddPlace(FoodBankOwnerActivity.this, 	Constants.URL, foodBankPairs).execute();
		}
		else
		{
			Toast.makeText(this, "Please fill in missing input", Toast.LENGTH_LONG).show();
		}
		//new AsyncAddPlace(FoodBankOwnerActivity.this, "http://10.159.226.169:9080/foodapp/foodbank", foodBankPairs).execute();
	}
	
	public boolean checkEmpty()
	{
		EditText foodPlaceOwnerName = (EditText) findViewById (R.id.foodPlaceOwnerName);
		EditText foodPlaceOwnerPassword = (EditText) findViewById (R.id.foodPlaceOwnerPassword);
		EditText foodPlaceName = (EditText) findViewById (R.id.foodPlaceName);
		EditText foodPlaceStreetAddress = (EditText) findViewById (R.id.foodPlaceStreetAddress);
		EditText foodPlaceCity = (EditText) findViewById (R.id.foodPlaceCity);
		EditText foodPlaceState = (EditText) findViewById (R.id.foodPlaceState);
		EditText foodPlaceZip = (EditText) findViewById (R.id.foodPlaceZip);
		EditText foodPlacePhone = (EditText) findViewById (R.id.foodPlacePhone);
		

		String a = foodPlaceOwnerName.getText().toString();
		String b = foodPlaceOwnerPassword.getText().toString();
		String c = foodPlaceName.getText().toString();
		String d = foodPlaceStreetAddress.getText().toString();
		String e = foodPlaceCity.getText().toString();
		String f = foodPlaceState.getText().toString();
		String g = foodPlaceZip.getText().toString();
		String h = foodPlacePhone.getText().toString();
		
		return a.trim().equals("") || b.trim().equals("") || c.trim().equals("") || d.trim().equals("") ||
				e.trim().equals("") || f.trim().equals("") || g.trim().equals("") || h.trim().equals("");
		
	}
	
	public void clearAll (View view)
	{
		EditText foodPlaceOwnerName = (EditText) findViewById (R.id.foodPlaceOwnerName);
		EditText foodPlaceOwnerPassword = (EditText) findViewById (R.id.foodPlaceOwnerPassword);
		EditText foodPlaceName = (EditText) findViewById (R.id.foodPlaceName);
		EditText foodPlaceStreetAddress = (EditText) findViewById (R.id.foodPlaceStreetAddress);
		EditText foodPlaceCity = (EditText) findViewById (R.id.foodPlaceCity);
		EditText foodPlaceState = (EditText) findViewById (R.id.foodPlaceState);
		EditText foodPlaceZip = (EditText) findViewById (R.id.foodPlaceZip);
		EditText foodPlacePhone = (EditText) findViewById (R.id.foodPlacePhone);
		
		foodPlaceOwnerName.setText("");
		foodPlaceOwnerPassword.setText("");
		foodPlaceName.setText("");
		foodPlaceStreetAddress.setText("");
		foodPlaceCity.setText("");
		foodPlaceState.setText("");
		foodPlaceZip.setText("");
		foodPlacePhone.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_bank_owner, menu);
		return true;
	}

}
