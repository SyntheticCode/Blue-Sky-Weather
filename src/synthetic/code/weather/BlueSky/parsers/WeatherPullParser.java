/**
 * 
 */
package synthetic.code.weather.BlueSky.parsers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import synthetic.code.weather.BlueSky.WeatherStation;
import synthetic.code.weather.BlueSky.R;
import synthetic.code.weather.BlueSky.WeatherData;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

/**
 * @author David
 *
 */
public class WeatherPullParser extends BaseFeedParser {
	
	// Start of Personal Weather Station feed
	static final String STATION_START_TAG = "current_observation";
	
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
	static final String WEATHER_STRING = "weather";
	
	private boolean weatherOnly;
	private WeatherStation currentStation;
	
	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * It is important that station is already created and has an ID.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param station : WeatherStation object to get info and weather for.
	 * @throws UnsupportedEncodingException
	 */
	public WeatherPullParser(Context parentContext, WeatherStation station) throws UnsupportedEncodingException {
		super(parentContext.getString(urlSelector(station.getStationType())) + URLEncoder.encode(station.getId()));
		
		currentStation = station;
		weatherOnly = false;
	}
	
	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param stationId : ID of WeatherStation to create a new WeatherStation object and parse data for.
	 * @param type : Type of weather station this parse is for.
	 * @throws UnsupportedEncodingException
	 */
	public WeatherPullParser(Context parentContext, String stationId, WeatherStation.StationType type) throws UnsupportedEncodingException {
		super(parentContext.getString(urlSelector(type)) + URLEncoder.encode(stationId));
			
		currentStation = new WeatherStation(type);
		weatherOnly = false;
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
	
	/**
	 * Call this function to setup parse options.
	 * @param parseOnlyWeather : Set to true to not parse station info.
	 */
	public void setupParse(boolean parseOnlyWeather) {
		weatherOnly = parseOnlyWeather;
	}
	
	public WeatherStation parse() {
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
							currentStation.setId(parser.nextText());
						}
						else if(name.equalsIgnoreCase(CITY)) {
							currentStation.setCity(parser.nextText());
						}
						else if(name.equalsIgnoreCase(STATE)) {
							currentStation.setState(parser.nextText());
						}
						else if(name.equalsIgnoreCase(LATITUDE)) {
							currentStation.setLatitude(parser.nextText());
						}
						else if(name.equalsIgnoreCase(LONGITUDE)) {
							currentStation.setLongitude(parser.nextText());
						}
						else if(name.equalsIgnoreCase(ELEVATION)) {
							currentStation.setElevation(cleanElevation(parser.nextText()));
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
					else if(name.equalsIgnoreCase(WEATHER_STRING)) {
						currentWeather.setWeatherString(parser.nextText());
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("WeatherWidget::WeatherPullParser", e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		currentStation.setWeather(currentWeather);
		
		return currentStation;
	}
	
	static private String cleanElevation(String elevation) {
		// Elevation includes " ft" so remove it so string to int conversion works
		return elevation.substring(0, elevation.length() - 3);
	}
}
