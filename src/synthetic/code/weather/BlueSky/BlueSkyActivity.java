/**
 * Copyright (C) 2011 David Schonert
 *
 * This file is part of BlueSky.
 *
 * BlueSky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * BlueSky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlueSky.  If not, see <http://www.gnu.org/licenses/>.
 */
package synthetic.code.weather.BlueSky;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class BlueSkyActivity extends TabActivity {
	public static final int SEARCH_CITY = 1;
	public static final int FORECAST_SHORT_COUNT = 2;
	public static final int FORECAST_EXTENDED_COUNT = 5;
	
	public static final int DIALOG_STATION_LOADING = 0;
	public static final int DIALOG_WEATHER_LOADING = 1;
	
	// Preference Keys/Defaults
	public static final String PREF_NAME = "activity_pref";
	public static final String PREF_KEY_LOCATION = "CURRENT_LOCATION";
	
	// Data
	public StationList stationList;
	public WeatherData currentWeather;
	public ForecastData currentForecast;
	public boolean refreshStationList;
	
	// Preferences
	public String currentLocation;
	public int currentStationIndex;
	
	// AsyncTasks
	public WeatherParserTask weatherTask = null;
	public ForecastParserTask forecastTask = null;
	public StationParserTask stationTask = null;
	
	public UiObjects ui;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Create all UI objects and link them to the layout
		ui = new UiObjects(this);
		
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
			currentForecast = save.forecastData;
			stationList = save.stationList;
			currentLocation = save.location;
			
			currentStationIndex = save.stationIndex;
			// Make sure there are stations
			if(stationList != null) {
				// Make sure the index is valid
				if(currentStationIndex >= 0 && currentStationIndex < stationList.size()) {
					// Update the selected station
					ui.updateSelectedStation(stationList.get(currentStationIndex).getStationTitle(), Integer.toString(stationList.get(currentStationIndex).getElevation()));
				}
			}
		}
		// If objects were not saved then create new objects and restore their data from the DB
		else {
			Log.v("BlueSky", "Restoring from DB");
			
			refreshStationList = true;
			
			SharedPreferences preferences = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
			
			currentLocation = preferences.getString(PREF_KEY_LOCATION, null);
			
			currentWeather = new WeatherData();
			currentWeather.restoreData(preferences);
		}
		
		// If any of the objects were restored then update the UI
		if(currentLocation != null) {
			ui.location.setText(currentLocation);
		}
		
		if(currentWeather != null) {
			ui.updateWeatherTab(currentWeather);
		}
		
		if(currentForecast != null) {
			ui.updateForecastTab(currentForecast);
		}
		else {
			ui.hideForecast("Refresh");
		}
		
		if(stationList != null) {
			ui.updateStationListView(stationList.getStationNamesList(), stationList.getStationTypesList());
		}
		else {
			ui.hideWeatherStation("Refresh");
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Stop any running threads
		if(stationTask != null) {
			stationTask.stopParsing();
		}
		if(weatherTask != null) {
			weatherTask.stopParsing();
		}
		if(forecastTask != null) {
			forecastTask.stopParsing();
		}
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
		if(stationTask != null) {
			stationTask.detach();
		}
		if(weatherTask != null) {
			weatherTask.detach();
		}
		if(forecastTask != null) {
			forecastTask.detach();
		}
		
		save.stationParserTask = stationTask;
		save.weatherParserTask = weatherTask;
		save.forecastParserTask = forecastTask;
		
		save.weatherData = currentWeather;
		save.forecastData = currentForecast;
		save.stationList = stationList;
		
		save.location = currentLocation;
		save.stationIndex = currentStationIndex;

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
			if(currentLocation != null) {
				// If the list of station is not valid the refresh it
				if(stationList == null || refreshStationList) {
					// Get the list of stations for the city (run in a thread)
					stationTask = new StationParserTask(this);
					stationTask.execute(currentLocation); // Weather will be refreshed
				}
				else {
					stationSelected(currentStationIndex);
				}
			}
			else {
				Toast.makeText(this, "Please Search for a City First", Toast.LENGTH_LONG).show();
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
				
				// Save location to DB
				SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
				editor.putString(PREF_KEY_LOCATION, currentLocation);
				editor.commit();
				
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
	



	public void updateStationList(ArrayList<String> list) {
		
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
	
	private class SaveObjects {
		// Data
		public StationList stationList = null;
		public WeatherData weatherData = null;
		public ForecastData forecastData = null;
		
		public String location = null;
		public int stationIndex = -1;
		
		// AsyncTasks
		public WeatherParserTask weatherParserTask = null;
		public ForecastParserTask forecastParserTask = null;
		public StationParserTask stationParserTask = null;
	}
}
