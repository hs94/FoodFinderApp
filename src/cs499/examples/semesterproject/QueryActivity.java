package cs499.examples.semesterproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class QueryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
	}
	
	public void sendSearchData (View view)
	{
		Bundle extras = getIntent().getExtras();
		String username = extras.getString("username");
		
		EditText searchCity = (EditText) findViewById (R.id.searchCity);
		EditText searchZip = (EditText) findViewById (R.id.searchZip);
		
		Intent i = new Intent (QueryActivity.this, DisplayMatchingPlaces.class);
		i.putExtra("username", username);
		i.putExtra("Search city", searchCity.getText().toString());
		i.putExtra("Search zip", searchZip.getText().toString());
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

}
