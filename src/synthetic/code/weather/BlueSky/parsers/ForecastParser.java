/**
 * 
 */
package synthetic.code.weather.BlueSky.parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import synthetic.code.weather.BlueSky.ForecastData;
import synthetic.code.weather.BlueSky.ForecastData.ForecastDayExtended;
import synthetic.code.weather.BlueSky.ForecastData.ForecastDayShort;
import synthetic.code.weather.BlueSky.R;

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
	
	private enum FORECAST_TYPE {NONE, SHORT, EXTENDED};
	private enum TEMP_TYPE {NONE, LOW, HIGH};
	
	private ForecastData forecast;

	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param location : Location to get forecast for {"city, state"}
	 * @throws UnsupportedEncodingException
	 */
	public ForecastParser(Context parentContext, String location) throws UnsupportedEncodingException {
		// Build the feed url from the query and the geoLookup url
		// Use URLEncoder.encode() to remove invalid characters from query
		super(parentContext.getString(R.string.wui_forecast) + URLEncoder.encode(location, "UTF-8"));
	}

	/* (non-Javadoc)
	 * @see synthetic.code.weather.BlueSky.parsers.BaseFeedParser#parse()
	 */
	@Override
	public ForecastData parse() throws RuntimeException {
		abort = false;
		forecast = new ForecastData();
		FORECAST_TYPE currentType = FORECAST_TYPE.NONE;
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			
			int eventType = parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT && !abort) {
				String tagName = null;
				
				switch(eventType) {
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					
					if(tagName.equalsIgnoreCase(FORECAST_SHORT)) {
						currentType = FORECAST_TYPE.SHORT;
					}
					else if(tagName.equalsIgnoreCase(FORECAST_EXTENDED)) {
						currentType = FORECAST_TYPE.EXTENDED;
					}
					else if(tagName.equalsIgnoreCase(FORECAST_DAY)) {
						if(currentType == FORECAST_TYPE.SHORT) {
							parser = parseForecastShort(parser);
						}
						else if(currentType == FORECAST_TYPE.EXTENDED) {
							parser = parseForecastExtended(parser);
						}
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
	
	private XmlPullParser parseForecastShort(XmlPullParser parser) throws XmlPullParserException, IOException {
		String tagName = null;
		boolean done = false;
		int index = -1;
		ForecastDayShort day = forecast.new ForecastDayShort();
		
		int eventType = 0;
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				
				if(tagName.equalsIgnoreCase(SHORT_INDEX)) {
					String temp = parser.nextText();
					temp.trim();
					
					try {
						index = Integer.parseInt(temp);
					} catch (NumberFormatException e) {
						// Set done to true because if the index is unknown then a forecast can not be added
						done = true;
						Log.v("BlueSky", "ForecastParser: NumberFormatException when parsing SHORT_INDEX");
						e.printStackTrace();
					}
				}
				else if(tagName.equalsIgnoreCase(SHORT_ICON)) {
					day.setIcon(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase(SHORT_TITLE)) {
					day.setTitle(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase(SHORT_FORECAST)) {
					day.setForecast(parser.nextText());
				}
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(FORECAST_DAY)) {
					// When the end of the Forecast Day element is reached, end the loop
					done = true;
				}
				break;
			}
		}
		
		// Make sure the index was parsed
		if(index != -1) {
			// Replace previous forecast day with this day
			forecast.forecastShort.set(index, day);
		}
		
		// Returning parser is not necessary but it is more Java like
		return parser;
	}
	
	private XmlPullParser parseForecastExtended(XmlPullParser parser) throws XmlPullParserException, IOException {
		String tagName = null;
		boolean done = false;
		int index = -1;
		ForecastDayExtended day = forecast.new ForecastDayExtended();
		TEMP_TYPE tempFlag = TEMP_TYPE.NONE;
		
		int eventType = 0;
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done && !abort) {
			// Do the parse at the start so that parser is in correct location on exit
			eventType = parser.next();
			
			switch(eventType) {
			case XmlPullParser.START_TAG:
				tagName = parser.getName();
				
				if(tagName.equalsIgnoreCase(EXTENDED_INDEX)) {
					String temp = parser.nextText();
					temp.trim();
					
					try {
						index = Integer.parseInt(temp);
					} catch (NumberFormatException e) {
						// Set done to true because if the index is unknown then a forecast can not be added
						done = true;
						Log.v("BlueSky", "ForecastParser: NumberFormatException when parsing EXTENDED_INDEX");
						e.printStackTrace();
					}
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_ICON)) {
					day.setIcon(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_DATE_DAY)) {
					day.setDateDay(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_DATE_MONTH)) {
					day.setDateMonth(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_DATE_WEEKDAY)) {
					day.setDateWeekday(parser.nextText());
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_HIGH)) {
					tempFlag = TEMP_TYPE.HIGH;
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_LOW)) {
					tempFlag = TEMP_TYPE.LOW;
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_TEMP_F)) {
					if(tempFlag == TEMP_TYPE.HIGH) {
						day.setHigh_F(parser.nextText());
					}
					else if(tempFlag == TEMP_TYPE.LOW) {
						day.setLow_F(parser.nextText());
					}
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_TEMP_C)) {
					if(tempFlag == TEMP_TYPE.HIGH) {
						day.setHigh_C(parser.nextText());
					}
					else if(tempFlag == TEMP_TYPE.LOW) {
						day.setLow_C(parser.nextText());
					}
				}
				else if(tagName.equalsIgnoreCase(EXTENDED_CONDITION)) {
					day.setCondition(parser.nextText());
				}
				break;
				
			case XmlPullParser.END_TAG:
				tagName = parser.getName();
				if(tagName.equalsIgnoreCase(FORECAST_DAY)) {
					// When the end of the Forecast Day element is reached, end the loop
					done = true;
				}
				break;
			}
		}
		
		// Make sure the index was parsed
		if(index != -1) {
			// Replace previous forecast day with this day
			forecast.forecastExtended.set(index, day);
		}
		
		// Returning parser is not necessary but it is more Java like
		return parser;
	}

}
