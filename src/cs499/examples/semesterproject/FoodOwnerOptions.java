package cs499.examples.semesterproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class FoodOwnerOptions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_owner_options);
	}
	
	public void addItems (View view)
	{
		Bundle extras = getIntent().getExtras();
		String userName = extras.getString("username");
		
		Intent i = new Intent (FoodOwnerOptions.this, AddItemsActivity.class);
		i.putExtra("username", userName);
		startActivity(i);
	}
	
	public void viewRequests (View view)
	{
		Bundle extras = getIntent().getExtras();
		String userName = extras.getString("username");
		
		Intent i = new Intent (FoodOwnerOptions.this, ViewRequests.class);
		i.putExtra("username", userName);
		startActivity(i);
	}
	
	public void userLogout (View view)
	{
		Intent intent = new Intent(FoodOwnerOptions.this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
	    startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_owner_options, menu);
		return true;
	}

}
