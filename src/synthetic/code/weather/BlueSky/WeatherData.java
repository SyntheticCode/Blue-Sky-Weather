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
package synthetic.code.weather.BlueSky;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Object that holds weather data at a specific time.
 * @author David
 *
 */
public class WeatherData {
	
	final public String unitMph = 		"mph";
	final public String unitF =			"°F";
	final public String unitC =			"°C";
	final public String unitIn =		"in";
	final public String unitMile =		"mile";
	final public String unitPercent =	"%";
	
	final private String KEY_UPDATETIME			= "WEATHER_UPDATETIME";
	final private String KEY_TEMP_F				= "WEATHER_TEMP_F";
	final private String KEY_TEMP_C				= "WEATHER_TEMP_C";
	final private String KEY_WINDDIRECTION		= "WEATHER_WINDDIRECTION";
	final private String KEY_WIND_MPH			= "WEATHER_WIND_MPH";
	final private String KEY_WINDGUST_MPH		= "WEATHER_WINDGUST_MPH";
	final private String KEY_WEATHERCONDITION	= "WEATHER_WEATHERCONDITION";
	final private String KEY_HUMIDITY			= "WEATHER_HUMIDITY";
	final private String KEY_RAINFALL_IN		= "WEATHER_RAINFALL_IN";
	final private String KEY_PRESSURE_IN		= "WEATHER_PRESSURE_IN";
	final private String KEY_VISIBILITY_MI		= "WEATHER_VISIBILITY_MI";
	final private String KEY_DEW_F				= "WEATHER_DEW_F";
	final private String KEY_DEW_C				= "WEATHER_DEW_C";
	final private String KEY_UV					= "WEATHER_UV";
	
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
	
	public void saveData(SharedPreferences pref) {
		Log.v("BlueSky", "Saving WeatherData");
		
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(KEY_UPDATETIME, this.updateTime);
		editor.putString(KEY_TEMP_F, this.temp_F);
		editor.putString(KEY_TEMP_C, this.temp_C);
		editor.putString(KEY_WINDDIRECTION, this.windDirection);
		editor.putString(KEY_WIND_MPH, this.wind_Mph);
		editor.putString(KEY_WINDGUST_MPH, this.windGust_Mph);
		editor.putString(KEY_WEATHERCONDITION, this.weatherCondition);
		editor.putString(KEY_HUMIDITY, this.humidity);
		editor.putString(KEY_RAINFALL_IN, this.rainfall_In);
		editor.putString(KEY_PRESSURE_IN, this.pressure_In);
		editor.putString(KEY_VISIBILITY_MI, this.visibility_Mi);
		editor.putString(KEY_DEW_F, this.dew_F);
		editor.putString(KEY_DEW_C, this.dew_C);
		editor.putString(KEY_UV, this.uv);
		
		editor.commit();
	}
	
	public void restoreData(SharedPreferences pref) {
		Log.v("BlueSky", "Restoring WeatherData");
		
		this.updateTime = pref.getString(KEY_UPDATETIME, null);
		this.temp_F = pref.getString(KEY_TEMP_F, null);
		this.temp_C = pref.getString(KEY_TEMP_C, null);
		this.windDirection = pref.getString(KEY_WINDDIRECTION, null);
		this.wind_Mph = pref.getString(KEY_WIND_MPH, null);
		this.windGust_Mph = pref.getString(KEY_WINDGUST_MPH, null);
		this.weatherCondition = pref.getString(KEY_WEATHERCONDITION, null);
		this.humidity = pref.getString(KEY_HUMIDITY, null);
		this.rainfall_In = pref.getString(KEY_RAINFALL_IN, null);
		this.pressure_In = pref.getString(KEY_PRESSURE_IN, null);
		this.visibility_Mi = pref.getString(KEY_VISIBILITY_MI, null);
		this.dew_F = pref.getString(KEY_DEW_F, null);
		this.dew_C = pref.getString(KEY_DEW_C, null);
		this.uv = pref.getString(KEY_UV, null);
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
