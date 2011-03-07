package synthetic.code.weather.BlueSky;

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

import synthetic.code.weather.BlueSky.parsers.GeoLookupParser;

public class SearchResultActivity extends ListActivity {
	public static final String KEY_QUERY = "QUERY";
	private static final String KEY_LIST = "LIST";
	
	private static final int DIALOG_SEARCHING = 0;
	
	private GeoLookupParserTask task = null;
	private ArrayList<String> list = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		// Get string entered in search box
		String query = intent.getStringExtra(KEY_QUERY);
		
		task = (GeoLookupParserTask) getLastNonConfigurationInstance();
		
		if(task == null) {
			task = new GeoLookupParserTask(this);
			task.execute(query);
		}
		else {
			// Task has already been created so re-attach it to the activity
			task.attach(this);
		}
		
		
		if(savedInstanceState != null) {
			// Redisplay results if screen was rotated and task is not running
			if(task.getStatus() != AsyncTask.Status.RUNNING) {
				showResults(savedInstanceState.getStringArrayList(KEY_LIST));
			}
		}
	}
	
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		task.detach();

		return(task);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if(list != null) {
			savedInstanceState.putStringArrayList(KEY_LIST, list);
		}
		
		super.onSaveInstanceState(savedInstanceState);
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
	
	public void showResults(ArrayList<String> results) {
		if(results != null) {
			this.list = results; // save results
			this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
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
		
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		
		// Send clicked item back to parent
		Intent intent = new Intent();
		intent.putExtra(KEY_QUERY, keyword);
		setResult(RESULT_OK, intent);
		finish();
	}

	
	/**
	 * Starts an AsyncTask for parsing the list of cities.
	 * Displays a ProgressDialog while parsing. Parse can be canceled with back button.
	 * @author David
	 *
	 */
	static private class GeoLookupParserTask extends AsyncTask<String, Void, ArrayList<String>> {
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
		
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> list = null;
			
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
		
		protected void onPostExecute(ArrayList<String> result) {
			if(noError) {
				parent.showResults(result);
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
	
}
