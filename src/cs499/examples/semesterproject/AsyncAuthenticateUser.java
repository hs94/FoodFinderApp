package cs499.examples.semesterproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import android.widget.Toast;

public class AsyncAuthenticateUser extends AsyncTask <String, String, JSONArray>
{
	Activity activityContext;
	List <NameValuePair> pairs;
	String url;
	ProgressDialog progressDialog;
	
	public AsyncAuthenticateUser (Activity a, List<NameValuePair> pairs, String url)
	{
		this.activityContext = a;
		this.pairs = pairs;
		this.url = url;
	}
	
	protected void onPreExecute() 
	{
    	progressDialog = new ProgressDialog(activityContext);
    	progressDialog.setMessage("Authenticating login info...");
        progressDialog.show();
    }
	
	
	@Override
	protected JSONArray doInBackground(String... params)   
	{
		return this.getJSONFromUrl(this.url);
	}
	
	private JSONArray getJSONFromUrl(String url)  // url is your json url
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
	   this.checkUser (jobj);
    }
	
	public void checkUser (JSONArray j)
	{
		String inputName = this.pairs.get(0).getValue();
		String inputPassword = this.pairs.get(1).getValue();
		boolean inDb;
		try
		{
				if (j == null || j.length() == 0)
				{ 
					String error = "Username does not exist. Try again, or register first";
					Toast.makeText(this.activityContext, error, Toast.LENGTH_SHORT).show();
					System.out.println("JSON Array is null");
					return;
				}
				JSONObject o = (JSONObject) j.getJSONObject(0);
				String userName = o.getString("username");
				String userPassword = o.getString("password");
				String userType = o.getString("usertype");
				
				Log.d("Username: ", userName);
				Log.d("Password: ", userPassword);
				Log.d("userType: ", userType);
				if (inputPassword.equals(userPassword))
					inDb = true;
				else
					inDb = false;
				
				Log.d("User in DB: ", "" + inDb);
				
				if (inDb)
				{
					if (userType.equals("owner"))
					{
						Intent i = new Intent (this.activityContext, FoodOwnerOptions.class);
						i.putExtra("username", userName);
						this.activityContext.startActivity(i);
					}
					else if (userType.equals("organization"))
					{
						Intent i = new Intent (this.activityContext, OrganizationOptions.class);
						i.putExtra("username", userName);
						this.activityContext.startActivity(i);
					}
				}
				else
				{
					Toast.makeText(this.activityContext, "Incorrect Password. Try again", Toast.LENGTH_SHORT).show();
				}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
   
}
