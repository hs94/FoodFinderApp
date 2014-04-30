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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AsyncViewRequests extends AsyncTask <String, String, JSONArray>
{
	List<NameValuePair> pairs;
	ListView lv;
	ViewRequests context;
	String url;
	ProgressDialog progressDialog;
	String[] requests;
	
	public AsyncViewRequests (List <NameValuePair> pairs, ListView lv, ViewRequests context, String url)
	{
		this.pairs = pairs;
		this.lv = lv;
		this.context = context;
		this.url = url;
	}
	
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		progressDialog = new ProgressDialog(context);
    	progressDialog.setMessage("Fetching requests...");
        progressDialog.show();	
    }
		

	@Override
	protected JSONArray doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		for (int i = 0; i < pairs.size(); i++)
		{
			Log.d(pairs.get(i).getName() + ": ", pairs.get(i).getValue());
		}
    	return this.getJSONFromUrl(this.url);
	}
	
	public JSONArray getJSONFromUrl(String url)
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
		   ArrayList<HashMap<String, String>> requestsList = new ArrayList<HashMap<String, String>>();
			try
			{
				JSONArray docs = j;
		        
	            for(int i = 0; i < docs.length(); i++)
	            {
	                JSONObject c = (JSONObject)docs.getJSONObject(i);
	                 
	                // Storing each json item in variable
	                String senderName = c.getString("sender");
	                String senderOrg = c.getString("org_name");
	                String requestItem = c.getString("itemname");
	                // creating new HashMap
	                HashMap<String, String> map = new HashMap<String, String>();
	                
	                map.put("sender", senderName);
	                map.put("org_name", senderOrg);
	                map.put("itemname",requestItem);
	                 
	                // adding each child node to HashMap key => value
	               /* map.put("pid", pid);
	                map.put("zip", zip);
	                map.put("name", name);
	                map.put("ownername", ownername);
	                map.put("streetaddress", streetAddress);
	                map.put("city", city);
	                map.put("state",state);*/
	 
	                // adding HashList to ArrayList
	                requestsList.add(map);
	                //Log.d("DisplayMatchingPlace", pid + " " + ownername + " " + name + " " + city + " " + state);
	            }
			}
	        catch (JSONException e)
	        {
	        	e.printStackTrace();
	        }
			
			/**
	         * Updating parsed JSON data into ListView
	         * */
			requests = new String[requestsList.size()];
			int i = 0;
			for (HashMap<String,String> map: requestsList)
			{
				String s = map.get("sender") + ", "+ (String)map.get("org_name")
							+ "," + (String)map.get("itemname");
				requests[i++] = s;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,android.R.layout.simple_list_item_1 , requests);
			lv.setAdapter(adapter);
			lv.setClickable(true);
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.d("DisplayRequestActivity", "Position = "+position);
					Intent displayRequest = new Intent(view.getContext(), DisplayRequestActivity.class);
					displayRequest.putExtra("request", requests[position]);
					context.startActivity(displayRequest);
				}
			});
	   }

}
