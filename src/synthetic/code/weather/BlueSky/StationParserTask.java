/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import synthetic.code.weather.BlueSky.parsers.StationPullParser;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author David
 * Starts an AsyncTask for parsing the list of stations.
 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
 */
public class StationParserTask extends AsyncTask<String, Void, StationList> {
	BlueSkyActivity parent = null;
	Context appContext = null;

	private StationPullParser parser;

	public StationParserTask(BlueSkyActivity activity) {
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
		parent.showDialog(BlueSkyActivity.DIALOG_STATION_LOADING);
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
	}
	
	public boolean stopParsing() {
		if(parser != null) {
			parser.stopParse();
		}
		// Cancel the AsyncTask (isCancled() needs to be checked during doInBackground())
		return cancel(true);
	}
}
