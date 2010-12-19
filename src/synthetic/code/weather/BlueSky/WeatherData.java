package synthetic.code.weather.BlueSky;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * Object that holds weather data at a specific time.
 * @author David
 *
 */
public class WeatherData {
	// Sun, 11 July 2010 22:47:9 GMT
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z");
	static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm aa");
	
	private String	updateTime;
	private short	tempF;
	private short	tempC;
	private String	windDirection;
	private short	windMph;
	
	public void setTime(String time) {
		this.updateTime = TIME_FORMAT.format(new Date());
	}
	
	public String getTime() {
		return this.updateTime;
	}
	
	public void setTempF(String temp) {
		Log.i("WeatherWidget", "TempF = " + temp + ", " + Float.parseFloat(temp.trim()));
		this.tempF = (short) Float.parseFloat(temp.trim());
	}
	
	public short getTempF() {
		return this.tempF;
	}
	
	public void setTempC(String temp) {
		this.tempC = (short) Float.parseFloat(temp.trim());
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
		this.windMph = (short) Float.parseFloat(speed.trim());
	}
	
	public short getWindSpeed() {
		return this.windMph;
	}
}
