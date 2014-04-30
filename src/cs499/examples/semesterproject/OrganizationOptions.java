package cs499.examples.semesterproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class OrganizationOptions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization_options);
	}
	
	public void searchFoodplaces (View view)
	{
		Bundle extras = getIntent().getExtras();
		String username = extras.getString("username");
		
		Intent i = new Intent (OrganizationOptions.this, QueryActivity.class);
		i.putExtra("username",username);
		startActivity(i);
	}
	
		public void userLogout (View view)
		{
			Intent intent = new Intent(OrganizationOptions.this, MainActivity.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
		    startActivity(intent);
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.organization_options, menu);
		return true;
	}

}
