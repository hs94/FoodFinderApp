package cs499.examples.semesterproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncAddRestaurantToDB extends AsyncTask <String, String, String>
{
	Connection c = null;
	Statement s = null;
	HashMap<String,String> h;
	Activity activityContext;
	ProgressDialog progressDialog;
	int queryExecuted = 0;
	String query = "";
	
	public AsyncAddRestaurantToDB (Activity activityContext, HashMap<String,String> h)
	{
		this.activityContext = activityContext;
		this.h = h;
	}

	@Override
	protected String doInBackground(String... params) 
	{
		this.postRestaurantInfo();
		return "Done";
	}

	@Override
	protected void onPreExecute() 
	{
		progressDialog = new ProgressDialog (activityContext);
		progressDialog.setMessage("Adding your restaurant/place info...");
		progressDialog.show();
	}
	
	private void postRestaurantInfo()
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} 
		
		catch (ClassNotFoundException e) 
		{
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;
		}
		
		try
		{
			c = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521/XE", "system", "admin");
			s = c.createStatement();
			
			query = "insert into foodappuser (username, password) values (" + h.get("foodPlaceOwnerName") + h.get("foodPlaceOwnerPassword") + ")";
			queryExecuted = s.executeUpdate(query);
			
			c.close();
			s.close();
		}
		catch (SQLException e) 
		{

			System.out.println("Program Failed! Check output console");
			e.printStackTrace();
			return;
		}
	}
	
	protected void onPostExecute ()
	{
	   if (this.progressDialog.isShowing())
	   {
		   this.progressDialog.dismiss();
	   }
    }

}
