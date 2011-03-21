/**
 * 
 */
package synthetic.code.weather.BlueSky;

import java.io.UnsupportedEncodingException;

import synthetic.code.weather.BlueSky.parsers.ForecastParser;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author David
 * Starts an AsyncTask for parsing the forecast.
 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
 */
public class ForecastParserTask extends AsyncTask<String, Void, ForecastData> {
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
}
