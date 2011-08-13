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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.SyntheticCode.BlueSkyWeather.CityData;
import com.SyntheticCode.BlueSkyWeather.ForecastData;
import com.SyntheticCode.BlueSkyWeather.ForecastData.ForecastDataObject;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

/**
 * @author David
 *
 */
public class ForecastParser extends BaseFeedParser {
	static final String FORECAST_SHORT = "txt_forecast";
	static final String FORECAST_EXTENDED = "simpleforecast";
	static final String FORECAST_DAY = "forecastday";
	
	static final String SHORT_INDEX = "period";
	static final String SHORT_ICON = "icon";
	static final String SHORT_TITLE = "title";
	static final String SHORT_FORECAST = "fcttext";
	
	static final String EXTENDED_INDEX = "period";
	static final String EXTENDED_ICON = "icon";
	static final String EXTENDED_DATE = "date";
	static final String EXTENDED_DATE_DAY = "day";
	static final String EXTENDED_DATE_MONTH = "month";
	static final String EXTENDED_DATE_WEEKDAY = "weekday";
	static final String EXTENDED_HIGH = "high";
	static final String EXTENDED_TEMP_F = "fahrenheit";
	static final String EXTENDED_TEMP_C = "celsius";
	static final String EXTENDED_LOW = "low";
	static final String EXTENDED_CONDITION = "conditions";
	
	/** Tags **/
	// Forecast period title
	static final String PERIOD_ROOT = "time-layout";
	static final String PERIOD_LAYOUT_KEY = "layout-key";
	static final String PERIOD = "start-valid-time";
	
	// Data
	static final String PARAM_ROOT = "parameters";
	static final String TEMP_ROOT = "temperature";
	static final String CONDITION_ROOT = "weather";
	static final String ICON_ROOT = "conditions-icon";
	static final String FORECAST_ROOT = "wordedForecast";
	static final String PARAM_VALUE = "value";
	static final String CONDITION = "weather-conditions";
	static final String ICON = "icon-link";
	static final String FORECAST_TEXT = "text";
	
	/** Attributes **/
	static final String PERIOD_TITLE = "period-name";
	static final String TEMP_TYPE = "type";
	static final String CONDITION_VALUE = "weather-summary";
	static final String LAYOUT_TYPE = "time-layout";
	
	private ForecastData forecast;

	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param location : Location to get forecast for {"city, state"}
	 * @throws UnsupportedEncodingException
	 */
	public ForecastParser(Context parentContext, CityData location) throws UnsupportedEncodingException {
		// Build the feed url from the query and the geoLookup url
		// Use URLEncoder.encode() to remove invalid characters from query
		super(urlBuilder(location));
	}
	
	private static String urlBuilder(CityData city) throws UnsupportedEncodingException {
		final String base = "http://forecast.weather.gov/MapClick.php?";
		final String cityTag = "CityName=";
		final String stateTag = "state=";
		final String siteTag = "site=HNX";
		final String latTag = "textField1=";
		final String lonTag = "textField2=";
		final String typeTag = "FcstType=dwml";
		
		String url = base;
		url += cityTag + URLEncoder.encode(city.getCity(), "UTF-8") + "&";
		url += stateTag + URLEncoder.encode(city.getState(), "UTF-8") + "&";
		url += siteTag + "&";
		url += latTag + URLEncoder.encode(city.getLat(), "UTF-8") + "&";
		url += lonTag + URLEncoder.encode(city.getLon(), "UTF-8") + "&";
		url += typeTag;
		
		Log.v("BlueSky", "NWS url = " + url);
		
		return url;
	}

