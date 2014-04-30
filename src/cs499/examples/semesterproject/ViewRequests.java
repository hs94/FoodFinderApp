package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class ViewRequests extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_requests);
		
		Bundle extras  = getIntent().getExtras();
		String username = extras.getString("username");
		List <NameValuePair> pairs = new ArrayList<NameValuePair>();
		
		Log.d("ViewRequests ownerName: ",username);
		String url = Constants.URL;
		ListView lv = (ListView) findViewById (R.id.requestsList);
		
		pairs.add(new BasicNameValuePair("ownerName", username));
		pairs.add(new BasicNameValuePair("action","viewrequests"));
		
		AsyncViewRequests task = new AsyncViewRequests (pairs, lv, ViewRequests.this, url);
		task.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_requests, menu);
		return true;
	}

}
