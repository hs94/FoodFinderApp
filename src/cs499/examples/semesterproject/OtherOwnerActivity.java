package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OtherOwnerActivity extends Activity 
{
	List <NameValuePair> organizationPairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_owner);
	}
	
	public void sendData (View view)
	{
		organizationPairs = new ArrayList<NameValuePair>();
		
		EditText username = (EditText) findViewById (R.id.organizationUsername);
		EditText organizationName = (EditText) findViewById (R.id.organizationName);
		EditText password = (EditText) findViewById (R.id.organizationPassword);
		
		if (!this.checkEmpty())
		{
			organizationPairs.add(new BasicNameValuePair ("memberName", username.getText().toString()));
			organizationPairs.add(new BasicNameValuePair ("memberPassword", password.getText().toString()));
			organizationPairs.add(new BasicNameValuePair ("organizationName", organizationName.getText().toString()));
			organizationPairs.add(new BasicNameValuePair ("action", "enterorganization"));
			
			Log.d("", "FoodBankActivity NameValuePairs");
			for (NameValuePair p: organizationPairs)
			{
				Log.d(p.getName(), p.getValue());
			}
			new AsyncAddPlace(OtherOwnerActivity.this, Constants.URL, organizationPairs).execute();
		}
		else
		{
			Toast.makeText(this, "Please fill in missing input", Toast.LENGTH_LONG).show();
		}
	}
	
	public boolean checkEmpty()
	{
		EditText username = (EditText) findViewById (R.id.organizationUsername);
		EditText organizationName = (EditText) findViewById (R.id.organizationName);
		EditText password = (EditText) findViewById (R.id.organizationPassword);
		
		String a = username.getText().toString();
		String b = password.getText().toString();
		String c = organizationName.getText().toString();
		
		return a.trim().equals("") || b.trim().equals("") || c.trim().equals("");
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.other_owner, menu);
		return true;
	}

}
