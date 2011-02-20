package synthetic.code.weather.BlueSky;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import synthetic.code.weather.BlueSky.WeatherActivity.StationAdapter;
import synthetic.code.weather.BlueSky.parsers.ForecastParser;
import synthetic.code.weather.BlueSky.parsers.StationPullParser;
//import synthetic.code.weather.BlueSky.parsers.WeatherPullParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class BlueSkyActivity extends TabActivity {
	public static final int SEARCH_CITY = 1;
	
	// Data
	private StationList stationList;
	private int currentStationIndex;
	private WeatherData currentWeather;
	private ForecastData currentForecast;
	private String currentLocation;
	
	// GUI objects
	private TabHost tabHost;
	private TextView location;
	// Station Tab
	private ListView stationListView;
	private TextView selectedStation;
	// Weather Tab
	private TextView updateTime;
	private TextView temperature;
	private TextView windSpeed;
	private TextView weatherCondtion;
	private TextView windGust;
	private TextView humidity;
	private TextView rainfall;
	private TextView pressure;
	private TextView visibility;
	private TextView dewPoint;
	private TextView uvIndex;
	// Forecast Tab
	private ForecastShortView forecastShort1;
	private ForecastShortView forecastShort2;
	private ForecastExtendedView forecastExtended1;
	private ForecastExtendedView forecastExtended2;
	private ForecastExtendedView forecastExtended3;
	private ForecastExtendedView forecastExtended4;
	private ForecastExtendedView forecastExtended5;
	
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
		
		tabHost.addTab(tabHost
				.newTabSpec("Stations")
				.setIndicator("Stations", res.getDrawable(R.drawable.ic_airport))
				.setContent(R.id.tabWeatherStationsLayout));
		
		tabHost.setCurrentTab(0);
		
		