	/* (non-Javadoc)
	 * @see synthetic.code.weather.BlueSky.parsers.BaseFeedParser#parse()
	 */
	@Override
	public ForecastData parse() throws RuntimeException {
		abort = false;
		forecast = new ForecastData();
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			
			int eventType = parser.getEventType();
			
			int periodCount = 0;
			
			while(eventType != XmlPullParser.END_DOCUMENT && !abort) {
				String tagName = null;
				
				switch(eventType) {
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					
					if(tagName.equalsIgnoreCase(PERIOD_ROOT)) {
						Log.v("BlueSky", "periodCount = " + periodCount);
						parser = parsePeriods(parser);
						periodCount++;
					}
					else if(tagName.equalsIgnoreCase(TEMP_ROOT)) {
						parser = parseTemperatures(parser);
					}
					else if(tagName.equalsIgnoreCase(CONDITION_ROOT)) {
						parser = parseConditions(parser);
					}
					else if(tagName.equalsIgnoreCase(ICON_ROOT)) {
						parser = parseIcons(parser);
					}
					else if(tagName.equalsIgnoreCase(FORECAST_ROOT)) {
						parser = parseForecast(parser);
					}
					break;
					
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					break;
				}
				
				eventType = parser.next();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return forecast;
	}
	
	private XmlPullParser parsePeriods(XmlPullParser parser) throws XmlPullParserException, IOException {
		String tagName = null;
		boolean done = false;
		int eventType = 0;
		String attribute = null;
		String layoutKey = "";
		ForecastData.ForecastDataObject period = null; // pointer to period to write to
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				// Get the layout key
				if(tagName.equalsIgnoreCase(PERIOD_LAYOUT_KEY)) {
					layoutKey = parser.nextText();
				}
				// Get all the period names
				else if(tagName.equalsIgnoreCase(PERIOD)) {
					attribute = parser.getAttributeValue(null, PERIOD_TITLE);
					if(attribute != null) {
						// Create the period object
						if(period == null) {
							period = new ForecastData.ForecastDataObject();
							period.timeLayout = layoutKey;
						}
						
						period.data.add(attribute);
					}
				}
				
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(PERIOD_ROOT)) {
					// end the loop when the closing root is reached
					done = true;
				}
				break;
			}
		}
		
		// If period was created then add it to the list of periods
		if(period != null) {
			forecast.periods.add(period);
		}
		
		if(period != null) {
			period.print("Period " + forecast.periods.size());
		}
		
		return parser;
	}
	
	private XmlPullParser parseTemperatures(XmlPullParser parser) throws XmlPullParserException, IOException {
		final String min = "minimum";
		String tagName = null;
		boolean done = false;
		int eventType = 0;
		
		ForecastDataObject temperature = null;

		// Check if the temperatures are max or min
		String type = parser.getAttributeValue(null, TEMP_TYPE);
		boolean isMin = min.equalsIgnoreCase(type);
		
		// Set the pointer to the correct temperature object
		if(isMin) {
			temperature = forecast.temperatureMin;
		}
		else {
			temperature = forecast.temperatureMax;
		}
		
		// Get the time layout
		temperature.timeLayout = parser.getAttributeValue(null, LAYOUT_TYPE);
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				// Get all the temperature values
				if(tagName.equalsIgnoreCase(PARAM_VALUE)) {
					temperature.data.add(parser.nextText());
				}
				
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(TEMP_ROOT)) {
					// end the loop when the closing root is reached
					done = true;
				}
				break;
			}
		}
		
		if(isMin) {
			temperature.print("Temp Min");
		}
		else {
			temperature.print("Temp Max");
		}
		
		return parser;
	}
	
	private XmlPullParser parseConditions(XmlPullParser parser) throws XmlPullParserException, IOException {
		String tagName = null;
		boolean done = false;
		int eventType = 0;
		String attribute = null;
		
		// Get the time layout
		forecast.weatherCondition.timeLayout = parser.getAttributeValue(null, LAYOUT_TYPE);
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				// Get all the weather conditions
				if(tagName.equalsIgnoreCase(CONDITION)) {
					attribute = parser.getAttributeValue(null, CONDITION_VALUE);
					if(attribute != null) {
						forecast.weatherCondition.data.add(attribute);
					}
				}
				
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(CONDITION_ROOT)) {
					// end the loop when the closing root is reached
					done = true;
				}
				break;
			}
		}
		
		forecast.weatherCondition.print("Weather");
		
		return parser;
	}
	
	private XmlPullParser parseIcons(XmlPullParser parser) throws XmlPullParserException, IOException {
		String tagName = null;
		boolean done = false;
		int eventType = 0;
		
		// Get the time layout
		forecast.conditionIcon.timeLayout = parser.getAttributeValue(null, LAYOUT_TYPE);
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				// Get all the icons for the days
				if(tagName.equalsIgnoreCase(ICON)) {
					forecast.conditionIcon.data.add(parser.nextText());
				}
				
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(ICON_ROOT)) {
					// end the loop when the closing root is reached
					done = true;
				}
				break;
			}
		}
		
		forecast.conditionIcon.print("Icon");
		
		return parser;
	}
	
	private XmlPullParser parseForecast(XmlPullParser parser) throws XmlPullParserException, IOException {
		String tagName = null;
		boolean done = false;
		int eventType = 0;
		
		// Get the time layout
		forecast.forecastText.timeLayout = parser.getAttributeValue(null, LAYOUT_TYPE);
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				// Get all the weather forecasts
				if(tagName.equalsIgnoreCase(FORECAST_TEXT)) {
					forecast.forecastText.data.add(parser.nextText());
				}
				
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(FORECAST_ROOT)) {
					// end the loop when the closing root is reached
					done = true;
				}
				break;
			}
		}
		
		forecast.forecastText.print("Forecast");
		
		return parser;
	}
}
