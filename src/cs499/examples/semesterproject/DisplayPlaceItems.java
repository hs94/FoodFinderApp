package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayPlaceItems extends Activity 
{
	String[] items;
	ArrayList<String> selectedItems = new ArrayList<String>();
	List<NameValuePair> requests;
	ListView lv;
	String url = Constants.URL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_place_items);
		
		Bundle extras = getIntent().getExtras();
		String sendername = extras.getString("sender");
		String placeName = extras.getString("placeName");
		lv = (ListView) findViewById (R.id.placesItemList);
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add (new BasicNameValuePair("senderName", sendername));
		pairs.add (new BasicNameValuePair("placeName", placeName));
		pairs.add (new BasicNameValuePair("action", "searchitems"));
		
		requests = new ArrayList<NameValuePair>();
		requests.add (0, new BasicNameValuePair("senderName", sendername));
		requests.add (1, new BasicNameValuePair("placeName", placeName));
		requests.add (2, new BasicNameValuePair("action", "requestitems"));
		
		AsyncSearchItems task = new AsyncSearchItems(DisplayPlaceItems.this, url, pairs, lv);
		task.execute();
		
	}
	
	public void submitItems (View view)
	{
		Log.d("In method: ", "submitItems");
		if (selectedItems.size() > 0)
		{
			String s = "";
			for (int i = 0; i < selectedItems.size(); i++)
			{
				if (i < selectedItems.size()-1)
					s += selectedItems.get(i) + ",";
				else
					s += selectedItems.get(i);
			}
			requests.add(3, new BasicNameValuePair("selectedItems", s));
			Log.d("post for selectedItems: ", s);
			AsyncSendRequest requestTask = new AsyncSendRequest(requests, url, DisplayPlaceItems.this);
			requestTask.execute();
			selectedItems = new ArrayList<String>();
		}
	}
	
	public void notifyDataReceived ()
	{
		//Log.d("Recieved data: ", s);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener ()
		{
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				String s = (String) lv.getItemAtPosition(position);
				String[] itemChars = s.substring(s.indexOf(':')+1, s.length()-1).split(",");
				String itemName = itemChars[0].trim();
				
				if (!alreadyInList(selectedItems, itemName))
				{
					selectedItems.add(itemName);
					Toast.makeText(DisplayPlaceItems.this, itemName + " added to request list ", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(DisplayPlaceItems.this, itemName + " is already in your request list ", Toast.LENGTH_LONG).show();
				}
			}
				
		});
	}
	
	public boolean alreadyInList (ArrayList<String> items, String item)
	{	
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).equals(item))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_place_items, menu);
		return true;
	}

}
