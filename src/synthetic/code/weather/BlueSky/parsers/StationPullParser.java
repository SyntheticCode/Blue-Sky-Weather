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
package synthetic.code.weather.BlueSky.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

//import synthetic.code.weather.BlueSky.Airport;
//import synthetic.code.weather.BlueSky.PWS;
import synthetic.code.weather.BlueSky.WeatherStation;
import synthetic.code.weather.BlueSky.R;
import synthetic.code.weather.BlueSky.StationList;

import android.util.Log;
import android.util.Xml;
import android.content.Context;

/**
 * Parser object for GeoLookupXML feed from Weather Underground.
 * Used for building a list of stations near a location.
 * @author David
 *
 */
public class StationPullParser extends BaseFeedParser {
	// Common tags between airport and pws
	static final String STATION_START_TAG = "station";
	static final String STATION_CITY = "city";
	static final String STATION_STATE = "state";
	static final String STATION_COUNTRY = "country";
	
	// Airport only tags
	static final String AIRPORT_START_TAG = "airport";
	static final String AIRPORT_CODE = "icao";
	static final String AIRPORT_LATITUDE = "lat";
	static final String AIRPORT_LONGITUDE = "lon";
	
	// PWS only tags
	static final String PWS_START_TAG = "pws";
	static final String PWS_NAME = "neighborhood";
	static final String PWS_ID = "id";
	static final String PWS_DIST_MI = "distance_mi";
	static final String PWS_DIST_KM = "distance_km";
	
	private enum STATION_TYPE {STATION_NULL, STATION_AIRPORT, STATION_PWS}
	
	StationList currentStationList;
	
	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param query : Location to build list of stations for {"city, state", "zipcode", "lat,lon", "airport code"}
	 * @throws UnsupportedEncodingException
	 */
	public StationPullParser(Context parentContext, String query) throws UnsupportedEncodingException {
		// Build the feed url from the query and the geoLookup url
		// Use URLEncoder.encode() to remove invalid characters from query
		super(parentContext.getString(R.string.wui_geo_lookup) + URLEncoder.encode(query, "UTF-8"));
	}
	
	/**
	 * Gets XML data for a location and parses XML for list of all stations around that location.
	 * @return StationList object with all of the stations for parsed location.
	 */
	public StationList parse() throws RuntimeException {
		abort = false;
		currentStationList = new StationList();
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			//Log.v("WeatherWidget::StationPullParser", "Got input Stream");
			int eventType = parser.getEventType();
			STATION_TYPE stationType = STATION_TYPE.STATION_NULL;
			
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
				String tagName = null;
				
				switch(eventType) {
					case XmlPullParser.START_TAG:
						//Log.v("WeatherWidget::StationPullParser", "Start Tag Found");
						tagName = parser.getName();
						//Log.v("WeatherWidget::StationPullParser", "Tag Name = " + tagName);
						
						// Look for start of airport section and set stationType to be airport
						if(tagName.equalsIgnoreCase(AIRPORT_START_TAG)) {
							//Log.v("WeatherWidget::StationPullParser", "Airport Start Tag Found");
							stationType = STATION_TYPE.STATION_AIRPORT;
						}
						// Look for start of pws section and set stationType to be pws
						else if(tagName.equalsIgnoreCase(PWS_START_TAG)) {
							//Log.v("WeatherWidget::StationPullParser", "PWS Start Tag Found");
							stationType = STATION_TYPE.STATION_PWS;
						}
						
						else if(tagName.equalsIgnoreCase(STATION_START_TAG)) {
							//Log.v("WeatherWidget::StationPullParser", "stationType = " + stationType);
							
							if(stationType == STATION_TYPE.STATION_AIRPORT) {
								//Log.v("WeatherWidget::StationPullParser", "Airport Station Tag Found");
								parseStationAirport(parser);
							}
							else if(stationType == STATION_TYPE.STATION_PWS) {
								//Log.v("WeatherWidget::StationPullParser", "PWS Station Tag Found");
								parseStationPws(parser);
							}
						}
						
						
						break;
					
					case XmlPullParser.END_TAG:
						//Log.v("WeatherWidget::StationPullParser", "End Tag Found");
						tagName = parser.getName();
						
						if(tagName.equalsIgnoreCase(AIRPORT_START_TAG)) {
							//Log.v("WeatherWidget::StationPullParser", "Airport End Tag Found");
							stationType = STATION_TYPE.STATION_AIRPORT;
						}
						
						else if(tagName.equalsIgnoreCase(PWS_START_TAG)) {
							//Log.v("WeatherWidget::StationPullParser", "PWS End Tag Found");
							stationType = STATION_TYPE.STATION_PWS;
						}
						
						break;
						
				}
				
				eventType = parser.next();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return currentStationList;
	}
	
	/**
	 * Parses PWS station element for all PWS info and adds a new PWS to the list.
	 * @param parser : Current XML parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void parseStationPws(XmlPullParser parser) 
	throws XmlPullParserException, IOException {
		String tag = null;
		int eventType;
		
		WeatherStation station = new WeatherStation(WeatherStation.StationType.PWS);
		
		while(!abort) {
			eventType = parser.next();
			
			if(eventType == XmlPullParser.START_TAG) {
				tag = parser.getName();
				if(tag.equalsIgnoreCase(PWS_NAME)) {
					station.setName(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(PWS_ID)) {
					station.setId(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(STATION_CITY)) {
					station.setCity(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(STATION_STATE)) {
					station.setState(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(STATION_COUNTRY)) {
					station.setCountry(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(PWS_DIST_KM)) {
					station.setDistance(parser.nextText(), true);
				}
				else if(tag.equalsIgnoreCase(PWS_DIST_MI)) {
					station.setDistance(parser.nextText(), false);
				}
			}
			
			else if(eventType == XmlPullParser.END_TAG) {
				tag = parser.getName();
				if(tag.equalsIgnoreCase(STATION_START_TAG)) {
					break;
				}
			}
		}
		
		if(!station.empty()) {
			this.currentStationList.add(station);
		}
	}

	/**
	 * Parses Airport station element for all Airport info and adds a new Airport to the list.
	 * @param parser : Current XML parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void parseStationAirport(XmlPullParser parser) 
	throws XmlPullParserException, IOException {
		String tag = null;
		int eventType;
		
		WeatherStation station = new WeatherStation(WeatherStation.StationType.AIRPORT);
		
		while(!abort) {
			eventType = parser.next();
			
			if(eventType == XmlPullParser.START_TAG) {
				tag = parser.getName();
				if(tag.equalsIgnoreCase(AIRPORT_CODE)) {
					station.setId(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(STATION_CITY)) {
					station.setCity(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(STATION_STATE)) {
					station.setState(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(STATION_COUNTRY)) {
					station.setCountry(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(AIRPORT_LATITUDE)) {
					station.setLatitude(parser.nextText());
				}
				else if(tag.equalsIgnoreCase(AIRPORT_LONGITUDE)) {
					station.setLongitude(parser.nextText());
				}
			}
			
			else if(eventType == XmlPullParser.END_TAG) {
				tag = parser.getName();
				if(tag.equalsIgnoreCase(STATION_START_TAG)) {
					break;
				}
				
			}
		}
		
		if(!station.empty()) {
			//Log.v("WeatherWidget::StationPullParser", "Added Airport");
			this.currentStationList.add(station);
		}
	}

}
