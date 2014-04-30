package cs499.examples.semesterproject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class DisplayRequestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_request);
		
		Bundle extras = getIntent().getExtras();
		String request = extras.getString("request");
		String[] requestElements = request.split(",");
		String sender = "Sender: " + requestElements[0] + "\n";
		String organization = "Organization: " + requestElements[1] + "\n";
		//String item = "Organization: " + request.substring(request.indexOf('s')+1,request.length()-1) + "\n";
		String item = "Item requested: " + requestElements[2] + "\n";
		
		TextView tv = (TextView) findViewById (R.id.requestInfo);
		tv.setText(sender + organization + item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_request, menu);
		return true;
	}

}
