package cs499.examples.semesterproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncSendRequest extends AsyncTask <String, String, String> 
{
	List<NameValuePair> requests;
	String url;
	Activity context;
	ProgressDialog progressDialog;
	boolean successful = false;
	
	public AsyncSendRequest (List<NameValuePair> requests, String url, Activity context)
	{
		this.requests = requests;
		this.url = url;
		this.context = context;
	}
	
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		progressDialog = new ProgressDialog(context);
    	progressDialog.setMessage("Sending request to restaurant");
        progressDialog.show();	
    }
	
	@Override
	protected String doInBackground(String... arg0) 
	{
		// TODO Auto-generated method stub
		return this.postRestaurantInfo(this.url);
	}

	@SuppressWarnings("finally")
	public String postRestaurantInfo(String url)  // url is your json url
	{
		String s = null;
		HttpURLConnection conn = null;
		Log.d("Post", "In send request's post method now");
		try
		{
			Log.d("", "AsyncTask NameValuePairs");
			for (NameValuePair p: this.requests)
			{
				
				Log.d(p.getName(), p.getValue());
			}
			System.setProperty("http.keepAlive", "false");
			URL newUrl = new URL(this.url);
			Log.d("tag:", "Connecting to URL....");
			conn = (HttpURLConnection) newUrl.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			Log.d("post: ", "Before post....");
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
			        new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(this.requests));
			writer.flush();
			writer.close();
			os.close();
	
			conn.connect();
			int responseCode = conn.getResponseCode();
			Log.d("tag: ", "After posting....");
			Log.d("Http response: ", "" + responseCode); 
			
			InputStream is = conn.getInputStream();
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
	        s = sb.toString();
	        Log.d("tag: ", "Characters read: "+count+", Length of S: "+s.length() + "S itself: " + s);
			
		}
		catch (Exception e)
		{
			Log.e("Http Exception", "", e);
		}
		
		finally
		{
			conn.disconnect();
		}
		successful = true;
		return s;
	}
	
	 private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	   {
	       StringBuilder result = new StringBuilder();
	       boolean first = true;

	       for (NameValuePair pair : params)
	       {
	           if (first)
	               first = false;
	           else
	               result.append("&");

	           result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	           result.append("=");
	           result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	       }

	       return result.toString();
	   }

	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		if (this.progressDialog.isShowing())
			this.progressDialog.dismiss();
		if (successful)
			Toast.makeText(this.context, "Request was successfully submitted.", Toast.LENGTH_LONG).show();
	}

	
}
