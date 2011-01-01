package synthetic.code.weather.BlueSky;

import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.content.Intent;

import synthetic.code.weather.BlueSky.parsers.GeoLookupParser;

public class SearchActivity extends ListActivity {
	public static final String KEY_QUERY = "QUERY";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		// Get string entered in search box
		String query = intent.getStringExtra(KEY_QUERY);
		
		ArrayList<String> searchResults = null;
		GeoLookupParser parser;
		// Send query to Weather Underground, parse the results, and display them
		try {
			parser = new GeoLookupParser(this, query);
			searchResults = parser.parse();
			
			// If there were any results then use the built in Android list view to display the results
			if(searchResults != null) {
				this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResults));
			}
			else {
				// Warn user and the cancel
				Toast.makeText(this, "No City Found", Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
				finish();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// Warn user and then cancel
			Toast.makeText(this, "Parse Error", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}
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

}
