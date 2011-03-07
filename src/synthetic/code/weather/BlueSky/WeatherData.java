package synthetic.code.weather.BlueSky;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Object that holds weather data at a specific time.
 * @author David
 *
 */
public class WeatherData implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3648462160478571371L;
	
	final public String unitMph = 		"mph";
	final public String unitF =			"°F";
	final public String unitC =			"°C";
	final public String unitIn =		"in";
	final public String unitMile =		"mile";
	final public String unitPercent =	"%";
	
	// Sun, 11 July 2010 22:47:9 GMT
	final SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z");
	final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aa");
	
	private String	updateTime;
	private String	temp_F;
	private String	temp_C;
	private String	windDirection;
	private String	wind_Mph;
	private String	windGust_Mph;
	private String	weatherCondition;
	private String	humidity;
	private String	rainfall_In;
	private String	pressure_In;
	private String	visibility_Mi;
	private String	dew_F;
	private String	dew_C;
	private String	uv;
	
	public WeatherData() {
		this.updateTime = "";
		this.temp_F = "";
		this.temp_C = "";
		this.windDirection = "";
		this.wind_Mph = "";
		this.windGust_Mph = "";
		this.weatherCondition = "";
		this.humidity = "";
		this.rainfall_In = "";
		this.pressure_In = "";
		this.visibility_Mi = "";
		this.dew_F = "";
		this.dew_C = "";
		this.uv = "";
	}

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
		this.temp_F = temp.trim();
	}
	
	public String getTempF() {
		return this.temp_F;
	}
	
	public String getTempFString() {
		if(this.temp_F != "")
			return this.temp_F + " " + this.unitF;
		else
			return this.temp_F;
	}
	
	public void setTempC(String temp) {
		this.temp_C = temp.trim();
	}
	
	public String getTempC() {
		return this.temp_C;
	}
	
	public String getTempCString() {
		if(this.temp_C != "")
			return this.temp_C + " " + this.unitC;
		else
			return this.temp_C;
	}
	
	public void setWindDirection(String direction) {
		this.windDirection = direction.trim();
	}
	
	public String getWindDirection() {
		return this.windDirection;
	}
	
	public void setWindSpeed(String speed) {
		this.wind_Mph = speed.trim();
	}
	
	public String getWindSpeed() {
		return this.wind_Mph;
	}
	
	public String getWindSpeedString() {
		if(this.wind_Mph != "")
			return this.wind_Mph + " " + this.unitMph;
		else
			return this.wind_Mph;
	}
	
	public String getWindDirSpeedString() {
		String space = "";
		if(this.windDirection != "")
			space = " ";
		
		return this.windDirection + space + this.getWindSpeedString();
	}
	
	public void setWindGustMph(String windGust_Mph) {
		this.windGust_Mph = windGust_Mph;
	}

	public String getWindGustMph() {
		return this.windGust_Mph;
	}
	
	public String getWindGustMphString() {
		if(this.windGust_Mph != "")
			return this.windGust_Mph + " " + this.unitMph;
		else
			return "";
	}

	public String getWeatherCondition() {
		return this.weatherCondition;
	}

	public void setWeatherCondition(String weatherString) {
		this.weatherCondition = weatherString.trim();
	}

	public void setHumidity(String humidity) {
		// Airport has % and PWS doesn't so always remove it
		this.humidity = humidity.replace("%", "").trim();
	}

	public String getHumidity() {
		return this.humidity;
	}
	
	public String getHumidityString() {
		if(this.humidity != "")
			return humidity + this.unitPercent;
		else
			return "";
	}

	public void setRainfallInch(String rainfall_In) {
		this.rainfall_In = rainfall_In;
	}

	public String getRainfallInch() {
		return rainfall_In;
	}
	
	public String getRainfallInchString() {
		if(this.rainfall_In != "")
			return rainfall_In + " " + this.unitIn;
		else
			return "";
	}

	public void setPressureInch(String pressure_In) {
		this.pressure_In = pressure_In;
	}

	public String getPressureInch() {
		return pressure_In;
	}
	
	public String getPressureInchString() {
		if(this.pressure_In != "")
			return pressure_In + " " + this.unitIn;
		else
			return "";
	}

	public void setVisibilityMile(String visibility_Mi) {
		this.visibility_Mi = visibility_Mi;
	}

	public String getVisibilityMile() {
		return visibility_Mi;
	}
	
	public String getVisibilityMileString() {
		if(this.visibility_Mi != "")
			return visibility_Mi + " " + this.unitMile;
		else
			return "";
	}

	public void setDewF(String dew_F) {
		this.dew_F = dew_F;
	}

	public String getDewF() {
		return dew_F;
	}
	
	public String getDewFString() {
		if(this.dew_F != "")
			return dew_F + " " + this.unitF;
		else
			return "";
	}

	public void setDewC(String dew_C) {
		this.dew_C = dew_C;
	}

	public String getDewC() {
		return dew_C;
	}
	
	public String getDewCString() {
		if(this.dew_C != "")
			return dew_C + " " + this.unitC;
		else
			return "";
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public String getUv() {
		return uv;
	}
	
	public String getUvString() {
		return uv;
	}
}