//		// Set tabs Colors
//		tabHost.setBackgroundColor(Color.BLACK);
//		tabHost.getTabWidget().setBackgroundColor(Color.BLACK);
		
		// Create class objects
		stationList = new StationList();
		location = (TextView) findViewById(R.id.weather_location);
		// Station Tab objects
		stationListView = (ListView) findViewById(R.id.weatherStations_list);
		selectedStation = (TextView) findViewById(R.id.weatherStations_selected);
		// Weather Tab objects
		updateTime = (TextView) findViewById(R.id.weather_updateTime);
		temperature = (TextView) findViewById(R.id.weather_temperture);
		windSpeed = (TextView) findViewById(R.id.weather_wind);
		weatherCondtion = (TextView) findViewById(R.id.weather_condition);
		windGust = (TextView) findViewById(R.id.weather_wind_gust);
		humidity = (TextView) findViewById(R.id.weather_humidity);
		rainfall = (TextView) findViewById(R.id.weather_rainfall);
		pressure = (TextView) findViewById(R.id.weather_pressure);
		visibility = (TextView) findViewById(R.id.weather_visibility);
		dewPoint = (TextView) findViewById(R.id.weather_dew);
		uvIndex = (TextView) findViewById(R.id.weather_uv);
		// Forecast Tab objects
		forecastShort1 = (ForecastShortView) findViewById(R.id.forecast_short_row1);
		forecastShort2 = (ForecastShortView) findViewById(R.id.forecast_short_row2);
		forecastExtended1 = (ForecastExtendedView) findViewById(R.id.forecast_extended_row1);
		forecastExtended2 = (ForecastExtendedView) findViewById(R.id.forecast_extended_row2);
		forecastExtended3 = (ForecastExtendedView) findViewById(R.id.forecast_extended_row3);
		forecastExtended4 = (ForecastExtendedView) findViewById(R.id.forecast_extended_row4);
		forecastExtended5 = (ForecastExtendedView) findViewById(R.id.forecast_extended_row5);
		
		
		stationListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				stationSelected(position);
				
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
		case R.id.menu_refresh:
			stationSelected(currentStationIndex);
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
				currentLocation = extras.getString(SearchResultActivity.KEY_QUERY);

				// Set the Location (city)
				location.setText(currentLocation);
				
				// Get the list of stations for the city (run in a thread)
				new StationParserTask().execute(currentLocation);
				
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
		// Make sure the position is valid
		if(position >= 0 && position < stationList.size()) {
			// Get the selected station
			WeatherStation currentStation = stationList.get(position);
			//Log.v("Station ID", currentStation.getId());
			
			// Make sure the station is not empty and there is a non empty id
			if(!currentStation.empty() && (currentStation.getId() != "")) {
				// Update the current index
				currentStationIndex = position;
				
				// Get the weather for the station
				new WeatherParserTask().execute(currentStation);
				
				// Move to the weather tab
				tabHost.setCurrentTab(0);
			}
			else {
				// Warn user the station is corrupt
				Toast.makeText(this, "Selected Station is Broken", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Update layout objects with current weather info.
	 */
	private void displayWeather() {
		updateTime.setText(currentWeather.getTime());
		temperature.setText(currentWeather.getTempFString());
		weatherCondtion.setText(currentWeather.getWeatherCondition());
		windSpeed.setText(currentWeather.getWindDirSpeedString());
		weatherCondtion.setText(currentWeather.getWeatherCondition());
		windGust.setText(currentWeather.getWindGustMphString());
		humidity.setText(currentWeather.getHumidityString());
		rainfall.setText(currentWeather.getRainfallInchString());
		pressure.setText(currentWeather.getPressureInchString());
		visibility.setText(currentWeather.getVisibilityMileString());
		dewPoint.setText(currentWeather.getDewFString());
		uvIndex.setText(currentWeather.getUvString());
	}


	private void updateStationList(ArrayList<String> list) {
		StationAdapter adapter = new StationAdapter(this, R.layout.stations_item, list);
		stationListView.setAdapter(adapter);
		
		if(list.size() != 0) {
			int index = stationList.getFirstPws(); // Find the first PWS (defaulting to PWS)
			if(index != -1) {
				stationSelected(index);
			}
			else {
				stationSelected(0);
			}
		}
	}
	
	private void displayForecast() {
		if(currentForecast.forecastShort.size() > 1 && currentForecast.forecastExtended.size() > 4) {
			forecastShort1.icon.setImageResource(R.drawable.ic_airport);
			forecastShort1.title.setText(currentForecast.forecastShort.get(0).getTitle());
			forecastShort1.condition.setText(currentForecast.forecastShort.get(0).getForecast());
			
			forecastShort2.icon.setImageResource(R.drawable.ic_pws);
			forecastShort2.title.setText(currentForecast.forecastShort.get(1).getTitle());
			forecastShort2.condition.setText(currentForecast.forecastShort.get(1).getForecast());
			
			forecastExtended1.icon.setImageResource(R.drawable.ic_airport);
			forecastExtended1.title.setText(currentForecast.forecastExtended.get(0).getDateWeekday());
			forecastExtended1.condition.setText(currentForecast.forecastExtended.get(0).getCondition());
			forecastExtended1.temperature.setText(currentForecast.forecastExtended.get(0).getHigh_F());
			
			forecastExtended2.icon.setImageResource(R.drawable.ic_airport);
			forecastExtended2.title.setText(currentForecast.forecastExtended.get(1).getDateWeekday());
			forecastExtended2.condition.setText(currentForecast.forecastExtended.get(1).getCondition());
			forecastExtended2.temperature.setText(currentForecast.forecastExtended.get(1).getHigh_F());
			
			forecastExtended3.icon.setImageResource(R.drawable.ic_airport);
			forecastExtended3.title.setText(currentForecast.forecastExtended.get(2).getDateWeekday());
			forecastExtended3.condition.setText(currentForecast.forecastExtended.get(2).getCondition());
			forecastExtended3.temperature.setText(currentForecast.forecastExtended.get(2).getHigh_F());
			
			forecastExtended4.icon.setImageResource(R.drawable.ic_airport);
			forecastExtended4.title.setText(currentForecast.forecastExtended.get(3).getDateWeekday());
			forecastExtended4.condition.setText(currentForecast.forecastExtended.get(3).getCondition());
			forecastExtended4.temperature.setText(currentForecast.forecastExtended.get(3).getHigh_F());
			
			forecastExtended5.icon.setImageResource(R.drawable.ic_airport);
			forecastExtended5.title.setText(currentForecast.forecastExtended.get(4).getDateWeekday());
			forecastExtended5.condition.setText(currentForecast.forecastExtended.get(4).getCondition());
			forecastExtended5.temperature.setText(currentForecast.forecastExtended.get(4).getHigh_F());
		}
		else {
			// Warn user
			Toast.makeText(BlueSkyActivity.this, "Forecast Data Error", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Starts an AsyncTask for parsing the list of stations.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	private class StationParserTask extends AsyncTask<String, Void, StationList> {

		private final ProgressDialog progressDialog = new ProgressDialog(BlueSkyActivity.this);
		private StationPullParser parser;

		
		protected void onPreExecute() {
			this.progressDialog.setMessage("Getting Stations...");
			this.progressDialog.setIndeterminate(true);
			this.progressDialog.setCancelable(true);
			this.progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if(parser != null) {
						parser.stopParse();
					}
					// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
					cancel(true);
				}
	    	});
			
			this.progressDialog.show();
		}
		
		protected StationList doInBackground(String... params) {
			StationList list = null;
			
			// Try creating the parser and then parsing
			try {
				parser = new StationPullParser(BlueSkyActivity.this, params[0]);
				list = parser.parse();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return list;
		}
		
		protected void onPostExecute(StationList result) {
			if(result != null) {
				// Update stations with result
				BlueSkyActivity.this.stationList = result;
				ArrayList<String> stations = stationList.getStationNamesList();
	
				// Update the station spinner with list of PWS stations
				updateStationList(stations);
			}
			else {
				// Warn user
				Toast.makeText(BlueSkyActivity.this, "Network Error", Toast.LENGTH_LONG).show();
			}
			
			// Close the dialog
			if(this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}
		}
	};
	
	/**
	 * Starts an AsyncTask for parsing the weather.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	private class WeatherParserTask extends AsyncTask<WeatherStation, Void, WeatherData> {

		private final ProgressDialog progressDialog = new ProgressDialog(BlueSkyActivity.this);
		//private WeatherPullParser parser;
		private WeatherStation station;
		
		protected void onPreExecute() {
			this.progressDialog.setMessage("Getting Weather...");
			this.progressDialog.setIndeterminate(true);
			this.progressDialog.setCancelable(true);
			this.progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if(station != null) {
						station.stopWeatherParse();
					}
					// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
					cancel(true);
				}
	    	});
			
			this.progressDialog.show();
		}
		
		protected WeatherData doInBackground(WeatherStation... params) {
			station = params[0];
			
			WeatherData weather = null;
			WeatherData extraWeather;
			
			//
			try {
				weather = station.parseWeather(BlueSkyActivity.this);
				
				// Only get extra weather for PWS (data that airports are missing should not be filled in)
				if(station.getStationType() == WeatherStation.StationType.PWS) {
					int index = stationList.getFirstAirport();
					// Airports have some information that weather stations don't so get weather from closes airport
					if(index != -1) {
						extraWeather = stationList.get(index).parseWeather(BlueSkyActivity.this);
						weather.setWeatherCondition(extraWeather.getWeatherCondition());
						weather.setVisibilityMile(extraWeather.getVisibilityMile());
					}
				}
				
			} catch (RuntimeException e) {
				e.printStackTrace();
				//return new WeatherData();
			}
			
			
			return weather;
		}
		
		protected void onPostExecute(WeatherData result) {
			if(result != null) {
				// Update station with result
				currentWeather = result;
				
				displayWeather();
				
				// Change the selected station (wait for parse to get elevation)
				selectedStation.setText(stationList.get(currentStationIndex).getStationTitle() + " Elevation " + stationList.get(currentStationIndex).getElevation() + " ft");
				
				// Start a forecast parse (if weather parse failed then don't parse forecast)
				// TODO: Check if forecast should be updated or not
				new ForecastParserTask().execute(currentLocation);
			}
			else {
				// Warn user 
				Toast.makeText(BlueSkyActivity.this, "Network Error", Toast.LENGTH_LONG).show();
			}
			
			// Close the dialog
			if(this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}
		}
	};
	
	
	/**
	 * Starts an AsyncTask for parsing the forecast.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	private class ForecastParserTask extends AsyncTask<String, Void, ForecastData> {
		private ForecastParser parser;
		
		protected ForecastData doInBackground(String... params) {
			ForecastData forecast = null;
			
			// Try creating the parser and then parsing
			try {
				parser = new ForecastParser(BlueSkyActivity.this, params[0]);
				forecast = parser.parse();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return forecast;
		}
		
		protected void onPostExecute(ForecastData result) {
			if(result != null) {
				
				BlueSkyActivity.this.currentForecast = result;
				
				displayForecast();
			}
			else {
				// Warn user
				Toast.makeText(BlueSkyActivity.this, "Network Error", Toast.LENGTH_LONG).show();
			}
			
		}
		
		public void stop() {
			if(parser != null) {
				parser.stopParse();
			}
			// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
			cancel(true);
		}
	};
	

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
			View row = inflater.inflate(R.layout.stations_item, parent, false);
			TextView label = (TextView) row.findViewById(R.id.station_text);
			label.setText(stationList.get(position).getStationTitle());

			ImageView icon = (ImageView) row.findViewById(R.id.station_icon);

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
