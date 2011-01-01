package synthetic.code.weather.BlueSky;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import synthetic.code.weather.BlueSky.parsers.StationPullParser;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Weather extends Activity {
	public static final int SEARCH_CITY = 1;
	
	private StationList stationList;
	private Spinner stationSpinner;
	private TextView location;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        
        // Create class objects
        stationList = new StationList();
        location = (TextView) findViewById(R.id.weather_location);
        stationSpinner = (Spinner) findViewById(R.id.weather_stationSpinner);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Create the menu for this page
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    public void onNewIntent(Intent intent) {
    	setIntent(intent); // Update intent that was set in onCreate
    	handleIntent(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_search:
    		onSearchRequested();
    		break;
    	}
    	return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    	case SEARCH_CITY:
    		// Only look at results that returned OK
        	if(resultCode == Activity.RESULT_OK) {
        		Bundle extras = data.getExtras();
        		String query = extras.getString(SearchActivity.KEY_QUERY);
        		
        		// Set the Location (city)
        		location.setText(query);
        		// Get the list of stations for the city
        		getStationList(query);
        		// Update the station spinner with list of PWS stations
        		updateStationSpinner(stationList.getPwsNameList());
        	}
    	}
    	
    }
    
    private void handleIntent(Intent intent) {
    	// When a search query comes in then start the SearchActivity to get and display the results
    	// SearchActivity passes result back
    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
    		String query = intent.getStringExtra(SearchManager.QUERY);
    		
    		Intent i = new Intent(this, SearchActivity.class);
    		i.putExtra(SearchActivity.KEY_QUERY, query);
    		
    		startActivityForResult(i, SEARCH_CITY);
    	}
    }
    
    private void getStationList(String city) {
    	try {
    		StationPullParser parser = new StationPullParser(this, city);
    		
    		stationList = parser.parse();
    	} catch(UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    }
    
    private void updateStationSpinner(ArrayList<String> list) {
    	ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stationSpinner.setAdapter(stationAdapter);
    }
}