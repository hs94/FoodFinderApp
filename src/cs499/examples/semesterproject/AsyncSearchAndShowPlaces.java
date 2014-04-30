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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AsyncSearchAndShowPlaces extends AsyncTask <String,String,JSONArray>
{
	String[] places;
	Activity ac;
	ListView lv;
	List<NameValuePair> pairs;
	String url;
	ProgressDialog progressDialog;
	
	public AsyncSearchAndShowPlaces (Activity ac, String url, List<NameValuePair> pairs, ListView lv)
    {
    	this.ac = ac;
    	this.url = url;
    	this.lv = lv;
    	this.pairs = pairs;
    }
    
    protected void onPreExecute() 
    {
    	progressDialog = new ProgressDialog(ac);
    	progressDialog.setMessage("Fetching Matching food places...");
        progressDialog.show();
    }
    

    @Override
    protected JSONArray doInBackground(String... params) 
    {
    	return this.getJSONFromUrl(this.url);
    } 
    
	public JSONArray getJSONFromUrl(String url)  // url is your json url
	{

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
	
	   protected void onPostExecute (JSONArray jobj)
	    {
		   if (this.progressDialog.isShowing())
			   this.progressDialog.dismiss();
		   this.showQueryResults (jobj);
	    }
	   
	   public void showQueryResults (JSONArray j)
	   {
		   ArrayList<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
			try
			{
				JSONArray docs = j;
		        
	            for(int i = 0; i < docs.length(); i++)
	            {
	                JSONObject c = (JSONObject)docs.getJSONObject(i);
	                 
	                // Storing each json item in variable
	                String zip = String.valueOf(c.getLong("zip"));
	                String name = c.getString("name");
	                String ownername = c.getString("ownername");
	                String city = c.getString("city");
	                String state = c.getString("state");
	                String streetAddress = c.getString("streetaddress");
	                String pid = String.valueOf(c.getInt("pid"));
	                //String phone = "" + c.getInt("phonenumber");
	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	                 
	                // adding each child node to HashMap key => value
	                map.put("pid", pid);
	                map.put("zip", zip);
	                map.put("name", name);
	                map.put("ownername", ownername);
	                map.put("streetaddress", streetAddress);
	                map.put("city", city);
	                map.put("state",state);
	                //map.put("phone",state);

	                // adding HashList to ArrayList
	                placesList.add(map);
	                Log.d("DisplayMatchingPlace", pid + " " + ownername + " " + name + " " + city + " " + state);
	            }
			}
	        catch (JSONException e)
	        {
	        	e.printStackTrace();
	        }
			
			/**
	         * Updating parsed JSON data into ListView
	         * */
			places = new String[placesList.size()];
			int i = 0;
			for (HashMap<String,String> map: placesList)
			{
				String s = (String)map.get("name") + ": "+ (String)map.get("name") + ", " + (String)map.get("streetaddress");
				s += ", " + (String)map.get("city") + ", " + (String)map.get("state") + " " + (String)map.get("zip");
						//+ ", " + (String)map.get("phone");
				places[i++] = s;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.ac,android.R.layout.simple_list_item_1 , places);
			lv.setAdapter(adapter);
			lv.setClickable(true);
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d("DisplayMessageActivity", "Position = "+position);
					Intent displayPlace = new Intent(view.getContext(),
							DisplayPlace.class);
					displayPlace.putExtra("Places", places[position]);
					displayPlace.putExtra("sendername", pairs.get(3).getValue());
					ac.startActivity(displayPlace);
				}
			});
	   }
	   
	   public void showQueryResultsAsJson (JSONArray j)
	   {
		   try 
		   {
		           JSONArray array = j;
		           String[] stringarray = new String[array.length()];
		           for (int i = 0; i < array.length(); i++) {
		               stringarray[i] = array.getString(i);
		           }
		           ArrayAdapter<String> adapter = new ArrayAdapter<String>
		           (this.ac, android.R.layout.simple_list_item_1, stringarray);
		           this.lv.setAdapter(adapter);
		   } 
		   catch (JSONException e) 
		   {
		         e.printStackTrace();  // handle JSON parsing exceptions...
		   }
	   }

}
