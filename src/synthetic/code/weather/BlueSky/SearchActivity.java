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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		
		//TextView searchText = (TextView) findViewById(R.id.main_text);
		Intent intent = getIntent();
		
		if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// Get string entered in search box
			String query = intent.getStringExtra(SearchManager.QUERY);
			
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
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG).show();
	}

}
