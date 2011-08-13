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

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

import com.SyntheticCode.BlueSkyWeather.parsers.GeoLookupParser;

public class SearchResultActivity extends ListActivity {
	public static final String KEY_QUERY = "QUERY";
	private static final String KEY_LIST = "LIST";
	
	private static final int DIALOG_SEARCHING = 0;
	
	private GeoLookupParserTask task = null;
	private ArrayList<CityData> list = null;
	private boolean citySelected = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		// Get string entered in search box
		String query = intent.getStringExtra(KEY_QUERY);
		
		SaveObjects save = (SaveObjects) getLastNonConfigurationInstance();
		
		if(save != null) {
			task = save.task;
			list = save.list;
			citySelected = save.citySelected;
		}
		
		if(task == null) {
			task = new GeoLookupParserTask(this);
			task.execute(query);
		}
		else {
			// Task has already been created so re-attach it to the activity
			task.attach(this);
		}
		
		// Redisplay results if screen was rotated and task is not running
		if(task.getStatus() != AsyncTask.Status.RUNNING) {
			showResults(list);
		}
	}
	
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		SaveObjects save = new SaveObjects();
		
		if(task != null) {
			task.detach();
		}
		
		save.task = task;
		save.list = list;
		save.citySelected = citySelected;

		return(save);
	}
	
	// Use the Activity to manage the dialog. If it is running and needs
	// to be recreated (rotate) then the Activity will show it again.
	// Only need to dismiss when task is done.
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == DIALOG_SEARCHING) {
			return new ProgressDialog(this);
		}
		else {
			return null;
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if(id == DIALOG_SEARCHING) {
			((ProgressDialog) dialog).setMessage("Searching...");
			((ProgressDialog) dialog).setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
					Log.v("BlueSky", "GeoLookupParser cancled = " + task.stopParsing());
					
					showResults(null); // "No City Found" will be displayed
				}
	    	});
		}
	}
	
	public void showResults(ArrayList<CityData> results) {
		if(results != null) {
			ArrayList<String> cityList = new ArrayList<String>();
			this.list = results; // save results
			
			Log.v("BlueSky", "Search Results = " + results.size());
			
			for(int i = 0; i < results.size(); i++) {
				cityList.add(results.get(i).getCityState());
				Log.v("BlueSky", list.get(i).getCityState());
			}
			
			this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityList));
		}
		else { // if results == null then search could not find a city
			// Warn user and then cancel
			Toast.makeText(this, "No City Found", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	public void showError() {
		// Warn user and then cancel
		Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// If all the data for the city is filled in then send back the results
		if(list.get(position).infoComplete()) {
			sendBackResults(position);
		}
		// Other wise get the rest of the data
		else {
			citySelected = true;
			task = new GeoLookupParserTask(this);
			task.execute(list.get(position).getCityState());
		}
	}
	
	private void sendBackResults(int position) {
		// Send clicked item back to parent
		Intent intent = new Intent();
		// Put the encoded CityData object
		intent.putExtra(KEY_QUERY, list.get(position).encodeObject());
		setResult(RESULT_OK, intent);
		finish();
	}

	
	/**
	 * Starts an AsyncTask for parsing the list of cities.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	static private class GeoLookupParserTask extends AsyncTask<String, Void, ArrayList<CityData>> {
		SearchResultActivity parent = null;
		Context appContext = null;
		
		private GeoLookupParser parser;

		private boolean noError;
		
		public GeoLookupParserTask(SearchResultActivity activity) {
			// Get the application context so that if the activity is destroyed
			// while thread is running the context will not be null.
			appContext = activity.getApplicationContext();
			
			attach(activity);
		}
		
		public void attach(SearchResultActivity activity) {
			parent = activity;
		}
		
		public void detach() {
			parent = null;
		}
		
		protected void onPreExecute() {
			parent.showDialog(SearchResultActivity.DIALOG_SEARCHING);
		}
		
		protected ArrayList<CityData> doInBackground(String... params) {
			ArrayList<CityData> list = null;
			
			// Try creating the parser and parsing the results
			try {
				parser = new GeoLookupParser(appContext, params[0]);
				list = parser.parse();
				noError = true;
			} catch (Exception e) { // Catching all exceptions (don't care why it failed)
				e.printStackTrace();
				noError = false;
			}
			
			return list;
		}
		
		protected void onPostExecute(ArrayList<CityData> result) {
			if(noError) {
				// If user has not selected a city then display results
				if(parent.citySelected == false) {
					parent.showResults(result);
				}
				// If user already selected a city then this task was called
				// to get more data. So just send back the results. Should only
				// be 1 city in list.
				else {
					parent.list = result;
					parent.sendBackResults(0);
				}
			}
			else {
				parent.showError();
			}
			
			parent.dismissDialog(SearchResultActivity.DIALOG_SEARCHING);
		}
		
		public boolean stopParsing() {
			if(parser != null) {
				parser.stopParse();
			}
			
			return cancel(true);
		}
	};
	
	private class SaveObjects {
		public GeoLookupParserTask task = null;
		public ArrayList<CityData> list = null;
		public boolean citySelected = false;
	}
}
