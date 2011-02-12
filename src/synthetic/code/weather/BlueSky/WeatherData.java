package synthetic.code.weather.BlueSky;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Object that holds weather data at a specific time.
 * @author David
 *
 */
public class WeatherData {
	// Sun, 11 July 2010 22:47:9 GMT
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z");
	static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aa");
	
	private String	updateTime;
	private short	tempF;
	private short	tempC;
	private String	windDirection;
	private short	windMph;
	private String	weatherString;
	

	public void setTime(String time) {
		try {
			Date date = FORMATTER.parse(time);
			this.updateTime = TIME_FORMAT.format(date);
		} catch (ParseException e) {
			this.updateTime = "";
		}
	}
	
	public String getTime() {
		return this.updateTime;
	}
	
	public void setTempF(String temp) {
		try {
			this.tempF = (short) Float.parseFloat(temp.trim());
		} catch (NumberFormatException e) {
			this.tempF = 0;
		}
	}
	
	public short getTempF() {
		return this.tempF;
	}
	
	public void setTempC(String temp) {
		try {
			this.tempC = (short) Float.parseFloat(temp.trim());
		} catch (NumberFormatException e) {
			this.tempC = 0;
		}
	}
	
	public short getTempC() {
		return this.tempC;
	}
	
	public void setWindDirection(String direction) {
		this.windDirection = direction.trim();
	}
	
	public String getWindDirection() {
		return this.windDirection;
	}
	
	public void setWindSpeed(String speed) {
		try {
			this.windMph = (short) Float.parseFloat(speed.trim());
		} catch (NumberFormatException e) {
			this.windMph = 0;
		}
	}
	
	public short getWindSpeed() {
		return this.windMph;
	}
	
	public String getWeatherString() {
		return weatherString;
	}

	public void setWeatherString(String weatherString) {
		this.weatherString = weatherString.trim();
	}
}
