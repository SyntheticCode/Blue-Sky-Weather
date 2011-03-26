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
package com.SyntheticCode.BlueSkyWeather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author David
 * Starts an AsyncTask for parsing the weather.
 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
 */
public class WeatherParserTask extends AsyncTask<WeatherStation, Void, WeatherData> {
	BlueSkyActivity parent = null;
	Context appContext = null;

	private WeatherStation station;
	
	public WeatherParserTask(BlueSkyActivity activity) {
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
	
	protected void onPreExecute() {
		parent.showDialog(BlueSkyActivity.DIALOG_WEATHER_LOADING);
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
		}
		
		
		return weather;
	}
	
	protected void onPostExecute(WeatherData result) {
		if(result != null) {
			// Update station with result
			parent.currentWeather = result;
			
			parent.ui.updateWeatherTab(parent.currentWeather);
			
			// Save Data to preferences
			SharedPreferences preferences = parent.getSharedPreferences(BlueSkyActivity.PREF_NAME, BlueSkyActivity.MODE_PRIVATE);
			//SharedPreferences.Editor editor = preferences.edit();
			parent.currentWeather.saveData(preferences);
			//editor.putString(parent.PREF_KEY_STATION, arg1)
			
			String stationTitle = parent.stationList.get(parent.currentStationIndex).getStationTitle();
			String stationElevation = Integer.toString(parent.stationList.get(parent.currentStationIndex).getElevation());
			
			// Change the selected station (wait for parse to get elevation)
			parent.ui.updateSelectedStation(stationTitle, stationElevation);
			//parent.ui.selectedStation.setText(parent.stationList.get(parent.currentStationIndex).getStationTitle() + " (" + parent.stationList.get(parent.currentStationIndex).getElevation() + "ft)");
			
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
	}
	
	public boolean stopParsing() {
		if(station != null) {
			station.stopWeatherParse();
		}
		// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
		return cancel(true);
	}
}
