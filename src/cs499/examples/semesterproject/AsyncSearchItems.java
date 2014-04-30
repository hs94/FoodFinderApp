package cs499.examples.semesterproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AsyncSearchItems extends AsyncTask <String, String, JSONArray>
{
	String [] items;
	ArrayList<String> selectedItems = new ArrayList<String>();
	DisplayPlaceItems ac;
	ListView lv;
	List<NameValuePair> pairs;
	String url;
	ProgressDialog progressDialog;
	
	private String s;
	private List<NameValuePair> requests;
	
	
	public AsyncSearchItems(DisplayPlaceItems ac, String url, List<NameValuePair> pairs,
			ListView lv) 
	{
		this.ac = ac;
    	this.url = url;
    	this.lv = lv;
    	this.pairs = pairs;		
	}

	 
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		progressDialog = new ProgressDialog(ac);
    	progressDialog.setMessage("Fetching items from food place...");
        progressDialog.show();	
    }
	

	 
	protected JSONArray doInBackground(String... params) {
		// TODO Auto-generated method stub
		for (int i = 0; i < pairs.size(); i++)
		{
			Log.d(pairs.get(i).getName() + ": ", pairs.get(i).getValue());
		}
    	return this.getJSONFromUrl(this.url);
	}

	 
	public JSONArray getJSONFromUrl(String url) {
		// Making HTTP request
				InputStream is = null; 
				String json = null;
				JSONArray jObj = null;
			    try {
			        // defaultHttpClient
			        DefaultHttpClient httpClient = new DefaultHttpClient();
			        HttpPost post = new HttpPost(url);
			        post.setEntity(new UrlEncodedFormEntity(pairs));
			        HttpResponse httpResponse = httpClient.execute(post);
			        HttpEntity httpEntity = httpResponse.getEntity();
			        is = httpEntity.getContent();           

			    } catch (UnsupportedEncodingException e) {
			        e.printStackTrace();
			    } catch (ClientProtocolException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }

			    try {
			        BufferedReader br = new BufferedReader(new InputStreamReader(is));
			        StringBuilder sb = new StringBuilder();
			        int value=0;
			        int count = 0;
			         
			         // reads to the end of the stream 
			        value = br.read();
			         while( value != -1)
			         {
			            // converts int to character
			            char c = (char)value;
			            sb.append(c);
			            count++;
			            //System.out.println(c);
			            value = br.read();
			         }
			        is.close();
			        String s = sb.toString();
			        System.out.println("Characters read: "+count+", Length of S: "+s.length());
			        json = s;
			        System.out.println("getJSONfromURL: "+ json);
			    } catch (Exception e) {
			        Log.e("Buffer Error", "Error converting result " + e.toString());
			    }
			    

			    // try parse the string to a JSON object
			    try {
			        jObj = new JSONArray(json);
			    } catch (JSONException e) {
			        Log.e("JSON Parser", "Error parsing data " + e.toString());
			    }

			    // return JSON String
			    return jObj;
	}

	public void showQueryResults(JSONArray j) 
	{
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String,String>>();
		
		try
		{
			JSONArray docs = j;
			Log.d("Length of JSON Array: ", "" + docs.length());
			if (docs.length() > 0)
			{
				for (int i = 0; i < docs.length(); i++)
				{
	                JSONObject c = (JSONObject)docs.getJSONObject(i);
	                
	                String foodplacename = c.getString("foodplacename");
	                String itemName = c.getString("itemname");
	                String itemAmount = String.valueOf(c.getInt("itemtype"));
	                String itemId = String.valueOf(c.getInt("itemid"));
	                
	                HashMap<String, String> map = new HashMap<String, String>();
	
	                map.put("foodplacename", foodplacename);
	                map.put("itemName", itemName);
	                map.put("itemAmount", itemAmount);
	                map.put("itemId", itemId);
	                
	                itemsList.add(map);
	                Log.d("Display Item: ", itemId + " " + foodplacename 
	                		+ " " + itemName + " " + itemAmount + " ");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		items = new String[itemsList.size()];
		int i = 0;
		for (HashMap<String,String> map: itemsList)
		{
			String a = "Item:" + (String)map.get("itemName")
					+ ", Quantity:" + (String)map.get("itemAmount");
			items[i++] = a;
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.ac,android.R.layout.simple_list_item_1, items);
		lv.setAdapter(adapter);
		lv.setClickable(true);
		
	}


	 
	protected void onPostExecute(JSONArray jobj) 
	{
		// TODO Auto-generated method stub
		if (this.progressDialog.isShowing())
			this.progressDialog.dismiss();
		this.showQueryResults (jobj);
		this.ac.notifyDataReceived();
	}
	
	
	
	

}
