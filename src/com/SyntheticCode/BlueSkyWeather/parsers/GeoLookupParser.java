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
package com.SyntheticCode.BlueSkyWeather.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.SyntheticCode.BlueSkyWeather.R;
import android.util.Log;
import android.util.Xml;
import android.content.Context;

/**
 * Parser object for GeoLookupXML feed from Weather Underground.
 * Used for building a list of Cities that matched queried location.
 * @author David
 *
 */
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
	
	// Use the calling Activity's context to get the string from resources
	public GeoLookupParser(Context parentContext, String query) throws UnsupportedEncodingException {
		// Build the feed url from the query and the base url
		// Use URLEncoder.encode() to remove invalid characters from query
		super(parentContext.getString(R.string.wui_geo_lookup) + URLEncoder.encode(query, "UTF-8"));
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
	 * @return List of Cities that match location.
	 */
	@Override
	public ArrayList<String> parse() throws RuntimeException {
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
						Log.v("BlueSky", "GeoLookupParser: Error Tag found!");
					}
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return cityList;
	}
	
	ArrayList<String> parseList(XmlPullParser xml) throws RuntimeException {
		ArrayList<String> cityList = new ArrayList<String>();
		
		try {
			int eventType = xml.getEventType();
			boolean done = false;
			String tagName = null;
			String atrValue = null;
			boolean skip = false;
			
			while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
				
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
	
	ArrayList<String> parseSingle(XmlPullParser xml) throws RuntimeException {
		ArrayList<String> cityList = new ArrayList<String>();
		
		try {
			int eventType = xml.getEventType();
			boolean done = false;
			String tagName = null;
			String atrValue = null;
			String cityValue = null;
			String stateValue = null;
			
			while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
				
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
