package synthetic.code.weather.BlueSky.parsers;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.util.Xml;

public class GeoLookupParser extends BaseFeedParser {
	static final String LOCATION_LIST_TAG = "locations";
	static final String LOCATION_TAG = "location";
	static final String LOCATION_TYPE_ATR = "type";
	static final String LOCATION_NAME_TAG = "name";
	static final String LOCATION_CITY_TAG = "city";
	static final String LOCATION_STATE_TAG = "state";
	static final String LOCATION_TYPE_VALUE = "CITY";
	static final String ERROR_TAG = "wui_error";
	static final String ERROR_TITLE_TAG = "title";
	
	public GeoLookupParser(String feedUrl) {
		super(feedUrl);
	}

	/**
	 * Returns an ArrayList of all the cities found in the XML feed.
	 * A null ArrayList will be returned if unable to parse feed or
	 * no city found.
	 * 
	 * This parser can handle two types of XML feeds that can be returned
	 * from a GeoLookupXML query. One type is a list of cities that match
	 * the queried value. The other type is information about the only city
	 * match that the query found.
	 */
	@Override
	public ArrayList<String> parse() {
		ArrayList<String> cityList = null;
		
		XmlPullParser xml = Xml.newPullParser();
		try {
			xml.setInput(this.getInputStream(), null);
			
			int eventType = xml.getEventType();
			String tagName = null;
			
			// Determine what type of feed was received by checking the first tag
			// The list type feed will start with <locations> tag
			// The single city type feed will start with <location> tag
			if(eventType == XmlPullParser.START_DOCUMENT) {
				eventType = xml.next();
				if(eventType == XmlPullParser.START_TAG) {
					tagName = xml.getName();
					if(tagName.equalsIgnoreCase(LOCATION_LIST_TAG)) {
						cityList = parseList(xml);
					}
					else if(tagName.equalsIgnoreCase(LOCATION_TAG)) {
						cityList = parseSingle(xml);
					}
					else if(tagName.equalsIgnoreCase(ERROR_TAG)) {
						// ToDo : Do something with the error title
					}
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return cityList;
	}
	
	ArrayList<String> parseList(XmlPullParser xml) {
		ArrayList<String> cityList = new ArrayList<String>();
		
		try {
			int eventType = xml.getEventType();
			boolean done = false;
			String tagName = null;
			String atrValue = null;
			boolean skip = false;
			
			while(eventType != XmlPullParser.END_DOCUMENT && !done) {
				
				switch(eventType) {
					case XmlPullParser.START_TAG:
						tagName = xml.getName();
						// Check type attribute of location
						if(tagName.equalsIgnoreCase(LOCATION_TAG)) {
							// Check the type attribute to make sure it matches valid value
							// Set skip to true if value is not valid
							atrValue = xml.getAttributeValue(null, LOCATION_TYPE_ATR);
							skip = !atrValue.equalsIgnoreCase(LOCATION_TYPE_VALUE);
						}
						// If locations name tag is found and skip is not set then add name to list
						else if(tagName.equalsIgnoreCase(LOCATION_NAME_TAG) && !skip) {
							cityList.add(xml.nextText());
						}
						break;
						
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = xml.next();
			}
			
			// Return null if nothing was found
			if(cityList.isEmpty()) {
				cityList = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return cityList;
	}
	
	ArrayList<String> parseSingle(XmlPullParser xml) {
		ArrayList<String> cityList = new ArrayList<String>();
		
		try {
			int eventType = xml.getEventType();
			boolean done = false;
			String tagName = null;
			String atrValue = null;
			String cityValue = null;
			String stateValue = null;
			
			while(eventType != XmlPullParser.END_DOCUMENT && !done) {
				
				switch(eventType) {
					case XmlPullParser.START_TAG:
						tagName = xml.getName();
						// Check type attribute of location
						if(tagName.equalsIgnoreCase(LOCATION_TAG)) {
							// Check the type attribute to make sure it matches valid value
							// If value is not valid then stop parsing
							atrValue = xml.getAttributeValue(null, LOCATION_TYPE_ATR);
							if(!atrValue.equalsIgnoreCase(LOCATION_TYPE_VALUE)) {
								done = true;
							}
						}
						// If locations city tag is found then save value
						else if(tagName.equalsIgnoreCase(LOCATION_CITY_TAG)) {
							cityValue = xml.nextText();
							
							// If state tag has already been parsed then stop
							if(stateValue != null) done = true;
						}
						else if(tagName.equalsIgnoreCase(LOCATION_STATE_TAG)) {
							stateValue = xml.nextText();
							
							// If city tag has already been parsed then stop
							if(cityValue != null) done = true;
						}
						break;
						
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = xml.next();
			}
			// If city and state were found then add them to the list
			if((cityValue != null) && (stateValue != null)) {
				cityList.add(cityValue + ", " + stateValue);
			}
			else {
				// Return null if nothing was found
				cityList = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return cityList;
	}

}