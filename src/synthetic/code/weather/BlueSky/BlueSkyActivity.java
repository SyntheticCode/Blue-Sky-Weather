package synthetic.code.weather.BlueSky;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//import synthetic.code.weather.BlueSky.WeatherActivity.StationAdapter;
import synthetic.code.weather.BlueSky.parsers.StationPullParser;
import synthetic.code.weather.BlueSky.parsers.WeatherPullParser;

import android.app.Activity;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class BlueSkyActivity extends TabActivity {
	public static final int SEARCH_CITY = 1;
	
	private TabHost tabHost;
	private StationList stationList;
	private ArrayList<String> stations;
	private WeatherStation currentStation;
	private Spinner stationSpinner;
	private TextView location;
	private TextView updateTime;
	private TextView temperature;
	private TextView wind;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Resources res = getResources();
		
		tabHost = getTabHost();
		
		tabHost.addTab(tabHost
				.newTabSpec("Weather")
				.setIndicator("Weather", res.getDrawable(R.drawable.ic_airport))
				.setContent(R.id.tabWeatherLayout));
		
		tabHost.addTab(tabHost
				.newTabSpec("Forecast")
				.setIndicator("Forecast", res.getDrawable(R.drawable.ic_pws))
				.setContent(R.id.tabForecastLayout));
		
		tabHost.setCurrentTab(0);
		

//		// Set tabs Colors
//		tabHost.setBackgroundColor(Color.BLACK);
//		tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
		
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
				Log.v("Spinner", "Position = " + position);
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
				String query = extras.getString(SearchResultActivity.KEY_QUERY);

				// Set the Location (city)
				location.setText(query);
				// Get the list of stations for the city
				getStationList(query);
				
				stations = stationList.getStationNamesList();

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

			Intent i = new Intent(this, SearchResultActivity.class);
			i.putExtra(SearchResultActivity.KEY_QUERY, query);

			startActivityForResult(i, SEARCH_CITY);
		}
	}
	
	/**
	 * Updates the weather info for the selected station.
	 * @param position : position in spinner of station
	 */
	protected void stationSelected(int position) {
		// All airports are first in the list
		//if(stationList.getStationType(position) == WeatherStation.StationType.AIRPORT) {
		//	// parse airport
		//}
		//else if(stationList.getStationType(position) == WeatherStation.StationType.PWS) {
		Log.v("sationSelected", "Position = " + position);
		
			// Get the selected station
			currentStation = stationList.get(position);
			Log.v("Station ID", currentStation.getId());
			
			// Make sure the station is not empty and there is a non empty id
			if(!currentStation.empty() && (currentStation.getId() != "")) {
				try {
					WeatherPullParser parser = new WeatherPullParser(this, currentStation);
					
					// Get only weather data for currentStation
					parser.setupParse(true);
					currentStation = parser.parse();
					
					updateWeather(currentStation);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					Log.v("WeatherPullParser", "Parse failed [" + e + "]");
					e.printStackTrace();
				}
			}
		//}
	}
	
	/**
	 * Update layout objects with current weather info.
	 * @param station : station to get weather info from.
	 */
	private void updateWeather(WeatherStation station) {
		updateTime.setText(station.getWeather().getTime());
		temperature.setText(station.getWeather().getTempF() + "�F");
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

			// Pick which icon to use
			if(stationList.getStationType(position) == WeatherStation.StationType.AIRPORT) {
				icon.setImageResource(R.drawable.ic_airport);
			}
			else if(stationList.getStationType(position) == WeatherStation.StationType.PWS) {
				icon.setImageResource(R.drawable.ic_pws);
			}

			return row;
		}
	}
}