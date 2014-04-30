package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class DisplayMatchingPlaces extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_matching_places);
		
		Bundle extras = getIntent().getExtras();
		String searchCity = extras.getString("Search city");
		String searchZip = extras.getString("Search zip");
		String username = extras.getString("username");
		
		List<NameValuePair> searchPairs = new ArrayList<NameValuePair>();
		String url = Constants.URL;
		ListView lv = (ListView) findViewById (R.id.placesList);
		
		searchPairs.add(new BasicNameValuePair("searchCity", searchCity));
		searchPairs.add(new BasicNameValuePair("searchZip", searchZip));
		searchPairs.add(new BasicNameValuePair("action", "searchplaces"));
		searchPairs.add(new BasicNameValuePair("sendername", username));
		
		AsyncSearchAndShowPlaces task = new AsyncSearchAndShowPlaces(this, url, searchPairs, lv); 
		task.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_matching_places, menu);
		return true;
	}

}
