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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import com.SyntheticCode.BlueSkyWeather.parsers.BaseFeedParser;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class WeatherStation implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3596685299006794648L;

	public enum StationType {
		AIRPORT, PWS, GENERIC
	}
	
	// All station data
	private StationType	type;
	private boolean 	empty;
	private String		stationID; // same as airport code
	private String 		city;
	private String 		state;
	private String 		country;
	private float		lat;
	private float		lon;
	private int			elevation;
	//private WeatherData	weather;
	
	// PWS station data
	private String	url;
	
	private String	name; // <neighborhood> in XML
	private short	distanceKm;
	private short	distanceMi;
	
	private WeatherPullParser weatherParser;
	private boolean firstParce;
	
	public WeatherStation(StationType stationType) {
		this.type = stationType;
		this.empty = true;
		this.city = "";
		this.state = "";
		this.country = "";
		this.lat = 0;
		this.lon = 0;
		this.elevation = 0;
		//this.weather = new WeatherData();
		//this.airportCode = "";
		this.url = "";
		this.stationID = "";
		this.name = "";
		this.distanceKm = 0;
		this.distanceMi = 0;
		firstParce = true;
	}
	
	public WeatherStation() {
		this(StationType.GENERIC);
	}
	
	// Check if object is empty (null)
	public boolean empty() {
		return this.empty;
	}
	
	public String getStationTitle() {
		String title;
		if(this.type == StationType.AIRPORT) {
			title = this.city + ", ";
			title += this.state + ", (";
			title += this.stationID + ")";
		}
		else if(this.type == StationType.PWS){
			title = this.name;
		}
		else {
			title = "Error";
		}
		
		return title;
	}
	
	/* Getters and Setters */
	public void setStationType(StationType type) {
		// Changing type does not make an object not empty
		this.type = type;
	}
	public StationType getStationType() {
		return type;
	}
	public void setCity(String city) {
		this.empty = false;
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	public void setState(String state) {
		this.empty = false;
		this.state = state;
	}
	public String getState() {
		return state;
	}
	public void setCountry(String country) {
		this.empty = false;
		this.country = country;
	}
	public String getCountry() {
		return country;
	}
	public void setLatitude(float lat) {
		this.empty = false;
		this.lat = lat;
	}
	public void setLatitude(String lat) {
		this.empty = false;
		try {
			this.lat = Float.parseFloat(lat);
		} catch (NumberFormatException e) {
			this.lat = 0;
		}
	}
	public float getLatitude() {
		return lat;
	}
	public void setLongitude(float lon) {
		this.empty = false;
		this.lon = lon;
	}
	public void setLongitude(String lon) {
		this.empty = false;
		try {
			this.lon = Float.parseFloat(lon);
		} catch (NumberFormatException e) {
			this.lon = 0;
		}
	}
	public float getLongitude() {
		return lon;
	}
	public void setElevation(int elevation) {
		this.empty = false;
		this.elevation = elevation;
	}
	public void setElevation(String elevation) {
		this.empty = false;
		try {
			this.elevation = Integer.parseInt(elevation);
		} catch (NumberFormatException e) {
			this.elevation = 0;
		}
	}
	public int getElevation() {
		return elevation;
	}
	public void setUrl(String url) {
		this.empty = false;
		this.url = url.trim();
	}
	public String getUrl() {
		return this.url;
	}
	public void setId(String id) {
		this.empty = false;
		this.stationID = id.trim();
	}
	public String getId() {
		return this.stationID;
	}
	public void setName(String name) {
		this.empty = false;
		this.name = name.trim();
	}
	public String getName() {
		return this.name;
	}
	public void setDistance(short distance, boolean metric) {
		this.empty = false;
		if(metric) {
			this.distanceKm = distance;
		}
		else {
			this.distanceMi = distance;
		}
	}
	public void setDistance(String distance, boolean metric) {
		this.empty = false;
		Short d;
		try {
			d = Short.parseShort(distance);
		} catch (NumberFormatException e) {
			d = 0;
		}
		
		if(metric) {
			this.distanceKm = d;
		}
		else {
			this.distanceMi = d;
		}
	}
	public short getDistance(boolean metric) {
		if(metric) return this.distanceKm;
		else return this.distanceMi;
	}
	
	public WeatherData parseWeather(Context parentContext) throws RuntimeException {
		WeatherData data = null;
		
		try {
			weatherParser = new WeatherPullParser(parentContext);
			
			// For the first parse of this station also get station data
			weatherParser.setupParse(!this.firstParce);
			
			this.firstParce = false;
			
			data = weatherParser.parse();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return data;
	}
	
	public void stopWeatherParse() {
		if(weatherParser != null) {
			weatherParser.stopParse();
		}
	}
	
	/**
	 * Selects the url that to use based on the station type. Note that this function
	 * has to be called with in a context that is aware of "R".
	 * @param type : Type of weather station to parse.
	 * @return R.string.wui_* number for the station type.
	 */
	private static int urlSelector(WeatherStation.StationType type) {
		if(type == WeatherStation.StationType.AIRPORT) {
			return R.string.wui_airport;
		}
		else {
			return R.string.wui_pws;
		}
	}
	
	private class WeatherPullParser extends BaseFeedParser implements java.io.Serializable {
		
		// Start of Personal Weather Station feed
		//static final String STATION_START_TAG = "current_observation";
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -6617193459947332306L;
		
		// Station Info
		static final String STATION_ID = "station_id";
		static final String CITY = "city";
		static final String STATE = "state";
		static final String LATITUDE = "latitude";
		static final String LONGITUDE = "longitude";
		static final String ELEVATION = "elevation";
		
		// Weather Info
		static final String TEMP_F = "temp_f";
		static final String TEMP_C = "temp_c";
		static final String TIME = "observation_time_rfc822"; // Time of weather update in GMT time
		static final String WIND_DIRECTION = "wind_dir";
		static final String WIND_SPEED = "wind_mph";
		static final String WEATHER_CONDITION = "weather";
		static final String WIND_GUST = "wind_gust_mph";
		static final String HUMIDITY = "relative_humidity";
		static final String RAINFALL_IN = "precip_today_in"; // inches
		static final String PRESSURE_IN = "pressure_in"; // inches
		static final String VISIBILITY_MI = "visibility_mi";
		static final String DEW_POINT_F = "dewpoint_f";
		static final String DEW_POINT_C = "dewpoint_c";
		static final String UV_INDEX = "UV";
		
		private boolean weatherOnly;
		
		/**
		 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
		 * It is important that station is already created and has an ID.
		 * @param parentContext : Parent of this object (needs to have a context)
		 * @throws UnsupportedEncodingException
		 */
		public WeatherPullParser(Context parentContext) throws UnsupportedEncodingException {
			super(parentContext.getString(urlSelector(WeatherStation.this.getStationType())) + URLEncoder.encode(WeatherStation.this.getId()));
			
			//currentStation = station;
			weatherOnly = false;
		}
		
		
		/**
		 * Call this function to setup parse options.
		 * @param parseOnlyWeather : Set to true to not parse station info.
		 */
		public void setupParse(boolean parseOnlyWeather) {
			weatherOnly = parseOnlyWeather;
		}
		
		public WeatherData parse() throws RuntimeException {
			WeatherData currentWeather = new WeatherData();
			
			XmlPullParser parser = Xml.newPullParser();
			try {
				// auto-detect the encoding from the stream
				parser.setInput(this.getInputStream(), null);
				int eventType = parser.getEventType();
				
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
					String name = null;
					switch(eventType) {
					case XmlPullParser.START_TAG:
						name = parser.getName();
						
						// Don't need to parse station info every time
						if(!weatherOnly) {
							if(name.equalsIgnoreCase(STATION_ID)) {
								WeatherStation.this.setId(parser.nextText());
							}
							else if(name.equalsIgnoreCase(CITY)) {
								WeatherStation.this.setCity(parser.nextText());
							}
							else if(name.equalsIgnoreCase(STATE)) {
								WeatherStation.this.setState(parser.nextText());
							}
							else if(name.equalsIgnoreCase(LATITUDE)) {
								WeatherStation.this.setLatitude(parser.nextText());
							}
							else if(name.equalsIgnoreCase(LONGITUDE)) {
								WeatherStation.this.setLongitude(parser.nextText());
							}
							else if(name.equalsIgnoreCase(ELEVATION)) {
								WeatherStation.this.setElevation(cleanElevation(parser.nextText()));
							}
						}
						
						if(name.equalsIgnoreCase(TIME)) {
							currentWeather.setTime(parser.nextText());
						}
						else if(name.equalsIgnoreCase(TEMP_F)) {
							currentWeather.setTempF(parser.nextText());
						}
						else if(name.equalsIgnoreCase(TEMP_C)) {
							currentWeather.setTempC(parser.nextText());
						}
						else if(name.equalsIgnoreCase(WIND_DIRECTION)) {
							currentWeather.setWindDirection(parser.nextText());
						}
						else if(name.equalsIgnoreCase(WIND_SPEED)) {
							currentWeather.setWindSpeed(parser.nextText());
						}
						else if(name.equalsIgnoreCase(WEATHER_CONDITION)) {
							currentWeather.setWeatherCondition(parser.nextText());
						}
						else if(name.equalsIgnoreCase(WIND_GUST)) {
							currentWeather.setWindGustMph(parser.nextText());
						}
						else if(name.equalsIgnoreCase(HUMIDITY)) {
							currentWeather.setHumidity(parser.nextText());
						}
						else if(name.equalsIgnoreCase(RAINFALL_IN)) {
							currentWeather.setRainfallInch(parser.nextText());
						}
						else if(name.equalsIgnoreCase(PRESSURE_IN)) {
							currentWeather.setPressureInch(parser.nextText());
						}
						else if(name.equalsIgnoreCase(VISIBILITY_MI)) {
							currentWeather.setVisibilityMile(parser.nextText());
						}
						else if(name.equalsIgnoreCase(DEW_POINT_F)) {
							currentWeather.setDewF(parser.nextText());
						}
						else if(name.equalsIgnoreCase(UV_INDEX)) {
							currentWeather.setUv(parser.nextText());
						}
						else if(name.equalsIgnoreCase(DEW_POINT_C)) {
							currentWeather.setDewC(parser.nextText());
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			
			return currentWeather;
		}
		
		private String cleanElevation(String elevation) {
			// Elevation includes " ft" so remove it so string to int conversion works
			return elevation.substring(0, elevation.length() - 3);
		}
	}
}
