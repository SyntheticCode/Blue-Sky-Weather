package synthetic.code.weather.BlueSky;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import synthetic.code.weather.BlueSky.parsers.PwsPullParser;
import synthetic.code.weather.BlueSky.parsers.StationPullParser;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Weather extends Activity {
	public static final int SEARCH_CITY = 1;

	
	private StationList stationList;
	private ArrayList<String> stations;
	private int pwsStartIndex;
	private PWS currentStation;
	// weather.xml layout objects
	private Spinner stationSpinner;
	private TextView location;
	private TextView updateTime;
	private TextView temperature;
	private TextView wind;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);

		// Create class objects
		stationList = new StationList();
		stationSpinner = (Spinner) findViewById(R.id.weather_stationSpinner);
		location = (TextView) findViewById(R.id.weather_location);
		updateTime = (TextView) findViewById(R.id.weather_updateTime);
		temperature = (TextView) findViewById(R.id.weather_temp);
		wind = (TextView) findViewById(R.id.weather_wind);
		
		// Set listener for spinner events
		stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				stationSelected(position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing
			}
			
		});
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
		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			break;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SEARCH_CITY:
			// Only look at results that returned OK
			if (resultCode == Activity.RESULT_OK) {
				Bundle extras = data.getExtras();
				String query = extras.getString(SearchActivity.KEY_QUERY);

				// Set the Location (city)
				location.setText(query);
				// Get the list of stations for the city
				getStationList(query);

				// Add airports to the list (airports must be first)
				stations = stationList.getAirportNameList();
				// Get the index of the first PWS
				pwsStartIndex = stations.size();
				// Add PWS to the list
				stations.addAll(stationList.getPwsNameList());

				// Update the station spinner with list of PWS stations
				updateStationSpinner(stations);
			}
		}

	}

	private void handleIntent(Intent intent) {
		// When a search query comes in then start the SearchActivity to get and
		// display the results
		// SearchActivity passes result back
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			Intent i = new Intent(this, SearchActivity.class);
			i.putExtra(SearchActivity.KEY_QUERY, query);

			startActivityForResult(i, SEARCH_CITY);
		}
	}
	
	/**
	 * Updates the weather info for the selected station.
	 * @param position : position in spinner of station
	 */
	protected void stationSelected(int position) {
		// All airports are first in the list
		if(position < pwsStartIndex) {
			// parse airport
		}
		else {
			// Get the selected station
			currentStation = stationList.getPws(position - pwsStartIndex); // stationList is not indexed like the spinner
			Log.v("Station ID", currentStation.getId());
			
			// Make sure the station is not empty and there is a non empty id
			if(!currentStation.empty() && (currentStation.getId() != "")) {
				try {
					PwsPullParser parser = new PwsPullParser(this, currentStation);
					
					// Get only weather data for currentStation
					parser.setupParse(true);
					currentStation = parser.parse();
					
					updateWeather(currentStation);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Update layout objects with current weather info.
	 * @param station : station to get weather info from.
	 */
	private void updateWeather(PWS station) {
		updateTime.setText(station.getWeather().getTime());
		temperature.setText(station.getWeather().getTempF() + "°F");
		wind.setText(station.getWeather().getWindDirection() + " " + station.getWeather().getWindSpeed() + "mph");
	}

	/**
	 * Parse results for city query to get a list of stations.
	 * @param city : city to search for, must be unique example : "city, state"
	 */
	private void getStationList(String city) {
		try {
			StationPullParser parser = new StationPullParser(this, city);

			stationList = parser.parse();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void updateStationSpinner(ArrayList<String> list) {
		StationAdapter adapter = new StationAdapter(this,
				R.layout.spinner_item, list);
		stationSpinner.setAdapter(adapter);
	}

	/**
	 * Custom Adapter class for stationSpinner.
	 * Has a custom layout with an icon and text for each row.
	 * @author David
	 *
	 */
	public class StationAdapter extends ArrayAdapter<String> {

		public StationAdapter(Context context, int resource,
				List<String> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.spinner_item, parent, false);
			TextView label = (TextView) row.findViewById(R.id.spinner_text);
			label.setText(stations.get(position));

			ImageView icon = (ImageView) row.findViewById(R.id.spinner_icon);

			// Pick which icon to use (important that all airport are first in the list)
			if (position < pwsStartIndex) {
				icon.setImageResource(R.drawable.airplane_med);
			} else {
				icon.setImageResource(R.drawable.pws_med);
			}

			return row;
		}
	}
}