package synthetic.code.weather.BlueSky;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
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
	public static final int FORECAST_SHORT_COUNT = 2;
	public static final int FORECAST_EXTENDED_COUNT = 5;
	private static final String FILE_WEATHER_DATA_OBJECT = "weather_data";
	private static final String FILE_FORECAST_DATA_OBJECT = "forecast_data";
	private static final String FILE_STATION_DATA_OBJECT = "station_data";
	
	private static final int DIALOG_STATION_LOADING = 0;
	private static final int DIALOG_WEATHER_LOADING = 1;
	
	// Preference Keys/Defaults
	private static final String PREF_NAME = "activity_pref";
	private static final String PREF_KEY_LOCATION = "location";
	private static final String PREF_KEY_INDEX = "index";
	private static final int PREF_DEFAUTL_INDEX = 0;
	
	// Data
	private StationList stationList;
	private WeatherData currentWeather;
	private ForecastData currentForecast;
	private boolean refreshStationList;
	
	// Preferences
	private String currentLocation;
	private int currentStationIndex;
	
	// AsyncTasks
	private WeatherParserTask weatherTask = null;
	private ForecastParserTask forecastTask = null;
	private StationParserTask stationTask = null;
	
	private UiObjects ui;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Create all UI objects and link them to the layout
		ui = new UiObjects(this);
		
		SharedPreferences preferences = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		currentLocation = preferences.getString(PREF_KEY_LOCATION, null);
		currentStationIndex = preferences.getInt(PREF_KEY_INDEX, PREF_DEFAUTL_INDEX);
		
		refreshStationList = true;
		
		stationList = new StationList();
		
		ui.stationListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView, View selectedItemView,
					int position, long id) {
				stationSelected(position);
				
			}
			
		});
		
		SaveObjects save = (SaveObjects) getLastNonConfigurationInstance();
		
		if(save != null) {
			stationTask = save.stationParserTask;
			if(stationTask != null) {
				stationTask.attach(this);
			}
			
			weatherTask = save.weatherParserTask;
			if(weatherTask != null) {
				weatherTask.attach(this);
			}
			
			forecastTask = save.forecastParserTask;
			if(forecastTask != null) {
				forecastTask.attach(this);
			}
			
			currentWeather = save.weatherData;
			if(currentWeather != null) {
				ui.updateWeatherTab(currentWeather);
			}
			
			currentForecast = save.forecastData;
			if(currentForecast != null) {
				ui.updateForecastTab(currentForecast);
			}
			
			stationList = save.stationList;
			if(stationList != null) {
				ui.updateStationListView(stationList.getStationNamesList(), stationList.getStationTypesList());
			}
		}
		
		//ui.loadState(savedInstanceState);
		
		
		if(currentLocation != null) {
			//restoreWeather();
		}
		else {
			Log.v("BlueSky", "No Weather to restore");
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
//		SharedPreferences preferences = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
//		editor.putString(PREF_KEY_LOCATION, currentLocation);
//		editor.putInt(PREF_KEY_INDEX, currentStationIndex);
//		editor.commit();
//		
//		try {
//			FileOutputStream weatherFile = openFileOutput(FILE_WEATHER_DATA_OBJECT, Context.MODE_PRIVATE);
//			ObjectOutputStream weatherObject = new ObjectOutputStream(weatherFile);
//			weatherObject.writeObject(currentWeather);
//			weatherFile.close();
//			
//			FileOutputStream forecastFile = openFileOutput(FILE_FORECAST_DATA_OBJECT, Context.MODE_PRIVATE);
//			ObjectOutputStream forecastObject = new ObjectOutputStream(forecastFile);
//			forecastObject.writeObject(currentForecast);
//			forecastFile.close();
//			
////			FileOutputStream stationFile = openFileOutput(FILE_WEATHER_DATA_OBJECT, Context.MODE_PRIVATE);
////			ObjectOutputStream stationObject = new ObjectOutputStream(stationFile);
////			stationObject.writeObject(stationList);
////			stationFile.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState = ui.saveState(savedInstanceState);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		SaveObjects save = new SaveObjects();
		
		// Detach all tasks from activity to prevent memory leaks
		stationTask.detach();
		weatherTask.detach();
		forecastTask.detach();
		
		save.stationParserTask = stationTask;
		save.weatherParserTask = weatherTask;
		save.forecastParserTask = forecastTask;
		
		save.weatherData = currentWeather;
		save.forecastData = currentForecast;
		save.stationList = stationList;

		return(save);
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
			if(refreshStationList) {
				if(currentLocation != null) {
					// Get the list of stations for the city (run in a thread)
					stationTask = new StationParserTask(this);
					stationTask.execute(currentLocation); // Weather will be refreshed
				}
			}
			else {
				stationSelected(currentStationIndex);
			}
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
				ui.location.setText(currentLocation);
				
				// Get the list of stations for the city (run in a thread)
				stationTask = new StationParserTask(this);
				stationTask.execute(currentLocation);
				
			}
		}

	}
	
	// Use the Activity to manage the dialog. If it is running and needs
	// to be recreated (rotate) then the Activity will show it again.
	// Only need to dismiss when task is done.
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == DIALOG_STATION_LOADING) {
			return new ProgressDialog(this);
		}
		else if(id == DIALOG_WEATHER_LOADING) {
			return new ProgressDialog(this);
		}
		else {
			return null;
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if(id == DIALOG_STATION_LOADING) {
			((ProgressDialog) dialog).setMessage("Getting Stations...");
			((ProgressDialog) dialog).setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// Cancel the AsyncTask
					Log.v("BlueSky", "StationParser cancled = " + stationTask.stopParsing());
				}
	    	});
		}
		else if(id == DIALOG_WEATHER_LOADING) {
			((ProgressDialog) dialog).setMessage("Getting Weather...");
			((ProgressDialog) dialog).setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// Cancel the AsyncTask
					Log.v("BlueSky", "WeatherParser cancled = " + weatherTask.stopParsing());
				}
	    	});
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
	
	private void restoreWeather() {
		Log.v("BlueSky", "Restoring Weather");
		
		boolean restoreError = false;
		
		// Restore objects from files
		try {
			// Restore Weather Data
			FileInputStream weatherFile = openFileInput(FILE_WEATHER_DATA_OBJECT);
			ObjectInputStream weatherObject = new ObjectInputStream(weatherFile);
			Object oW = weatherObject.readObject();
			weatherFile.close();
			if(oW instanceof WeatherData) {
				currentWeather = (WeatherData) oW;
			}
			else {
				restoreError = true;
			}
			
			// Restore Forecast Data
			FileInputStream forecastFile = openFileInput(FILE_FORECAST_DATA_OBJECT);
			ObjectInputStream forecastObject = new ObjectInputStream(forecastFile);
			Object oF = forecastObject.readObject();
			forecastFile.close();
			if(oF instanceof ForecastData) {
				currentForecast = (ForecastData) oF;
			}
			else {
				restoreError = true;
			}
			
//			// Restore Station Data
//			FileInputStream stationFile = openFileInput(FILE_WEATHER_DATA_OBJECT);
//			ObjectInputStream stationObject = new ObjectInputStream(stationFile);
//			Object oS = stationObject.readObject();
//			stationFile.close();
//			if(oS instanceof StationList) {
//				stationList = (StationList) oS;
//			}
//			else {
//				restoreError = true;
//			}
		} catch (FileNotFoundException e) {
			restoreError = true;
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			restoreError = true;
			e.printStackTrace();
		} catch (IOException e) {
			restoreError = true;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			restoreError = true;
			e.printStackTrace();
		}
		
		Log.v("BlueSky", "restoreError = " + restoreError);
		
		if(!restoreError) {
			// Set the Location (city)
			ui.location.setText(currentLocation);
			ui.updateWeatherTab(currentWeather);
			ui.updateForecastTab(currentForecast);
			//displayWeather();
			//displayForecast();
			//displayStations();
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
				weatherTask = new WeatherParserTask(this);
				weatherTask.execute(currentStation, stationList.get(stationList.getFirstAirport()));
				
				// Move to the weather tab
				ui.tabHost.setCurrentTab(0);
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
//	private void displayWeather() {
//		ui.updateTime.setText(currentWeather.getTime());
//		ui.temperature.setText(currentWeather.getTempFString());
//		ui.weatherCondtion.setText(currentWeather.getWeatherCondition());
//		ui.windSpeed.setText(currentWeather.getWindDirSpeedString());
//		ui.weatherCondtion.setText(currentWeather.getWeatherCondition());
//		ui.windGust.setText(currentWeather.getWindGustMphString());
//		ui.humidity.setText(currentWeather.getHumidityString());
//		ui.rainfall.setText(currentWeather.getRainfallInchString());
//		ui.pressure.setText(currentWeather.getPressureInchString());
//		ui.visibility.setText(currentWeather.getVisibilityMileString());
//		ui.dewPoint.setText(currentWeather.getDewFString());
//		ui.uvIndex.setText(currentWeather.getUvString());
//	}


	private void updateStationList(ArrayList<String> list) {
//		StationAdapter adapter = new StationAdapter(this, R.layout.stations_item, list);
//		ui.stationListView.setAdapter(adapter);
		
		ui.updateStationListView(stationList.getStationNamesList(), stationList.getStationTypesList());
		
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
	
//	private void displayStations() {
//		StationAdapter adapter = new StationAdapter(this, R.layout.stations_item, stationList.getStationNamesList());
//		ui.stationListView.setAdapter(adapter);
//		ui.selectedStation.setText(stationList.get(currentStationIndex).getStationTitle() + " (" + stationList.get(currentStationIndex).getElevation() + "ft)");
//	}
	
//	private void displayForecast() {
//		if(currentForecast.forecastShort.size() > 1 && currentForecast.forecastExtended.size() > 4) {
//			// Short Forecast display data
//			for(int i = 0; i < BlueSkyActivity.FORECAST_SHORT_COUNT; i++) {
//				ui.forecastShort.get(i).icon.setImageResource(currentForecast.forecastShort.get(i).getIconId());
//				ui.forecastShort.get(i).title.setText(currentForecast.forecastShort.get(i).getTitle());
//				ui.forecastShort.get(i).condition.setText(currentForecast.forecastShort.get(i).getForecast());
//			}
//			
//			// Extended Forecast display data
//			for(int i = 0; i < BlueSkyActivity.FORECAST_EXTENDED_COUNT; i++) {
//				ui.forecastExtended.get(i).icon.setImageResource(currentForecast.forecastExtended.get(i).getIconId());
//				ui.forecastExtended.get(i).title.setText(currentForecast.forecastExtended.get(i).getDateWeekday());
//				ui.forecastExtended.get(i).condition.setText(currentForecast.forecastExtended.get(i).getCondition());
//				ui.forecastExtended.get(i).temperatureHigh.setText(currentForecast.forecastExtended.get(i).getHigh_F());
//				ui.forecastExtended.get(i).temperatureLow.setText(currentForecast.forecastExtended.get(i).getLow_F());
//				ui.forecastExtended.get(i).temperatureUnit.setText(this.getString(R.string.unit_english_temperature));
//			}
//		}
//		else {
//			// Warn user
//			Toast.makeText(BlueSkyActivity.this, "Forecast Data Error", Toast.LENGTH_LONG).show();
//		}
//	}
	
	/**
	 * Starts an AsyncTask for parsing the list of stations.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	static private class StationParserTask extends AsyncTask<String, Void, StationList> {
		BlueSkyActivity parent = null;
		Context appContext = null;

		//private ProgressDialog progressDialog;// = new ProgressDialog(BlueSkyActivity.this);
		private StationPullParser parser;

		public StationParserTask(BlueSkyActivity activity) {
			// Get the application context so that if the activity is destroyed
			// while thread is running the context will not be null.
			appContext = activity.getApplicationContext();
			
			attach(activity);
		}
		
		public void attach(BlueSkyActivity activity) {
			this.parent = activity;
//			setupProgressDialog();
//			
//			// If re-attaching to a running task then show progressDialog again
//			if(getStatus() == AsyncTask.Status.RUNNING) {
//				this.progressDialog.show();
//			}
		}
		
		public void detach() {
			this.parent = null;
//			// Close progress dialog
//			this.progressDialog.dismiss();
//			this.progressDialog = null;
		}
		
//		private void setupProgressDialog() {
//			this.progressDialog = new ProgressDialog(parent);
//			this.progressDialog.setMessage("Getting Stations...");
//			this.progressDialog.setIndeterminate(true);
//			this.progressDialog.setCancelable(true);
//			this.progressDialog.setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					if(parser != null) {
//						parser.stopParse();
//					}
//					// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
//					cancel(true);
//				}
//	    	});
//		}
		
		protected void onPreExecute() {
			parent.showDialog(BlueSkyActivity.DIALOG_STATION_LOADING);
			//this.progressDialog.show();
		}
		
		protected StationList doInBackground(String... params) {
			StationList list = null;
			
			// Try creating the parser and then parsing
			try {
				parser = new StationPullParser(this.appContext, params[0]);
				list = parser.parse();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return list;
		}
		
		protected void onPostExecute(StationList result) {
			if(result != null) {
				// Update stations with result
				parent.stationList = result;
				ArrayList<String> stations = parent.stationList.getStationNamesList();
	
				// Update the station spinner with list of PWS stations
				parent.updateStationList(stations);
			}
			else {
				// Warn user
				Toast.makeText(parent, "Network Error", Toast.LENGTH_LONG).show();
			}
			
			// Close the dialog
			parent.dismissDialog(BlueSkyActivity.DIALOG_STATION_LOADING);
//			if(this.progressDialog.isShowing()) {
//				this.progressDialog.dismiss();
//			}
		}
		
		public boolean stopParsing() {
			if(parser != null) {
				parser.stopParse();
			}
			// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
			return cancel(true);
		}
	};
	
	/**
	 * Starts an AsyncTask for parsing the weather.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	static private class WeatherParserTask extends AsyncTask<WeatherStation, Void, WeatherData> {
		BlueSkyActivity parent = null;
		Context appContext = null;

		//private ProgressDialog progressDialog;// = new ProgressDialog(BlueSkyActivity.this);
		//private WeatherPullParser parser;
		private WeatherStation station;
		
		public WeatherParserTask(BlueSkyActivity activity) {
			// Get the application context so that if the activity is destroyed
			// while thread is running the context will not be null.
			appContext = activity.getApplicationContext();
			
			attach(activity);
		}
		
		public void attach(BlueSkyActivity activity) {
			this.parent = activity;
			//setupProgressDialog();
			
//			// If re-attaching to a running task then show progressDialog again
//			if(getStatus() == AsyncTask.Status.RUNNING) {
//				this.progressDialog.show();
//			}
		}
		
		public void detach() {
			this.parent = null;
//			// Close progress dialog
//			this.progressDialog.dismiss();
//			this.progressDialog = null;
		}
		
//		private void setupProgressDialog() {
//			this.progressDialog = new ProgressDialog(parent);
//			this.progressDialog.setMessage("Getting Weather...");
//			this.progressDialog.setIndeterminate(true);
//			this.progressDialog.setCancelable(true);
//			this.progressDialog.setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					if(station != null) {
//						station.stopWeatherParse();
//					}
//					// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
//					cancel(true);
//					
//					// try this code
//					dialog.dismiss();
//				}
//	    	});
//		}
		
		protected void onPreExecute() {
			parent.showDialog(BlueSkyActivity.DIALOG_WEATHER_LOADING);
			//this.progressDialog.show();
		}
		
		protected WeatherData doInBackground(WeatherStation... params) {
			
			station = params[0];
			WeatherStation firstAirport = params[1];
			
			WeatherData weather = null;
			WeatherData extraWeather;
			
			//
			try {
				weather = station.parseWeather(this.appContext);
				
				// Only get extra weather for PWS (data that airports are missing should not be filled in)
				if(station.getStationType() == WeatherStation.StationType.PWS) {
					//int index = stationList.getFirstAirport();
					// Airports have some information that weather stations don't so get weather from closest airport
					//if(index != -1) {
					if(firstAirport != null) {
						extraWeather = firstAirport.parseWeather(this.appContext);
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
				parent.currentWeather = result;
				
				parent.ui.updateWeatherTab(parent.currentWeather);
				//displayWeather();
				
				// Change the selected station (wait for parse to get elevation)
				parent.ui.selectedStation.setText(parent.stationList.get(parent.currentStationIndex).getStationTitle() + " (" + parent.stationList.get(parent.currentStationIndex).getElevation() + "ft)");
				
				// Start a forecast parse (if weather parse failed then don't parse forecast)
				// TODO: Check if forecast should be updated or not
				Log.v("BlueSky", "location = " + parent.currentLocation);
				parent.forecastTask = new ForecastParserTask(parent);
				parent.forecastTask.execute(parent.currentLocation);
			}
			else {
				// Warn user 
				Toast.makeText(parent, "Network Error", Toast.LENGTH_LONG).show();
			}
			
			// Close the dialog
			parent.dismissDialog(BlueSkyActivity.DIALOG_WEATHER_LOADING);
//			if(this.progressDialog.isShowing()) {
//				this.progressDialog.dismiss();
//			}
		}
		
		public boolean stopParsing() {
			if(station != null) {
				station.stopWeatherParse();
			}
			// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
			return cancel(true);
		}
	};
	
	
	/**
	 * Starts an AsyncTask for parsing the forecast.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	static private class ForecastParserTask extends AsyncTask<String, Void, ForecastData> {
		BlueSkyActivity parent = null;
		Context appContext = null;
		
		private ForecastParser parser;
		
		public ForecastParserTask(BlueSkyActivity activity) {
			// Get the application context so that if the activity is destroyed
			// while thread is running the context will not be null.
			appContext = activity.getApplicationContext();
			
			attach(activity);
		}
		
		public void attach(BlueSkyActivity activity) {
			this.parent = activity;
		}
		
		public void detach() {
			this.parent = null;
		}
		
		protected ForecastData doInBackground(String... params) {
			ForecastData forecast = null;
			
			// Try creating the parser and then parsing
			try {
				parser = new ForecastParser(appContext, params[0]);
				forecast = parser.parse();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			return forecast;
		}
		
		protected void onPostExecute(ForecastData result) {
			if(result != null) {
				
				parent.currentForecast = result;
				
				parent.ui.updateForecastTab(parent.currentForecast);
				//displayForecast();
			}
			else {
				// Warn user
				Toast.makeText(parent, "Network Error", Toast.LENGTH_LONG).show();
			}
			
		}
		
		public boolean stopParsing() {
			if(parser != null) {
				parser.stopParse();
			}
			// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
			return cancel(true);
		}
	};
	
	private class SaveObjects {
		// Data
		public StationList stationList = null;
		public WeatherData weatherData = null;
		public ForecastData forecastData = null;
		
		// AsyncTasks
		public WeatherParserTask weatherParserTask = null;
		public ForecastParserTask forecastParserTask = null;
		public StationParserTask stationParserTask = null;
	}
}
