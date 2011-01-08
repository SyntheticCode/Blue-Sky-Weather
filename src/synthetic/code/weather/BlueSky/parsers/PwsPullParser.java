/**
 * 
 */
package synthetic.code.weather.BlueSky.parsers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;

import synthetic.code.weather.BlueSky.PWS;
import synthetic.code.weather.BlueSky.R;
import synthetic.code.weather.BlueSky.WeatherData;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

/**
 * @author David
 *
 */
public class PwsPullParser extends BaseFeedParser {
	
	// Start of Personal Weather Station feed
	static final String PWS_START_TAG = "current_observation";
	static final String STATION_ID = "station_id";
	static final String CITY = "city";
	static final String STATE = "state";
	static final String TEMP_F = "temp_f";
	static final String TEMP_C = "temp_c";
	// Time of weather update in GMT time
	static final String TIME = "observation_time_rfc822";
	static final String WIND_DIRECTION = "wind_dir";
	static final String WIND_SPEED = "wind_mph";
	
	private boolean weatherOnly;
	private PWS currentPws;
	
	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * It is important that station is already created and has an ID.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param station : PWS object to get info and weather for.
	 * @throws UnsupportedEncodingException
	 */
	public PwsPullParser(Context parentContext, PWS station) throws UnsupportedEncodingException {
		super(parentContext.getString(R.string.wui_pws) + URLEncoder.encode(station.getId()));
			
		currentPws = station;
		weatherOnly = false;
	}
	
	/**
	 * Creator builds the XML feed url. Uses the calling Activity's context to get the string from resources.
	 * @param parentContext : Parent of this object (needs to have a context)
	 * @param stationId : ID of PWS to create a new PWS object and parse data for.
	 * @throws UnsupportedEncodingException
	 */
	public PwsPullParser(Context parentContext, String stationId) throws UnsupportedEncodingException {
		super(parentContext.getString(R.string.wui_pws) + URLEncoder.encode(stationId));
			
		currentPws = new PWS();
		weatherOnly = false;
	}
	
	/**
	 * Call this function to setup parse options.
	 * @param parseOnlyWeather : Set to true to not parse station info.
	 */
	public void setupParse(boolean parseOnlyWeather) {
		weatherOnly = parseOnlyWeather;
	}
	
	public PWS parse() {
		//PWS currentPWS = new PWS();
		WeatherData currentWeather = new WeatherData();
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			// auto-detect the encoding from the stream
			parser.setInput(this.getInputStream(), null);
			int eventType = parser.getEventType();
			
			boolean done = false;
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				String name = null;
				switch(eventType) {
				case XmlPullParser.START_TAG:
					name = parser.getName();
					
					// Don't need to parse station info every time
					if(!weatherOnly) {
						if(name.equalsIgnoreCase(STATION_ID)) {
							currentPws.setId(parser.nextText());
						}
						else if(name.equalsIgnoreCase(CITY)) {
							currentPws.setCity(parser.nextText());
						}
						else if(name.equalsIgnoreCase(STATE)) {
							currentPws.setState(parser.nextText());
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
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("WeatherWidget::PwsPullParser", e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		currentPws.setWeather(currentWeather);
		
		return currentPws;
	}
	
}
