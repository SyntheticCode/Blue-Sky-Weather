package synthetic.code.weather.BlueSky;

import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

import synthetic.code.weather.BlueSky.parsers.GeoLookupParser;

public class SearchResultActivity extends ListActivity {
	public static final String KEY_QUERY = "QUERY";
	//String query;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		// Get string entered in search box
		String query = intent.getStringExtra(KEY_QUERY);
		
		new GeoLookupParserTask().execute(query);
		
	}
	
	public void showResults(ArrayList<String> results) {
		if(results != null) {
			this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
		}
		else {
			// Warn user and then cancel
			Toast.makeText(this, "No City Found", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	public void showError() {
		// Warn user and then cancel
		Toast.makeText(this, "Parse Error", Toast.LENGTH_LONG).show();
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
	private class GeoLookupParserTask extends AsyncTask<String, Void, ArrayList<String>> {

		private final ProgressDialog progressDialog = new ProgressDialog(SearchResultActivity.this);
		private GeoLookupParser parser;

		
		protected void onPreExecute() {
			this.progressDialog.setMessage("Searching...");
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
		
		protected ArrayList<String> doInBackground(String... params) {
			// Try creating the parser
			try {
				parser = new GeoLookupParser(SearchResultActivity.this, params[0]);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new ArrayList<String>();
			}
			
			// Parse the stations
			return parser.parse();
		}
		
		protected void onPostExecute(ArrayList<String> result) {
			showResults(result);
			
			// Close the dialog
			if(this.progressDialog.isShowing()) {
				this.progressDialog.dismiss();
			}
		}
	};
	
}
