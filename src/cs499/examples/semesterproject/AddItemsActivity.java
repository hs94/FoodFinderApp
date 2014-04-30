package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddItemsActivity extends Activity 
{
	List <NameValuePair> pairs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_items);
	}
	
	public void submitItems (View view)
	{
		Bundle extras = getIntent().getExtras();
		String username = extras.getString("username");
		EditText itemName = (EditText) findViewById (R.id.itemName);
		EditText itemQuantity = (EditText) findViewById (R.id.itemQuantity); 
		pairs = new ArrayList<NameValuePair>();
		String url = Constants.URL;
		pairs.add (new BasicNameValuePair ("itemName", itemName.getText().toString()));
		pairs.add (new BasicNameValuePair ("itemQuantity", itemQuantity.getText().toString()));
		pairs.add (new BasicNameValuePair ("ownername", username));
		pairs.add (new BasicNameValuePair ("action", "additems"));
		
		AsyncAddItems task = new AsyncAddItems (AddItemsActivity.this, pairs, url);
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_items, menu);
		return true;
	}

}
