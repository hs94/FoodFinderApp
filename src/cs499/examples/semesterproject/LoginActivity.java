package cs499.examples.semesterproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void submitLogin (View view)
	{
		EditText loginUser = (EditText) findViewById (R.id.loginUsername);
		EditText loginPassword = (EditText) findViewById (R.id.loginPassword);
		
		String url = Constants.URL;
		List <NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("loginUser", loginUser.getText().toString()));
		pairs.add(new BasicNameValuePair("loginPassword", loginPassword.getText().toString()));
		pairs.add(new BasicNameValuePair("action", "authenticate"));

		AsyncAuthenticateUser task = new AsyncAuthenticateUser(this, pairs, url); 
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
